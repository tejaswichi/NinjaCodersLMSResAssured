package com.lms.api.stepdef.user;

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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserDeleteStepDef {
	
	RequestSpecification RequestSpec;
	Response response;
	String userId;
	String path;
	String sheetDelete;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;

	Properties properties;

	public UserDeleteStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
	}

	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {

		this.scenario = scenario;
		sheetDelete = properties.getProperty("sheetDelete");
		// System.out.println(sheetPost);
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("userapi.excel.path"));
		excelSheetReaderUtil.readSheet(sheetDelete);
	}
	
	public void requestSpecificationDelete() {
		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.log().all();

		// Validation of requestBody with User schema
		//assertThat(pbodyExcel, matchesJsonSchemaInClasspath("put_schema.json"));
		System.out.println(path);
		response = RequestSpec.when().delete(path);
	}
	
	@Given("User is on Delete Method with endpoint")
	public void user_is_on_delete_method_with_endpoint() throws IOException {
		RestAssured.baseURI = properties.getProperty("base_uri");
		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));

		String userId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserId");
		System.out.println("UserId is : " + userId);
		path = properties.getProperty("endpointDelete") + userId;
		System.out.println("Path for Delete is: " + path);
	}
	
	@When("User sends request with existing User_id as input")
	public void user_sends_request_with_existing_user_id_as_input() throws IOException {
		requestSpecificationDelete();
	}
	
	@When("User sends request with  non_existing User_id as input")
	public void user_sends_request_with_non_existing_user_id_as_input() {
		requestSpecificationDelete();
	}
	
	@When("User sends request with user_id as alphanumeric")
	public void user_sends_request_with_user_id_as_alphanumeric() {
		requestSpecificationDelete();
	}
	

	@When("User sends request with user_id as blank")
	public void user_sends_request_with_user_id_as_blank() {
		requestSpecificationDelete();
	}
	
	@Then("User should receive status code and message for delete")
	public void user_should_receive_status_code_and_message_for_delete() throws Exception {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		//System.out.println("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);

		String responseBody = response.prettyPrint();
	//	System.out.println(response.statusCode());
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		JsonPath js= new JsonPath(responseBody);
		System.out.println(js);
		System.out.println("The Message in PUT is :  " +js.get("message"));
		response.then().assertThat().extract().asString().contains("Skill Successfully Updated");
	//	System.out.println("Response Status code is =>  " + response.statusCode());
	//	System.out.println("Response Body is =>  " + responseBody);
	}
	
	@Given("User is on Delete Method with endpoint without userid as parameter")
	public void user_is_on_delete_method_with_endpoint_without_userid_as_parameter() {
		RestAssured.baseURI = properties.getProperty("base_uri");
		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));

		path = properties.getProperty("endpointDelete");
		
	}
	
	@When("User sends request without a user_id")
	public void user_sends_request_without_a_user_id() {
		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.log().all();
		response = RequestSpec.when().delete(path);
	}
	

	
	@Then("User should receive status code {int} for delete without parameter")
	public void user_should_receive_status_code_for_delete_without_parameter(Integer expStatusCode) {
		String responseBody = response.prettyPrint();
		//System.out.println(response.statusCode());
		//assertEquals(expStatusCode, response.statusCode());
		response.then().statusCode(expStatusCode);
		
		//System.out.println("Response Status code is =>  " + response.statusCode());
		//System.out.println("Response Body is =>  " + responseBody);
	}



}
