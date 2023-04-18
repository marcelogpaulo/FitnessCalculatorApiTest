Feature: Validate responses from Calculators Collection

  @CalculatorStatus200
  Scenario: Validate status code 200
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "male" and 180
    Then the response should have status code 200
    And schema for the response is correct

  @ErrorStatus422
  Scenario: Validate status code 422 when the height value overlaps
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "female" and 231
    Then the response should have status code 422
    And schema for the response is correct

  @ErrorStatus422
  Scenario: Validate status code 422 when the gender is invalid
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "man" and 180
    Then the response should have status code 422
    And schema for the response is correct

  @ErrorStatus422
  Scenario: Validate status code 422 when the gender and height are invalid
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "man" and 231
    Then the response should have status code 422
    And schema for the response is correct

  @ErrorStatus400
  Scenario: Validate status code 400 when sending only the gender parameter
    Given I have the API endpoint "/idealweight"
    When I send a GET request using only the gender parameter
    Then the response should have status code 400
    And schema for the response is correct

  @ErrorStatus400
  Scenario: Validate status code 400 when sending only the height parameter
    Given I have the API endpoint "/idealweight"
    When I send a GET request using only the height parameter
    Then the response should have status code 400
    And schema for the response is correct

  @ErrorStatus400
  Scenario: Validate status code 400 when sending no parameters
    Given I have the API endpoint "/idealweight"
    When I send a GET request using no parameter
    Then the response should have status code 400
    And schema for the response is correct

  @ErrorStatus401
  Scenario: Validate status code 400 when sending no API_KEY
    Given I have the API endpoint "/idealweight"
    When I send a GET request without the API_KEY
    Then the response should have status code 401
    And schema for the response is correct