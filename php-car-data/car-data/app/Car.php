<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Car extends Model
{
    public function trimLevels()
    {
        return $this->hasMany('App\TrimLevel');
    }
}
