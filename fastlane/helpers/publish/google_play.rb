# frozen_string_literal: true
require_relative "./models"

def publish_apk_to_google_play_store(track, apk_path, mapping_path, package_name, upload_service_account_json_path)
  UI.message("Publishing app to Google Play's #{track} track...")

  begin
    supply(
      validate_only: false,
      track: track.nil? ? "internal" : track,
      apk: apk_path,
      mapping: mapping_path,
      package_name: package_name,
      json_key: upload_service_account_json_path,
      skip_upload_aab: true,
      skip_upload_metadata: true,
      skip_upload_changelogs: true,
      skip_upload_images: true,
      skip_upload_screenshots: true
    )
    published = true
    version_name = get_google_play_store_published_version_name(track, package_name, upload_service_account_json_path)
    version_code = get_google_play_store_published_version_code(track, package_name, upload_service_account_json_path)
  rescue => e
    UI.error(e)

    published = false
    version_name = nil
    version_code = nil
  end

  PublishVersionResult.new(published, version_name, version_code)
end

def promote_google_play_store_track(from_track, to_track, package_name, upload_service_account_json_path, staged_rollout_percentage = nil)
  UI.message("Promoting Google Play version from \"#{from_track}\" to \"#{to_track}\""\
    "#{" with rollout of #{staged_rollout_percentage}%" unless staged_rollout_percentage.nil?}...")

  rollout = staged_rollout_percentage.to_f / 100.0 unless staged_rollout_percentage.nil?

  begin
    supply(
      validate_only: false,
      track: from_track,
      track_promote_to: to_track,
      rollout: rollout,
      package_name: package_name,
      json_key: upload_service_account_json_path,
      skip_upload_apk: true,
      skip_upload_changelogs: true,
      skip_upload_metadata: true,
      skip_upload_images: true,
      skip_upload_screenshots: true
    )

    published = true
    version_name = get_google_play_store_published_version_name(to_track, package_name, upload_service_account_json_path)
    version_code = get_google_play_store_published_version_code(to_track, package_name, upload_service_account_json_path)
  rescue => e
    UI.error(e)

    published = false
    version_name = nil
    version_code = nil
  end

  PublishVersionResult.new(published, version_name, version_code)
end

def get_google_play_store_published_version_name(track, package_name, upload_service_account_json_path)
  version_name = google_play_track_release_names(
    track: track,
    package_name: package_name,
    json_key: upload_service_account_json_path,
  )[0]

  UI.message("Google Play version name published in the \"#{track}\" track: #{version_name}")
  version_name
end

def get_google_play_store_published_version_code(track, package_name, upload_service_account_json_path)
  version_code = google_play_track_version_codes(
    track: track,
    package_name: package_name,
    json_key: upload_service_account_json_path,
  )[0]

  UI.message("Google Play version code published in the \"#{track}\" track: #{version_code}")
  version_code
end
