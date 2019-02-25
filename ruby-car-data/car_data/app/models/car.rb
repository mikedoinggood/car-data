class Car < ApplicationRecord
  has_many :trim_levels, dependent: :destroy
  accepts_nested_attributes_for :trim_levels, allow_destroy: true, reject_if: proc { |attributes| attributes['name'].blank? }
end
