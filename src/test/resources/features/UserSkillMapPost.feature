Feature: UserSkillMap POST API automation testing
  
          
Scenario: To map new user and skill
			 Given User is on Post Method with endpoint url\UserSkills with valid JSON schema
			 When User sends request with valid input     
			 Then User should receive valid status codes
			 
#JSON schema validation is done in the When before the post request. 

Scenario: To map new user and skill , with skill id as alpha numeric
			 Given User is on Post Method with endpoint url\UserSkills with valid JSON schema
			 When User sends request with inputs where skill id is alphanumeric
			 Then User should receive Bad Request status codes
			 
			 
Scenario: To map new user and skill , with skill id as null
			 Given User is on Post Method with endpoint url\UserSkills with valid JSON schema
			 When User sends request with inputs where skill id is null    
			 Then User should receive Bad Request status codes
			 
			 
Scenario: To map new user and skill , with user id as null
			 Given User is on Post Method with endpoint url\UserSkills with valid JSON schema
			 When User sends request with inputs where user id is null    
			 Then User should receive Bad Request status codes		 
			 
Scenario: To map new user and skill , with months of experience as alpha numeric
			 Given User is on Post Method with endpoint url\UserSkills with valid JSON schema
			 When User sends request with inputs where month of experience is alphanumeric    
			 Then User should receive Bad Request status codes
			 
			 
	Scenario: To map new user and skill , with months of experience as null
			 Given User is on Post Method with endpoint url\UserSkills with valid JSON schema
			 When User sends request with inputs where months of experience as null    
			 Then User should receive Bad Request status codes			 
			 			 