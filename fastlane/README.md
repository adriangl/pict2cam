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
### android unit_tests
```
fastlane android unit_tests
```
Run unit tests in the project



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.

* `clean` - (Optional) Boolean that enables cleaning the project before running. Defaults to false.



Returns:

* a `TestsResult` object containing whether or not the tests passed and an array of JUnit XML files.
### android instrumented_tests_in_firebase_test_lab
```
fastlane android instrumented_tests_in_firebase_test_lab
```
Run instrumented tests in the project using Firebase Test Lab.



Ensure that you have installed `gcloud` and `gsutil` in your system and are accesible in PATH before running this lane.



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.

* `clean` - (Optional) Boolean that enables cleaning the project before running. Defaults to false.



Returns:

* a `TestsResult` object containing whether or not the tests passed and an array of JUnit XML files.
### android debug_apk
```
fastlane android debug_apk
```
Generates a debug APK build of the app



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.

* `clean` - (Optional) Boolean that enables cleaning the project before running. Defaults to false.



Returns:

* `BuildResult` object containing the generated APK path and the Proguard Mapping file if generated
### android tests_apk
```
fastlane android tests_apk
```
Generates a test APK build of the app to use with a debug APK to run instrumented tests



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.

* `clean` - (Optional) Boolean that enables cleaning the project before running. Defaults to false.



Returns:

* a `BuildResult` object containing the generated APK path and the Proguard Mapping file if generated
### android release_apk
```
fastlane android release_apk
```
Generates a release APK build of the app



Ensure that the apps' `signingConfig` blocks are defined and that they can be obtained from the Gradle project like this:

```groovy

project.property("releaseKeystorePath")

project.property("releaseKeystorePwd")

project.property("releaseKeystoreAlias")

project.property("releaseKeystoreAliasPwd")

```

If you add the `property_prefix` parameter, don't forget to include it in your key loading. For example, if you set `free` 

as the prefix, you'll have the following project properties:

```groovy

project.property("freeReleaseKeystorePath")

project.property("freeReleaseKeystorePwd")

project.property("freeReleaseKeystoreAlias")

project.property("freeReleaseKeystoreAliasPwd")

```



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.

* `clean` - (Optional) Boolean that enables cleaning the project before running. Defaults to false.

* `property_prefix` - (Optional) Prefix to add to keystore variables' names. Defaults to empty string.



Returns:

* a `BuildResult` object containing the generated APK path and the Proguard Mapping file if generated
### android release_aab
```
fastlane android release_aab
```
Generates a release AAB build of the app



Ensure that the apps' `signingConfig` blocks are defined and that they can be obtained from the Gradle project like this:

```groovy

project.property("releaseKeystorePath")

project.property("releaseKeystorePwd")

project.property("releaseKeystoreAlias")

project.property("releaseKeystoreAliasPwd")

```

If you add the `property_prefix` parameter, don't forget to include it in your key loading. For example, if you set `free` 

as the prefix, you'll have the following project properties:

```groovy

project.property("freeReleaseKeystorePath")

project.property("freeReleaseKeystorePwd")

project.property("freeReleaseKeystoreAlias")

project.property("freeReleaseKeystoreAliasPwd")

```



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.

* `clean` - (Optional) Boolean that enables cleaning the project before running. Defaults to false.

* `property_prefix` - (Optional) Prefix to add to keystore variables' names. Defaults to empty string.



Returns:

* a `BuildResult` object containing the generated AAB path and the Proguard Mapping file if generated
### android publish_apk_play_store
```
fastlane android publish_apk_play_store
```
Publishes an APK to the Google Play Store in the given track



Options:

* `app_name` - Name of the app in the keysafe.

* `track` - The track to use when publishing.

* `apk_paths` - Comma-separated list of absolute paths to the APKs to publish.

* `mapping_paths` - Comma-separated list of absolute paths to the Proguard mapping paths to attach to the publication. Defaults to nil.



Returns:

* a `PublishVersionResult` object containing if the publication was published and the version name of the published app
### android publish_aab_play_store
```
fastlane android publish_aab_play_store
```
Publishes an AAB to the Google Play Store in the given track



Options:

* `app_name` - Name of the app in the keysafe.

* `track` - The track to use when publishing.

* `aab_paths` - Comma-separated list of absolute paths to the AABs to publish.

* `mapping_paths` - Comma-separated list of absolute paths to the Proguard mapping paths to attach to the publication. Defaults to nil.



Returns:

* a `PublishVersionResult` object containing if the publication was published and the version name of the published app
### android publish_apk_appgallery
```
fastlane android publish_apk_appgallery
```
Publishes an APK to HUAWEI AppGallery.



Options:

* `app_name` - Name of the app in the keysafe.

* `submit_for_review` - (Optional) Whether or not submit for review. Defaults to false.

* `apk_path` - Absolute path to the APK to publish.



Returns:

* a `PublishVersionResult` object containing if the publication was published and the version name of the published app
### android publish_aab_appgallery
```
fastlane android publish_aab_appgallery
```
Publishes an AAB to HUAWEI AppGallery.



Options:

* `app_name` - Name of the app in the keysafe.

* `submit_for_review` - (Optional) Whether or not submit for review. Defaults to false.

* `aab_path` - Absolute path to the AAB to publish.



Returns:

* a `PublishVersionResult` object containing if the publication was published and the version name of the published app
### android promote_google_play_store
```
fastlane android promote_google_play_store
```
Promotes the version in a track into another track in Google Play Store, with a given staged rollout percentage.



Options:

* `app_name` - Name of the app in the keysafe.

* `from_track` - The track to promote from.

* `to_track` - The track to promote to.

* `staged_rollout_percentage` - (Optional) Float from 0 to 100 to perform a staged rollout, or nil if no staged rollout



Returns:

* a `PublishVersionResult` object containing if the publication was published and the version name of the published app
### android submit_for_review_appgallery
```
fastlane android submit_for_review_appgallery
```
Updates the version to submit for review in HUAWEI AppGallery



Options:

* `app_name` - Name of the app in the keysafe.

* `staged_rollout_percentage` - (Optional) Float from 0 to 100 to perform a staged rollout, or nil if no staged rollout



Returns:

* a `PublishVersionResult` object containing if the publication was published and the version name of the published app
### android publish_artifact_to_firebase_app_distribution
```
fastlane android publish_artifact_to_firebase_app_distribution
```
Uploads an APK to Firebase App Distribution



Options:

* `app_name` - Name of the app in the keysafe.

* `artifact_path` - Absolute path to the artifact (APK or AAB) to publish.

* `artifact_type` - (Optional) Type of artifact (`APK` or `AAB`) to publish. Defaults to `APK`.

* `app_variant` - (Optional) Comma separated list of the flavors that compose the variant of the app. Defaults to "release".

* `tester_emails` - (Optional) Comma separated list of tester emails. Defaults to nil.

* `tester_groups` - (Optional) Comma separated list of tester groups. Defaults to nil.

* `release_notes` - (Optional) Release notes for the version. Defaults to nil



Returns:

* a `PublishVersionResult` object containing if the publication was published
### android generate_internal
```
fastlane android generate_internal
```
Builds, tests and publishes an app version to stores' internal tracks.



Options:

* `app_name` - Name of the app in the keysafe.

* `app_module` - (Optional) The module to build. Defaults to `app`.

* `app_variant` - (Optional) Comma separated list off the flavors that compose the variant of the app. Defaults to empty string.
### android promote_internal_to_alpha
```
fastlane android promote_internal_to_alpha
```
Promote internal track binary to alpha track in stores



Options:

* `app_name` - Name of the app in the keysafe.
### android promote_alpha_to_production
```
fastlane android promote_alpha_to_production
```
Promote alpha track binary to production track in stores with 20% rollout



Options:

* `app_name` - Name of the app in the keysafe.
### android update_production_percentage
```
fastlane android update_production_percentage
```
Update production staged rollout percentage

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
