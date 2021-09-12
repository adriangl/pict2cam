# Changelog
All notable changes to this project will be documented in this file.
Only changes between relevant releases (aka: released to production in Google Play Store) will be listed in this document.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- No new features!
### Changed
- No changed features!
### Deprecated
- No deprecated features!
### Removed
- No removed features!
### Fixed
- No fixed issues!
### Security
- No security issues fixed!

## [1.0.62] - 2021-09-12
### Added
- Add warning for Android 11+ with incompatibility disclaimer
- Add Jetpack Compose for UI
### Changed
- Migrate CI toolchain to fastlane
- Migrate to Android 12 build tools and API target
- Update library dependencies

## [1.0.53] - 2021-07-30
### Added
- Set crop area to full size by default

## [1.0.27] - 2021-01-16
### Added
- Show error toast if the cropping library cannot crop the image for any reason
- Add `requestLegacyExternalStorage` flag to support apps requesting images with `file://` Uris in Android 10

## [1.0.18] - 2021-01-16
### Changed
- Limit maxSdkVersion to API 29 (Android 10)

## [1.0.0] - 2020-08-27
### Added
- Initial release.

## [0.1.0] - 2019-09-12
### Added
- Initial commit.

[Unreleased]: https://github.com/adriangl/pict2cam/compare/1.0.62...HEAD
[1.0.62]: https://github.com/adriangl/pict2cam/compare/1.0.53...1.0.62
[1.0.53]: https://github.com/adriangl/pict2cam/compare/1.0.27...1.0.53
[1.0.27]: https://github.com/adriangl/pict2cam/compare/1.0.18...1.0.27
[1.0.18]: https://github.com/adriangl/pict2cam/compare/1.0.0...1.0.18
[1.0.0]: https://github.com/adriangl/pict2cam/compare/0.1.0...1.0.0
[0.1.0]: https://github.com/adriangl/pict2cam/releases/tag/0.1.0
