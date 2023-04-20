package steps;

import constants.Constants;
import constants.RequestParameter;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;

import static io.restassured.RestAssured.*;

public class IdealWeightStep implements Constants {

    CommonSteps commonSteps = new CommonSteps();

    public static Response response;
    public static RequestSpecification requestHeadersAndAllParameters;
    public static int statusCode;
    public static String responseBody;
    public static String gender;
    public static int height;

    @When("I send a GET request using {string} and {int}")
    public void i_send_a_get_request(String genderCucumber, int heightCucumber) throws Exception {
        gender = genderCucumber;
        height = heightCucumber;
        requestHeadersAndAllParameters = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("gender", gender, "height", height);
        getRequestBuilder(RequestParameter.ALL_PARAMETERS);
        commonSteps.saveResponseInCommonStepsClass(response);
        commonSteps.saveStatusCodeInCommonStepsClass();
        getStatusCode();
        getResponseBodyAsString();
        commonSteps.validateResponseNotNull();
        validateResponseBodyElementsAreNotNull();
        commonSteps.getResponseBodyAsObject();
        commonSteps.validateRequestResult();
        validateRequestErrors();
    }

    public void getRequestBuilder(RequestParameter parameter) throws Exception {
        if (Objects.requireNonNull(parameter) == RequestParameter.ALL_PARAMETERS) {
            response = requestHeadersAndAllParameters.get();
        } else {
            throw new Exception("Option not valid: " + parameter);
        }
    }

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

    public void getStatusCode() {
        statusCode = response.getStatusCode();
    }

    public void getResponseBodyAsString() {
        responseBody = response.getBody().asString();
    }

    public void validateRequestErrors() {
        if (statusCode == 422) {
            if (!gender.equals("male") && !gender.equals("female")) {
                if (height < 130 || height > 230) {
                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", commonSteps.baseResponseModel.errors.get(0));
                    Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", commonSteps.baseResponseModel.errors.get(1));
                } else {
                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", commonSteps.baseResponseModel.errors.get(0));
                }
            } else {
                Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", commonSteps.baseResponseModel.errors.get(0));
            }
        }
    }
}
