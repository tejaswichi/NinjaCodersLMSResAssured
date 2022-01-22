Feature: UserSkillMap GetAPI automation testing
  
       
Scenario: To get all the Users with their Skill details
			 Given User is on GETall Method
			 When User sends request     
			 Then User receives status code with valid json schemaforall
			 
			 
Scenario: To get all the Users with their Skill details
			 Given User is on GET Methods
			 When User sends request with Valid id   
			 Then User receives status code with valid json schemas			
			 
Scenario: To fetch the data with invalid userskill_id
			 Given User is on GET Methods
			 When User sends request with Invalid id 
			 Then User gets Response as Bad Request 	
			 
Scenario: To fetch the data with blank userskill_id
			 Given User is on GET Methods
			 When User sends request with Blank id
			 Then User gets Response as Bad Request
			 
			 
			 
Scenario: To fetch the data with alphanumeric userskill_id
			 Given User is on GET Methods
			 When User sends request  with AlphaNumeric id 
			 Then User gets Response as Bad Request
			 
			 
Scenario: To fetch the data with decimal no userskill_id
			 Given User is on GET Methods
			 When User sends request with Decimal as id
			 Then User gets Response as Bad Request 	
			 
			 
Scenario: Retrieve all users and all skills
			 Given User  sets GET request with a valid endpoint as url/UserSkillsMap
			 When User sends request     
			 Then User receives status code with valid json schemaforallSkills		
			 
Scenario: Retreive particular user and  her skills
			 Given User  sets GET request with a valid endpoint as url/UserSkillsMap
			 When User sends request with query param     
			 Then User receives status code with valid json schemaforallSkills	
			 
Scenario: Retrieve users of particular skill id
			 Given User  sets GET request with a valid endpoint as url/UserSkillsMap
			 When User sends request with query param     
			 Then User receives status code with valid json schemaforallSkills				 			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 	   