Feature: Validate responses from Calculators Collection

  @CalculatorStatus200
  Scenario: Validate status code 200
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "male" and 180
    Then the response should have status code 200

  @CalculatorStatus422
  Scenario: Validate status code 422
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "female" and 231
    Then the response should have status code 422

  @CalculatorStatus400
  Scenario: Validate status code 400 when sending only the gender parameter
    Given I have the API endpoint "/idealweight"
    When I send a GET request using only the gender parameter
    Then the response should have status code 400

  @CalculatorStatus400
  Scenario: Validate status code 400 when sending only the height parameter
    Given I have the API endpoint "/idealweight"
    When I send a GET request using only the height parameter
    Then the response should have status code 400