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

    public static final String AGE_PARAMETER = "age";
    public static final int AGE_DEFAULT_VALUE = 20;
    public static final int AGE_WRONG_VALUE = 81;
    public static final String AGE_ERROR_MESSAGE = "age must be between 0 to 80 .";

    public static final String GENDER_PARAMETER = "gender";
    public static final String GENDER_DEFAULT_VALUE = "male";
    public static final String GENDER_WRONG_VALUE = "man";
    public static final String GENDER_ERROR_MESSAGE = "gender input must be male or female";

    public static final String WEIGHT_PARAMETER = "weight";
    public static final int WEIGHT_DEFAULT_VALUE = 100;
    public static final int WEIGHT_WRONG_VALUE = 161;
    public static final String WEIGHT_ERROR_MESSAGE = "weight must be between 40 kg to 160 kg. ";

    public static final String HEIGHT_PARAMETER = "height";
    public static final int HEIGHT_DEFAULT_VALUE = 180;
    public static final int HEIGHT_WRONG_VALUE = 231;
    public static final String HEIGHT_ERROR_MESSAGE = "height must be between 130 cm to 230 cm. ";

    public static final String ACTIVITY_ID_PARAMETER = "activityid";
    public static final String ACTIVITY_ID_DEFAULT_VALUE = "bi_1";
    public static final String ACTIVITY_ID_WRONG_VALUE = "bi_123";
    public static final String ACTIVITY_ID_ERROR_MESSAGE = "check the activityid";

    public static final String ACTIVITY_MIN_PARAMETER = "activitymin";
    public static final int ACTIVITY_MIN_DEFAULT_VALUE = 30;
    public static final int ACTIVITY_MIN_WRONG_VALUE = -30;
    public static final String ACTIVITY_MIN_ERROR_MESSAGE = "activitymin cannot be negative";

    public static final String ACTIVITY_LEVEL_PARAMETER = "activitylevel";
    public static final int ACTIVITY_LEVEL_DEFAULT_VALUE = 5;
    public static final int ACTIVITY_LEVEL_WRONG_VALUE = 8;
    public static final String ACTIVITY_LEVEL_ERROR_MESSAGE = "activity level must be 1-7";

    public static final String GOAL_PARAMETER = "goal";
    public static final String GOAL_DEFAULT_VALUE = "extremelose";
    public static final String GOAL_WRONG_VALUE = "false";
    public static final String GOAL_ERROR_MESSAGE = "goal must be one of maintain,mildlose,weightlose,extremelose,mildgain,weightgain,extremegain";

}
