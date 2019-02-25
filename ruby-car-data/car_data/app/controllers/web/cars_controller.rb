class Web::CarsController < ApplicationController
  before_action :logged_in_user

  # Confirms a logged-in user.
  def logged_in_user
    unless logged_in?
      redirect_to login_url
    end
  end
end
