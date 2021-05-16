# frozen_string_literal: true
require "active_support"
require "active_support/core_ext" # imports camelize

def clean
  gradle(task: "clean", print_command: false)
end

def build_debug_apk(app_module, app_variant)
  begin
    gradle(task: "#{app_module}:assemble#{app_variant}Debug", print_command: false)

    successful = true
    apk_path = Actions.lane_context[SharedValues::GRADLE_APK_OUTPUT_PATH]
  rescue => e
    UI.error(e)

    successful = false
    apk_path = nil
  end

  BuildResult.new(successful, apk_path)
end

def build_instrumented_tests_apk(app_module, app_variant)
  begin
    gradle(task: "#{app_module}:assemble#{app_variant}DebugAndroidTest", print_command: false)

    successful = true
    apk_path = Actions.lane_context[SharedValues::GRADLE_APK_OUTPUT_PATH]
  rescue => e
    UI.error(e)

    successful = false
    apk_path = nil
  end

  BuildResult.new(successful, apk_path)
end

def build_release_apk(
  app_module,
  app_variant,
  keystore_path,
  keystore_password,
  keystore_alias,
  keystore_alias_password,
  property_prefix = ""
)
  signing_properties = {
    "#{property_prefix}ReleaseKeystorePath".camelize(:lower) => keystore_path,
    "#{property_prefix}ReleaseKeystorePwd".camelize(:lower) => keystore_password,
    "#{property_prefix}ReleaseKeystoreAlias".camelize(:lower) => keystore_alias,
    "#{property_prefix}ReleaseKeystoreAliasPwd".camelize(:lower) => keystore_alias_password,
  }

  begin
    gradle(
      task: "#{app_module}:assemble#{app_variant}Release",
      print_command: false,
      properties: signing_properties
    )

    successful = true
    apk_path = Actions.lane_context[SharedValues::GRADLE_APK_OUTPUT_PATH]
    mapping_path = Actions.lane_context[SharedValues::GRADLE_MAPPING_TXT_OUTPUT_PATH]
  rescue => e
    UI.error(e)

    successful = false
    apk_path = nil
    mapping_path = nil
  end

  BuildResult.new(successful, apk_path, mapping_path)
end

def build_release_aab(
  app_module,
  app_variant,
  keystore_path,
  keystore_password,
  keystore_alias,
  keystore_alias_password,
  property_prefix = ""
)
  signing_properties = {
    "#{property_prefix}ReleaseKeystorePath".camelize(:lower) => keystore_path,
    "#{property_prefix}ReleaseKeystorePwd".camelize(:lower) => keystore_password,
    "#{property_prefix}ReleaseKeystoreAlias".camelize(:lower) => keystore_alias,
    "#{property_prefix}ReleaseKeystoreAliasPwd".camelize(:lower) => keystore_alias_password,
  }

  begin
    gradle(
      task: "#{app_module}:bundle#{app_variant}Release",
      print_command: false,
      properties: signing_properties
    )

    successful = true
    aab_path = Actions.lane_context[SharedValues::GRADLE_AAB_OUTPUT_PATH]
    mapping_path = Actions.lane_context[SharedValues::GRADLE_MAPPING_TXT_OUTPUT_PATH]
  rescue => e
    UI.error(e)

    successful = false
    aab_path = nil
    mapping_path = nil
  end

  BuildResult.new(successful, aab_path, mapping_path)
end

class BuildResult
  # We use attr_reader here so we dont have access to read the variables, and not to write them
  attr_reader :successful
  attr_reader :apk_path
  attr_reader :mapping_path

  def initialize(successful, apk_path, mapping_path = nil)
    @successful = successful
    @apk_path = apk_path
    @mapping_path = mapping_path
  end
end
