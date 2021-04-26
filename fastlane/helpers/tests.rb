# frozen_string_literal: true
def run_unit_tests(app_module, app_variant)
  UI.message("Running unit tests...")

  task_name = "test#{app_variant}DebugUnitTest"
  begin
    gradle(task: "#{app_module}:#{task_name}", print_command: false)
    tests_passed = true
  rescue => e
    UI.error(e)

    tests_passed = false
  end

  UI.message("Unit tests finished, result: #{tests_passed ? "OK" : "KO"}")

  # We need to do this since Fastfile code which is not an action or a plugin doesn't run in the root
  # folder: https://stackoverflow.com/a/48086544/9288365
  junit_firebase_test_lab_xml_results = []
  Dir.chdir("..") do
    # The map expands the path so we have an absolute path to test results
    junit_firebase_test_lab_xml_results = Dir["#{app_module}/build/test-results/#{task_name}/*.xml"].map { |f| File.expand_path(f) }
  end

  TestsResults.new(tests_passed, junit_firebase_test_lab_xml_results)
end

def run_instrumented_tests_in_firebase_test_lab(gcp_project_id,
  gcp_service_account_json_path,
  app_apk_path,
  tests_apk_path,
  device = FtlDevice.new("Pixel2", 28, "es", "portrait"),
  results_bucket = nil,
  shards = 1)

  # Create folder where local test results will be saved
  test_lab_results_local_folder = nil
  Dir.chdir("..") do
    test_lab_results_local_folder = File.expand_path("app/build/test-results/firebase-test-lab/")
    sh("mkdir -p #{test_lab_results_local_folder}")
  end

  # Authenticate in Google Cloud and set the project
  UI.message("Authenticating with Google Cloud...")

  sh("gcloud auth activate-service-account --key-file=#{gcp_service_account_json_path}")
  sh("gcloud --quiet config set project #{gcp_project_id}")

  # Run the instrumented tests
  UI.message("Running tests in Firebase Test Lab...")

  command_output_file_path = "#{test_lab_results_local_folder}/log.txt"
  begin
    # tee command help: https://stackoverflow.com/a/418899/9288365
    sh("gcloud beta firebase test android run "\
      "--type=instrumentation "\
      "--app=#{app_apk_path} "\
      "--test=#{tests_apk_path} "\
      "#{device.get_command_line_argument} "\
      "#{results_bucket ? "--results-bucket #{results - bucket} " : ""}"\
      "#{shards > 1 ? "--num-uniform-shards=#{shards} " : ""}"\
      "2>&1 | tee #{command_output_file_path}")
    tests_passed = true
  rescue => e
    UI.error(e)

    tests_passed = false
  end

  UI.message("Instrumentation tests with Firebase Test Lab finished, result: #{tests_passed ? "OK" : "KO"}")

  UI.message("Trying to recover results from Firebase Test Lab tests...")
  # Try to recover the gs bucket results URI so we can download the results XML files
  gs_results_uri = scrape_results_uri(command_output_file_path)

  if gs_results_uri
    UI.message("Bucket URL retrieved successfylly. Recovering result files...")

    # The device folder follows the pattern "device-api-locale-orientation"
    device_results_folder = device.get_tests_folder_name

    junit_firebase_test_lab_xml_results = []
    if shards > 1
      # If we have more than one shard, the device folder is split in multiple folders, one for each shard, so we need
      # to get the results from all of them
      (0..shards - 1).each do |shard_num|
        device_shard_folder = "#{device_results_folder}_shard_#{shard_num}"
        junit_firebase_test_lab_xml_results << "#{gs_results_uri}/#{device_shard_folder}/test_result_1.xml"
      end
    else
      # If only one shard, just pick the result from the device folder
      junit_firebase_test_lab_xml_results << "#{gs_results_uri}/#{device_results_folder}/test_result_1.xml"
    end

    # Recover files from Uris
    UI.message("Downloading result files...")
    junit_local_xml_results = []
    Dir.chdir("..") do
      junit_firebase_test_lab_xml_results.each_with_index do |uri, index|
        xml_local_result = File.expand_path("#{test_lab_results_local_folder}/tests_results_#{index}.xml")
        sh("gsutil -m cp #{uri} #{xml_local_result}")
        junit_local_xml_results << xml_local_result
      end
    end

    TestsResults.new(tests_passed, junit_local_xml_results)
  else
    UI.warning("Bucket URL could not be retrieved")
    TestsResults.new(tests_passed, [])
  end
end

def scrape_results_uri(command_output_file)
  match_regex = %r{\[https://console\.developers\.google\.com/storage/browser/(.*)/(.*)/\]}
  File.read(command_output_file).each_line do |line|
    scan_result = line.scan(match_regex)
    next if scan_result.empty?
    test_bucket = scan_result[0][0]
    results_folder = scan_result[0][1]

    return "gs://#{test_bucket}/#{results_folder}"
  end

  nil
end

class TestsResults
  # We use attr_reader here so we don't have access to read the variables, and not to write them
  attr_reader :passed
  attr_reader :junit_xml_results

  def initialize(passed, junit_xml_results)
    @passed = passed
    @junit_xml_results = junit_xml_results
  end
end

class FtlDevice
  def initialize(model, version, locale, orientation)
    @model = model
    @version = version
    @locale = locale
    @orientation = orientation
  end

  def get_command_line_argument
    "--device model=#{@model},version=#{@version},locale=#{@locale},orientation=#{@orientation}"
  end

  def get_tests_folder_name
    [@model, @version, @locale, @orientation].join("-")
  end
end
