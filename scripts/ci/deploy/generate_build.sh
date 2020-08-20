#!/bin/sh -e
##########################
# Usage:
# ./generate_build.sh module flavor release_track
##########################

# Misc variables, only set them in interactive shells
# https://unix.stackexchange.com/a/26827
case $- in
  *i*)
    BOLD=$(tput bold)
    RED=$(tput setaf 1)
    GREEN=$(tput setaf 2)
    YELLOW=$(tput setaf 3)
    NO_STYLE=$(tput sgr0)
  ;;
  *) ;;
esac

# Input parameters (will extract them as script parameters)
MODULE=$1
FLAVOR=$2
RELEASE_TRACK=$3

BUILD_FLAVOR=$(python -c "print(\"${FLAVOR}\".capitalize())")
RELEASE_TRACK_CAPITALIZED=$(python -c "print(\"${RELEASE_TRACK}\".capitalize())")

echo "${BOLD}Prepare release notes for flavor${YELLOW} ${BUILD_FLAVOR}${NO_STYLE}..."
./gradlew clean :"${MODULE}":copyGitChangelogTo"${BUILD_FLAVOR}"Release"${RELEASE_TRACK_CAPITALIZED}"ReleaseNotes


echo "${BOLD}Initialize and apply tag-flow...${NO_STYLE}"
git submodule update --init --recursive --remote
python tag-flow/tag.py
git push --tags


echo "${BOLD}Publish flavor${YELLOW} ${BUILD_FLAVOR}${NO_STYLE}..."
./gradlew :"${MODULE}":publish"${BUILD_FLAVOR}"ReleaseApk


echo "${BOLD}Leave release artifacts in release folder...${NO_STYLE}"
# Compose the flavor folder so we can properly get the debug and test APKs; this will be empty if the flavor is empty
FLAVOR_FOLDER=$(if [ -z "$BUILD_FLAVOR" ]; then echo ""; else echo "${BUILD_FLAVOR}/"; fi)

mkdir -p release/artifacts
cp "${MODULE}"/build/outputs/apk/"${FLAVOR_FOLDER}"release/*-release.apk release/artifacts/
# Uncomment if the build is obfuscated
# cp "${MODULE}"/build/outputs/mapping/"${FLAVOR_FOLDER}"release/*-release-mapping.txt release/artifacts/
cp "${MODULE}"/src/"${FLAVOR_FOLDER}"release/play/release-notes/*/"${RELEASE_TRACK}".txt release/release-notes.txt
