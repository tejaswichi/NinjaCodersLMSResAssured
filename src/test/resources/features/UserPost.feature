@post
Feature: Post User Feature
  
          
Scenario: Check if user is able to create new User Record
		Given User is on Post Method with endpoint
		When User sends request with valid inputs     
		Then User should receive status code and message for post
			 
#JSON schema validation is done in the When before the post request. 

Scenario: To create user record with visa status and location blank
		Given User is on Post Method with endpoint
		When User sends request with blank inputs for firstname
		Then User should receive error status code and message for post
			 
Scenario: To create user record with only First name
		Given User is on Post Method with endpoint
		When User sends request with only First name with all other fields valid 
		Then User should receive error status code and message for post
			 
Scenario: To create user record with only Last name
		Given User is on Post Method with endpoint
		When User sends request with only Last name with all other fields valid  
		Then User should receive error status code and message for post
			 
Scenario: To create record with alphanumeric name and location
		Given User is on Post Method with endpoint
		When User sends request with Alphanumeric inputs 
		Then User should receive status code and message for post
			 
Scenario: To create record with invalid linkedin url
		Given User is on Post Method with endpoint
		When User sends request with invalid linkedin url   
		Then User should receive error status code and message for post
			 
Scenario: To create record with invalid time zone
		Given User is on Post Method with endpoint
		When User sends request with invalid time zone   
		Then User should receive error status code and message for post
			 
Scenario: To create record with invalid visa status
		Given User is on Post Method with endpoint
		When User sends request with invalid visa status  
		Then User should receive error status code and message for post

Scenario: To create record with invalid phone number
		Given User is on Post Method with endpoint
		When User sends request with invalid visa status  
		Then User should receive error status code and message for post
			
Scenario: To create User Record with blank linkedin_url, education_pg, comments
		Given User is on Post Method with endpoint
		When User sends request with blank inputs for linkedin_url, education_pg, comments     
		Then User should receive status code and message for post

			 
			 


	 