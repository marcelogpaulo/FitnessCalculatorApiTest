Feature: Validate responses from Calculators Collection - BMI API

  @CalculatorStatus200
  Scenario: Validate status code 200
    Given I have the API endpoint "/bmi"
    When I send a GET request using 80, 65 and 180
    Then the response should have status code 200
    And base_schema for the response is correct