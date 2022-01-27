package com.lms.api.stepdef.user;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.lms.api.dbmanager.Dbmanager;
import com.lms.api.stepdef.skills.SkillDeleteStepDef;
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
	Dbmanager dbmanager;
	
	private static final Logger logger = LogManager.getLogger(UserDeleteStepDef.class);
	
	public UserDeleteStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
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

		response = RequestSpec.when().delete(path);
	}

	@Given("User is on Delete Method with endpoint")
	public void user_is_on_delete_method_with_endpoint() throws IOException {
		logger.info("@Given User is on Delete Method with endpoint");
		RestAssured.baseURI = properties.getProperty("base_uri");
		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));

		String userId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserId");
		path = properties.getProperty("endpointDelete") + userId;
		logger.info("Path for Delete is: " + path);
	}

	@When("User sends request with existing User_id as input")
	public void user_sends_request_with_existing_user_id_as_input() throws IOException {
		logger.info("@When User sends request with existing User_id as input");
		requestSpecificationDelete();
	}

	@When("User sends request with  non_existing User_id as input")
	public void user_sends_request_with_non_existing_user_id_as_input() {
		logger.info("@When User sends request with  non_existing User_id as input");
		requestSpecificationDelete();
	}

	@When("User sends request with user_id as alphanumeric")
	public void user_sends_request_with_user_id_as_alphanumeric() {
		logger.info("@When User sends request with user_id as alphanumeric");
		requestSpecificationDelete();
	}

	@When("User sends request with user_id as blank")
	public void user_sends_request_with_user_id_as_blank() {
		logger.info("@When User sends request with user_id as blank");
		requestSpecificationDelete();
	}

	@Then("User should receive status code and message for delete")
	public void user_should_receive_status_code_and_message_for_delete() throws Exception {
		logger.info("@Then User should receive status code and message for delete");
		String userId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserId");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		String responseBody = response.prettyPrint();
		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		// Message validation
		JsonPath js = new JsonPath(responseBody);
		response.then().assertThat().extract().asString().contains("Deleted");

		try {
			//retrieve an array list from DBmanager
			ArrayList<String> dbValidList = dbmanager.dbvalidationUser(userId);

			if (dbValidList.get(0) == "Deleted")
				ExtentCucumberAdapter.addTestStepLog("DB validation for User " + userId + " is Deleted");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Given("User is on Delete Method with endpoint without userid as parameter")
	public void user_is_on_delete_method_with_endpoint_without_userid_as_parameter() {
		logger.info("@Given User is on Delete Method with endpoint without userid as parameter");
		RestAssured.baseURI = properties.getProperty("base_uri");
		RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));

		path = properties.getProperty("endpointDelete");
	}

	@When("User sends request without a user_id")
	public void user_sends_request_without_a_user_id() {
		logger.info("@When User sends request without a user_id");
		RequestSpec.header("Content-Type", "application/json");
		RequestSpec.log().all();
		response = RequestSpec.when().delete(path);
	}

	@Then("User should receive status code <code> for delete without parameter")
	public void user_should_receive_status_code_nf_status_code_for_delete_without_parameter() {
		logger.info("@Then User should receive status code <code> for delete without parameter");
		String expStatusCode = properties.getProperty("nfStatusCode");
		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		logger.info("Response Status code is => " + response.statusCode());
		
	}

}