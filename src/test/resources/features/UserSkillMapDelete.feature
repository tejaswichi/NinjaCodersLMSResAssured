Feature: UserSkillMap DeleteAPI automation testing
  
@deleteAUser       
Scenario: User trying to delete the user_skill_id with Existing user_skill_Id
			 Given  User is on DELETE Method
			 When User sends request with existing user_skill_ Id  as input    
			 Then User gets Response

Scenario: User trying to delete the user_skill_id with non Existing user_skill_Id
			 Given  User is on DELETE Method
			 When User sends request with nonexisting user_skill_ Id  as input    
			 Then User gets Response
			 
			 
Scenario: User trying to delete the user_skill_id with blank user_skill_ Id
			 Given  User is on DELETE Method
			 When User sends request with user_skill_id as blank   
			 Then User gets Response	
			 
Scenario: User trying to delete the user_skill_id with alphanumeric user_skill_ Id
			 Given  User is on DELETE Method
			 When User sends request with  user_skill_id as alphanumeric   
			 Then User gets Response		
			 
			 
Scenario: User trying to delete the user_skill_id with decimal user_skill_ Id
			 Given  User is on DELETE Method
			 When User sends request with  user_skill_id as decimal no    
			 Then User gets Response			 	 		 			 