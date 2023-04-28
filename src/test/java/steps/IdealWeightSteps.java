package steps;

import constants.Constants;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import static io.restassured.RestAssured.*;

public class IdealWeightSteps implements Constants {

    CommonSteps commonSteps = new CommonSteps();

    public static Response response;
    public static RequestSpecification requestHeadersAndAllParameters;
    public static String gender;
    public static int height;

    @When("I send a GET request using {string} and {int}")
    public void i_send_a_get_request(String genderCucumber, int heightCucumber) throws Exception {
        gender = genderCucumber;
        height = heightCucumber;
        requestHeadersAndAllParameters = given().headers(API_KEY_KEY, API_KEY_VALUE, API_HOST_KEY, API_HOST_VALUE).queryParams(GENDER_PARAMETER, gender, HEIGHT_PARAMETER, height);
        commonSteps.sendRequest(requestHeadersAndAllParameters);
        commonSteps.saveResponseInCommonStepsClass(response);
        commonSteps.saveStatusCodeInCommonStepsClass();
        commonSteps.validateResponseNotNull();
        commonSteps.validateResponseBodyElementsAreNotNull();
        commonSteps.saveResponseBodyAsObject();
        commonSteps.validateRequestResult();
        validateRequestErrors();;
    }

    public void validateRequestErrors() {
        if (CommonSteps.statusCodeCommon == 422) {
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
