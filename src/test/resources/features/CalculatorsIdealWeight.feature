Feature: Validate responses from Calculators Collection - Ideal Weight API

@CalculatorStatus200TESTE
  Scenario: Validate status code 200 TESTE
    Given I have the API endpoint "/idealweight"
    When I send a GET request with all parameters
    Then the response should have status code 200
    And schema is correct

  @ErrorStatus422
  Scenario: Validate status code 422 when the height value overlaps
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "female" and 231
    Then the response should have status code 422
    And schema is correct

  @ErrorStatus422
  Scenario: Validate status code 422 when the gender is invalid
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "man" and 180
    Then the response should have status code 422
    And schema is correct

  @ErrorStatus422
  Scenario: Validate status code 422 when the gender and height are invalid
    Given I have the API endpoint "/idealweight"
    When I send a GET request using "man" and 231
    Then the response should have status code 422
    And schema is correct

  @ErrorStatus400
  Scenario: Validate status code 400 when sending only the gender parameter
    Given I have the API endpoint "/idealweight"
    When I send a GET request using only the parameter "gender" and the value "male"
    Then the response should have status code 400
    And schema is correct

  @ErrorStatus400
  Scenario: Validate status code 400 when sending only the height parameter
    Given I have the API endpoint "/idealweight"
    When I send a GET request using only the parameter "height" and the value "180"
    Then the response should have status code 400
    And schema is correct

  @ErrorStatus400
  Scenario: Validate status code 400 when sending no parameters
    Given I have the API endpoint "/idealweight"
    When I send a GET request using no parameter
    Then the response should have status code 400
    And schema is correct

  @ErrorStatus401
  Scenario: Validate status code 401 when sending no API_KEY
    Given I have the API endpoint "/idealweight"
    When I send a GET request without the API_KEY
    Then the response should have status code 401
    And schema is correct