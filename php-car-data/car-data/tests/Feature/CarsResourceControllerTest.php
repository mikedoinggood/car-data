<?php

namespace Tests\Feature;

use App\User;
use App\Car;
use App\TrimLevel;
use Tests\TestCase;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Facades\DB;

class CarControllerTest extends TestCase
{
    use RefreshDatabase;

    const MIN_YEAR = 1885;
    const MAX_YEAR = 3000;

    public function setUp() :void
    {
        parent::setUp();

        $this->carsUrl = "/resources/cars";
        $this->statsUrl = "/resources/stats";
        $this->user = factory(User::class)->create();
        $this->setupCars();
    }

    public function testReadCars()
    {
        $response = $this->actingAs($this->user)->json("GET", $this->carsUrl);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(0, count($responseData["data"][0]["trim_levels"]));
        $this->assertSame(2, count($responseData["data"][1]["trim_levels"]));
        $this->assertSame(2, count($responseData["data"]));
    }

    public function testReadOneCar()
    {
        $existingCarId = $this->carArray[0]->id;
        $response = $this->actingAs($this->user)->json("GET", $this->carsUrl . "/" . $existingCarId);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2017, $responseData["year"]);
        $this->assertSame("Honda", $responseData["make"]);
        $this->assertSame("Accord", $responseData["model"]);
        $this->assertSame("EX", $responseData["trim_levels"][0]["name"]);
        $this->assertSame("LX", $responseData["trim_levels"][1]["name"]);
        $this->assertSame(2, count($responseData["trim_levels"]));
    }

    public function testReadOneCarNotFound()
    {
        $response = $this->actingAs($this->user)->json("GET", $this->carsUrl . "/9999");
        $this->printResponse($response);

        $response->assertStatus(404);
    }

    public function testAddCar()
    {
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2010, $responseData["year"]);
        $this->assertSame("Toyota", $responseData["make"]);
        $this->assertSame("Prius", $responseData["model"]);
        $this->assertSame("Plus", $responseData["trim_levels"][0]["name"]);
        $this->assertSame("Premium", $responseData["trim_levels"][1]["name"]);
        $this->assertSame("Advanced", $responseData["trim_levels"][2]["name"]);
        $this->assertSame(3, count($responseData["trim_levels"]));
    }

    public function testAddCarNoYear()
    {
        unset($this->testCar["year"]);
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarEmptyYear()
    {
        $this->testCar["year"] = "";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarNonNumericYear()
    {
        $this->testCar["year"] = "abc";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarWhitespaceYear()
    {
        $this->testCar["year"] = " ";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarYear1884()
    {
        $this->doAddCarTestWithYear(1884);
    }

    public function testAddCarYear1885()
    {
        $this->doAddCarTestWithYear(1885);
    }

    public function testAddCarYear1886()
    {
        $this->doAddCarTestWithYear(1886);
    }

    public function testAddCarYear2999()
    {
        $this->doAddCarTestWithYear(2999);
    }

    public function testAddCarYear3000()
    {
        $this->doAddCarTestWithYear(3000);
    }

    public function testAddCarYear3001()
    {
        $this->doAddCarTestWithYear(3001);
    }

    public function testAddCarNoMake()
    {
        unset($this->testCar["make"]);
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarEmptyMake()
    {
        $this->testCar["make"] = "";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarWhitespaceMake()
    {
        $this->testCar["make"] = " ";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarNoModel()
    {
        unset($this->testCar["model"]);
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarEmptyModel()
    {
        $this->testCar["model"] = "";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarWhitespaceModel()
    {
        $this->testCar["model"] = " ";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testAddCarNoTrimLevels()
    {
        unset($this->testCar["trimLevels"]);
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(0, count($responseData["trim_levels"]));
    }

    public function testAddCarEmptyTrimLevels()
    {
        $this->testCar["trimLevels"] = "";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(0, count($responseData["trim_levels"]));
    }

    public function testAddCarNewEmptyTrimLevel()
    {
        array_push($this->testCar["trimLevels"], array("name" => ""));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(3, count($responseData["trim_levels"]));
    }

    public function testAddCarNewWhitespaceTrimLevel()
    {
        array_push($this->testCar["trimLevels"], array("name" => " "));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(3, count($responseData["trim_levels"]));
    }

    public function testAddCarNewBlankTrimLevel()
    {
        array_push($this->testCar["trimLevels"], json_encode((object) null));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(3, count($responseData["trim_levels"]));
    }

    public function testAddCarExtraData()
    {
        $this->testCar["something"] = "extra";
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(200);
    }

    public function testEditCar()
    {
        $existingCar = $this->carArray[0];
        echo("\nExisting car:\n");
        print_r(json_encode($existingCar, JSON_PRETTY_PRINT));
        echo("\nTrim levels:\n");
        print_r(json_encode($existingCar->trimLevels, JSON_PRETTY_PRINT));

        $this->testCar["trimLevels"] = array();
        array_push($this->testCar["trimLevels"], array("id" => $existingCar->trimlevels[0]->id, "name" => "A"));
        array_push($this->testCar["trimLevels"], array("id" => $existingCar->trimlevels[1]->id, "name" => "B"));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2010, $responseData["year"]);
        $this->assertSame("Toyota", $responseData["make"]);
        $this->assertSame("Prius", $responseData["model"]);
        $this->assertSame("A", $responseData["trim_levels"][0]["name"]);
        $this->assertSame("B", $responseData["trim_levels"][1]["name"]);
        $this->assertSame(2, count($responseData["trim_levels"]));
    }

    public function testEditCarNotFound()
    {
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/9999", $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(404);
    }

    public function testEditCarNoYear()
    {
        unset($this->testCar["year"]);
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarEmptyYear()
    {
        $this->testCar["year"] = "";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarNonNumericYear()
    {
        $this->testCar["year"] = "abc";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarWhitespaceYear()
    {
        $this->testCar["year"] = " ";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarYear1884()
    {
        $existingCar = $this->carArray[0];
        $this->doEditCarTestWithYear($existingCar->id, 1884);
    }

    public function testEditCarYear1885()
    {
        $existingCar = $this->carArray[0];
        $this->doEditCarTestWithYear($existingCar->id, 1885);
    }

    public function testEditCarYear1886()
    {
        $existingCar = $this->carArray[0];
        $this->doEditCarTestWithYear($existingCar->id, 1886);
    }

    public function testEditCarYear2999()
    {
        $existingCar = $this->carArray[0];
        $this->doEditCarTestWithYear($existingCar->id, 2999);
    }

    public function testEditCarYear3000()
    {
        $existingCar = $this->carArray[0];
        $this->doEditCarTestWithYear($existingCar->id, 3000);
    }

    public function testEditCarYear3001()
    {
        $existingCar = $this->carArray[0];
        $this->doEditCarTestWithYear($existingCar->id, 3001);
    }

    public function testEditCarNoMake()
    {
        unset($this->testCar["make"]);
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarEmptyMake()
    {
        $this->testCar["make"] = "";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarWhitespaceMake()
    {
        $this->testCar["make"] = " ";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarNoModel()
    {
        unset($this->testCar["model"]);
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarEmptyModel()
    {
        $this->testCar["model"] = "";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarWhitespaceModel()
    {
        $this->testCar["model"] = " ";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarNoTrimLevels()
    {
        unset($this->testCar["trimLevels"]);
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2, count($responseData["trim_levels"]));
    }

    public function testEditCarExistingTrimLevelNoName()
    {
        $existingCar = $this->carArray[0];
        $existingTrimLevelId = $existingCar->trimLevels[0]->id;
        array_push($this->testCar["trimLevels"], array("id" => $existingTrimLevelId));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarExistingTrimLevelEmptyName()
    {
        $existingCar = $this->carArray[0];
        $existingTrimLevelId = $existingCar->trimLevels[0]->id;
        array_push($this->testCar["trimLevels"], array("id" => $existingTrimLevelId, "name" => ""));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);

        $response->assertStatus(422);
    }

    public function testEditCarTrimLevelNotFound()
    {
        array_push($this->testCar["trimLevels"], array("id" => 9999, "name" => "Invalid"));
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(5, count($responseData["trim_levels"]));
    }

    public function testEditCarTrimLevelOtherCar()
    {
        $existingCar = $this->carArray[1];
        $otherCar = $this->carArray[0];
        $otherCarTrimLevelId = $otherCar->trimLevels[0]->id;
        array_push($this->testCar["trimLevels"], array("id" => $otherCarTrimLevelId, "name" => "should not update"));
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        echo("\nEdit car response:\n");
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2010, $responseData["year"]);
        $this->assertSame("Toyota", $responseData["make"]);
        $this->assertSame("Prius", $responseData["model"]);
        $this->assertSame("Plus", $responseData["trim_levels"][0]["name"]);
        $this->assertSame("Premium", $responseData["trim_levels"][1]["name"]);
        $this->assertSame("Advanced", $responseData["trim_levels"][2]["name"]);
        $this->assertSame(3, count($responseData["trim_levels"]));

        // Check other car
        $response = $this->actingAs($this->user)->json("GET", $this->carsUrl . "/" . $otherCar->id);
        echo("\nOther car:\n");
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2017, $responseData["year"]);
        $this->assertSame("Honda", $responseData["make"]);
        $this->assertSame("Accord", $responseData["model"]);
        $this->assertSame("EX", $responseData["trim_levels"][0]["name"]);
        $this->assertSame("LX", $responseData["trim_levels"][1]["name"]);
        $this->assertSame(2, count($responseData["trim_levels"]));
    }

    public function testEditCarNewTrimLevel() {
        $this->testCar["trimLevels"] = array(array("name" => "New Trim Level"));
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(3, count($responseData["trim_levels"]));
    }

    public function testEditCarNewBlankTrimLevel() {
        $this->testCar["trimLevels"] = array(json_encode((object) null));
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(2, count($responseData["trim_levels"]));
    }

    public function testEditCarExtraData() {
        $this->testCar["something"] = "extra";
        $this->printTestCar();

        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCar->id, $this->testCar);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();
    
        $response->assertStatus(200);
    }

    public function testDeleteCar() {
        $existingCar = $this->carArray[0];
        $response = $this->actingAs($this->user)->json("DELETE", $this->carsUrl . "/" . $existingCar->id);
        $this->printResponse($response);

        $response->assertStatus(200);

        $response = $this->actingAs($this->user)->json("GET", $this->carsUrl);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(1, count($responseData["data"]));
    }

    public function testDeleteCarNotFound() {
        $response = $this->actingAs($this->user)->json("DELETE", $this->carsUrl . "/9999");
        $this->printResponse($response);

        $response->assertStatus(404);
    }

    public function testReadStats() {
        $response = $this->actingAs($this->user)->json("GET", $this->statsUrl);
        $this->printResponse($response);
        $responseData = $response->decodeResponseJson();

        $response->assertStatus(200);
        $this->assertSame(1, $responseData["makeCounts"]["Honda"]);
        $this->assertSame(1, $responseData["makeCounts"]["Ford"]);
        $this->assertSame(1, $responseData["yearCounts"][2017]);
        $this->assertSame(1, $responseData["yearCounts"][2019]);
    }

    // Helper functions
    private function doAddCarTestWithYear($year)
    {
        $this->testCar["year"] = $year;
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("POST", $this->carsUrl, $this->testCar);
        $this->printResponse($response);

        if ($year < CarControllerTest::MIN_YEAR || $year > CarControllerTest::MAX_YEAR) {
            $response->assertStatus(422);
        } else {
            $response->assertStatus(200);
        }
    }

    private function doEditCarTestWithYear($existingCarId, $year)
    {
        $this->testCar["year"] = $year;
        $this->printTestCar();

        $response = $this->actingAs($this->user)->json("PUT", $this->carsUrl . "/" . $existingCarId, $this->testCar);
        $this->printResponse($response);

        if ($year < CarControllerTest::MIN_YEAR || $year > CarControllerTest::MAX_YEAR) {
            $response->assertStatus(422);
        } else {
            $response->assertStatus(200);
        }
    }

    private function setupCars()
    {
        // First car
        $car1 = new Car();
        $car1->year = 2017;
        $car1->make = "Honda";
        $car1->model = "Accord";
        $car1->save();

        $newTrimLevel1 = new TrimLevel();
        $newTrimLevel1->name = "EX";
        $newTrimLevel1->car_id = $car1->id;
        $newTrimLevel1->save();

        $newTrimLevel2 = new TrimLevel();
        $newTrimLevel2->name = "LX";
        $newTrimLevel2->car_id = $car1->id;
        $newTrimLevel2->save();

        // Second car
        $car2 = new Car();
        $car2->year = 2019;
        $car2->make = "Ford";
        $car2->model = "Focus";
        $car2->save();

        $this->carArray = array($car1, $car2);

        // Test car
        $this->testCar = array(
            "year" => 2010,
            "make" => "Toyota",
            "model" => "Prius",
            "trimLevels" => array(
                array("name" => "Plus"),
                array("name" => "Premium"),
                array("name" => "Advanced")
            )
        );
    }

    private function printResponse($response)
    {
        echo("\nResponse status: " . $response->status());
        echo("\nResponse JSON:\n");
        print_r($response->json());
    }

    private function printTestCar()
    {
        echo("\nTest car:\n");
        print_r(json_encode($this->testCar, JSON_PRETTY_PRINT));
    }
}
