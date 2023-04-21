package constants;

public interface Constants {

    public static final String URL = "https://fitness-calculator.p.rapidapi.com";
    public static final String API_KEY_VALUE = "X-RapidAPI-Key";
    public static final String API_KEY_KEY = "74c9ddd75dmsh30dd461a78196cep15d5cajsn6f4dcb3787b0";
    public static final String API_HOST_VALUE = "X-RapidAPI-Host";
    public static final String API_HOST_KEY = "fitness-calculator.p.rapidapi.com";
    public static final String IDEAL_WEIGHT_ENDPOINT = "/idealweight";
    public static final String BMI_ENDPOINT = "/bmi";
    public static final String MACROS_CALCULATOR_ENDPOINT = "/macrocalculator";
    public static final String BURNED_CALORIE_ENDPOINT = "/burnedcalorie";
    public static final String DAILY_CALORIE_ENDPOINT = "/dailycalorie";
    public static final String SCHEMA_PATH_WRONG_PARAMETER = "schemas/BaseSchemas/BaseWrongParameterSchema.json";
    public static final String SCHEMA_PATH_BAD_REQUEST = "schemas/BaseSchemas/BaseBadRequestSchema.json";
    public static final String SCHEMA_PATH_UNAUTHORIZED = "schemas/BaseSchemas/BaseUnauthorizedSchema.json";
    public static final String REQUEST_RESULT_200 = "Successful";
    public static final String REQUEST_RESULT_400 = "Bad Request";
    public static final String REQUEST_RESULT_401 = "Invalid API key. Go to https://docs.rapidapi.com/docs/keys for more info.";
    public static final String REQUEST_RESULT_422 = "Unprocessable Entity";

}
