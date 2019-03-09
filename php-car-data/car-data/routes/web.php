<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

/*
 * Resources
*/
Route::resource('/resources/cars', 'CarResourceController');

Route::get('/resources/stats', 'CarResourceController@stats');


/*
 * Views
*/
Route::view('/', 'index')->middleware('auth');

Route::view('/cars/{id}', 'car_detail')->middleware('auth');

Route::view('/addcar', 'add_car')->middleware('auth');

Route::view('/cars/{id}/edit', 'edit_car')->middleware('auth');

Route::view('/charts', 'charts')->middleware('auth');

/*
 * Auth
 * Not using all routes created by Auth::routes(); entry that is
 * created after running 'php artisan make:auth'
*/
Route::get('login', 'Auth\LoginController@showLoginForm')->name('login');;

Route::post('login', 'Auth\LoginController@login');

Route::post('logout', 'Auth\LoginController@logout')->name('logout');;

