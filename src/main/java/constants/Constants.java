package constants;

public interface Constants {

    public static final String URL = "https://fitness-calculator.p.rapidapi.com";
    public static final String API_KEY_KEY = "X-RapidAPI-Key";
    public static final String API_KEY_VALUE = "74c9ddd75dmsh30dd461a78196cep15d5cajsn6f4dcb3787b0";
    public static final String API_HOST_KEY = "X-RapidAPI-Host";
    public static final String API_HOST_VALUE = "fitness-calculator.p.rapidapi.com";
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
    public static final String PARAMETER_AGE = "age";
    public static final int PARAMETER_AGE_DEFAULT_VALUE = 20;
    public static final String PARAMETER_GENDER = "gender";
    public static final String PARAMETER_GENDER_DEFAULT_VALUE = "male";
    public static final String PARAMETER_WEIGHT = "weight";
    public static final int PARAMETER_WEIGHT_DEFAULT_VALUE = 100;
    public static final String PARAMETER_HEIGHT = "height";
    public static final int PARAMETER_HEIGHT_DEFAULT_VALUE = 180;
    public static final String PARAMETER_ACTIVITY_ID = "activityid";
    public static final String PARAMETER_ACTIVITY_ID_DEFAULT_VALUE = "bi_1";
    public static final String PARAMETER_ACTIVITY_MIN = "activitymin";
    public static final int PARAMETER_ACTIVITY_MIN_DEFAULT_VALUE = 30;
    public static final String PARAMETER_ACTIVITY_LEVEL = "activitylevel";
    public static final int PARAMETER_ACTIVITY_LEVEL_DEFAULT_VALUE = 5;
    public static final String PARAMETER_GOAL = "goal";
    public static final String PARAMETER_GOAL_DEFAULT_VALUE = "extremelose";

}
