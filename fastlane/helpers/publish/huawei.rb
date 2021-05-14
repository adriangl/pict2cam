# frozen_string_literal: true
require "date"
require "time"
require "active_support/core_ext" # imports methods like 3.weeks or 1.day for datetime manipulation or String.truncate

require_relative "./models"

def publish_apk_to_huawei_appgallery(
  submit_for_review,
  apk_path,
  api_client_id,
  api_client_secret,
  app_id,
  is_phased_release = false,
  phased_release_percentage = nil,
  phased_release_start_datetime = nil,
  phased_release_end_datetime = nil,
  phased_release_description = ""
)
  UI.user_error!("Provide a phased release percentage if using phased releases") if is_phased_release && phased_release_percentage.nil?

  date_format_string = "%Y-%m-%dT%H:%M:%S%z"

  # Default phased release dates: set the maximum time that we're allowed for the phased release dates if not defined -> 30 days time window
  phased_release_start = phased_release_start_datetime ? phased_release_start_datetime : Time.now + 2.minutes if is_phased_release
  phased_release_end = phased_release_end_datetime ? phased_release_end_datetime : phased_release_start + 30.days if is_phased_release
  phased_percentage = format("%.2f", phased_release_percentage.to_f) if is_phased_release && phased_release_percentage.nil?
  phased_description = if phased_release_description.strip.empty?
    "Phased release from #{phased_release_start} to #{phased_release_end}"
  else
    phased_release_description.truncate(
      500, separator: /\s/, omission: " (and more)"
    )
  end if is_phased_release

  UI.message("Publishing app to HUAWEI AppGallery...")

  begin
    huawei_appgallery_connect(
      client_id: api_client_id,
      client_secret: api_client_secret,
      app_id: app_id,
      apk_path: apk_path,
      is_aab: false,
      submit_for_review: submit_for_review,
      phase_wise_release: is_phased_release,
      phase_release_start_time: phased_release_start&.strftime(date_format_string),
      phase_release_end_time: phased_release_end&.strftime(date_format_string),
      phase_release_description: phased_description,
      phase_release_percent: phased_percentage
    )

    published = true
    version_name = get_huawei_published_version_name(api_client_id, api_client_secret, app_id)
  rescue => e
    UI.error(e)

    published = false
    version_name = nil
  end

  PublishVersionResult.new(published, version_name, nil)
end

def update_huawei_appgallery_release_for_review(
  api_client_id,
  api_client_secret,
  app_id,
  is_phased_release = false,
  phased_release_percentage = nil,
  phased_release_start_datetime = nil,
  phased_release_end_datetime = nil,
  phased_release_description = ""
)

  UI.user_error!("Provide a phased release percentage if using phased releases") if is_phased_release && phased_release_percentage.nil?

  date_format_string = "%Y-%m-%dT%H:%M:%S%z"

  # Default phased release dates: set the maximum time that we're allowed for the phased release dates if not defined -> 30 days time window
  phased_release_start = phased_release_start_datetime ? phased_release_start_datetime : Time.now + 1.minutes if is_phased_release
  phased_release_end = phased_release_end_datetime ? phased_release_end_datetime : phased_release_start + 30.days if is_phased_release
  phased_percentage = format("%.2f", phased_release_percentage.to_f) if is_phased_release && phased_release_percentage.nil?
  phased_description = if phased_release_description.strip.empty?
    "Phased release from #{phased_release_start} to #{phased_release_end}"
  else
    phased_release_description.truncate(
      500, separator: /\s/, omission: " (and more)"
    )
  end if is_phased_release

  puts phased_release_start&.strftime(date_format_string)
  puts phased_release_end&.strftime(date_format_string)

  begin
    huawei_appgallery_connect_submit_for_review(
      client_id: api_client_id,
      client_secret: api_client_secret,
      app_id: app_id,
      phase_wise_release: is_phased_release,
      phase_release_start_time: phased_release_start&.strftime(date_format_string),
      phase_release_end_time: phased_release_end&.strftime(date_format_string),
      phase_release_description: phased_description,
      phase_release_percent: phased_percentage
    )

    published = true
    version_name = nil
  rescue => e
    UI.error(e)

    published = false
    version_name = nil
  end

  PublishVersionResult.new(published, version_name, nil)
end

def get_huawei_published_version_name(api_client_id, api_client_secret, app_id)
  app_info = huawei_appgallery_connect_get_app_info(
    client_id: api_client_id,
    client_secret: api_client_secret,
    app_id: app_id
  )
  version_name = app_info["versionNumber"]

  UI.message("HUAWEI AppGallery version name published: #{version_name}")
  version_name
end
