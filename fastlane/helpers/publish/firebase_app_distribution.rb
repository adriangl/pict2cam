# frozen_string_literal: true
require_relative "./models"

def upload_to_firebase_app_distribution(
  firebase_app_id,
  apk_path,
  publish_service_account_json_path,
  tester_emails = nil,
  tester_groups = nil,
  release_notes = nil
)
  UI.message("Uploading app to Firebase App Distribution...")

  begin
    firebase_app_distribution(
      app: firebase_app_id,
      apk_path: apk_path,
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
