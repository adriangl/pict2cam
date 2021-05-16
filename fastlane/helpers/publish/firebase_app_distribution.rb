# frozen_string_literal: true
require_relative "./models"

def upload_to_firebase_app_distribution(
  firebase_app_id,
  artifact_path,
  artifact_type,
  publish_service_account_json_path,
  tester_emails = nil,
  tester_groups = nil,
  release_notes = nil
)
  UI.message("Uploading app to Firebase App Distribution...")

  begin
    firebase_app_distribution(
      app: firebase_app_id,
      android_artifact_path: artifact_path,
      android_artifact_type: artifact_type,
      service_credentials_file: publish_service_account_json_path,
      testers: tester_emails,
      groups: tester_groups,
      release_notes: release_notes
    )
    published = true
  rescue => e
    UI.error(e)
    published = false
  end

  PublishVersionResult.new(published)
end
