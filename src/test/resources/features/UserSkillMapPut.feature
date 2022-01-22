Feature: UserSkillMap Put API automation testing
  
          
Scenario: To update new skill Id in existing User_skill_Id
			 Given User is on Put Method
			 When User sends request with input as valid User_skill_Id     
			 Then User should receive valid status code
			 
#JSON schema validation is done in the When before the put request. 

Scenario: To update User Id or skill Id in non-existing User_skill_Id
			 Given User is on Put Method
			 When  User sends request with input as invalid User_skill_Id   
			 Then User should receive Bad Requests 
			 
Scenario: To update with invalid skill Id in user_skill_Id
			 Given User is on Put Method
			 When  User sends request with input as invalid skill Id   
			 Then User should receive Bad Requests 
			 
			 
Scenario: To update months of exp in existing User_skill_Id
			 Given User is on Put Method
			 When User sends request with number of months as input   
			 Then User should receive valid status code 
			 
Scenario: To update with invalid months of exp
			 Given User is on Put Method
			 When User sends request with invalid input as boolean   
			 Then User should receive Bad Requests 		 			 			 