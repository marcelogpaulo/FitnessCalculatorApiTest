package utils;

import constants.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BuildQueryParameters implements Constants {

    public static String url;
    public static Response response;
    public static int statusCode;
    public static String responseBody;
    public static String gender;
    public static int height;

    public BuildQueryParameters(String url, String gender, int height) {
        this.url = url;
        this.gender = gender;
        this.height = height;
    }

    public Response buildRequest() {
        response = RestAssured.given()
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .queryParam("gender", gender)
                .queryParam("height", height)
                .get(url);

        statusCode = response.getStatusCode();
        responseBody = response.getBody().asString();

        return response;
    }

}
