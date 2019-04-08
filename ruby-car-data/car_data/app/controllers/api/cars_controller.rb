class Api::CarsController < ApplicationController
  before_action :logged_in_user

  def index
    page_size = 20.0
    page = [1, params[:page].to_i].max
    total_pages = (Car.count / page_size).ceil

    if !page.blank?
      offset = page_size * (page - 1)
    end

    case params[:sort]
    when "newest"
      sort = "year DESC, make"
    when "oldest"
      sort = "year ASC, make"
    else
      sort = "make"
    end

    cars = Car.all.order(sort).limit(page_size).offset(offset)
  
    car_list = cars.map do |c|
      convert_car_to_dict(c)
    end

    render json: { content: car_list, number: page, totalPages: total_pages }
  end

  def stats
    make_data = Car.group(:make).count
    year_data = Car.group(:year).count

    render json: { makeCounts: make_data, yearCounts: year_data }
  end

  def show
    begin
      car = Car.find(params[:id])

      render json: convert_car_to_dict(car)
    rescue ActiveRecord::RecordNotFound
      render json: {}, status: :not_found
    end
  end

  def create
    if !is_car_valid?(params[:car])
      render json: {}, status: :bad_request
    else
      car = Car.create(car_params)

      render json: convert_car_to_dict(car)
    end
  end

  def update
    begin
      car = Car.find(params[:id])

      if !is_car_valid?(params[:car])
        render json: {}, status: :bad_request
      else
        car.update(car_params)
        render json: convert_car_to_dict(car)
      end
    rescue ActiveRecord::RecordNotFound
      render json: {}, status: :not_found
    end
  end

  def destroy
    begin
        car = Car.find(params[:id])
        car.destroy

        render json: {}
    rescue ActiveRecord::RecordNotFound
      render json: {}, status: :not_found
    end
  end

  private
    def car_params
      params.require(:car).permit(:year, :make, :model, :trim_levels_attributes => [:id, :name, :_destroy])
    end

    def is_car_valid?(car)
      if car['year'].blank? || car['make'].blank? || car['model'].blank?
        return false
      end

      if car['year'].to_i < 1885 || car['year'].to_i > 3000
        return false
      end
 
      if !car['trim_levels_attributes'].blank?
        car['trim_levels_attributes'].map do |t|
          if !t['id'].blank? and t['name'].blank?
            return false
          end
        end
      end

      return true
    end

    def convert_car_to_dict(car)
        car_json = {  :id => car.id, :year => car.year, :make => car.make, :model => car.model}
        car_json['trimLevels'] = car.trim_levels.map do |t|
            { :id => t.id, :name => t.name}
        end

        return car_json
    end

    # Confirms a logged-in user.
    def logged_in_user
      unless logged_in?
        render json: {}, status: :forbidden
      end
    end
end
