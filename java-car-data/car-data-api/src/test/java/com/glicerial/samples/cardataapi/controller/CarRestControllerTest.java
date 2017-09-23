package com.glicerial.samples.cardataapi.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.glicerial.samples.cardataapi.Application;
import com.glicerial.samples.cardataapi.entity.Car;
import com.glicerial.samples.cardataapi.repository.CarRepository;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CarRestControllerTest {
    private List<Car> carList = new ArrayList<>();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        carRepository.deleteAll();

        carList.add(carRepository.save(new Car(2017, "Nissan", "Maxima")));
        carList.add(carRepository.save(new Car(2016, "Tesla", "Model S")));
    }

    @Test
    public void readCars() throws Exception {
        mockMvc.perform(get("/resources/cars"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].year", is(carList.get(0).getYear())))
            .andExpect(jsonPath("$[0].make", is(carList.get(0).getMake())))
            .andExpect(jsonPath("$[0].model", is(carList.get(0).getModel())))
            .andExpect(jsonPath("$[1].year", is(carList.get(1).getYear())))
            .andExpect(jsonPath("$[1].make", is(carList.get(1).getMake())))
            .andExpect(jsonPath("$[1].model", is(carList.get(1).getModel())));
    }

    @Test
    public void addCar() throws Exception {
        String carJson = json(new Car(2018, "Mazda", "CX-5"));

        mockMvc.perform(post("/resources/cars")
            .contentType(contentType)
            .content(carJson))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }
}
