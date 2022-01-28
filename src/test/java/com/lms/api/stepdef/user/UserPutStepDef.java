package com.lms.api.stepdef.user;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.lms.api.dbmanager.Dbmanager;
import com.lms.api.utilities.ExcelReaderUtil;
import com.lms.api.utilities.PropertiesReaderUtil;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserPutStepDef {

	RequestSpecification RequestSpec;
	Response response;
	String userId;
	String path;
	String sheetPut;
	boolean putResult;
	static String bodyarray[];

	static String exUserId;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;
	Properties properties;
	Dbmanager dbmanager;
	private static final Logger logger = LogManager.getLogger(UserPutStepDef.class);
	
	
	public UserPutStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
		}

	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetPut = properties.getProperty("sheetPut");
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("userapi.excel.path"));
		excelSheetReaderUtil.readSheet(sheetPut);

	}

	public void requestSpecification() throws Exception {
		RequestSpec.header("Content-Type", "application/json");
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		RequestSpec.body(bodyExcel).log().all();

		// Validation of requestBody with User schema
		assertThat("Schema Validation Failed", bodyExcel, matchesJsonSchemaInClasspath("userPutRequest_schema.json"));
		response = RequestSpec.when().put(path);

	}
	
	public List dataValidation(String excelString) {
		System.out.println("bodyy" + excelString);
		StringTokenizer bodyToken  = new StringTokenizer(excelString, ",");
		System.out.println(bodyToken);
		ArrayList list = new ArrayList<String>();
		while(bodyToken.hasMoreTokens()) {
			String str  = bodyToken.nextToken();
			StringTokenizer stringToken  = new StringTokenizer(str, ":");

			 int i = 1;
			  while(stringToken.hasMoreTokens()) { 
				  String str22 = stringToken.nextToken();
				  
				  if (i%2 ==0) {
					  String str33 = str22.replaceAll("\"", "");
					   list.add(str33);
				  }
				  i++;
				 }
			}
		return list;
	}
		

	@Given("^User is on Put Method with endpoint")
	public void user_is_on_put_method_with_endpoint() throws IOException {
		logger.info("@Given User is on Put Method with endpoint");

		RestAssured.baseURI = properties.getProperty("base_uri");
		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));

		String userId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserId");

		path = properties.getProperty("endpointGet") + userId;

	}

	@When("User sends request to update user name with valid input")
	public void user_sends_request_to_update_user_name_with_valid_input() throws Exception {
		logger.info("@When User sends request to update user name with valid input");
		requestSpecification();

	}

	@Then("User should receive status code and message for put")
	public void user_should_receive_status_code_and_message_for_put() throws Exception {
		logger.info("@Then User should receive status code and message for put");
		String userId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserId");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.info("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);

		String responseBody = response.prettyPrint();
		System.out.println(response.statusCode());
		// Put Schema Validation
		assertThat("Schema Validation Passed",responseBody, matchesJsonSchemaInClasspath("userResponse_schema.json"));

		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		// Message validation
		response.then().assertThat().extract().asString().contains("User Successfully Updated");
		
		String resString = response.then().extract().asString();
				
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		List skillFromExcel= dataValidation(bodyExcel);
		
		System.out.println(skillFromExcel);
		for (Object ob : skillFromExcel) {
			String single = (String) ob;
				response.then().assertThat().extract().response().asString().contains(single);
		}
		ExtentCucumberAdapter.addTestStepLog("User is updated and data is validated: " + skillFromExcel);
		
		// Retrieve an updated user_id from tbl_lms_user
		ArrayList<String> dbValidList = dbmanager.dbvalidationUser(userId);
		String dbUserId = dbValidList.get(0);
	
		// DB validation for a put request for updated user_id
		assertEquals(userId, dbUserId);

		logger.info("Response Status code is =>  " + response.statusCode());
		logger.info("Response Body is =>  " + responseBody);
	}

	@When("User sends request to update user name with alphanumeric input")
	public void user_sends_request_to_update_user_name_with_alphanumeric_input() throws Exception {
		logger.info("@When User sends request to update user name with alphanumeric input");
		requestSpecification();
	}

	@When("User sends request to update phone number with invalid input")
	public void user_sends_request_to_update_phone_number_with_invalid_input() throws Exception {
		logger.info("@When User sends request to update phone number with invalid input");
		requestSpecification();
	}

	@When("User sends request to update location and comments with alphanumeric input.")
	public void user_sends_request_to_update_location_and_comments_with_alphanumeric_input() throws Exception {
		logger.info("@When User sends request to update location and comments with alphanumeric input.");
		requestSpecification();
	}

	@When("User sends request to update time zone with valid input")
	public void user_sends_request_to_update_time_zone_with_valid_input() throws Exception {
		logger.info("@When User sends request to update time zone with valid input");
		requestSpecification();
	}

	@When("User sends request to update time zone with invalid input")
	public void user_sends_request_to_update_time_zone_with_invalid_input() throws Exception {
		logger.info("@When User sends request to update time zone with invalid input");
		requestSpecification();
	}

	@When("User sends request to update Linkedin id with valid input")
	public void user_sends_request_to_update_linkedin_id_with_valid_input() throws Exception {
		logger.info("@When User sends request to update Linkedin id with valid input");
		requestSpecification();
	}

	@When("user sends request to update visa status with numeric input")
	public void user_sends_request_to_update_visa_status_with_numeric_input() throws Exception {
		logger.info("@When user sends request to update visa status with numeric input");
		requestSpecification();
	}

	@Then("User should receive error status code and message for put")
	public void user_should_receive_error_status_code_and_message_for_put() throws Exception {
		logger.info("@When User should receive error status code and message for put");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		System.out.println("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);

		JsonPath js = response.jsonPath();
		String resp_msg = js.get("message");

		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		logger.info("Response Status code is =>  " + response.statusCode());
		logger.info("Resposne Message is =>  " + resp_msg);
		logger.info("Response Body is =>  " + response.prettyPrint());
	}

}