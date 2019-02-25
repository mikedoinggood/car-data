class CreateTrimLevels < ActiveRecord::Migration[5.2]
  def change
    create_table :trim_levels do |t|
      t.references :car, foreign_key: true
      t.string :name

      t.timestamps
    end
  end
end
