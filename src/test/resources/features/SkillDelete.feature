#@check
Feature: Delete Skill Feature 

Scenario: User wants to delete with existing skill id
    Given User is on DELETE method with endpoint
    When User sends request with existing skill_id 
		Then User should be able to delete the existing skill_id
	   
Scenario: User wants to delete  with non-existing skill id
    Given User is on DELETE method with endpoint
    When User sends request with non-existing skill_id
		Then User should recieve an error status code
		
Scenario: User leaves the Skill id blank.
    Given User is on DELETE method with endpoint
    When User sends request with blank skill_id
		Then User should recieve an error status code
  
Scenario: User enters the Skill_id as alphanumeric.
    Given User is on DELETE method with endpoint
    When User sends the request with alphanumeric skill Id
    Then User should recieve an error status code
		
Scenario: User enters the skill id as decimal
    Given User is on DELETE method with endpoint
    When User enter the Skill_id as decimal
		Then User should recieve an error status code
		
