@put
Feature: Put user feature

@check
Scenario: To update user name with valid input
    Given User is on Put Method with endpoint
    When User sends request to update user name with valid input
    Then User should receive status code and message for put
 
Scenario: To update user name with alphanumeric input
    Given User is on Put Method with endpoint
    When User sends request to update user name with alphanumeric input
    Then User should receive status code and message for put
   
Scenario: To update with invalid phone number
    Given User is on Put Method with endpoint
    When User sends request to update phone number with invalid input
    Then User should receive error status code and message for put
   
Scenario: To update record with alphanumeric location and comments
    Given User is on Put Method with endpoint
    When User sends request to update location and comments with alphanumeric input.
    Then User should receive status code and message for put
    
Scenario: To update time zone with valid input
    Given User is on Put Method with endpoint
    When User sends request to update time zone with valid input
    Then User should receive status code and message for put

Scenario: To update time zone with invalid input
    Given User is on Put Method with endpoint
    When User sends request to update time zone with invalid input
    Then User should receive error status code and message for put
    
Scenario: To update Linkedin id with valid input
    Given User is on Put Method with endpoint
    When User sends request to update Linkedin id with valid input
    Then User should receive status code and message for put
    
Scenario: To update visa status with invalid input
    Given User is on Put Method with endpoint
    When user sends request to update visa status with numeric input
    Then User should receive error status code and message for put

    