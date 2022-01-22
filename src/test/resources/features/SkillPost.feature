Feature: Post Skill Feature

  Scenario: To create new Skill Record with Skill Name
    Given User is on POST method with endpoint url Skills
    When User sends request with inputs  skill name
    Then User is able to create a new Skill id

  Scenario: To create User Record with blank Skill Name
    Given User is on POST method with endpoint url Skills
    When User sends request with blank inputs
    Then User cannot create a new Skill id

  Scenario: To create user record with boolean value
    Given User is on POST method with endpoint url Skills
    When User sends request with boolean as skill name
    Then User is able to create a new Skill id

  Scenario: To create user record with integer
    Given User is on POST method with endpoint url Skills
    When User sends request with integer as  skill name
    Then User cannot create a new Skill id

  Scenario: To create record with alphanumeric
    Given User is on POST method with endpoint url Skills
    When User sends request with Alphanumeric as skill name
    Then User is able to create a new Skill id
