package utils;

import constants.Constants;

import java.util.Map;

public class QueryParams implements Constants {

    public static Map<String, Integer> getAgeParams() {
        return Map.of(AGE_PARAMETER, AGE_DEFAULT_VALUE);
    }

    public static Map<String, Integer> getAgeParamsWrongValue() {
        return Map.of(AGE_PARAMETER, AGE_WRONG_VALUE);
    }

    public static Map<String, String> getGenderParams() {
        return Map.of(GENDER_PARAMETER, GENDER_DEFAULT_VALUE);
    }

    public static Map<String, String> getGenderParamsWrongValue() {
        return Map.of(GENDER_PARAMETER, GENDER_WRONG_VALUE);
    }

    public static Map<String, Integer> getHeightParams() {
        return Map.of(HEIGHT_PARAMETER, HEIGHT_DEFAULT_VALUE);
    }

    public static Map<String, Integer> getHeightParamsWrongValue() {
        return Map.of(HEIGHT_PARAMETER, HEIGHT_WRONG_VALUE);
    }

    public static Map<String, Integer> getWeightParams() {
        return Map.of(WEIGHT_PARAMETER, WEIGHT_DEFAULT_VALUE);
    }

    public static Map<String, Integer> getWeightParamsWrongValue() {
        return Map.of(WEIGHT_PARAMETER, WEIGHT_WRONG_VALUE);
    }

    public static Map<String, Integer> getActivityLevelParams() {
        return Map.of(ACTIVITY_LEVEL_PARAMETER, ACTIVITY_LEVEL_DEFAULT_VALUE);
    }

    public static Map<String, Integer> getActivityLevelParamsWrongValue() {
        return Map.of(ACTIVITY_LEVEL_PARAMETER, ACTIVITY_LEVEL_WRONG_VALUE);
    }

    public static Map<String, String> getActivityIdParams() {
        return Map.of(ACTIVITY_ID_PARAMETER, ACTIVITY_ID_DEFAULT_VALUE);
    }

    public static Map<String, String> getActivityIdParamsWrongValue() {
        return Map.of(ACTIVITY_ID_PARAMETER, ACTIVITY_ID_WRONG_VALUE);
    }

    public static Map<String, Integer> getActivityMinParams() {
        return Map.of(ACTIVITY_MIN_PARAMETER, ACTIVITY_MIN_DEFAULT_VALUE);
    }

    public static Map<String, Integer> getActivityMinParamsWrongValue() {
        return Map.of(ACTIVITY_MIN_PARAMETER, ACTIVITY_MIN_WRONG_VALUE);
    }

    public static Map<String, String> getGoalParams() {
        return Map.of(GOAL_PARAMETER, GOAL_DEFAULT_VALUE);
    }

    public static Map<String, String> getGoalParamsWrongValue() {
        return Map.of(GOAL_PARAMETER, GOAL_WRONG_VALUE);
    }

}
