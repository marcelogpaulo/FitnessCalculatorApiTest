package utils;

import java.util.Map;

public class EndpointSchemas {

    public static Map<String, String> getEndpointSchemas() {
        return Map.of(
                "/idealweight", "schemas/BaseSchemas/IdealWeightSuccessSchema.json",
                "/bmi", "schemas/BaseSchemas/BmiSuccessSchema.json",
                "/dailycalorie", "schemas/BaseSchemas/DailyCalorieSuccessSchema.json",
                "/burnedcalorie", "schemas/BaseSchemas/BurnedCaloriesSuccessSchema.json",
                "/macrocalculator?", "schemas/BaseSchemas/MacrosCalculatorSuccessSchema.json"
        );
    }

}
