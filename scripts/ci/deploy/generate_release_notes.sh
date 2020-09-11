#!/bin/sh -e
##########################
# Creates a release notes file from the latest tag to HEAD.
#
# Usage:
# ./generate_release_changelog.sh release_notes_relative_path_txt character_limit_if_needed
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

# Footer to add to trimmed files, just to know if that there should be more text but we're restrained for some reason
# (example: Google Play release notes are limited to 500 characters)
FILE_TRIM_FOOTER="…\n\nAnd more…"
FILE_TRIM_FOOTER_CHARACTER_COUNT=${#FILE_TRIM_FOOTER}

# Script variables read from console
RELEASE_NOTES_FILE_PATH=$1
CHARACTER_LIMIT=$2

# Obtain the latest, closest tag to HEAD
LATEST_TAG=$(git describe --abbrev=0 --tags)
echo "Latest tag found: ${BOLD}${LATEST_TAG}${NO_STYLE}"
echo

# Compose a basic changelog using git log
CHANGELOG=$(git log "${LATEST_TAG}"..HEAD --no-merges --pretty=format:%s)
if [ -z "${CHANGELOG}" ];
then
  CHANGELOG="No changes found"
fi;

# Print the changelog in console
echo "${BOLD}Version changelog${NO_STYLE}"
echo "-----------------"
echo "${CHANGELOG}"
echo "-----------------"
echo

# Dump the changelog to a file
echo "Dumping changelog to file: ${BOLD}${RELEASE_NOTES_FILE_PATH}${NO_STYLE}..."
mkdir -p "$(dirname "${RELEASE_NOTES_FILE_PATH}")" && touch "${RELEASE_NOTES_FILE_PATH}"
printf "%b" "${CHANGELOG}" > "${RELEASE_NOTES_FILE_PATH}"

if [ -z "${CHARACTER_LIMIT}" ];
then
  echo "No file trim needed"
else
  # If we need to trim the file, re-read the file, trim it and dump it again to the final file with the footer text
  CHANGELOG_MAX_CHARACTER_LENGTH=$(python -c "print(${CHARACTER_LIMIT} - ${FILE_TRIM_FOOTER_CHARACTER_COUNT})")
  TRIMMED_CHANGELOG=$(python -c "print(open('${RELEASE_NOTES_FILE_PATH}', 'rt').read(${CHANGELOG_MAX_CHARACTER_LENGTH}))")

  printf "%b" "${TRIMMED_CHANGELOG}" > "${RELEASE_NOTES_FILE_PATH}"
  printf "%b" "${FILE_TRIM_FOOTER}" >> "${RELEASE_NOTES_FILE_PATH}"
  echo "File trimmed to ${CHARACTER_LIMIT} characters"
fi;