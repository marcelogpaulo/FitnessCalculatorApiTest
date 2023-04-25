package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.Constants;
import constants.RequestParameter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import utils.BaseResponseModel;

import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CommonSteps implements Constants {

    //TODO when i provide this class and ask for suggestions
    //TODO Add more descriptive error messages: When throwing an exception or failing an assertion, it's a good practice to provide a more descriptive error message. This will make it easier to diagnose the issue when the test fails.
    //TODO Consider using an assertion library: The code currently uses JUnit assertions, but you might consider using a more comprehensive assertion library like AssertJ or Hamcrest. These libraries provide a wide range of assertion methods that can make your tests more expressive and easier to read.
    //TODO Add logging: Adding logging statements throughout the code can help with debugging when tests fail. You could add log statements at key points in the code to output information such as the request and response headers, the request URL, and the response body. This will help you quickly identify issues when tests fail.
    //TODO validar como fazer teste de performance

    public static Response lastResponse;
    public static int lastStatusCode;

//    Instead of hard-coding the path of the schema for each endpoint, you can store all the paths in a map and retrieve them based on the endpoint.
//    This will make the code more modular and easier to maintain.
    public static Map<String, String> endpointSchemas = Map.of(
            "/idealweight", "schemas/BaseSchemas/IdealWeightSuccessSchema.json",
            "/bmi", "schemas/BaseSchemas/BmiSuccessSchema.json",
            "/dailycalorie", "schemas/BaseSchemas/DailyCalorieSuccessSchema.json",
            "/burnedcalorie", "schemas/BaseSchemas/BurnedCaloriesSuccessSchema.json",
            "/macrocalculator?", "schemas/BaseSchemas/MacrosCalculatorSuccessSchema.json"
    );

    public static String schemaPath;
    public static String endpointCucumber;
    public static RequestSpecification requestHeadersAndOnlyOneParameter;
    public static RequestSpecification requestHeadersWithoutParameters;
    public static RequestSpecification requestHeaderWithoutApiKey;
    public static RequestSpecification requestHeadersAndAllParameters;
    public BaseResponseModel baseResponseModel;

    @Given("I have the API endpoint {string}")
    public void i_have_the_api_endpoint(String endpoint) {
        choseRightSchemaPath(endpoint);
        validateEndpoint(endpoint);
        RestAssured.baseURI = URL + endpoint;
    }

    @When("I send a GET request with all parameters")
    public void sendGetRequest() throws Exception {
        getRequestSpecification();
        getRequestBuilder();
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        getResponseBodyAsObject();
        validateRequestResult();
    }

    @When("I send a GET request using only the parameter {string} and the value {string}")
    public void iSendAGETRequestUsingOnlyTheGenderParameter(String firstParameter, String value) throws Exception {
        requestHeadersAndOnlyOneParameter = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(firstParameter, value);
        getRequestBuilder(RequestParameter.FIRST_PARAMETER_ONLY);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        getResponseBodyAsObject();
        validateRequestResult();
        //TODO ValidateRequestErrors
    }

    @When("I send a GET request using no parameter")
    public void iSendAGETRequestUsingNoParameters() throws Exception {
        requestHeadersWithoutParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE);
        getRequestBuilder(RequestParameter.NO_PARAMETERS);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        getResponseBodyAsObject();
        validateRequestResult();
    }

    @When("I send a GET request without the API_KEY")
    public void iSendAGETRequestWithoutApiKey() throws Exception {
        requestHeaderWithoutApiKey = given().headers( API_HOST_KEY, API_HOST_VALUE);
        getRequestBuilder(RequestParameter.NO_API_KEY);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        getResponseBodyAsObject();
        validateRequestResult();
    }

    @Then("the response should have status code {int}")
    public void the_response_should_have_status_code(Integer code) {
        System.out.println("Printing the Response Body");
        lastResponse.prettyPrint();
        lastResponse.then().statusCode(code);
    }

    @And("schema is correct")
    public void validateSchemaCucumberStep() throws Exception {
        choseRightSchemaPath(endpointCucumber);
        handleSchemaBasedOnStatusCode(lastStatusCode);
    }

    public void validateEndpoint(String endpoint) {
        if (schemaPath == null) {
            throw new IllegalArgumentException("Invalid endpoint: " + endpoint);
        }
        endpointCucumber = endpoint;
    }

    public void choseRightSchemaPath(String endpoint) {
        schemaPath = endpointSchemas.get(endpoint);
    }

    public void getRequestBuilder(RequestParameter parameter) throws Exception {
        switch (parameter) {
            case FIRST_PARAMETER_ONLY -> lastResponse = requestHeadersAndOnlyOneParameter.get();
            case NO_PARAMETERS -> lastResponse = requestHeadersWithoutParameters.get();
            case NO_API_KEY -> lastResponse = requestHeaderWithoutApiKey.get();
            default -> throw new Exception("Option for Parameter is not valid: " + parameter);
        }
    }

    public void getRequestBuilder() throws Exception {
        switch (endpointCucumber) {
            case IDEAL_WEIGHT_ENDPOINT
                , BMI_ENDPOINT
                , MACROS_CALCULATOR_ENDPOINT
                , BURNED_CALORIE_ENDPOINT
                , DAILY_CALORIE_ENDPOINT -> lastResponse = requestHeadersAndAllParameters.get();
            default -> throw new Exception("Endpoint is not valid: " + endpointCucumber);
        }
    }

    public void getRequestSpecification() throws Exception {
        switch (endpointCucumber) {
            case IDEAL_WEIGHT_ENDPOINT -> requestHeadersAndAllParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(PARAMETER_GENDER, PARAMETER_GENDER_DEFAULT_VALUE, PARAMETER_HEIGHT, PARAMETER_HEIGHT_DEFAULT_VALUE);
            case BMI_ENDPOINT -> requestHeadersAndAllParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(PARAMETER_AGE, PARAMETER_AGE_DEFAULT_VALUE, PARAMETER_WEIGHT, PARAMETER_WEIGHT_DEFAULT_VALUE, PARAMETER_HEIGHT, PARAMETER_HEIGHT_DEFAULT_VALUE);
            case MACROS_CALCULATOR_ENDPOINT -> requestHeadersAndAllParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(PARAMETER_AGE, PARAMETER_AGE_DEFAULT_VALUE, PARAMETER_GENDER, PARAMETER_GENDER_DEFAULT_VALUE, PARAMETER_HEIGHT, PARAMETER_HEIGHT_DEFAULT_VALUE, PARAMETER_WEIGHT, PARAMETER_WEIGHT_DEFAULT_VALUE, PARAMETER_ACTIVITY_LEVEL, PARAMETER_ACTIVITY_LEVEL_DEFAULT_VALUE);
            case BURNED_CALORIE_ENDPOINT -> requestHeadersAndAllParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(PARAMETER_ACTIVITY_ID, PARAMETER_ACTIVITY_ID_DEFAULT_VALUE, PARAMETER_ACTIVITY_MIN, PARAMETER_ACTIVITY_MIN_DEFAULT_VALUE, PARAMETER_WEIGHT, PARAMETER_WEIGHT_DEFAULT_VALUE);
            case DAILY_CALORIE_ENDPOINT -> requestHeadersAndAllParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(PARAMETER_AGE, PARAMETER_AGE_DEFAULT_VALUE, PARAMETER_GENDER, PARAMETER_GENDER_DEFAULT_VALUE, PARAMETER_HEIGHT, PARAMETER_HEIGHT_DEFAULT_VALUE, PARAMETER_WEIGHT, PARAMETER_WEIGHT_DEFAULT_VALUE, PARAMETER_ACTIVITY_LEVEL, PARAMETER_ACTIVITY_LEVEL_DEFAULT_VALUE, PARAMETER_GOAL, PARAMETER_GOAL_DEFAULT_VALUE);
            default -> throw new Exception("Endpoint is not valid: " + endpointCucumber);
        }
    }

    public void handleSchemaBasedOnStatusCode(int statusCode) throws Exception {
        switch (statusCode) {
            case 200 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            case 422 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(SCHEMA_PATH_WRONG_PARAMETER));
            case 400 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(SCHEMA_PATH_BAD_REQUEST));
            case 401 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(SCHEMA_PATH_UNAUTHORIZED));
            default -> throw new Exception("StatusCode not mapped yet: " + statusCode);
        }

        System.out.println("Schema successfully validated");
    }

    public void saveResponseInCommonStepsClass(Response response) {
        lastResponse = response;
    }

    public void saveStatusCodeInCommonStepsClass() {
        lastStatusCode = lastResponse.getStatusCode();
        System.out.println("Printing Status Code: " + lastStatusCode);
    }

    public void validateResponseNotNull() {
        Assertions.assertTrue(Objects.nonNull(lastResponse));
    }

    public void getResponseBodyAsObject() throws JsonProcessingException {
        String responseString = lastResponse.asString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        baseResponseModel = mapper.readValue(responseString, BaseResponseModel.class);
    }

    public void validateRequestResult() {
        switch (lastStatusCode) {
            case 200 -> Assert.assertEquals("request_result_200", REQUEST_RESULT_200, baseResponseModel.request_result);
            case 422 -> Assert.assertEquals("request_result_422", REQUEST_RESULT_422, baseResponseModel.request_result);
            case 400 -> Assert.assertEquals("request_result_400", REQUEST_RESULT_400, baseResponseModel.request_result);
            case 401 -> Assert.assertEquals("request_result_401", REQUEST_RESULT_401, baseResponseModel.message);
        }
    }

    public void validateResponseBodyElementsAreNotNull() throws Exception {
        if (lastStatusCode == 200 || lastStatusCode == 422 || lastStatusCode == 400) {
            Assertions.assertNotNull(lastResponse.jsonPath().getString("status_code"), "status_code is null.");
            Assertions.assertNotNull(lastResponse.jsonPath().getString("request_result"), "request_result is null.");

            if (lastStatusCode == 200) {
                Assertions.assertNotNull(lastResponse.jsonPath().getString("data"), "data is null.");

                switch (endpointCucumber) {
                    case IDEAL_WEIGHT_ENDPOINT -> {
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.Hamwi"), "data.Hamwi is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.Devine"), "data.Devine is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.Miller"), "data.Miller is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.Robinson"), "data.Robinson is null.");
                    }
                    case BMI_ENDPOINT -> {
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.bmi"), "data.bmi is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.health"), "data.health is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.healthy_bmi_range"), "data.healthy_bmi_range is null.");
                    }
                    case BURNED_CALORIE_ENDPOINT -> {
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.burnedCalorie"), "data.burnedCalorie is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.unit"), "data.unit is null.");
                    }
                    case DAILY_CALORIE_ENDPOINT -> {
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.BMR"), "data.BMR is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals"), "data.goals is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.maintain weight"), "data.goals.maintain weight is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Mild weight loss"), "data.goals.Mild weight loss is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Mild weight loss.loss weight"), "data.goals.Mild weight loss.loss weight is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Mild weight loss.calory"), "data.goals.Mild weight loss.calory is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Weight loss"), "data.goals.Weight loss is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Weight loss.loss weight"), "data.goals.Weight loss.loss weight is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Weight loss.calory"), "data.goals.Weight loss.calory is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Extreme weight loss"), "data.goals.Extreme weight loss is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Extreme weight loss.loss weight"), "data.goals.Extreme weight loss.loss weight is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Extreme weight loss.calory"), "data.goals.Extreme weight loss.calory is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Mild weight gain"), "data.goals.Mild weight gain is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Mild weight gain.gain weight"), "data.goals.Mild weight gain.gain weight is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Mild weight gain.calory"), "data.goals.Mild weight gain.calory is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Weight gain"), "data.goals.Weight gain is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Weight gain.gain weight"), "data.goals.Weight gain.gain weight is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Weight gain.calory"), "data.goals.Weight gain.calory is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Extreme weight gain"), "data.goals.Extreme weight gain is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Extreme weight gain.gain weight"), "data.goals.Extreme weight gain.gain weight is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.goals.Extreme weight gain.calory"), "data.goals.Extreme weight gain.calory is null.");
                    }
                    case MACROS_CALCULATOR_ENDPOINT -> {
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.calorie"), "data.calorie is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.balanced"), "data.balanced is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.balanced.protein"), "data.balanced.protein is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.balanced.fat"), "data.balanced.fat is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.balanced.carbs"), "data.balanced.carbs is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowfat"), "data.lowfat is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowfat.protein"), "data.lowfat.protein is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowfat.fat"), "data.lowfat.fat is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowfat.carbs"), "data.lowfat.carbs is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowcarbs"), "data.lowcarbs is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowcarbs.protein"), "data.lowcarbs.protein is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowcarbs.fat"), "data.lowcarbs.fat is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.lowcarbs.carbs"), "data.lowcarbs.carbs is null.");

                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.highprotein"), "data.highprotein is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.highprotein.protein"), "data.highprotein.protein is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.highprotein.fat"), "data.highprotein.fat is null.");
                        Assertions.assertNotNull(lastResponse.jsonPath().getString("data.highprotein.carbs"), "data.highprotein.carbs is null.");
                    }
                    default -> throw new Exception("Endpoint not valid: " + endpointCucumber);
                }
            }
            else if (lastStatusCode == 422) {
                Assertions.assertNotNull(lastResponse.jsonPath().getString("errors"), "errors is null.");
            }
        }
        else {
            Assertions.assertNotNull(lastResponse.jsonPath().getString("message"), "message is null.");
        }
    }

}
