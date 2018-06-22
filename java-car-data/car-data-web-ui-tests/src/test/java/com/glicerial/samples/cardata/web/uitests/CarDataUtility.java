package com.glicerial.samples.cardata.web.uitests;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

import java.util.Map;

import org.apache.commons.text.RandomStringGenerator;

public class CarDataUtility {

    public String generateRandomTrimLevel() {
        int length = 5;
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(LETTERS, DIGITS)
            .build();

        return  generator.generate(length);
    }

    public String getCarString(Map<String, String> carMap) {
        return carMap.get("year") + " " +
            carMap.get("make") + " " +
            carMap.get("model") +
            " With trim levels: " + carMap.get("trimLevels").replaceAll("\n", ",");
    }
}
