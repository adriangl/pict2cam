# frozen_string_literal: true
require "date"
require "active_support/core_ext" # imports methods like 3.weeks or 1.day for datetime manipulation

require_relative "./models"

def publish_apk_to_huawei_appgallery(submit_for_review, apk_path, api_client_id, api_client_secret, app_id)
  UI.message("Publishing app to Huawei AppGallery...")

  begin
    huawei_appgallery_connect(
      client_id: api_client_id,
      client_secret: api_client_secret,
      app_id: app_id,
      apk_path: apk_path,
      is_aab: false,
      submit_for_review: submit_for_review
    )

    published = true
    version_name = get_huawei_published_version_name(track, package_name, upload_service_account_json_path)
  rescue => e
    UI.error(e)

    published = false
    version_name = nil
  end

  PublishVersionResult(published, version_name, nil)
end

def update_huawei_appgallery_release_for_review(
  api_client_id,
  api_client_secret,
  app_id,
  is_phased_release,
  phased_release_percentage = nil,
  phased_release_start_datetime = nil,
  phased_release_end_datetime = nil,
  phased_release_description = ""
)

  UI.user_error!("Provide a phased release percentage if using phased releases") if is_phased_release && phased_release_percentage.nil?

  # Default phased release dates: set the maximum time that we're allowed for the phased release dates if not defined -> 30 days time window
  phased_release_start = phased_release_start_datetime ? phased_release_start_datetime : Time.now + 2.minutes
  phased_release_end = phased_release_end_datetime ? phased_release_end_datetime : phased_release_start_datetime + 30.days

  begin
    huawei_appgallery_connect_submit_for_review(
      client_id: api_client_id,
      client_secret: api_client_secret,
      app_id: app_id,
      phase_wise_release: is_phased_release,
      phase_release_start_time: phased_release_start.iso8601,
      phase_release_end_time: phased_release_end.iso8601,
      phase_release_description: phased_release_description,
      phase_release_percent: rollout_percentage.to_s
    )
  rescue => e
    UI.error(e)

    published = false
    version_name = nil
  end

  PublishVersionResult(published, version_name, nil)
end

def get_huawei_published_version_name(api_client_id, _api_client_secret, app_id)
  app_info = huawei_appgallery_connect_get_app_info(
    client_id: api_client_id,
    client_secret: api_client_secret,
    app_id: app_id
  )
  version_name = app_info["versionNumber"]

  UI.message("Huawei AppGallery version name published: #{version_name}")
  version_name
end
