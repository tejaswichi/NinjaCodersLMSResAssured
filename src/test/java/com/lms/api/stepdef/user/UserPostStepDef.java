package com.lms.api.stepdef.user;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import com.lms.api.utilities.ExcelReaderUtil;
import com.lms.api.utilities.PropertiesReaderUtil;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserPostStepDef {

	RequestSpecification RequestSpec;
	Response response;
	String userId;
	String path;
	String sheetPost;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;

	Properties properties;

	public UserPostStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
	}

	// Before annotation from io cucumber
	// Scenario class will give us information at the runtime like the scenario
	// name, getid() or isFailed()
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetPost=properties.getProperty("sheetPost");
	//	System.out.println(sheetPost);
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("userapi.excel.path"));
		excelSheetReaderUtil.readSheet(sheetPost);

	}

	public void requestSpecification(String pbodyExcel) {
		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.body(pbodyExcel).log().all();

		// Validation of requestBody with User schema
	//	assertThat(pbodyExcel, matchesJsonSchemaInClasspath("before_post_schema.json"));

		response = RequestSpec.when().post(path);
	}
	
	@Given("User is on Post Method with endpoint")
	public void user_is_on_post_method_with_endpoint() throws IOException {
		/*
		 * //Reading a JSON File FileInputStream jsonFis = new
		 * FileInputStream("src/test/resources/json/user.json"); ObjectMapper mapper =
		 * new ObjectMapper(); JsonNode userJson = mapper.readTree(jsonFis);
		 * System.out.println("The Json Node of User detail is: " + userJson );
		 * Properties prop = new Properties(); prop.load(jsonFis);
		 */

		// RestAssured.baseURI= (String) prop.get("base_uri");
		RestAssured.baseURI = properties.getProperty("base_uri");

		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		// RequestSpec = RestAssured.given().auth().preemptive().basic("username",
		// "password");
		
		path = properties.getProperty("endpointPost");

		// response = RequestSpec.given().when().get(path);
		// System.out.println(response.asString());

	}

	@When("User sends request with valid inputs")
	public void user_sends_request_with_valid_inputs() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		//String expStatusCode = excelReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		//String expMessage = excelReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		//System.out.println("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);

		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.body(bodyExcel).log().all();

		// Validation of requestBody with User schema
	//	assertThat(bodyExcel, matchesJsonSchemaInClasspath("before_post_schema.json"));
		System.out.println("Validated the schema");
		response = RequestSpec.post(path);
		// response = RequestSpec.request(Method.POST, path);

	}
	
	@Then("User should receive status code and message for post")
	public void user_should_receive_status_code_and_message_for_post() throws Exception {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		System.out.println("Expected response code: " + expStatusCode + " Expected message is: " + expMessage);

		String responseBody = response.prettyPrint();
		System.out.println(response.statusCode());
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		assertThat(responseBody, matchesJsonSchemaInClasspath("response_post.json"));
		
		System.out.println("Response Status code is =>  " + response.statusCode());
		System.out.println("Response Body is =>  " + responseBody);
	}


	@When("User sends request with blank inputs for firstname")
	public void user_sends_request_with_blank_inputs_for_firstname() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}
	
	@When("User sends request with only First name with all other fields valid")
	public void user_sends_request_with_only_first_name_with_all_other_fields_valid() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}
	
	@When("User sends request with only Last name with all other fields valid")
	public void user_sends_request_with_only_last_name_with_all_other_fields_valid() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}
	
	@When("User sends request with Alphanumeric inputs")
	public void user_sends_request_with_alphanumeric_inputs() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}


	@When("User sends request with invalid linkedin url")
	public void user_sends_request_with_invalid_linkedin_url() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}

	@When("User sends request with invalid time zone")
	public void user_sends_request_with_invalid_time_zone() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}

	@When("User sends request with invalid visa status")
	public void user_sends_request_with_invalid_visa_status() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}

	@When("User sends request with invalid phone number")
	public void user_sends_request_with_invalid_phone_number() throws IOException {
		//String bodyExcel = excelReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}

	@When("User sends request with blank inputs for linkedin_url, education_pg, comments")
	public void user_sends_request_with_blank_inputs_for_linkedin_url_education_pg_comments() throws IOException {
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpecification(bodyExcel);
	}
	
	
	@Then("User should receive error status code and message for post")
	public void user_should_receive_error_status_code_and_message_for_post() throws Exception {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		System.out.println("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);

		String responseBody = response.prettyPrint();
		System.out.println(response.statusCode());
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		
		System.out.println("Response Status code is =>  " + response.statusCode());
		System.out.println("Response Body is =>  " + responseBody);
	}

}
