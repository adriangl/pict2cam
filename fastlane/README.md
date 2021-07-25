fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew install fastlane`

# Available Actions
## Android
### android generate_daily_snapshot
```
fastlane android generate_daily_snapshot
```
Generates daily snapshot and uploads it to GitHub as a pre-release

[String] app_keysafe_name: Name of the application in the keysafe

[String] app_keysafe_password: Password of the application in the keysafe

[String] artifact_type: Artifact to generate: APK or AAB

[String] github_repository_name: GitHub repository name

[String] github_api_token: GitHub API token that can publish releases
### android validate_pr
```
fastlane android validate_pr
```
Tests PRs and checks for integrity

[String] app_keysafe_name: Name of the application in the keysafe

[String] app_keysafe_password: Password of the application in the keysafe

[String] artifact_type: Artifact to generate: APK or AAB
### android promote_to_alpha
```
fastlane android promote_to_alpha
```
Promote current internal build to alpha track

[String] app_keysafe_name: Name of the application in the keysafe

[String] app_keysafe_password: Password of the application in the keysafe
### android promote_to_production_20
```
fastlane android promote_to_production_20
```
Promote current alpha build to production track with 20% rollout

[String] app_keysafe_name: Name of the application in the keysafe

[String] app_keysafe_password: Password of the application in the keysafe
### android promote_to_production_100
```
fastlane android promote_to_production_100
```
Promote current stged production build to production track with 100% rollout

[String] app_keysafe_name: Name of the application in the keysafe

[String] app_keysafe_password: Password of the application in the keysafe

[String] artifact_type: Artifact to generate for HUAWEI AppGallery: APK or AAB

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
