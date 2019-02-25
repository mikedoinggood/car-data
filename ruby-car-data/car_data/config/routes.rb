Rails.application.routes.draw do
  root   'web/cars#index'
  get    '/cars/:id',      to: 'web/cars#show'
  get    '/cars/:id/edit', to: 'web/cars#edit'
  get    '/addcar',        to: 'web/cars#new'
  get    '/charts',        to: 'web/cars#charts'
  get    '/login',         to: 'sessions#new'
  post   '/login',         to: 'sessions#create'
  delete '/logout',        to: 'sessions#destroy'

  namespace :api do
    get :stats, to: 'cars#stats'
    resources :cars
  end
end
