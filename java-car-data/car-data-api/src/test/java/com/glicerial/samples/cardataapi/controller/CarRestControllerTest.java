package com.glicerial.samples.cardataapi.controller;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glicerial.samples.cardataapi.Application;
import com.glicerial.samples.cardataapi.entity.Car;
import com.glicerial.samples.cardataapi.entity.TrimLevel;
import com.glicerial.samples.cardataapi.repository.CarRepository;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CarRestControllerTest {
    private List<Car> carList = new ArrayList<>();
    private String carsUrl = "/resources/cars/";
    private ObjectNode carNode;
    private ObjectMapper mapper = new ObjectMapper();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private static final int MIN_VALID_MODEL_YEAR = 1885;
    private static final int MAX_VALID_MODEL_YEAR = 3000;

    @Rule
    public TestRule watcher = new CarRestControllerTestWatcher();

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

        // Add a car with trim levels
        Car carWithTrimLevels = new Car(2017, "Nissan", "Maxima");
        HashSet<TrimLevel> trimLevelSet = new HashSet<TrimLevel>();
        carWithTrimLevels.setTrimLevels(trimLevelSet);

        TrimLevel trimLevel1 = new TrimLevel("S");
        trimLevel1.setCar(carWithTrimLevels);
        trimLevelSet.add(trimLevel1);

        TrimLevel trimLevel2 = new TrimLevel("SV");
        trimLevel2.setCar(carWithTrimLevels);
        trimLevelSet.add(trimLevel2);

        TrimLevel trimLevel3 = new TrimLevel("L");
        trimLevel3.setCar(carWithTrimLevels);
        trimLevelSet.add(trimLevel3);

        carList.add(carRepository.save(carWithTrimLevels));

        // Add a car without trim levels
        carList.add(carRepository.save(new Car(2016, "Tesla", "Model S")));

        setupCarNode();
    }

    @Test
    public void readCars() throws Exception {
        mockMvc.perform(get(carsUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].year", is(carList.get(0).getYear())))
                .andExpect(jsonPath("$.content[0].make", is(carList.get(0).getMake())))
                .andExpect(jsonPath("$.content[0].model", is(carList.get(0).getModel())))
                .andExpect(jsonPath("$.content[0].trimLevels", hasSize(3)))
                .andExpect(jsonPath("$.content[1].year", is(carList.get(1).getYear())))
                .andExpect(jsonPath("$.content[1].make", is(carList.get(1).getMake())))
                .andExpect(jsonPath("$.content[1].model", is(carList.get(1).getModel())))
                .andExpect(jsonPath("$.content[1].trimLevels", hasSize(0)));
    }

    @Test
    public void readOneCar() throws Exception {
        Long carId = carList.get(0).getId();

        mockMvc.perform(get(carsUrl + carId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.trimLevels", hasSize(3)));
    }

    @Test
    public void readOneCarNotFound() throws Exception {
        mockMvc.perform(get(carsUrl + 9999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void addCar() throws Exception {
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.trimLevels", hasSize(2)));
    }

    @Test
    public void addCarNoYear() throws Exception {
        carNode.remove("year");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarEmptyYear() throws Exception {
        carNode.put("year", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarNonNumericYear() throws Exception {
        carNode.put("year", "abc");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarWhitespaceYear() throws Exception {
        carNode.put("year", " ");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarYear1884() throws Exception {
        doAddCarTestWithYear(1884);
    }

    @Test
    public void addCarYear1885() throws Exception {
        doAddCarTestWithYear(1885);
    }

    @Test
    public void addCarYear1886() throws Exception {
        doAddCarTestWithYear(1886);
    }

    @Test
    public void addCarYear2999() throws Exception {
        doAddCarTestWithYear(2999);
    }

    @Test
    public void addCarYear3000() throws Exception {
        doAddCarTestWithYear(3000);
    }

    @Test
    public void addCarYear3001() throws Exception {
        doAddCarTestWithYear(3001);
    }

    @Test
    public void addCarNoMake() throws Exception {
        carNode.remove("make");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarEmptyMake() throws Exception {
        carNode.put("make", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarWhitespaceMake() throws Exception {
        carNode.put("make", " ");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarNoModel() throws Exception {
        carNode.remove("model");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarEmptyModel() throws Exception {
        carNode.put("model", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarWhitespaceModel() throws Exception {
        carNode.put("model", " ");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarNoTrimLevels() throws Exception {
        carNode.remove("trimLevels");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(0)));
    }

    @Test
    public void addCarEmptyTrimLevels() throws Exception {
        carNode.put("trimLevels", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCarNewEmptyTrimLevel() throws Exception {
        ObjectNode trimLevelNode = mapper.createObjectNode();
        trimLevelNode.put("name",  "");
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(2)));
    }

    @Test
    public void addCarNewWhitespaceTrimLevel() throws Exception {
        ObjectNode trimLevelNode = mapper.createObjectNode();
        trimLevelNode.put("name",  " ");
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(2)));
    }

    @Test
    public void addCarNewBlankTrimLevel() throws Exception {
        ObjectNode trimLevelNode = mapper.createObjectNode();
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(2)));
    }

    @Test
    public void addCarExtraData() throws Exception {
        ObjectNode extraNode = mapper.createObjectNode();
        extraNode.put("something", "extra");
        carNode.set("extra", extraNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void editCar() throws Exception {
        Car existingCar = carList.get(0);
        existingCar.setYear(2018);
        existingCar.setMake("Toyota");
        existingCar.setModel("Corolla");

        Set<TrimLevel> trimLevelSet = existingCar.getTrimLevels();
        Iterator<TrimLevel> iterator = trimLevelSet.iterator();
        iterator.next().setName("Updated");

        String carJson = json(existingCar);
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + existingCar.getId())
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.year", is(existingCar.getYear())))
                .andExpect(jsonPath("$.make", is(existingCar.getMake())))
                .andExpect(jsonPath("$.model", is(existingCar.getModel())))
                .andExpect(jsonPath("$.trimLevels", hasSize(3)));
    }

    @Test
    public void editCarNotFound() throws Exception {
        String carJson = carNode.toString();

        mockMvc.perform(put(carsUrl + 9999)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void editCarNoYear() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.remove("year");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarEmptyYear() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("year", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarNonNumericYear() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("year", "abc");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarWhitespaceYear() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("year", " ");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarYear1884() throws Exception {
        doEditCarTestWithYear(1884);
    }

    @Test
    public void editCarYear1885() throws Exception {
        doEditCarTestWithYear(1885);
    }

    @Test
    public void editCarYear1886() throws Exception {
        doEditCarTestWithYear(1886);
    }

    @Test
    public void editCarYear2999() throws Exception {
        doEditCarTestWithYear(2999);
    }

    @Test
    public void editCarYear3000() throws Exception {
        doEditCarTestWithYear(3000);
    }

    @Test
    public void editCarYear3001() throws Exception {
        doEditCarTestWithYear(3001);
    }

    @Test
    public void editCarNoMake() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.remove("make");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarEmptyMake() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("make", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarWhitespaceMake() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("make", " ");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarNoModel() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.remove("model");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarEmptyModel() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("model", "");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarWhitespaceModel() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("model", " ");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarNoTrimLevels() throws Exception {
        Long carId = carList.get(0).getId();
        carNode.remove("trimLevels");
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(0)));
    }

    @Test
    public void editCarExistingTrimLevelNoName() throws Exception {
        // Get an existing car id and trim level id
        Car existingCar = carList.get(0);
        Long carId = existingCar.getId();
        Long existingTrimLevelId = getExistingTrimLevel(existingCar).getId();

        // Update car node to have trim level array having a trim level with valid id but no name
        ObjectNode trimLevelNode = mapper.createObjectNode();
        trimLevelNode.put("id",  existingTrimLevelId);
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarExistingTrimLevelEmptyName() throws Exception {
        // Get an existing car id and trim level id
        Car existingCar = carList.get(0);
        Long carId = existingCar.getId();
        Long existingTrimLevelId = getExistingTrimLevel(existingCar).getId();

        // Update car node to have trim level array having a trim level with valid id but empty name
        ObjectNode trimLevelNode = mapper.createObjectNode();
        trimLevelNode.put("id",  existingTrimLevelId);
        trimLevelNode.put("name",  "");
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editCarTrimLevelNotFound() throws Exception {
        Car existingCar = carList.get(0);
        Long carId = existingCar.getId();

        // Update car node to have trim level array having a trim level with id not belonging to car
        ObjectNode trimLevelNode = mapper.createObjectNode();
        trimLevelNode.put("id",  9999);
        trimLevelNode.put("name",  "Invalid");
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        int expectedYear = carNode.get("year").asInt();
        String expectedMake = carNode.get("make").asText();
        String expectedModel = carNode.get("model").asText();

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.year", is(expectedYear)))
                .andExpect(jsonPath("$.make", is(expectedMake)))
                .andExpect(jsonPath("$.model", is(expectedModel)))
                .andExpect(jsonPath("$.trimLevels", hasSize(2)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void editCarTrimLevelOtherCar() throws Exception {
        Car existingCar = carList.get(1);
        Long carId = existingCar.getId();
        Car otherExistingCar = carList.get(0);
        TrimLevel otherExistingTrimLevel = getExistingTrimLevel(otherExistingCar);

        // Update car node to have trim level array having a trim level with id of different car
        ObjectNode trimLevelNode = mapper.createObjectNode();
        trimLevelNode.put("id",  otherExistingTrimLevel.getId());
        trimLevelNode.put("name",  "Other Car Trim Level Updated");
        ArrayNode arrayNode = (ArrayNode) carNode.get("trimLevels");
        arrayNode.add(trimLevelNode);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        int expectedYear = carNode.get("year").asInt();
        String expectedMake = carNode.get("make").asText();
        String expectedModel = carNode.get("model").asText();

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.year", is(expectedYear)))
                .andExpect(jsonPath("$.make", is(expectedMake)))
                .andExpect(jsonPath("$.model", is(expectedModel)))
                .andExpect(jsonPath("$.trimLevels", hasSize(2)))
                .andDo(print())
                .andExpect(status().isOk());

        // Check other car's trim level
        MvcResult result = mockMvc.perform(get(carsUrl + otherExistingCar.getId())
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(3)))
                .andReturn();

        String resultJsonString = result.getResponse().getContentAsString();
        Car responseCar = mapper.readValue(resultJsonString, Car.class);

        for (TrimLevel tl: responseCar.getTrimLevels()) {
            if (otherExistingTrimLevel.getId().equals(tl.getId())) {
                assertEquals(otherExistingTrimLevel.getName(), tl.getName());
                break;
            }
        }
    }

    @Test
    public void editCarNewTrimLevel() throws Exception {
        Car existingCar = carList.get(0);
        TrimLevel newTrimLevel = new TrimLevel("New Trim Level");
        existingCar.getTrimLevels().add(newTrimLevel);

        String carJson = json(existingCar);
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + existingCar.getId())
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(4)));
    }

    @Test
    public void editCarNewBlankTrimLevel() throws Exception {
        Car existingCar = carList.get(0);
        System.out.println("Existing car:");
        printCar(existingCar);

        ObjectNode blankTrimLevel = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(blankTrimLevel);
        carNode.putArray("trimLevels").addAll(arrayNode);
        String carJson = carNode.toString();
        System.out.println("\ncarJson:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(carNode));

        mockMvc.perform(put(carsUrl + existingCar.getId())
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevels", hasSize(0)));
    }

    @Test
    public void editCarExtraData() throws Exception {
        Car existingCar = carList.get(0);
        Long carId = existingCar.getId();

        ObjectNode extraNode = mapper.createObjectNode();
        extraNode.put("something", "extra");
        carNode.set("extra", extraNode);

        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCar() throws Exception {
        Long idToDelete = carList.get(0).getId();

        mockMvc.perform(delete(carsUrl + idToDelete)
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCarNotFound() throws Exception {
        mockMvc.perform(delete("/resources/cars/9999")
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void readStats() throws Exception {
        // Add another 2016
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/resources/stats/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.makeCounts.Nissan", is(1)))
                .andExpect(jsonPath("$.makeCounts.Tesla", is(1)))
                .andExpect(jsonPath("$.makeCounts.Honda", is(1)))
                .andExpect(jsonPath("$.yearCounts.2017", is(1)))
                .andExpect(jsonPath("$.yearCounts.2016", is(2)));
    }

    private TrimLevel getExistingTrimLevel(Car existingCar) {
        Set<TrimLevel> existingTrimLevelSet = existingCar.getTrimLevels();
        Iterator<TrimLevel> iterator = existingTrimLevelSet.iterator();

        return iterator.next();
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);

        return mockHttpOutputMessage.getBodyAsString();
    }

    private void setupCarNode() {
        carNode = mapper.createObjectNode();
        carNode.put("year",  2016);
        carNode.put("make", "Honda");
        carNode.put("model", "Civic");

        ObjectNode trimLevelNode1 = mapper.createObjectNode();
        trimLevelNode1.put("name", "EX");
        ObjectNode trimLevelNode2 = mapper.createObjectNode();
        trimLevelNode2.put("name", "LX");
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add(trimLevelNode1);
        arrayNode.add(trimLevelNode2);
        carNode.putArray("trimLevels").addAll(arrayNode);
    }

    private void doAddCarTestWithYear(int year) throws Exception {
        carNode.put("year", year);
        String carJson = carNode.toString();
        System.out.println("carJson;\n" + carJson);

        ResultMatcher expectedStatus;

        if (year < MIN_VALID_MODEL_YEAR || year > MAX_VALID_MODEL_YEAR) {
            expectedStatus = status().isBadRequest();
        } else {
            expectedStatus = status().isOk();
        }

        mockMvc.perform(post(carsUrl)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(expectedStatus);

    }

    private void doEditCarTestWithYear(int year) throws Exception {
        Long carId = carList.get(0).getId();
        carNode.put("id", carId);
        carNode.put("year", year);
        String carJson = carNode.toString();
        System.out.println("carJson:\n" + carJson);

        ResultMatcher expectedStatus;

        if (year < MIN_VALID_MODEL_YEAR || year > MAX_VALID_MODEL_YEAR) {
            expectedStatus = status().isBadRequest();
        } else {
            expectedStatus = status().isOk();
        }

        mockMvc.perform(put(carsUrl + carId)
                .contentType(contentType)
                .content(carJson))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    private void printCar(Car car) {
        System.out.println("ID: " + car.getId());
        System.out.println("Year: " + car.getYear());
        System.out.println("Make: " + car.getMake());
        System.out.println("Model: " + car.getModel());
        System.out.println("Trim Levels:");
        for (TrimLevel tl : car.getTrimLevels()) {
            System.out.println("  ID: " + tl.getId() + ", " + "Name: " + tl.getName());
        }
    }

    private class CarRestControllerTestWatcher extends TestWatcher {
        @Override
        protected void starting(Description description) {
            System.out.println("*** Starting test: " + description.getMethodName() + "() ***");
        }
    }
}
