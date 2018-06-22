package com.glicerial.samples.cardata.web.uitests;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddCarsTest.class,
    CarDetailTest.class,
    DeleteCarTest.class,
    EditCarTest.class
})

public class CarDataTestSuite {

}
