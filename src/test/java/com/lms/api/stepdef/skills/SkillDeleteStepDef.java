package com.lms.api.stepdef.skills;

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
public class SkillDeleteStepDef {

	RequestSpecification requestSpec;
	Response response;
	String path;
	String sheetDelete;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;
	Properties properties;
	Dbmanager dbmanager;
	private static final Logger logger = LogManager.getLogger(SkillDeleteStepDef.class);
	
	public SkillDeleteStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
	}

	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetDelete = properties.getProperty("sheetDelete");
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("skills.excel.path"));
		excelSheetReaderUtil.readSheet(sheetDelete);

	}

	public void requestSpecificationDelete() {
		requestSpec.header("Content-Type", "application/json");
		requestSpec.log();
		response = requestSpec.when().delete(path);
	}

	@Given("User is on DELETE method with endpoint")
	public void user_is_on_delete_method_with_endpoint() throws IOException {
		logger.info("@Given User is on DELETE method with endpoint");
		RestAssured.baseURI = properties.getProperty("base_uri");
		requestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		String skill_id = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Skill_id");
		
		logger.debug("SkillId is : " + skill_id);
		
		path = properties.getProperty("skills.endpoint.Delete") + skill_id;
		logger.debug("Path for Delete is " + path);

	}

	@When("User sends request with existing skill_id")
	public void user_sends_request_with_existing_skill_id() throws IOException {
		logger.info("@When User sends request with existing skill_id");
		requestSpecificationDelete();
		
	}

	@Then("User should be able to delete the existing skill_id")
	public void user_should_be_able_to_delete_the_existing_skill_id() throws IOException {
		logger.info("@Then User should be able to delete the existing skill_id");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		String skill_id = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Skill_id");
		String responseBody = response.asPrettyString();
		logger.debug("Actual Response Status code=>  " + response.statusCode()
				+ "  Expected Response Status code=>  " + expStatusCode);
		logger.debug("Response Body is =>  " + responseBody);
		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		
		// Message validation
		JsonPath js = new JsonPath(responseBody);
		logger.debug(js);
		response.then().assertThat().extract().asString().contains("Deleted");
		try {
			//retrieve an array list from DBmanager
			ArrayList<String> dbValidList = dbmanager.dbvalidationSkill(skill_id);

			if (dbValidList.get(0) == "Deleted")
				ExtentCucumberAdapter.addTestStepLog("DB validation for User " + skill_id + " is Deleted");

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
			logger.debug("Response Message =>  " + responseMessage);
	}

	@When("User sends request with non-existing skill_id")
	public void user_sends_request_with_non_existing_skill_id() {
		logger.info("@When User sends request with non-existing skill_id");
		requestSpecificationDelete();
	}

	@When("User sends request with blank skill_id")
	public void user_sends_request_with_blank_skill_id() {
		logger.info("@When User sends request with blank skill_id");
		requestSpecificationDelete();
	}

	@When("User sends the request with alphanumeric skill Id")
	public void user_sends_the_request_with_alphanumeric_skill_id() {
		logger.info("@When User sends the request with alphanumeric skill Id");
		requestSpecificationDelete();
	}

	@When("User enter the Skill_id as decimal")
	public void user_enter_the_skill_id_as_decimal() {
		logger.info("@When User enter the Skill_id as decimal");
		requestSpecificationDelete();
	}

	@Then("User should recieve an error status code")
	public void user_should_recieve_an_error_status_code() throws IOException {
		logger.info("@Then User should recieve an error status code");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.debug("Actual Response Status code=>  " + response.statusCode()
				+ "  Expected Response Status code=>  " + expStatusCode);
		String responseBody = response.asPrettyString();
		logger.debug("Response Body is =>  " + responseBody);
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		logger.debug("Response Message =>  " + responseMessage);

	}
}
