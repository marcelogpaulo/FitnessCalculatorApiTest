Feature: Validate responses from Calculators Collection - BMI API

  @CalculatorStatus200
  Scenario: Validate status code 200
    Given I have the API endpoint "/bmi"
    When I send a GET request using age 80, weight 65 and height 180
    Then the response should have status code 200
    And schema is correct for the endpoint "/bmi"

  @ErrorStatus400
  Scenario: Validate status code 400 when sending no parameters
    Given I have the API endpoint "/bmi"
    When I send a GET request using no parameter
    Then the response should have status code 400
    And schema is correct for the endpoint "/bmi"

  @ErrorStatus401
  Scenario: Validate status code 400 when sending no API_KEY
    Given I have the API endpoint "/bmi"
    When I send a GET request without the API_KEY
    Then the response should have status code 401
    And schema is correct for the endpoint "/bmi"