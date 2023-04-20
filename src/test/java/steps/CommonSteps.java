package steps;

import constants.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.Objects;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CommonSteps implements Constants {

    public static Response lastResponse;
    public static int lastStatusCode;

    //TODO - validar se tem como usar o mesmo step para varias classes diferentes, porque dependente, vai mudar o response e o statusCode

    @Given("I have the API endpoint {string}")
    public void i_have_the_api_endpoint(String endpoint) throws Exception {
        if (endpoint.equals("/idealweight") || endpoint.equals("/macrocalculator") || endpoint.equals("/bmi") || endpoint.equals("/burnedcalorie") || endpoint.equals("/dailycalorie")) {
            System.out.println("Endpoint is valid: " + endpoint);
        } else throw new Exception("Endpoint is not valid: " + endpoint);

        RestAssured.baseURI = URL + endpoint;
    }

    @Then("the response should have status code {int}")
    public void the_response_should_have_status_code(Integer code) {
        System.out.println("Printing the Response Body");
        lastResponse.prettyPrint();
        lastResponse.then().statusCode(code);
    }

    @And("base_schema for the response is correct")
    public void validateSchema() {
        switch (lastStatusCode) {
            case 200 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseSuccessSchema.json"));
            case 422 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseWrongParameterSchema.json"));
            case 400 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseBadRequestSchema.json"));
            case 401 -> lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/BaseSchemas/BaseUnauthorizedSchema.json"));
        }

        System.out.println("Schema successfully validated");
    }

    public void saveResponseFromAnotherClass(Response response) {
        lastResponse = response;
        lastStatusCode = lastResponse.getStatusCode();
    }

    public void validateResponseNotNull() {
        Assertions.assertTrue(Objects.nonNull(lastResponse));
    }

}
