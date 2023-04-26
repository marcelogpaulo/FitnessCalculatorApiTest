package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.Constants;
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
import utils.EndpointSchemas;
import utils.HeaderParams;
import utils.QueryParams;

import java.util.HashMap;
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

    public static Response responseCommon;
    public static int statusCodeCommon;
    public static Map<String, String> endpointSchemas = EndpointSchemas.getEndpointSchemas();
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
        sendRequest(getRequestSpecificationWithAllParameters());
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        saveResponseBodyAsObject();
        validateRequestResult();
    }

    @When("I send a GET request with wrong parameters")
    public void sendGetRequestWrongParameters() throws Exception {
        sendRequest(getRequestSpecificationWithWrongParameters());
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        saveResponseBodyAsObject();
        validateRequestResult();
        validateRequestErrors();
    }

    @When("I send a GET request using only the parameter {string} and the value {string}")
    public void iSendAGETRequestUsingOnlyTheGenderParameter(String firstParameter, String value) throws Exception {
        requestHeadersAndOnlyOneParameter = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(firstParameter, value);
        sendRequest(requestHeadersAndOnlyOneParameter);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        saveResponseBodyAsObject();
        validateRequestResult();
    }

    @When("I send a GET request using no parameter")
    public void iSendAGETRequestUsingNoParameters() throws Exception {
        requestHeadersWithoutParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE);
        sendRequest(requestHeadersWithoutParameters);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        saveResponseBodyAsObject();
        validateRequestResult();
    }

    @When("I send a GET request without the API_KEY")
    public void iSendAGETRequestWithoutApiKey() throws Exception {
        requestHeaderWithoutApiKey = given().headers( API_HOST_KEY, API_HOST_VALUE);
        sendRequest(requestHeaderWithoutApiKey);
        saveStatusCodeInCommonStepsClass();
        validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        saveResponseBodyAsObject();
        validateRequestResult();
    }

    @Then("the response should have status code {int}")
    public void the_response_should_have_status_code(Integer code) {
        System.out.println("Printing the Response Body");
        responseCommon.prettyPrint();
        responseCommon.then().statusCode(code);
    }

    @And("schema is correct")
    public void validateSchemaCucumberStep() throws Exception {
        choseRightSchemaPath(endpointCucumber);
        handleSchemaBasedOnStatusCode(statusCodeCommon);
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

    public void sendRequest(RequestSpecification requestSpecification) throws Exception {
        try {
            responseCommon = requestSpecification.get();
        } catch (Exception e) {
            throw new Exception("Invalid RequestSpecification: " + requestSpecification);
        }
//        switch (parameter) {
//            case FIRST_PARAMETER_ONLY -> responseCommon = requestHeadersAndOnlyOneParameter.get();
//            case NO_PARAMETERS -> responseCommon = requestHeadersWithoutParameters.get();
//            case NO_API_KEY -> responseCommon = requestHeaderWithoutApiKey.get();
//            default -> throw new Exception("Option for Parameter is not valid: " + parameter);
//        }
    }

    public RequestSpecification getRequestSpecificationWithAllParametersTESTGGG() throws Exception {
        switch (endpointCucumber) {
            case IDEAL_WEIGHT_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(GENDER_PARAMETER, GENDER_DEFAULT_VALUE, HEIGHT_PARAMETER, HEIGHT_DEFAULT_VALUE)
                        .queryParams(QueryParams.getGenderParams())
                        .queryParams(QueryParams.getHeightParams());
            }
            case BMI_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(AGE_PARAMETER, AGE_DEFAULT_VALUE, WEIGHT_PARAMETER, WEIGHT_DEFAULT_VALUE, HEIGHT_PARAMETER, HEIGHT_DEFAULT_VALUE)
                        .queryParams(QueryParams.getAgeParams())
                        .queryParams(QueryParams.getWeightParams())
                        .queryParams(QueryParams.getHeightParams());
            }
            case MACROS_CALCULATOR_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(AGE_PARAMETER, AGE_DEFAULT_VALUE, GENDER_PARAMETER, GENDER_DEFAULT_VALUE, HEIGHT_PARAMETER, HEIGHT_DEFAULT_VALUE, WEIGHT_PARAMETER, WEIGHT_DEFAULT_VALUE, ACTIVITY_LEVEL_PARAMETER, ACTIVITY_LEVEL_DEFAULT_VALUE)
                        .queryParams(QueryParams.getAgeParams())
                        .queryParams(QueryParams.getGenderParams())
                        .queryParams(QueryParams.getHeightParams())
                        .queryParams(QueryParams.getWeightParams())
                        .queryParams(QueryParams.getActivityLevelParams());
            }
            case BURNED_CALORIE_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(ACTIVITY_ID_PARAMETER, ACTIVITY_ID_DEFAULT_VALUE, ACTIVITY_MIN_PARAMETER, ACTIVITY_MIN_DEFAULT_VALUE, WEIGHT_PARAMETER, WEIGHT_DEFAULT_VALUE)
                        .queryParams(QueryParams.getActivityIdParams())
                        .queryParams(QueryParams.getActivityMinParams())
                        .queryParams(QueryParams.getWeightParams());
            }
            case DAILY_CALORIE_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(AGE_PARAMETER, AGE_DEFAULT_VALUE, GENDER_PARAMETER, GENDER_DEFAULT_VALUE, HEIGHT_PARAMETER, HEIGHT_DEFAULT_VALUE, WEIGHT_PARAMETER, WEIGHT_DEFAULT_VALUE, ACTIVITY_LEVEL_PARAMETER, ACTIVITY_LEVEL_DEFAULT_VALUE, GOAL_PARAMETER, GOAL_DEFAULT_VALUE)
                        .queryParams(QueryParams.getAgeParams())
                        .queryParams(QueryParams.getGenderParams())
                        .queryParams(QueryParams.getHeightParams())
                        .queryParams(QueryParams.getWeightParams())
                        .queryParams(QueryParams.getActivityLevelParams())
                        .queryParams(QueryParams.getGoalParams());
            }
            default -> throw new Exception("Endpoint is not valid: " + endpointCucumber);
        }
    }

    public RequestSpecification getRequestSpecificationWithAllParameters() throws Exception {
        String[] headerNames = {API_KEY_KEY, API_HOST_KEY};
        String[] headerValues = {API_KEY_VALUE, API_HOST_VALUE};

        Map<String, String[]> queryParamsMap = new HashMap<>();
        queryParamsMap.put(IDEAL_WEIGHT_ENDPOINT, new String[] {GENDER_PARAMETER + "=" + GENDER_DEFAULT_VALUE, HEIGHT_PARAMETER + "=" + HEIGHT_DEFAULT_VALUE});
        queryParamsMap.put(BMI_ENDPOINT, new String[] {AGE_PARAMETER + "=" + AGE_DEFAULT_VALUE, WEIGHT_PARAMETER + "=" + WEIGHT_DEFAULT_VALUE, HEIGHT_PARAMETER + "=" + HEIGHT_DEFAULT_VALUE});
        queryParamsMap.put(MACROS_CALCULATOR_ENDPOINT, new String[] {AGE_PARAMETER + "=" + AGE_DEFAULT_VALUE, GENDER_PARAMETER + "=" + GENDER_DEFAULT_VALUE, HEIGHT_PARAMETER + "=" + HEIGHT_DEFAULT_VALUE, WEIGHT_PARAMETER + "=" + WEIGHT_DEFAULT_VALUE, ACTIVITY_LEVEL_PARAMETER + "=" + ACTIVITY_LEVEL_DEFAULT_VALUE});
        queryParamsMap.put(BURNED_CALORIE_ENDPOINT, new String[] {ACTIVITY_ID_PARAMETER + "=" + ACTIVITY_ID_DEFAULT_VALUE, ACTIVITY_MIN_PARAMETER + "=" + ACTIVITY_MIN_DEFAULT_VALUE, WEIGHT_PARAMETER + "=" + WEIGHT_DEFAULT_VALUE});
        queryParamsMap.put(DAILY_CALORIE_ENDPOINT, new String[] {AGE_PARAMETER + "=" + AGE_DEFAULT_VALUE, GENDER_PARAMETER + "=" + GENDER_DEFAULT_VALUE, HEIGHT_PARAMETER + "=" + HEIGHT_DEFAULT_VALUE, WEIGHT_PARAMETER + "=" + WEIGHT_DEFAULT_VALUE, ACTIVITY_LEVEL_PARAMETER + "=" + ACTIVITY_LEVEL_DEFAULT_VALUE, GOAL_PARAMETER + "=" + GOAL_DEFAULT_VALUE});

        if (!queryParamsMap.containsKey(endpointCucumber)) {
            throw new Exception("Endpoint is not valid: " + endpointCucumber);
        }

        String[] queryParams = queryParamsMap.get(endpointCucumber);

        RequestSpecification requestSpec = given();
        for (int i = 0; i < headerNames.length; i++) {
            requestSpec = requestSpec.header(headerNames[i], headerValues[i]);
        }
        for (String queryParam : queryParams) {
            requestSpec = requestSpec.queryParam(queryParam);
        }

//        System.out.println(requestSpec.get);
        //TODO parece que nao tá enviando os 2 headers

        return requestSpec;
    }

    public RequestSpecification getRequestSpecificationWithWrongParameters() throws Exception {
        switch (endpointCucumber) {
            case IDEAL_WEIGHT_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(GENDER_PARAMETER, GENDER_WRONG_VALUE, HEIGHT_PARAMETER, HEIGHT_WRONG_VALUE)
                        .queryParams(QueryParams.getGenderParamsWrongValue())
                        .queryParams(QueryParams.getHeightParamsWrongValue());
            }
            case BMI_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(AGE_PARAMETER, AGE_WRONG_VALUE, WEIGHT_PARAMETER, WEIGHT_WRONG_VALUE, HEIGHT_PARAMETER, HEIGHT_WRONG_VALUE)
                        .queryParams(QueryParams.getAgeParamsWrongValue())
                        .queryParams(QueryParams.getWeightParamsWrongValue())
                        .queryParams(QueryParams.getHeightParamsWrongValue());
            }
            case MACROS_CALCULATOR_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(AGE_PARAMETER, AGE_WRONG_VALUE, GENDER_PARAMETER, GENDER_WRONG_VALUE, HEIGHT_PARAMETER, HEIGHT_WRONG_VALUE, WEIGHT_PARAMETER, WEIGHT_WRONG_VALUE, ACTIVITY_LEVEL_PARAMETER, ACTIVITY_LEVEL_WRONG_VALUE)
                        .queryParams(QueryParams.getAgeParamsWrongValue())
                        .queryParams(QueryParams.getGenderParamsWrongValue())
                        .queryParams(QueryParams.getHeightParamsWrongValue())
                        .queryParams(QueryParams.getWeightParamsWrongValue())
                        .queryParams(QueryParams.getActivityLevelParamsWrongValue());
            }
            case BURNED_CALORIE_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(ACTIVITY_ID_PARAMETER, ACTIVITY_ID_WRONG_VALUE, ACTIVITY_MIN_PARAMETER, ACTIVITY_MIN_WRONG_VALUE, WEIGHT_PARAMETER, WEIGHT_WRONG_VALUE)
                        .queryParams(QueryParams.getActivityIdParamsWrongValue())
                        .queryParams(QueryParams.getActivityMinParamsWrongValue())
                        .queryParams(QueryParams.getWeightParamsWrongValue());
            }
            case DAILY_CALORIE_ENDPOINT -> {
                return requestHeadersAndAllParameters = given()
//                        .headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE)
                        .headers(HeaderParams.getApiKeyHeader())
                        .headers(HeaderParams.getApiHostHeader())
//                        .queryParams(AGE_PARAMETER, AGE_WRONG_VALUE, GENDER_PARAMETER, GENDER_WRONG_VALUE, HEIGHT_PARAMETER, HEIGHT_WRONG_VALUE, WEIGHT_PARAMETER, WEIGHT_WRONG_VALUE, ACTIVITY_LEVEL_PARAMETER, ACTIVITY_LEVEL_WRONG_VALUE, GOAL_PARAMETER, GOAL_WRONG_VALUE)
                        .queryParams(QueryParams.getAgeParamsWrongValue())
                        .queryParams(QueryParams.getGenderParamsWrongValue())
                        .queryParams(QueryParams.getHeightParamsWrongValue())
                        .queryParams(QueryParams.getWeightParamsWrongValue())
                        .queryParams(QueryParams.getActivityLevelParamsWrongValue())
                        .queryParams(QueryParams.getGoalParamsWrongValue());
            }
            default -> throw new Exception("Endpoint is not valid: " + endpointCucumber);
        }
    }

    public void handleSchemaBasedOnStatusCode(int statusCode) throws Exception {
        switch (statusCode) {
            case 200 -> responseCommon.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
            case 422 -> responseCommon.then().assertThat().body(matchesJsonSchemaInClasspath(SCHEMA_PATH_WRONG_PARAMETER));
            case 400 -> responseCommon.then().assertThat().body(matchesJsonSchemaInClasspath(SCHEMA_PATH_BAD_REQUEST));
            case 401 -> responseCommon.then().assertThat().body(matchesJsonSchemaInClasspath(SCHEMA_PATH_UNAUTHORIZED));
            default -> throw new Exception("StatusCode not mapped yet: " + statusCode);
        }

        System.out.println("Schema successfully validated");
    }

    public void saveResponseInCommonStepsClass(Response response) {
        responseCommon = response;
    }

    public void saveStatusCodeInCommonStepsClass() {
        statusCodeCommon = responseCommon.getStatusCode();
        System.out.println("Printing Status Code: " + statusCodeCommon);
    }

    public void validateResponseNotNull() {
        Assertions.assertTrue(Objects.nonNull(responseCommon));
    }

    public void saveResponseBodyAsObject() throws JsonProcessingException {
        String responseString = responseCommon.asString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        baseResponseModel = mapper.readValue(responseString, BaseResponseModel.class);
    }

    public void validateRequestResult() {
        switch (statusCodeCommon) {
            case 200 -> Assert.assertEquals("request_result_200", REQUEST_RESULT_200, baseResponseModel.request_result);
            case 422 -> Assert.assertEquals("request_result_422", REQUEST_RESULT_422, baseResponseModel.request_result);
            case 400 -> Assert.assertEquals("request_result_400", REQUEST_RESULT_400, baseResponseModel.request_result);
            case 401 -> Assert.assertEquals("request_result_401", REQUEST_RESULT_401, baseResponseModel.message);
        }
    }

    public void validateResponseBodyElementsAreNotNull() throws Exception {
        if (statusCodeCommon == 200 || statusCodeCommon == 422 || statusCodeCommon == 400) {
            Assertions.assertNotNull(responseCommon.jsonPath().getString("status_code"), "status_code is null.");
            Assertions.assertNotNull(responseCommon.jsonPath().getString("request_result"), "request_result is null.");

            if (statusCodeCommon == 200) {
                Assertions.assertNotNull(responseCommon.jsonPath().getString("data"), "data is null.");

                switch (endpointCucumber) {
                    case IDEAL_WEIGHT_ENDPOINT -> {
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.Hamwi"), "data.Hamwi is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.Devine"), "data.Devine is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.Miller"), "data.Miller is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.Robinson"), "data.Robinson is null.");
                    }
                    case BMI_ENDPOINT -> {
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.bmi"), "data.bmi is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.health"), "data.health is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.healthy_bmi_range"), "data.healthy_bmi_range is null.");
                    }
                    case BURNED_CALORIE_ENDPOINT -> {
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.burnedCalorie"), "data.burnedCalorie is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.unit"), "data.unit is null.");
                    }
                    case DAILY_CALORIE_ENDPOINT -> {
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.BMR"), "data.BMR is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals"), "data.goals is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.maintain weight"), "data.goals.maintain weight is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Mild weight loss"), "data.goals.Mild weight loss is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Mild weight loss.loss weight"), "data.goals.Mild weight loss.loss weight is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Mild weight loss.calory"), "data.goals.Mild weight loss.calory is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Weight loss"), "data.goals.Weight loss is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Weight loss.loss weight"), "data.goals.Weight loss.loss weight is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Weight loss.calory"), "data.goals.Weight loss.calory is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Extreme weight loss"), "data.goals.Extreme weight loss is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Extreme weight loss.loss weight"), "data.goals.Extreme weight loss.loss weight is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Extreme weight loss.calory"), "data.goals.Extreme weight loss.calory is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Mild weight gain"), "data.goals.Mild weight gain is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Mild weight gain.gain weight"), "data.goals.Mild weight gain.gain weight is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Mild weight gain.calory"), "data.goals.Mild weight gain.calory is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Weight gain"), "data.goals.Weight gain is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Weight gain.gain weight"), "data.goals.Weight gain.gain weight is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Weight gain.calory"), "data.goals.Weight gain.calory is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Extreme weight gain"), "data.goals.Extreme weight gain is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Extreme weight gain.gain weight"), "data.goals.Extreme weight gain.gain weight is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.goals.Extreme weight gain.calory"), "data.goals.Extreme weight gain.calory is null.");
                    }
                    case MACROS_CALCULATOR_ENDPOINT -> {
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.calorie"), "data.calorie is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.balanced"), "data.balanced is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.balanced.protein"), "data.balanced.protein is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.balanced.fat"), "data.balanced.fat is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.balanced.carbs"), "data.balanced.carbs is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowfat"), "data.lowfat is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowfat.protein"), "data.lowfat.protein is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowfat.fat"), "data.lowfat.fat is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowfat.carbs"), "data.lowfat.carbs is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowcarbs"), "data.lowcarbs is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowcarbs.protein"), "data.lowcarbs.protein is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowcarbs.fat"), "data.lowcarbs.fat is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.lowcarbs.carbs"), "data.lowcarbs.carbs is null.");

                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.highprotein"), "data.highprotein is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.highprotein.protein"), "data.highprotein.protein is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.highprotein.fat"), "data.highprotein.fat is null.");
                        Assertions.assertNotNull(responseCommon.jsonPath().getString("data.highprotein.carbs"), "data.highprotein.carbs is null.");
                    }
                    default -> throw new Exception("Endpoint not valid: " + endpointCucumber);
                }
            }
            else if (statusCodeCommon == 422) {
                Assertions.assertNotNull(responseCommon.jsonPath().getString("errors"), "errors is null.");
            }
        }
        else {
            Assertions.assertNotNull(responseCommon.jsonPath().getString("message"), "message is null.");
        }
    }

    public void validateRequestErrors() throws Exception {
        if (CommonSteps.statusCodeCommon == 422) {
            switch (endpointCucumber) {
                case IDEAL_WEIGHT_ENDPOINT -> {
                    Assert.assertEquals("parameter_error_gender", GENDER_ERROR_MESSAGE, baseResponseModel.errors.get(0));
                    Assert.assertEquals("parameter_error_height", HEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(1));
                }
                case BMI_ENDPOINT -> {
                    Assert.assertEquals("parameter_error_weight", WEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(0));
                    Assert.assertEquals("parameter_error_height", HEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(1));
                    Assert.assertEquals("parameter_error_age", AGE_ERROR_MESSAGE, baseResponseModel.errors.get(2));
                }
                case BURNED_CALORIE_ENDPOINT -> {
                    Assert.assertEquals("parameter_error_activitymin", ACTIVITY_MIN_ERROR_MESSAGE, baseResponseModel.errors.get(0));
                    Assert.assertEquals("parameter_error_weight", WEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(1));
                    //TODO quando manda só erro no activityID vem o campo "error"
                }
                case DAILY_CALORIE_ENDPOINT -> {
                    Assert.assertEquals("parameter_error_gender", GENDER_ERROR_MESSAGE, baseResponseModel.errors.get(0));
                    Assert.assertEquals("parameter_error_weight", WEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(1));
                    Assert.assertEquals("parameter_error_height", HEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(2));
                    Assert.assertEquals("parameter_error_age", AGE_ERROR_MESSAGE, baseResponseModel.errors.get(3));
                    Assert.assertEquals("parameter_error_activitylevel", ACTIVITY_LEVEL_ERROR_MESSAGE, baseResponseModel.errors.get(4));
                }
                case MACROS_CALCULATOR_ENDPOINT -> {
                    Assert.assertEquals("parameter_error_gender", GENDER_ERROR_MESSAGE, baseResponseModel.errors.get(0));
                    Assert.assertEquals("parameter_error_weight", WEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(1));
                    Assert.assertEquals("parameter_error_height", HEIGHT_ERROR_MESSAGE, baseResponseModel.errors.get(2));
                    Assert.assertEquals("parameter_error_age", AGE_ERROR_MESSAGE, baseResponseModel.errors.get(3));
                    Assert.assertEquals("parameter_error_activitylevel", ACTIVITY_LEVEL_ERROR_MESSAGE, baseResponseModel.errors.get(4));
                    Assert.assertEquals("parameter_error_goal", GOAL_ERROR_MESSAGE, baseResponseModel.errors.get(4));
                }
                default -> throw new Exception("Invalid Endpoint: " + statusCodeCommon);
            }
        } else throw new Exception("Status Code suposed to be 422, but was: " + statusCodeCommon);
    }

}
