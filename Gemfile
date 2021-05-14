# frozen_string_literal: true
source "https://rubygems.org"

git_source(:github) { |repo_name| "https://github.com/#{repo_name}" }

gem "fastlane"

plugins_path = File.join(File.dirname(__FILE__), "fastlane", "Pluginfile")
eval_gemfile("fastlane/Pluginfile") if File.exist?(plugins_path)

gem "activesupport"

gem "pry"
gem "rubocop"
gem "rubocop-require_tools"
gem "rubocop-shopify"
