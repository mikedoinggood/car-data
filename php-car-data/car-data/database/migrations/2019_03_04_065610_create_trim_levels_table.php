<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTrimLevelsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('trim_levels', function (Blueprint $table) {
            $table->bigIncrements('id');
	    $table->string('name');
	    $table->unsignedBigInteger('car_id');
	    $table->foreign('car_id')->references('id')->on('cars')->onDelete('cascade')->onUpdate('cascade');
	    $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('trim_levels');
    }
}
