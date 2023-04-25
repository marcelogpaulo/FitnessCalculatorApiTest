package utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class BaseResponseModel {

    @JsonProperty("IdealWeightResponseModel")
    public BaseResponseModel BaseResponseModel;

    @JsonProperty("status_code")
    public int status_code;

    @JsonProperty("request_result")
    public String request_result;

    @JsonProperty("data")
    public Map<String, Object> data;

    @JsonProperty("errors")
    public List<String> errors;

    @JsonProperty("message")
    public String message;
}
