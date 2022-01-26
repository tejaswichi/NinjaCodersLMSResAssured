package com.lms.api.stepdef.skillmap;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
import io.restassured.module.jsv.JsonSchemaValidationException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSkillMapPutStepDef {

	RequestSpecification RequestSpec;
	Response response;
	String path;
	String sheetPut;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;
	Properties properties;
	Dbmanager dbmanager;
	private static final Logger logger = LogManager.getLogger(UserSkillMapPutStepDef.class);
	public UserSkillMapPutStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
	}

	// Before annotation from io cucumber
	// Scenario class will give us information at the runtime like the scenario
	// name, getid() or isFailed()
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetPut = properties.getProperty("sheetPut");
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("skillmap.excel.path"));
		excelSheetReaderUtil.readSheet(sheetPut);

	}

	public void requestSpecificationPut() throws IOException {

		String UserSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.body(bodyExcel).log().all();
		//Soft Assertion
		/*assertThrows(JsonSchemaValidationException.class, () -> {

			// Validation of requestBody with User schema
			assertThat("Schema Validation Failed",bodyExcel, matchesJsonSchemaInClasspath("userSkillMap_schema.json"));
			
		});*/
		
		//Hard Assertion
		assertThat("Schema Validation Failed",bodyExcel, matchesJsonSchemaInClasspath("userSkillMap_schema.json"));
		response = RequestSpec.when().put(path + UserSkillsId);

	}

	@Given("User is on Put Method")
	public void user_is_on_put_method() {
		logger.info("@Given User is on Put Method ");
		RestAssured.baseURI = properties.getProperty("base_uri");
		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		path = properties.getProperty("skillmap.endpoint.put");
	}

	@When("User sends request with input as valid User_skill_Id")
	public void user_sends_request_with_input_as_valid_user_skill_id() throws IOException {
		logger.info("@When User sends request with input as valid User_skill_Id");

		String UserSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.body(bodyExcel).log().all();

		// Validation of requestBody with User schema
		assertThat(bodyExcel, matchesJsonSchemaInClasspath("userSkillMap_schema.json"));
		response = RequestSpec.when().put(path + UserSkillsId);
	}

	@Then("User should receive valid status code")
	public void user_should_receive_valid_status_code() throws IOException, Exception {
		logger.info("@Then User should receive valid status code");
		String UserSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");

		String expUpdateField = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UpdateField");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");

		String responseBody = response.prettyPrint();
		logger.info("Response Body is =>  " + responseBody);

		// Put Schema Validation
		assertThat(responseBody, matchesJsonSchemaInClasspath("userSkillMapPutResponse_schema.json"));
		logger.info("Validated the response body schema");

		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		// Retrieve a particular skill record from tbl_lms_skillmaster
		ArrayList<String> dbValidList = dbmanager.dbvalidationUserSkillMap(UserSkillsId);
		String dbuser_skill_id = dbValidList.get(0);

		// DB validation for a put request for an updated field
		assertEquals(UserSkillsId, dbuser_skill_id);

		// verifying the updates made
		ExtentCucumberAdapter.addTestStepLog(
				"Updated skillId to be: " + expUpdateField + " but found " + response.jsonPath().getInt("skill_id"));
		assertEquals(Integer.parseInt(expUpdateField), response.jsonPath().getInt("skill_id"));

		// Message validation
		response.then().assertThat().extract().asString().contains(expMessage);

		logger.info("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);
		logger.info("Response Status code is =>  " + response.statusCode());

	}

	@When("User sends request with input as invalid User_skill_Id")
	public void user_sends_request_with_input_as_invalid_user_skill_id() throws IOException {
		logger.info("@When User sends request with input as invalid User_skill_Id");
		requestSpecificationPut();
	}

	@When("User sends request with input as invalid skill Id")
	public void user_sends_request_with_input_as_invalid_skill_id() throws IOException {
		logger.info("@When User sends request with input as invalid skill Id");
		requestSpecificationPut();
	}

	@When("User sends request with number of months as input")
	public void user_sends_request_with_number_of_months_as_input() throws IOException {
		logger.info("@When User sends request with number of months as input");
		requestSpecificationPut();
	}

	@Then("User should receive valid status code with updated months of exp")
	public void user_should_receive_valid_status_code_with_updated_months_of_exp() throws IOException, Exception {
		logger.info("@Then user_should_receive_valid_status_code_with_updated_months_of_exp");
		String UserSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");
		String expUpdateField = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UpdateField");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");

		String responseBody = response.prettyPrint();
		logger.info("Response Body is =>  " + responseBody);

		// Put Schema Validation
		assertThat(responseBody, matchesJsonSchemaInClasspath("userSkillMapPutResponse_schema.json"));
		logger.info("Validated the response body schema");

		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		// Retrieve a particular skill record from tbl_lms_skillmaster
		ArrayList<String> dbValidList = dbmanager.dbvalidationUserSkillMap(UserSkillsId);
		String dbuser_skill_id = dbValidList.get(0);

		// DB validation for a put request for an updated field
		assertEquals(UserSkillsId, dbuser_skill_id);

		// verifying the updates made
		ExtentCucumberAdapter.addTestStepLog("Updated monthsOfExp to be: " + expUpdateField + " but found "
				+ response.jsonPath().getInt("months_of_exp"));
		assertEquals(Integer.parseInt(expUpdateField), response.jsonPath().getInt("months_of_exp"));

		// Message validation
		response.then().assertThat().extract().asString().contains(expMessage);

		logger.info("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);
		logger.info("Response Status code is =>  " + response.statusCode());

	}

	@When("User sends request with invalid input as boolean")
	public void user_sends_request_with_invalid_input_as_boolean() throws IOException {
		logger.info("@When User sends request with invalid input as boolean");
		requestSpecificationPut();
	}

	@Then("User should receive Bad Requests")
	public void user_should_receive_bad_requests() throws IOException {
		logger.info("@Then User should receive Bad Requests");

		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");

		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		// Message validation
		response.then().assertThat().extract().asString().contains(expMessage);
		String responseBody = response.prettyPrint();

		logger.info(
				"Expected response code: " + expStatusCode + "Actual response code:  " + response.statusCode());
		logger.info("Response Body is =>  " + responseBody);

	}

}
