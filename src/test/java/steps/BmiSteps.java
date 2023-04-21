package steps;

import constants.Constants;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.BaseResponseModel;

import static io.restassured.RestAssured.given;

public class BmiSteps implements Constants {

    CommonSteps commonSteps = new CommonSteps();

    public static Response response;
    public static RequestSpecification requestHeadersAndAllParameters;
    public static RequestSpecification requestHeadersAndOnlyGenderParameter;
    public static RequestSpecification requestHeadersAndOnlyHeightParameter;
    public static RequestSpecification requestHeadersWithoutParameters;
    public static RequestSpecification requestHeaderWithoutApiKey;
    public static int statusCode;
    public static String responseBody;
    public static int age = 32;
    public static int weight = 105;
    public static int height = 175;

    public BaseResponseModel baseResponseModel;

    @When("I send a GET request using age {int}, weight {int} and height {int}")
    public void i_send_a_get_request(int ageCucumber, int weightCucumber, int heightCucumber) throws Exception {
        age = ageCucumber;
        weight = weightCucumber;
        height = heightCucumber;
        requestHeadersAndAllParameters = given().headers(API_KEY_VALUE, API_KEY_KEY, API_HOST_VALUE, API_HOST_KEY).queryParams("age", age, "weight", weight, "height", height);
//        getRequestBuilder(ALL_PARAMETERS);
    }
//
//    @When("I send a GET request using only the gender parameter")
//    public void iSendAGETRequestUsingOnlyTheGenderParameter() throws Exception {
//        gender = "male";
//        requestHeadersAndOnlyGenderParameter = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("gender", gender);
//        getRequestBuilder(GENDER_PARAMETER);
//    }
//
//    @When("I send a GET request using only the height parameter")
//    public void iSendAGETRequestUsingOnlyTheHeightParameter() throws Exception {
//        height = 150;
//        requestHeadersAndOnlyHeightParameter = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("height", height);
//        getRequestBuilder(HEIGHT_PARAMETER);
//    }

    public void getRequestBuilder(String parameter) throws Exception {
        switch (parameter) {
            case "all" -> response = requestHeadersAndAllParameters.get();
            case "gender" -> response = requestHeadersAndOnlyGenderParameter.get();
            case "height" -> response = requestHeadersAndOnlyHeightParameter.get();
            case "none" -> response = requestHeadersWithoutParameters.get();
            case "no_api_key" -> response = requestHeaderWithoutApiKey.get();
            default -> throw new Exception("Option not valid: " + parameter);
        }

//        getStatusCodeAndResponseBodyAsString();
//        validateResponseNotNull();
//        validateResponseBodyElementsAreNotNull();
        commonSteps.saveResponseInCommonStepsClass(response);
//        returnResponseBodyAsObject();
//        validateRequestResult();
//        validateRequestErrors();
    }
//
//    public void getStatusCodeAndResponseBodyAsString() {
//        statusCode = response.getStatusCode();
//        responseBody = response.getBody().asString();
//    }
//
//    public void returnResponseBodyAsObject() throws JsonProcessingException {
//        String responseString = response.asString();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        idealWeightResponseModel = mapper.readValue(responseString, IdealWeightResponseModel.class);
//    }
//
//    public void validateRequestErrors() {
//        if (statusCode == 422) {
//            if (!gender.equals("male") && !gender.equals("female")) {
//                if (height < 130 || height > 230) {
//                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", idealWeightResponseModel.errors.get(0));
//                    Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", idealWeightResponseModel.errors.get(1));
//                } else {
//                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", idealWeightResponseModel.errors.get(0));
//                }
//            } else {
//                Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", idealWeightResponseModel.errors.get(0));
//            }
//        }
//    }

}
