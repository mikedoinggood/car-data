ENV['RAILS_ENV'] ||= 'test'
require_relative '../config/environment'
require 'rails/test_help'

class ActiveSupport::TestCase
  # Setup all fixtures in test/fixtures/*.yml for all tests in alphabetical order.
  fixtures :all

  # Add more helper methods to be used by all tests here...
  MIN_MODEL_YEAR = 1885
  MAX_MODEL_YEAR = 3000

  def sign_in()
    post login_url("session[username]": "user", "session[password]": "secret")
  end

  def pretty_print(data)
    puts JSON.pretty_generate(data)
  end

  def print_response(response)
    puts "Response:"
    puts "Status: #{response.status}"
    puts "Body:\n"
    pretty_print(JSON.parse(response.body))
  end

  def print_test_car(test_car)
    puts "Test car:"
    pretty_print(test_car)
  end

  def do_add_car_test_with_year(test_car, year)
    test_car[:car][:year] = year
    print_test_car(test_car)

    post api_cars_url, params: test_car, as: :json
    print_response(@response)

    if year < MIN_MODEL_YEAR || year > MAX_MODEL_YEAR
      assert_response :bad_request
    else
      assert_response :success
    end
  end

  def do_edit_car_test_with_year(existing_car_id, test_car, year)
    test_car[:car][:year] = year
    print_test_car(test_car)

    put api_cars_url + "/#{existing_car_id}", params: test_car, as: :json
    print_response(@response)

    if year < MIN_MODEL_YEAR || year > MAX_MODEL_YEAR
      assert_response :bad_request
    else
      assert_response :success
    end
  end
end
