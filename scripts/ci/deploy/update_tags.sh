#!/bin/sh -e
##########################
# Bumps patch version of current biggest tag in order to do incremental versioning based on Semantic Versioning
# (https://semver.org/).
#
# Verify that tag-flow (https://github.com/bq/tag-flow) is installed as a Git submodule in the project before running
# this.
#
# Usage:
# ./update_tags.sh
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

echo "${BOLD}Initialize and apply tag-flow...${NO_STYLE}"
git submodule update --init --recursive --remote
python tag-flow/tag.py
git push --tags