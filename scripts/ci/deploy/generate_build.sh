#!/bin/sh -e
##########################
# Usage:
# ./generate_build.sh module flavor release_track_if_any
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

FLAVOR_CAPITALIZED=$(python -c "print(\"${FLAVOR}\".capitalize())")


if [ -z "${RELEASE_TRACK}" ];
then
  # If we don't define a release track, we'll just assemble the app with the given flavor
  echo "${BOLD}Generate flavor${YELLOW} ${FLAVOR}${NO_STYLE}..."
  ./gradlew :"${MODULE}":assemble"${FLAVOR_CAPITALIZED}"Release
else
  # If we define a release track, we'll publish the app with the given flavor
  echo "${BOLD}Publish flavor${YELLOW} ${FLAVOR}${NO_STYLE}..."
  ./gradlew :"${MODULE}":publish"${FLAVOR_CAPITALIZED}"ReleaseApk
fi


echo "${BOLD}Leave release artifacts in release folder...${NO_STYLE}"
# Compose the flavor folder so we can properly get the debug and test APKs; this will be empty if the flavor is empty
FLAVOR_APK_FOLDER=$(if [ -z "$FLAVOR" ]; then echo "release"; else echo "${FLAVOR}/release"; fi)

mkdir -p release/artifacts
cp "${MODULE}"/build/outputs/apk/"${FLAVOR_APK_FOLDER}"/*-release.apk release/artifacts/
# Comment if the build is not obfuscated
#FLAVOR_MAPPING_FOLDER=$(if [ -z "$FLAVOR" ]; then echo "release"; else echo "${FLAVOR}Release"; fi)
#cp "${MODULE}"/build/outputs/mapping/"${FLAVOR_MAPPING_FOLDER}"/*-release-mapping.txt release/artifacts/
