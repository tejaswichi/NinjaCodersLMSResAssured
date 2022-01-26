#User logged in as "APIPROCESSING" with "Basic Auth"
#JSON schema validation is done in the When before the put request and in Then of the response body.
#DB Validation is done for the updated skill.


Feature: Put Skill Feature
@skill
  Scenario: To update skill name in existing Skill Id
    Given User is on PUT method with endpoint Skills
    When User sends request  with valid skill id with valid Json Schema
    Then User should be able to update the Skill name and db is validated

  Scenario: To update skill name in non-existing Skill Id
    Given User is on PUT method with endpoint Skills
    When  User sends request with valid skill name but non existing skill id
    Then User should not be able to update the Skill name

  Scenario: To update more than one skill name in skill Id
    Given User is on PUT method with endpoint Skills
    When  User sends request with inputs like Selenium and Java
    Then User should not be able to update the Skill name

  Scenario: To update skill name with invalid datatypes
    Given User is on PUT method with endpoint Skills
    When To update skill name with invalid datatypes
    Then User should not be able to update the Skill name

  Scenario: To update Skill name with skill id blank
    Given User is on PUT method with endpoint Skills
    When  User sends request with blank skill Id
    Then User should not be able to update the Skill name

  Scenario: To update with a duplicate skill name in existing Skill Id
    Given User is on PUT method with endpoint Skills
    When User sends request  with existing skill name with valid Json Schema
    Then User should not be able to update the Skill name
