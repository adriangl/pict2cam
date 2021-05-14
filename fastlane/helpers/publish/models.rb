# frozen_string_literal: true
class PublishVersionResult
  attr_reader :published
  attr_reader :version_name
  attr_reader :version_code

  def initialize(published, version_name = nil, version_code = nil)
    @published = published
    @version_name = version_name
    @version_code = version_code
  end
end
