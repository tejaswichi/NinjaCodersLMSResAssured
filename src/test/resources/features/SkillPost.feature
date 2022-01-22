#User logged in as "APIPROCESSING" with "Basic Auth"
#JSON schema validation is done in the When before the post request and in Then of the response body.
#DB Validation is done for the newly created skill.

@skill
Feature: Post Skill Feature

Scenario: To create new Skill Record with Skill Name
    Given User is on POST method with endpoint url Skills
    When User sends request with inputs  skill name with valid Json Schema
    Then User is able to create a new Skill id and db is validated

Scenario: To create User Record with blank Skill Name
    Given User is on POST method with endpoint url Skills
    When User sends request with blank inputs
    Then User cannot create a new Skill id

Scenario: To create user record with boolean value
    Given User is on POST method with endpoint url Skills
    When User sends request with boolean as skill name
    Then User is able to create a new Skill id and db is validated

Scenario: To create user record with integer
    Given User is on POST method with endpoint url Skills
    When User sends request with integer as  skill name
    Then User cannot create a new Skill id

Scenario: To create record with alphanumeric
    Given User is on POST method with endpoint url Skills
    When User sends request with Alphanumeric as skill name
    Then User is able to create a new Skill id and db is validated
    
Scenario: To create record with an existing skill name
    Given User is on POST method with endpoint url Skills
    When User sends request with an existing skill name
    Then User cannot create a new Skill id