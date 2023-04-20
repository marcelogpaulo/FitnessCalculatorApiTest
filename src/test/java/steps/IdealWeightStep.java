package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import utils.BaseResponseModel;

import java.util.Objects;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class IdealWeightStep implements Constants {

    CommonSteps commonSteps = new CommonSteps();

    public static Response response;
    public static RequestSpecification requestHeadersAndAllParameters;
    public static RequestSpecification requestHeadersAndOnlyGenderParameter;
    public static RequestSpecification requestHeadersAndOnlyHeightParameter;
    public static RequestSpecification requestHeadersWithoutParameters;
    public static RequestSpecification requestHeaderWithoutApiKey;
    public static int statusCode;
    public static String responseBody;
    public static String gender = "male";
    public static int height = 180;

    public BaseResponseModel baseResponseModel;

    @When("I send a GET request using {string} and {int}")
    public void i_send_a_get_request(String genderCucumber, int heightCucumber) throws Exception {
        gender = genderCucumber;
        height = heightCucumber;
        requestHeadersAndAllParameters = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("gender", gender, "height", height);
        getRequestBuilder(ALL_PARAMETERS);
    }

    @When("I send a GET request using only the gender parameter")
    public void iSendAGETRequestUsingOnlyTheGenderParameter() throws Exception {
        gender = "male";
        requestHeadersAndOnlyGenderParameter = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("gender", gender);
        getRequestBuilder(GENDER_PARAMETER);
    }

    @When("I send a GET request using only the height parameter")
    public void iSendAGETRequestUsingOnlyTheHeightParameter() throws Exception {
        height = 150;
        requestHeadersAndOnlyHeightParameter = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("height", height);
        getRequestBuilder(HEIGHT_PARAMETER);
    }

    @When("I send a GET request using no parameter")
    public void iSendAGETRequestUsingNoParameters() throws Exception {
        requestHeadersWithoutParameters = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST);
        getRequestBuilder(NONE_PARAMETERS);
    }

    @When("I send a GET request without the API_KEY")
    public void iSendAGETRequestWithoutApiKey() throws Exception {
        requestHeaderWithoutApiKey = given().headers( "X-RapidAPI-Host", API_HOST);
        getRequestBuilder(NO_API_KEY);
    }

    public void getRequestBuilder(String parameter) throws Exception {
        switch (parameter) {
            case "all" -> response = requestHeadersAndAllParameters.get();
            case "gender" -> response = requestHeadersAndOnlyGenderParameter.get();
            case "height" -> response = requestHeadersAndOnlyHeightParameter.get();
            case "none" -> response = requestHeadersWithoutParameters.get();
            case "no_api_key" -> response = requestHeaderWithoutApiKey.get();
            default -> throw new Exception("Option not valid: " + parameter);
        }

        commonSteps.saveResponseFromAnotherClass(response);
        getStatusCodeAndResponseBodyAsString();
        commonSteps.validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        returnResponseBodyAsObject();
        validateRequestResult();
        validateRequestErrors();
    }

//    public void validateResponseNotNull() {
//        Assertions.assertTrue(Objects.nonNull(response));
//    }

    public void validateResponseBodyElementsAreNotNull() {
        if (statusCode == 200 || statusCode == 422 || statusCode == 400) {
            Assertions.assertNotNull(response.jsonPath().getString("status_code"), "status_code is null.");
            Assertions.assertNotNull(response.jsonPath().getString("request_result"), "request_result is null.");

            if (statusCode == 200) {
                Assertions.assertNotNull(response.jsonPath().getString("data"), "data is null.");
                Assertions.assertNotNull(response.jsonPath().getString("data.Hamwi"), "Hamwi is null.");
                Assertions.assertNotNull(response.jsonPath().getString("data.Devine"), "Devine is null.");
                Assertions.assertNotNull(response.jsonPath().getString("data.Miller"), "Miller is null.");
                Assertions.assertNotNull(response.jsonPath().getString("data.Robinson"), "Robinson is null.");
            }
            else if (statusCode == 422) {
                Assertions.assertNotNull(response.jsonPath().getString("errors"), "errors is null.");
            }
        }
        else {
            Assertions.assertNotNull(response.jsonPath().getString("message"), "message is null.");
        }
    }

    public void getStatusCodeAndResponseBodyAsString() {
        statusCode = response.getStatusCode();
        responseBody = response.getBody().asString();
    }

    public void returnResponseBodyAsObject() throws JsonProcessingException {
        String responseString = response.asString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        baseResponseModel = mapper.readValue(responseString, BaseResponseModel.class);
    }

    public void validateRequestResult() {
        switch (statusCode) {
            case 200 -> Assert.assertEquals("request_result_200", "Successful", baseResponseModel.request_result);
            case 422 -> Assert.assertEquals("request_result_422", "Unprocessable Entity", baseResponseModel.request_result);
            case 400 -> Assert.assertEquals("request_result_400", "Bad Request", baseResponseModel.request_result);
            case 401 -> Assert.assertEquals("request_result_401", "Invalid API key. Go to https://docs.rapidapi.com/docs/keys for more info.", baseResponseModel.message);
        }
    }

    public void validateRequestErrors() {
        if (statusCode == 422) {
            if (!gender.equals("male") && !gender.equals("female")) {
                if (height < 130 || height > 230) {
                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", baseResponseModel.errors.get(0));
                    Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", baseResponseModel.errors.get(1));
                } else {
                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", baseResponseModel.errors.get(0));
                }
            } else {
                Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", baseResponseModel.errors.get(0));
            }
        }
    }
}
