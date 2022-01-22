#@check
Feature: Get Skill Feature 

Scenario: To get all the Skill details
    Given User is on GET method with endpoint Skills
    When User sends request from Skill API
		Then User receives list of all Skills
 
Scenario: To get specific Skill id details 
    Given User is on GET method with endpoint Skills with Skill_id
    When User sends the request with specific Skill_Id
		Then User receives the particular Skill_Id details
		
Scenario: To get specific Skill details with invalid id
    Given User is on GET method with endpoint Skills with Skill_id
    When User sends the request with invalid Skill Id
		Then User doesnot get the particular Skill_Id 
  
Scenario: To get specific Skill id details with skill id as alphanumeric
    Given User is on GET method with endpoint Skills with Skill_id
    When User sends the request with alphanumeric Skill Id
		Then User doesnot get the particular Skill_Id 
  
Scenario: To get specific Skill id details with skillid as null
    Given User is on GET method with endpoint Skills and Skill id null
    When User sends the request with skill id as null
		Then User doesnot get the particular Skill_Id 
