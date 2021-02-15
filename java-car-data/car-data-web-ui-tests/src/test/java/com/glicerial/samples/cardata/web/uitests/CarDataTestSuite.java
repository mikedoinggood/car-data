package com.glicerial.samples.cardata.web.uitests;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddAndFindCarsTest.class,
    CarDetailTest.class,
    DeleteCarTest.class,
    EditCarTest.class,
    LoginTest.class
})

public class CarDataTestSuite {

}
