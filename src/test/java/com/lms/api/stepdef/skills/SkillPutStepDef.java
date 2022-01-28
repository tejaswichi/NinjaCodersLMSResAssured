package com.lms.api.stepdef.skills;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
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

public class SkillPutStepDef {

	RequestSpecification requestSpec;
	Response response;
	String sheetPut;
	String path;
	boolean putResult;

	Scenario scenario;
	ExcelReaderUtil excelSheetReaderUtil;
	Properties properties;
	Dbmanager dbmanager;
	private static final Logger logger = LogManager.getLogger(SkillPutStepDef.class);
	public SkillPutStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
		
	}

	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetPut = properties.getProperty("sheetPut");
		excelSheetReaderUtil = new ExcelReaderUtil(properties.getProperty("skills.excel.path"));
		excelSheetReaderUtil.readSheet(sheetPut);

	}

	public void requestSpecificationPut() throws IOException {
		requestSpec.header("Content-Type", "application/json");
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		requestSpec.body(bodyExcel).log().body();
		assertThat("Schema Validation Failed", bodyExcel, matchesJsonSchemaInClasspath("skillPut_schema.json"));
		logger.info("Validated the schema");
		response = requestSpec.when().put(path);

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
		
	
	@Given("User is on PUT method with endpoint Skills")
	public void user_is_on_put_method_with_endpoint_skills() throws IOException {
		logger.info("@Given User is on PUT method with endpoint Skills");
		RestAssured.baseURI = properties.getProperty("base_uri");
		requestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				properties.getProperty("password"));
		String skill_id = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Skill_id");
		logger.info("SkillId is : " + skill_id);
		path = properties.getProperty("skills.endpoint") + skill_id;
		logger.info("Path for Put is " + path);

	}

	@When("User sends request  with valid skill id with valid Json Schema")
	public void user_sends_request_with_valid_skill_id_with_valid_json_schema() throws IOException {
		logger.info("@When User sends request  with valid skill id with valid Json Schema");
		requestSpecificationPut();

	}

	@Then("User should be able to update the Skill name and db is validated")
	public void user_should_be_able_to_update_the_skill_name() throws IOException, SQLException {
		logger.info("@Then User should be able to update the Skill name and db is validated");
		String skill_id = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Skill_id");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.info("Actual Response Status code=>  " + response.statusCode()
				+ "  Expected Response Status code=>  " + expStatusCode);
		String responseBody = response.prettyPrint();

		// Put Schema Validation
		assertThat("Schema Validation Passed", responseBody, matchesJsonSchemaInClasspath("skillResponse_schema.json"));

		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());

		// Message validation
		response.then().assertThat().extract().asString().contains("Skill Successfully Updated");

		logger.info("Response Message =>  " + responseMessage);

		// Retrieve a particular skill record from tbl_lms_skillmaster
		ArrayList<String> dbValidList = dbmanager.dbvalidationSkill(skill_id);
		String dbskill_Id = dbValidList.get(0);
		putResult = response.body().equals(dbValidList);
		if(putResult = true)
			ExtentCucumberAdapter.addTestStepLog("User is updated: ");
		else 
			ExtentCucumberAdapter.addTestStepLog("Failed to update the user");
			
		// DB validation for a get request for an existing skill_id
		assertEquals(skill_id, dbskill_Id);

		// Extracting a specific string response
		String ResString = response.then().extract().asString();
		JsonPath js = new JsonPath(ResString);
		
		String bodyExcel = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Body");
		List skillFromExcel= dataValidation(bodyExcel);
		
		String excelString=(String) skillFromExcel.get(1);
		String responseString =response.jsonPath().get("skill_name");
		logger.info("Response in String form :"+responseString);
		assertEquals(excelString.replaceAll("}", "").trim(),response.jsonPath().get("skill_name"));
		ExtentCucumberAdapter.addTestStepLog("Data validation Passed.Skill_id and Skill_name matched");
		logger.info("The Message in PUT is :  " + js.get("message"));
		logger.info("Response Body is =>  " + response.asPrettyString());
	}

	@When("User sends request with valid skill name but non existing skill id")
	public void user_sends_request_with_valid_skill_name_but_non_existing_skill_id() throws IOException {
		logger.info("@When User sends request with valid skill name but non existing skill id");
		requestSpecificationPut();
	}

	@When("User sends request with inputs like Selenium and Java")
	public void user_sends_request_with_inputs_like_selenium_and_java() throws IOException {
		logger.info("@When User sends request with inputs like Selenium and Java");
		requestSpecificationPut();

	}

	@When("To update skill name with invalid datatypes")
	public void to_update_skill_name_with_invalid_datatypes() throws IOException {
		logger.info("@When To update skill name with invalid datatypes");
		requestSpecificationPut();
	}

	@When("User sends request with blank skill Id")
	public void user_sends_request_with_blank_skill_id() throws IOException {
		logger.info("@When User sends request with blank skill Id");
		requestSpecificationPut();
	}

	@When("User sends request  with existing skill name with valid Json Schema")
	public void user_sends_request_with_existing_skill_name_with_valid_json_schema() throws IOException {
		logger.info("@When User sends request  with existing skill name with valid Json Schema");
		requestSpecificationPut();
	}

	@Then("User should not be able to update the Skill name")
	public void user_should_not_be_able_to_update_the_skill_name() throws IOException {
		logger.info("@Then User should not be able to update the Skill name");
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String expresponseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		logger.info("Actual Response Status code=>  " + response.statusCode()
				+ "  Expected Response Status code=>  " + expStatusCode);
		JsonPath js = response.jsonPath();
		Object resp_msg = js.get("message");
		// Status code validation
		assertEquals(Integer.parseInt(expStatusCode), response.statusCode());
		logger.info("Expected Response Message =>  " + expresponseMessage);
		logger.info("Response Body is =>  " + response.asPrettyString());
		logger.info("Response Message is =>  " + resp_msg);
	}
}
