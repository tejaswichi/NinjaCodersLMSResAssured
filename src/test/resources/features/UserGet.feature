#JSON schema validation is done in the @When before the get request and in Then of the response body.
#DB Validation is done for the user.
@user
Feature: Get User Feature
       
Scenario: To check Authorization without providing username and password
    Given User is on Get Method with end point and no authentication
    When User sends request without username and password
    Then User should receive error status code and message for auth
    
Scenario: To check Authorization with valid username and invalid password
    Given User is on Get Method with end point
    When User sends correct username and incorrect password
    Then User should receive error status code and message for get

Scenario: To check Authorization with invalid username and valid password
    Given User is on Get Method with end point
    When User sends incorrect username and correct password
    Then User should receive error status code and message for get
    
Scenario: To Check Authorization with valid username and password
    Given User is on Get Method with end point
    When User sends valid username and password
    Then User should receive success status code for get

#  Only the required fields are shown in the response body, validated using schema 

Scenario: To check if able to read all the user records
    Given User is on Get Method with end point
    When  User sends the request with valid inputs
    Then User is able to read all user details and receive status code for get


Scenario: To check if able to read specific valid user record
    Given User is on Get Method with end point for single user
    When  User sends the request with existing userId
    Then User should receive status code and db is validated for specific user
    
Scenario: To check if able to read record with invalid userId
    Given User is on Get Method with end point for single user
    When  User sends request with invalid userId
    Then User should receive error status code and message for get
    
Scenario: To check if able to read record with userid as alphanumeric
    Given User is on Get Method with end point for single user
    When  User sends request with alphanumeric userId
    Then User should receive error status code and message for get
    
Scenario: To check if able to read record with userId as null
    Given User is on Get Method with end point for single user
    When  User sends request with a null userId
    Then User should receive error status code and message for get
    
Scenario: To check if able to read record with userId as decimal
    Given User is on Get Method with end point for single user
    When  User sends request with a decimal userId
    Then User should receive error status code and message for get

Scenario: To check if able to read record with userId as blank
    Given User is on Get Method with end point with userID as blank
    When  User sends request with a blank userId
    Then User should receive error status code for get

