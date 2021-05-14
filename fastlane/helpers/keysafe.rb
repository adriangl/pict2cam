# frozen_string_literal: true
require "fileutils"

def init_keysafe(app)
  Dir.chdir("..") do
    repo_url = ENV["KEYSAFE_REPO_URL"]
    if repo_url
      sh("git clone #{repo_url} keysafe && cd keysafe && git checkout #{app}")
    else
      UI.user_error!("Keysafe repository URL not found!")
    end
  end

  UI.message("Keysafe initialized")
end

def delete_keysafe
  Dir.chdir("..") do
    FileUtils.rm_rf("keysafe")
  end

  UI.message("Keysafe removed")
end

def get_firebase_test_lab_data
  UI.message("Retrieving Firebase Test Lab data...")
  ftl_folder = "keysafe/firebase-test-lab"
  Dir.chdir("..") do
    project_id = File.read("#{ftl_folder}/gcp-project-id.txt")
    service_account_json_path = File.expand_path("#{ftl_folder}/service-account.json")

    return FirebaseTestLabData.new(project_id, service_account_json_path)
  end
end

def get_keys_data
  UI.message("Retrieving signing data...")

  keys_folder = "keysafe/keys"
  Dir.chdir("..") do
    signing_keystore_path = File.expand_path("#{keys_folder}/signing-keystore.jks")
    signing_keystore_password = File.read("#{keys_folder}/signing-keystore-pwd.txt").strip
    signing_keystore_alias = File.read("#{keys_folder}/signing-keystore-alias.txt").strip
    signing_keystore_alias_password = File.read("#{keys_folder}/signing-keystore-alias-pwd.txt").strip

    upload_keystore_path = File.expand_path("#{keys_folder}/upload-keystore.jks")
    upload_keystore_password = File.read("#{keys_folder}/upload-keystore-pwd.txt").strip
    upload_keystore_alias = File.read("#{keys_folder}/upload-keystore-alias.txt").strip
    upload_keystore_alias_password = File.read("#{keys_folder}/upload-keystore-alias-pwd.txt").strip

    return KeysData.new(signing_keystore_path,
      signing_keystore_password,
      signing_keystore_alias,
      signing_keystore_alias_password,
      upload_keystore_path,
      upload_keystore_password,
      upload_keystore_alias,
      upload_keystore_alias_password)
  end
end

def get_google_play_data
  UI.message("Retrieving Google Play data...")

  google_play_folder = "keysafe/google-play"
  Dir.chdir("..") do
    package_name = File.read("#{google_play_folder}/package-name.txt").strip
    upload_service_account_path = File.expand_path("#{google_play_folder}/upload-service-account.json")

    return GooglePlayData.new(package_name,
      upload_service_account_path)
  end
end

def get_huawei_data
  UI.message("Retrieving Huawei AppGallery data...")

  huawei_folder = "keysafe/huawei"
  Dir.chdir("..") do
    api_client_id = File.read("#{huawei_folder}/api-client-id.txt").strip
    api_client_secret = File.read("#{huawei_folder}/api-client-secret.txt").strip
    app_id = File.read("#{huawei_folder}/app-id.txt").strip

    return HuaweiData.new(api_client_id,
      api_client_secret,
      app_id)
  end
end

def get_firebase_app_distribution_data
  UI.message("Retrieving Firebase App Distribution data...")
  fad_folder = "keysafe/firebase-app-distribution"
  Dir.chdir("..") do
    service_account_json_path = File.expand_path("#{fad_folder}/service-account.json")

    # Search for app IDs in the folder
    app_id_file_name_regex = /((.*)-)+app-id.txt/
    build_variant_app_id_hash = Dir["#{fad_folder}/*app-id.txt"].map do |file|
      file_absolute_path = File.expand_path(file)
      file_name = File.basename(file_absolute_path)
      app_id = File.read(file_absolute_path).strip

      scan_result = file_name.scan(app_id_file_name_regex)
      next if scan_result.empty?
      build_variant = scan_result[0][1]

      build_variant_array = build_variant.split("-")

      [build_variant_array, app_id]
    end.to_h

    return FirebaseAppDistributionData.new(service_account_json_path, build_variant_app_id_hash)
  end
end

class FirebaseTestLabData
  attr_reader :project_id
  attr_reader :service_account_json_path

  def initialize(project_id, service_account_json_path)
    @project_id = project_id
    @service_account_json_path = service_account_json_path
  end
end

class KeysData
  attr_reader :signing_keystore_path
  attr_reader :signing_keystore_password
  attr_reader :signing_keystore_alias
  attr_reader :signing_keystore_alias_password
  attr_reader :upload_keystore_path
  attr_reader :upload_keystore_password
  attr_reader :upload_keystore_alias
  attr_reader :upload_keystore_alias_password

  def initialize(signing_keystore_path,
    signing_keystore_password,
    signing_keystore_alias,
    signing_keystore_alias_password,
    upload_keystore_path,
    upload_keystore_password,
    upload_keystore_alias,
    upload_keystore_alias_password)
    @signing_keystore_path = signing_keystore_path
    @signing_keystore_password = signing_keystore_password
    @signing_keystore_alias = signing_keystore_alias
    @signing_keystore_alias_password = signing_keystore_alias_password
    @upload_keystore_path = upload_keystore_path
    @upload_keystore_password = upload_keystore_password
    @upload_keystore_alias = upload_keystore_alias
    @upload_keystore_alias_password = upload_keystore_alias_password
  end
end

class GooglePlayData
  attr_reader :package_name
  attr_reader :upload_service_account_json_path

  def initialize(package_name, upload_service_account_json_path)
    @package_name = package_name
    @upload_service_account_json_path = upload_service_account_json_path
  end
end

class HuaweiData
  attr_reader :api_client_id
  attr_reader :api_client_secret
  attr_reader :app_id

  def initialize(api_client_id, api_client_secret, app_id)
    @api_client_id = api_client_id
    @api_client_secret = api_client_secret
    @app_id = app_id
  end
end

class FirebaseAppDistributionData
  attr_reader :service_account_json_path
  attr_reader :build_variant_app_id_hash

  def initialize(service_account_json_path, build_variant_app_id_hash)
    @service_account_json_path = service_account_json_path
    @build_variant_app_id_hash = build_variant_app_id_hash
  end
end
