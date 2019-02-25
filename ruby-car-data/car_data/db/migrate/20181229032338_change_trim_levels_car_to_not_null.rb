class ChangeTrimLevelsCarToNotNull < ActiveRecord::Migration[5.2]
  def change
    change_column_null :trim_levels, :car_id, false
  end
end
