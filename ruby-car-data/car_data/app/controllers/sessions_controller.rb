# https://www.railstutorial.org

class SessionsController < ApplicationController
  def create
    user = User.find_by(username: params[:session][:username].downcase)
    if user && user.authenticate(params[:session][:password])
      log_in user
      redirect_to controller: 'web/cars', action: 'index'
    else
      flash.now[:danger] = 'Invalid username or password.'
      render 'new'
    end
  end

  def destroy
    log_out
    redirect_to login_url
  end
end
