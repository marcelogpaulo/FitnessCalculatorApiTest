package steps;

import constants.Constants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class IdealWeightStep implements Constants {

    public static String url;
    public static Response response;
    public static int statusCode;
    public static String responseBody;

    @Given("I have the API endpoint {string}")
    public void i_have_the_api_endpoint(String endpoint) {
        url = URL + endpoint;
    }

    @When("I send a GET request using {string} and {int}")
    public void i_send_a_get_request(String gender, int height) {
        response = RestAssured.given()
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .queryParam("gender", gender)
                .queryParam("height", height)
                .get(url);

        storeStatusCodeAndResponseBody();
    }

    public void storeStatusCodeAndResponseBody() {
        statusCode = response.getStatusCode();
        responseBody = response.getBody().asString();
    }

    @Then("the response should have status code {int}")
    public void the_response_should_have_status_code(Integer code) {
        System.out.println(response.toString());
        response.then().statusCode(code);
    }

    @When("I send a GET request using only the gender parameter")
    public void iSendAGETRequestUsingOnlyTheGenderParameter() {
        response = RestAssured.given()
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .queryParam("gender", "male")
                .get(url);

        storeStatusCodeAndResponseBody();;
    }

    @When("I send a GET request using only the height parameter")
    public void iSendAGETRequestUsingOnlyTheHeightParameter() {
        response = RestAssured.given()
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .queryParam("height", 150)
                .get(url);

        storeStatusCodeAndResponseBody();;
    }
}
