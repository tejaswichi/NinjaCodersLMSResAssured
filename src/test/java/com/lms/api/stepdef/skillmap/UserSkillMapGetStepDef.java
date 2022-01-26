package com.lms.api.stepdef.skillmap;

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
import com.lms.api.stepdef.skills.SkillGetStepDef;
import com.lms.api.utilities.ExcelReaderUtil;
import com.lms.api.utilities.PropertiesReaderUtil;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSkillMapGetStepDef {

	RequestSpecification requestSpec;
	Response response;
	String path;
	String sheetGet;
	static String validId;
	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;

	Properties properties;
	Dbmanager dbmanager;
	private static final Logger logger = LogManager.getLogger(UserSkillMapGetStepDef.class);
	public UserSkillMapGetStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
		
		
	}

	// Scenario class will give us information at the runtime like the scenario
	// name, getid() or isFailed()
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetGet = properties.getProperty("sheetGet");
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("skillmap.excel.path"));
		excelSheetReaderUtil.readSheet(sheetGet);
		
	}
	
	public void requestSpecificationGET() throws IOException {

		//String UserSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");
		requestSpec.header("Content-Type", "application/json");
		requestSpec.log();
		response = requestSpec.when().get(path);
		
	}

	@Given("User is on GETall Method")
	public void user_is_on_getall_method() throws IOException {
		logger.info("@Given User is on GETall Method");
		RestAssured.baseURI = properties.getProperty("base_uri");
		requestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		path = properties.getProperty("skillmap.endpoint.getAll");
		

	}

	@When("User sends request from UserSkillMap API")
	public void user_sends_request_from_UserSkillMap_API() {
		logger.info("@When User sends request from UserSkillMap API");
		requestSpec.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON);
		requestSpec.log().all();
		response = requestSpec.when().get(path);
	
	}

	@Then("User receives status code with valid json schemaforall")
	public void user_receives_status_code_with_valid_json_schemaforall() throws IOException {
		logger.info("@Then User receives status code with valid json schemaforall");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseBody = response.prettyPrint();
		
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		assertThat(responseBody, matchesJsonSchemaInClasspath("userSkillMap_schemaAll.json"));
		logger.info("Validated the schema");
		
		logger.info("Expected response code: " + expStatusCode);
		logger.info("Response Body is =>  " + responseBody);


	}

	@Given("User is on GET Method")
	public void user_is_on_get_methods() throws IOException {
		logger.info("@Given User is on GET Method");
		RestAssured.baseURI = properties.getProperty("base_uri");
		requestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		String userSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");
		path = properties.getProperty("skillmap.endpoint.get")+userSkillsId;
				
	}

	@When("User sends request with Valid id")
	public void user_sends_request_with_valid_id() throws IOException {
		logger.info("@When User sends request with Valid id");
		requestSpecificationGET();
		
	}

	@Then("User receives status code with valid json schemas")
	public void user_receives_status_code_with_valid_json_schemas() throws IOException, Exception {
		logger.info("@Then User receives status code with valid json schemas");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseBody = response.prettyPrint();
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		assertThat(responseBody, matchesJsonSchemaInClasspath("userSkillMap_schema.json"));
		logger.info("Validated the schema");
		
		logger.info("Response Status code is =>  " + response.statusCode());
		logger.info("Response Body is =>  " + responseBody);
		
		String userSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(),"UserSkills");
		validId = userSkillsId;
		logger.info("UserId from excel : " + validId);
		
		JsonPath js = response.jsonPath();
		String rsUser_id = js.get("user_skill_id");
		
		// Retrieve a particular user record from tbl_lms_user
		ArrayList<String> dbValidList = dbmanager.dbvalidationUserSkillMap(rsUser_id);
		//String record = dbValidList.toString();
		String dbUserSkillsId = dbValidList.get(0);

		// DB validation for a get request for an existing user_id
		assertEquals(validId, dbUserSkillsId);
		ExtentCucumberAdapter.addTestStepLog("Get specific user " +	dbUserSkillsId 
				+ " record from DB : "+ dbValidList.toString());
	}

	@When("User sends request with Invalid id")
	public void user_sends_request_with_invalid_id() throws IOException {
		logger.info("@When User sends request with Invalid id");
		requestSpecificationGET();
		
	}

	@Then("User gets Response as Bad Request")
	public void user_gets_response_as_bad_request() throws IOException {
		logger.info("@Then User gets Response as Bad Request");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.info("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		String responseBody = response.prettyPrint();
		logger.info("Response Status code is =>  " + response.statusCode());
		logger.info("Response Body is =>  " + responseBody);
		
	}

	@When("User sends request with Blank id")
	public void user_sends_request_with_blank_id() throws IOException {
		logger.info("@When User sends request with Blank id");
		requestSpecificationGET();
		
	}

	@When("User sends request  with AlphaNumeric id")
	public void user_sends_request_with_alpha_numeric_id() throws IOException {
		logger.info("@When User sends request  with AlphaNumeric id");
		requestSpecificationGET();
		
	}

	@When("User sends request with Decimal as id")
	public void user_sends_request_with_decimal_as_id() throws IOException {
		logger.info("@When User sends request with Decimal as id");
		requestSpecificationGET();
		
	}

	@Given("User  sets GET request with a valid endpoint UserSkillsMap")
	public void user_sets_get_request_with_a_valid_endpoint_as_url_user_skills_map() {
		logger.info("@Given User  sets GET request with a valid endpoint UserSkillsMap");
		RestAssured.baseURI = properties.getProperty("base_uri");
		requestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		path = properties.getProperty("skillmap.endpoint.getSkillMap");
		
	}
	
	@When("User sends request with query param")
	public void user_sends_request_with_query_param() throws IOException {
		logger.info("@When User sends request with query param");
		requestSpecificationGET();
		
	}

	@Then("User receives status code with valid json schema for all Skills")
	public void user_receives_status_code_with_valid_json_schemaforall_skills() throws IOException {
		logger.info("@Then User receives status code with valid json schema for all Skills");
		String responseBody = response.prettyPrint();
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		assertThat(responseBody, matchesJsonSchemaInClasspath("userSkillMap_schemaGetAllSkills.json"));
		logger.info("Validated the schema");
		logger.info("Response Status code is =>  " + response.statusCode() + "Response Body is =>  " + responseBody);
		
	
	}

	

}
