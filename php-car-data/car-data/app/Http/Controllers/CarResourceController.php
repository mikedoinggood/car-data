<?php

namespace App\Http\Controllers;

use App\Car;
use App\TrimLevel;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;

class CarResourceController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    {
         $page_size = 20;
         $sort = "make";
         $order = "asc";

         if ($request->sort == "newest") {
             $cars = Car::with('trimLevels')->orderBy('year', 'desc')->orderBy('make', 'asc')->paginate($page_size);
         } else if ($request->sort == "oldest") {
             $cars = Car::with('trimLevels')->orderBy('year', 'asc')->orderBy('make', 'asc')->paginate($page_size);
         } else {
             $cars = Car::with('trimLevels')->orderBy('make', 'asc')->paginate($page_size);
         }

         return $cars;
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $this->validateRequest($request);

        $car = new Car();
        $car->year = $request->year;
        $car->make = $request->make;
	$car->model = $request->model;

        DB::transaction(function() use ($car, $request) {
            $car->save();
            $this->saveTrimLevels($car, $request->trimLevels);
        });

	return response()->json();
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Car  $car
     * @return \Illuminate\Http\Response
     */
    public function show(Car $car)
    {
        return Car::with('trimLevels')->findOrFail($car->id);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Car  $car
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Car $car)
    {
        $this->validateRequest($request);

        $car->year = $request->year;
        $car->make = $request->make;
        $car->model = $request->model;

        DB::transaction(function() use ($car, $request) {
            $car->save();
            $this->saveTrimLevels($car, $request->trimLevels);
        });

        return response()->json();
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Car  $car
     * @return \Illuminate\Http\Response
     */
    public function destroy(Car $car)
    {
        $car->delete();

        return response()->json();
    }

    /**
     * Validate car.
     */
    private function validateRequest($request) {
        $request->validate([
            'year' => 'required|gte:1885|lte:3000',
            'make' => 'required',
            'model' => 'required',
        ]);

        foreach ($request->trimLevels as $trimLevel) {
            $v = Validator::make($trimLevel, [
                // No rules to define that always apply
            ]);
            // Require name if id exists
            $v->sometimes('name', 'required', function ($input) {
                return !is_null($input->get("id"));
            });
            $v->validate();
        }
    }

    /**
     * Delete, update, or add trim levels.
     * Skip new entries that are blank.
     */
    private function saveTrimLevels($car, $requestTrimLevels) {
        foreach($requestTrimLevels as $trimLevel) {
            // Deletes and updates
            if (array_key_exists('id', $trimLevel)) {
                $existingTrimLevel = $car->trimLevels()->where('id', $trimLevel["id"]);

                if (!is_null($existingTrimLevel->get())) {
                    if (array_key_exists('delete', $trimLevel)) {
                        $existingTrimLevel->delete();
                    } else {
                        $existingTrimLevel->update(['name' => $trimLevel["name"]]);
                    }
                }
            // Adds
            } else {
                if (!trim($trimLevel["name"]) == "") {
                    $newTrimLevel = new TrimLevel();
                    $newTrimLevel->name = $trimLevel["name"];
                    $newTrimLevel->car_id = $car->id;
                    $car->trimLevels->add($newTrimLevel);
                }
            }
        }

        $car->push();
    }

    /**
     * Get stats.
     */
    public function stats()
    {
        $groupedByMake = Car::all()->groupBy('make');
        $makeCountData = array();
        foreach ($groupedByMake as $make => $cars) {
            $makeCountData[$make] = count($cars);
        }

        $groupedByYear = Car::all()->groupBy('year');
        $yearCountData = array();
        foreach ($groupedByYear as $year => $cars) {
            $yearCountData[$year] = count($cars);
        }

        return response()->json(["makeCounts" => $makeCountData, "yearCounts" => $yearCountData], 200);
    }
}
