
Feature: Put Skill Feature
#@check
  Scenario: To update skill name in existing Skill Id
    Given User is on PUT method with endpoint Skills
    When  To update skill name in existing Skill Id
    Then User should be able to update the Skill name

  Scenario: To update skill name in non-existing Skill Id
    Given User is on PUT method with endpoint Skills
    When  User sends request with valid skill name but non existing skill id
    Then User should not be able to update the Skill name

  Scenario: To update more than one skill name in skill Id
    Given User is on PUT method with endpoint Skills
    When  User sends request with inputs like Java and Python
    Then User should not be able to update the Skill name

  Scenario: To update skill name with invalid datatypes
    Given User is on PUT method with endpoint Skills
    When To update skill name with invalid datatypes
    Then User should not be able to update the Skill name

  Scenario: To update Skill name with skill id blank
    Given User is on PUT method with endpoint Skills
    When  User sends request with blank skill Id
    Then User should not be able to update the Skill name
