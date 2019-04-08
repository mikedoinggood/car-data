require 'test_helper'

class Api::CarsControllerTest < ActionDispatch::IntegrationTest
  setup do
    @test_car = {
      "car": {
        "year": 2010,
        "make": "Toyota",
        "model": "Prius",
        "trim_levels_attributes": [
          {"name": "Plus"},
          {"name": "Premium"},
          {"name": "Advanced"},
        ]
      }
    }

    sign_in
  end

  test "read cars" do
    get api_cars_url, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 0, response_data['content'][0]['trimLevels'].length
    assert_equal 2, response_data['content'][1]['trimLevels'].length
    assert_equal 2, response_data['content'].length
  end

  test "read one car" do
    existing_car_id = cars(:one).id
    get api_cars_url + "/#{existing_car_id}", as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2017, response_data['year']
    assert_equal "Honda", response_data['make']
    assert_equal "Accord", response_data["model"]
    assert_equal "LX", response_data['trimLevels'][0]['name']
    assert_equal "EX", response_data['trimLevels'][1]['name']
    assert_equal 2, response_data['trimLevels'].length
  end

  test "read one car not found" do
    get api_cars_url + "/9999", as: :json
    print_response(@response)

    assert_response :not_found
  end

  test "add car" do
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2010, response_data['year']
    assert_equal "Toyota", response_data["make"]
    assert_equal "Prius", response_data["model"]
    assert_equal "Plus", response_data["trimLevels"][0]["name"]
    assert_equal "Premium", response_data["trimLevels"][1]["name"]
    assert_equal "Advanced", response_data["trimLevels"][2]["name"]
    assert_equal 3, response_data['trimLevels'].length
  end

  test "add car no year" do
    @test_car[:car].delete(:year)
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "add car empty year" do
    @test_car[:car][:year] = ""
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "add year 1884" do
    do_add_car_test_with_year(@test_car, 1884)
  end

  test "add year 1885" do
    do_add_car_test_with_year(@test_car, 1885)
  end

  test "add year 1886" do
    do_add_car_test_with_year(@test_car, 1886)
  end

  test "add year 2999" do
    do_add_car_test_with_year(@test_car, 2999)
  end

  test "add year 3000" do
    do_add_car_test_with_year(@test_car, 3000)
  end

  test "add year 3001" do
    do_add_car_test_with_year(@test_car, 3001)
  end

  test "add car no make" do
    @test_car[:car].delete(:make)
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "add car empty make" do
    @test_car[:car][:make] = ""
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "add car no model" do
    @test_car[:car].delete(:model)
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "add car empty model" do
    @test_car[:car][:model] = ""
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "add car no trim levels" do
    @test_car[:car].delete(:trim_levels_attributes)
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 0, response_data["trimLevels"].length
  end

  test "add car empty trim levels" do
    @test_car[:car][:trim_levels_attributes] = ""
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :success
  end

  test "add car new empty trim level" do
    @test_car[:car][:trim_levels_attributes].append({"name": ""})
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 3, response_data["trimLevels"].length
  end

  test "add car extra data" do
    @test_car[:extra] = {"something": "extra"}
    print_test_car(@test_car)

    post api_cars_url, params: @test_car, as: :json
    print_response(@response)

    assert_response :success
  end

  test "edit car" do
    existing_car = cars(:one)
    puts "Existing car:"
    pretty_print(existing_car.as_json)
    puts "Trim levels:"
    pretty_print(existing_car.trim_levels.as_json)

    @test_car[:car][:trim_levels_attributes].clear
    @test_car[:car][:trim_levels_attributes].append("id": existing_car.trim_levels[0].id, "name": "A")
    @test_car[:car][:trim_levels_attributes].append("id": existing_car.trim_levels[1].id, "name": "B")

    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2010, response_data["year"]
    assert_equal "Toyota", response_data["make"]
    assert_equal "Prius", response_data["model"]
    assert_equal "A", response_data["trimLevels"][0]["name"]
    assert_equal "B", response_data["trimLevels"][1]["name"]
    assert_equal 2, response_data['trimLevels'].length
  end

  test "edit car not found" do
    put api_cars_url + "/9999", params: @test_car, as: :json
    print_response(@response)

    assert_response :not_found
  end

  test "edit car no year" do
    @test_car[:car].delete(:year)
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car empty year" do
    @test_car[:car][:year] = ""
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car year 1884" do
    existing_car = cars(:one)
    do_edit_car_test_with_year(existing_car.id, @test_car, 1884)
  end

  test "edit car year 1885" do
    existing_car = cars(:one)
    do_edit_car_test_with_year(existing_car.id, @test_car, 1885)
  end

  test "edit car year 1886" do
    existing_car = cars(:one)
    do_edit_car_test_with_year(existing_car.id, @test_car, 1886)
  end

  test "edit car year 2999" do
    existing_car = cars(:one)
    do_edit_car_test_with_year(existing_car.id, @test_car, 2999)
  end

  test "edit car year 3000" do
    existing_car = cars(:one)
    do_edit_car_test_with_year(existing_car.id, @test_car, 3000)
  end

  test "edit car year 3001" do
    existing_car = cars(:one)
    do_edit_car_test_with_year(existing_car.id, @test_car, 3001)
  end

  test "edit car no make" do
    @test_car[:car].delete(:make)
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car empty make" do
    @test_car[:car][:make] = ""
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car no model" do
    @test_car[:car].delete(:model)
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car empty model" do
    @test_car[:car][:model] = ""
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car no trim levels" do
    @test_car[:car].delete(:trim_levels_attributes)
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2, response_data["trimLevels"].length
  end

  test "edit car existing trim level no name" do
    existing_car = cars(:one)
    existing_trim_level_id = existing_car.trim_levels[0]["id"]
    @test_car[:car][:trim_levels_attributes].append({"id": existing_trim_level_id})
    print_test_car(@test_car)

    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car existing trim level empty name" do
    existing_car = cars(:one)
    existing_trim_level_id = existing_car.trim_levels[0]["id"]
    @test_car[:car][:trim_levels_attributes].append({"id": existing_trim_level_id, "name": ""})
    print_test_car(@test_car)

    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :bad_request
  end

  test "edit car existing trim level not found" do
    existing_car = cars(:one)
    @test_car[:car][:trim_levels_attributes].append({"id": 9999, "name": "Invalid"})
    print_test_car(@test_car)

    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)

    assert_response :not_found
  end

  test "edit car trim level other car" do
    existing_car = cars(:two)
    other_car = cars(:one)
    other_car_trim_level = other_car.trim_levels[0].id
    @test_car[:car][:trim_levels_attributes].append({"id": other_car_trim_level, "name": "should not update"})
    print_test_car(@test_car)

    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    puts "Edit car response:"
    print_response(@response)

    assert_response :not_found

    # Check existing car
    get api_cars_url + "/#{existing_car.id}"
    puts "Existing car:"
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2019, response_data['year']
    assert_equal "Ford", response_data['make']
    assert_equal "Focus", response_data['model']
    assert_equal 0, response_data['trimLevels'].length

    # Check other car
    get api_cars_url + "/#{other_car.id}"
    puts "Other car:"
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2017, response_data['year']
    assert_equal "Honda", response_data['make']
    assert_equal "Accord", response_data['model']
    assert_equal "LX", response_data['trimLevels'][0]['name']
    assert_equal "EX", response_data['trimLevels'][1]['name']
    assert_equal 2, response_data['trimLevels'].length
  end

  test "edit car new trim level" do
    @test_car[:car].delete(:trim_levels_attributes)
    @test_car[:car][:trim_levels_attributes] = [{"name": "New Trim Level"}]
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 3, response_data["trimLevels"].length
  end

  test "edit car new blank trim level" do
    @test_car[:car].delete(:trim_levels_attributes)
    @test_car[:car][:trim_levels_attributes] = [{}]
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 2, response_data["trimLevels"].length
  end

  test "edit car extra data" do
    @test_car[:car][:extra] = {"something": "extra"}
    print_test_car(@test_car)

    existing_car = cars(:one)
    put api_cars_url + "/#{existing_car.id}", params: @test_car, as: :json
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
  end

  test "delete car" do
    existing_car = cars(:one)
    delete api_cars_url + "/#{existing_car.id}", as: :json
    print_response(@response)

    assert_response :success

    get api_cars_url
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 1, response_data['content'].length
  end

  test "delete non existing car" do
    delete api_cars_url + "/9999", as: :json
    print_response(@response)

    assert_response :not_found
  end

  test "read stats" do
    get api_stats_url
    print_response(@response)
    response_data = JSON.parse(@response.body)

    assert_response :success
    assert_equal 1, response_data['makeCounts']['Honda']
    assert_equal 1, response_data['makeCounts']['Ford']
    assert_equal 1, response_data['yearCounts']['2017']
    assert_equal 1, response_data['yearCounts']['2019']
  end
end
