package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import utils.IdealWeightResponseModel;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class IdealWeightStep implements Constants {

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

    public IdealWeightResponseModel idealWeightResponseModel;

    @Given("I have the API endpoint {string}")
    public void i_have_the_api_endpoint(String endpoint) throws Exception {
        if (endpoint.equals("/idealweight") || endpoint.equals("/macrocalculator") || endpoint.equals("/bmi") || endpoint.equals("/burnedcalorie") || endpoint.equals("/dailycalorie")) {
            System.out.println("Endpoint is valid: " + endpoint);
        } else throw new Exception("Endpoint is not valid: " + endpoint);

        RestAssured.baseURI = URL + endpoint;
    }

    @When("I send a GET request using {string} and {int}")
    public void i_send_a_get_request(String genderCucumber, int heightCucumber) throws Exception {
        gender = genderCucumber;
        height = heightCucumber;
        requestHeadersAndAllParameters = given().headers("X-RapidAPI-Key", API_KEY, "X-RapidAPI-Host", API_HOST).queryParams("gender", gender, "height", height);
        getRequestBuilder(BOTH_PARAMETERS);
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

    @Then("the response should have status code {int}")
    public void the_response_should_have_status_code(Integer code) {
        System.out.println("Printing the Response Body");
        response.prettyPrint();
        response.then().statusCode(code);
    }

    @And("schema for the response is correct")
    public void validateSchema() {
        switch (statusCode) {
            case 200 -> response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/IdealWeightSchema.json"));
            case 422 -> response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/IdealWeightWrongParameterSchema.json"));
            case 400 -> response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/IdealWeightBadRequestSchema.json"));
            case 401 -> response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/IdealWeightUnauthorizedSchema.json"));
        }

        System.out.println("Schema successfully validated");
    }

    public void getRequestBuilder(String parameter) throws Exception {
        switch (parameter) {
            case "both" -> response = requestHeadersAndAllParameters.get();
            case "gender" -> response = requestHeadersAndOnlyGenderParameter.get();
            case "height" -> response = requestHeadersAndOnlyHeightParameter.get();
            case "none" -> response = requestHeadersWithoutParameters.get();
            case "no_api_key" -> response = requestHeaderWithoutApiKey.get();
            default -> throw new Exception("Option not valid: " + parameter);
        }

        statusCode = response.getStatusCode();
        responseBody = response.getBody().asString();

        returnResponseBodyAsObject();
        validateRequestResult();
        validateRequestErrors();
    }

    public void returnResponseBodyAsObject() throws JsonProcessingException {
        String responseString = response.asString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        idealWeightResponseModel = mapper.readValue(responseString, IdealWeightResponseModel.class);
    }

    public void validateRequestResult() {
        switch (statusCode) {
            case 200 -> Assert.assertEquals("request_result_200", "Successful", idealWeightResponseModel.request_result);
            case 422 -> Assert.assertEquals("request_result_422", "Unprocessable Entity", idealWeightResponseModel.request_result);
            case 400 -> Assert.assertEquals("request_result_400", "Bad Request", idealWeightResponseModel.request_result);
            case 401 -> Assert.assertEquals("request_result_401", "Invalid API key. Go to https://docs.rapidapi.com/docs/keys for more info.", idealWeightResponseModel.message);
        }
    }

    public void validateRequestErrors() {
        if (statusCode == 422) {
            if (!gender.equals("male") && !gender.equals("female")) {
                if (height < 130 || height > 230) {
                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", idealWeightResponseModel.errors.get(0));
                    Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", idealWeightResponseModel.errors.get(1));
                } else {
                    Assert.assertEquals("error_both_parameters_wrong_gender", "gender input must be male or female", idealWeightResponseModel.errors.get(0));
                }
            } else {
                Assert.assertEquals("error_both_parameters_wrong_height", "height must be between 130 cm to 230 cm. ", idealWeightResponseModel.errors.get(0));
            }
        }
    }
}
