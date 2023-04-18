package utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class IdealWeightResponseModel {

    @JsonProperty("IdealWeightResponseModel")
    public IdealWeightResponseModel IdealWeightResponseModel;

    @JsonProperty("status_code")
    public int status_code;

    @JsonProperty("request_result")
    public String request_result;

    @JsonProperty("data")
    public Map<String, Double> data;

    @JsonProperty("errors")
    public List<String> errors;

    @JsonProperty("message")
    public String message;
}
