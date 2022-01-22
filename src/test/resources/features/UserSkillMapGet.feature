#JSON schema validation is done in the When before the get request and in Then of the response body.
#DB Validation is done for the userSkillMap.
@skillmap
Feature: Get UserSkillMap Feature
       
Scenario: To get all the Users with their Skill details
			 Given User is on GETall Method
			 When User sends request from UserSkillMap API
			 Then User receives status code with valid json schemaforall
			 
Scenario: To get all the details for specific User
			 Given User is on GET Method
			 When User sends request with Valid id   
			 Then User receives status code with valid json schemas	
			 
Scenario: To fetch the data with invalid userskill_id
			 Given User is on GET Method
			 When User sends request with Invalid id 
			 Then User gets Response as Bad Request 	
			 
Scenario: To fetch the data with blank userskill_id
			 Given User is on GET Method
			 When User sends request with Blank id
			 Then User gets Response as Bad Request
			 
Scenario: To fetch the data with alphanumeric userskill_id
			 Given User is on GET Method
			 When User sends request  with AlphaNumeric id 
			 Then User gets Response as Bad Request
			 
Scenario: To fetch the data with decimal no userskill_id
			 Given User is on GET Method
			 When User sends request with Decimal as id
			 Then User gets Response as Bad Request 	
			 
Scenario: Retrieve all users and all skills
			 Given User  sets GET request with a valid endpoint UserSkillsMap
			 When User sends request from UserSkillMap API  
			 Then User receives status code with valid json schema for all Skills		
			 
Scenario: Retreive particular user and  her skills with query param
			 Given User  sets GET request with a valid endpoint UserSkillsMap
			 When User sends request with query param     
			 Then User receives status code with valid json schema for all Skills	
			 
Scenario: Retrieve users of particular skill id with query param
			 Given User  sets GET request with a valid endpoint UserSkillsMap
			 When User sends request with query param     
			 Then User receives status code with valid json schema for all Skills				 			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 	   