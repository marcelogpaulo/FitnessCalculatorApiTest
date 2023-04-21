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
    public BaseResponseModel baseResponseModel;

    @Given("I have the API endpoint {string}")
    public void i_have_the_api_endpoint(String endpoint) {
        choseRightSchemaPath(endpoint);
        validateEndpoint(endpoint);
        RestAssured.baseURI = URL + endpoint;
    }

    @When("I send a GET request using only the parameter {string} and the value {string}")
    public void iSendAGETRequestUsingOnlyTheGenderParameter(String firstParameter, String value) throws Exception {
        requestHeadersAndOnlyOneParameter = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams(firstParameter, value);
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
        requestHeadersWithoutParameters = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST);
        getRequestBuilder(RequestParameter.NO_PARAMETERS);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        getResponseBodyAsObject();
        validateRequestResult();
    }

    @When("I send a GET request without the API_KEY")
    public void iSendAGETRequestWithoutApiKey() throws Exception {
        requestHeaderWithoutApiKey = given().headers( "X-RapidAPI-Host", API_HOST);
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

    @And("schema is correct for the endpoint {string}")
    public void validateSchemaCucumberStep(String endpoint) {
        choseRightSchemaPath(endpoint);
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
            default -> throw new Exception("Option not valid: " + parameter);
        }
    }

    public void handleSchemaBasedOnStatusCode(int statusCode) {
        switch (statusCode) {
            case 200 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            case 422 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseWrongParameterSchema.json"));
            case 400 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseBadRequestSchema.json"));
            case 401 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseUnauthorizedSchema.json"));
        }

        System.out.println("Schema successfully validated");
    }

    public void saveResponseInCommonStepsClass(Response response) {
        lastResponse = response;
    }

    public void saveStatusCodeInCommonStepsClass() {
        lastStatusCode = lastResponse.getStatusCode();
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
            case 200 -> Assert.assertEquals("request_result_200", "Successful", baseResponseModel.request_result);
            case 422 -> Assert.assertEquals("request_result_422", "Unprocessable Entity", baseResponseModel.request_result);
            case 400 -> Assert.assertEquals("request_result_400", "Bad Request", baseResponseModel.request_result);
            case 401 -> Assert.assertEquals("request_result_401", "Invalid API key. Go to https://docs.rapidapi.com/docs/keys for more info.", baseResponseModel.message);
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
                    default -> throw new Exception("Option not valid: " + endpointCucumber);
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
