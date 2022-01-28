package com.lms.api.stepdef.user;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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

public class UserPostStepDef {

	RequestSpecification requestSpec;
	Response response;
	String userId;
	String path;
	String sheetPost;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;

	Properties properties;
	Dbmanager dbmanager;
	private static final Logger logger = LogManager.getLogger(UserPostStepDef.class);
	public UserPostStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
	}

	// Scenario class will give us information at the runtime like the scenario,
	// name, getid() or isFailed()
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetPost = properties.getProperty("sheetPost");
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("userapi.excel.path"));
		excelSheetReaderUtil.readSheet(sheetPost);

	}

	public void requestSpecificationPost() throws Exception {
		requestSpec.header("Content-Type", "application/json");
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		assertThat("Schema Validation Failed", bodyExcel, matchesJsonSchemaInClasspath("userPostRequest_schema.json"));
		requestSpec.body(bodyExcel).log().all();

		response = requestSpec.when().post(path);
	}

	@Given("User is on Post Method with endpoint")
	public void user_is_on_post_method_with_endpoint() throws IOException {
		logger.info("@Given User is on Post Method with endpoint");
		
		RestAssured.baseURI = properties.getProperty("base_uri");
		requestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));

		path = properties.getProperty("endpointPost");
	}

	@When("User sends request with valid inputs")
	public void user_sends_request_with_valid_inputs() throws Exception {
		logger.info("@When User sends request with valid inputs");
		requestSpecificationPost();
	}

	@Then("User should receive status code and message for post")
	public void User_should_receive_status_code_and_message_for_post() throws Exception {
		logger.info("@Then User should receive status code and message for post");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.info("Expected response code: " + expStatusCode + " Expected message is: " + expMessage);
		String responseBody = response.prettyPrint();
		JsonPath js = response.jsonPath();
		String newUser = js.get("user_id");
		
		// Post Schema Validation
		assertThat(responseBody, matchesJsonSchemaInClasspath("userResponse_schema.json"));
				
		//Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		
		//Message validation
		response.then().assertThat().extract().asString().contains("User successfully Created!");

		// Retrieve an auto generated user_id for newly created user from tbl_lms_user
		ArrayList<String> dbValidList = dbmanager.dbvalidationUser(newUser);
		String dbUserId = dbValidList.get(0);
		
		ExtentCucumberAdapter.addTestStepLog("Newly created User record from DB :  : " + dbValidList.toString());
		// DB validation for a post request for a newly created user_id
		assertEquals(newUser, dbUserId);
		logger.info("Response Status code is =>  " + response.statusCode());
		logger.info("Response Body is =>  " + responseBody);
	}
	
	@When ("User sends request with duplicate record")
	public void User_sends_request_with_duplicate_record() throws Exception {
		requestSpecificationPost();
	}

	@When("User sends request with blank inputs for visa status and location")
	public void user_sends_request_with_blank_inputs_for_visa_status_and_location() throws Exception {
		logger.info("@When User sends request with blank inputs for visa status and location");
		requestSpecificationPost();
	}

	@When("User sends request with only First name with all other fields valid")
	public void user_sends_request_with_only_first_name_with_all_other_fields_valid() throws Exception {
		logger.info("@When User sends request with only First name with all other fields valid");
		requestSpecificationPost();
	}

	@When("User sends request with only Last name with all other fields valid")
	public void user_sends_request_with_only_last_name_with_all_other_fields_valid() throws Exception {
		logger.info("@When User sends request with only Last name with all other fields valid");
		requestSpecificationPost();
	}

	@When("User sends request with alphanumeric inputs")
	public void user_sends_request_with_alphanumeric_inputs() throws Exception {
		logger.info("@When User sends request with alphanumeric inputs");
		requestSpecificationPost();
	}

	@When("User sends request with invalid linkedin url")
	public void user_sends_request_with_invalid_linkedin_url() throws Exception {
		logger.info("@When User sends request with invalid linkedin url");
		requestSpecificationPost();
	}

	@When("User sends request with invalid time zone")
	public void user_sends_request_with_invalid_time_zone() throws Exception {
		logger.info("@When User sends request with invalid time zone");
		requestSpecificationPost();
	}

	@When("User sends request with invalid visa status")
	public void user_sends_request_with_invalid_visa_status() throws Exception {
		logger.info("@When User sends request with invalid visa status");
		requestSpecificationPost();
	}

	@When("User sends request with invalid phone number")
	public void user_sends_request_with_invalid_phone_number() throws Exception {
		logger.info("@When User sends request with invalid phone number");
		requestSpecificationPost();
	}
	
	@When("User sends request with phone number less than ten digits")
	public void user_sends_request_with_phone_number_less_than_ten_digits() throws Exception {
		logger.info("@When User sends request with invalid phone number");
		requestSpecificationPost();
	}

	@When("User sends request with blank inputs for linkedin_url, education_pg, comments")
	public void user_sends_request_with_blank_inputs_for_linkedin_url_education_pg_comments() throws Exception {
		logger.info("@When User sends request with blank inputs for linkedin_url, education_pg, comments");
		requestSpecificationPost();
	}

	@Then("User should receive error status code and message for post")
	public void user_should_receive_error_status_code_and_message_for_post() throws Exception {
		logger.info("@Then User should receive error status code and message for post");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.info("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);
		
		//Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		logger.info("Response Status code is =>  " + response.statusCode());
		logger.info("Response Body is =>  " + response.prettyPrint());
	}

}