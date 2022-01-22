package com.lms.api.stepdef.skills;

import com.lms.api.dbmanager.Dbmanager;
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
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class SkillPutStepDef {
	
	RequestSpecification requestSpec;
	Response response;
	//int skill_id;
	String sheetPut;
	String path;
	
	Scenario scenario;
	ExcelReaderUtil excelSheetReaderUtil;
	Properties properties;
	Dbmanager dbmanager;

	public SkillPutStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();

		dbmanager = new Dbmanager();

	}
	
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
	this.scenario=scenario;
	sheetPut=properties.getProperty("sheetPut");
	excelSheetReaderUtil =new ExcelReaderUtil(properties.getProperty("skills.excel.path"));
	excelSheetReaderUtil.readSheet(sheetPut);
	
	}
	
	public void requestSpecificationPut() throws IOException {
		requestSpec.header("Content-Type", "application/json");
		String bodyExcel=excelSheetReaderUtil.getDataFromExcel(scenario.getName(),"Body");
		requestSpec.body(bodyExcel).log().body();
		try {
			assertThat(bodyExcel, matchesJsonSchemaInClasspath("skillput-schema.json"));
			System.out.println("Validated the schema");	
		}catch(AssertionError ex) {
			System.out.println("Schema Validation Failed");
			
		}
		
		response = requestSpec.when().put(path);
		
	}
	
	@Given("User is on PUT method with endpoint Skills")
	public void user_is_on_put_method_with_endpoint_skills() throws IOException {
		RestAssured.baseURI=properties.getProperty("base_uri");
		requestSpec=RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				    properties.getProperty("password"));
		String skill_id=excelSheetReaderUtil.getDataFromExcel(scenario.getName(),"Skill_id");
		System.out.println("SkillId is : " +skill_id);
		path=properties.getProperty("skills.endpoint") + skill_id;
		System.out.println("Path for Put is "+ path);
				
	}

	@When("To update skill name in existing Skill Id")
	public void user_sends_request_with_valid_skill_name() throws IOException {
		requestSpecificationPut();
		
	}

	@Then("User should be able to update the Skill name")
	public void user_should_be_able_to_update_the_skill_name() throws IOException, SQLException {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		System.out.println("Actual Response Status code=>  " + response.statusCode() + "  Expected Response Status code=>  " + expStatusCode);
			
		// FROM REST CALL - RESPONSE
		String responseBody = response.asPrettyString();
		assertThat(responseBody, matchesJsonSchemaInClasspath("responseskill-schema.json"));
		System.out.println("Validated the Response Schema");
		System.out.println("Response Body is =>  " + responseBody);
		assertEquals(Integer.parseInt(expStatusCode),response.statusCode());
		System.out.println("Response Message =>  " + responseMessage);
		String skill_id=excelSheetReaderUtil.getDataFromExcel(scenario.getName(),"Skill_id");
		dbmanager.dbvalidation(responseBody, skill_id);

	}

	@When("User sends request with valid skill name but non existing skill id")
	public void user_sends_request_with_valid_skill_name_but_non_existing_skill_id() throws IOException {
		requestSpecificationPut();
	}
	
	

	@When("User sends request with inputs like Java and Python")
	public void user_sends_request_with_inputs_like_java_and_python() throws IOException {
		requestSpecificationPut();
			    
	}
	
	@When("To update skill name with invalid datatypes")
	public void to_update_skill_name_with_invalid_datatypes() throws IOException {
		requestSpecificationPut();
	}
		

	@When("User sends request with blank skill Id")
	public void user_sends_request_with_blank_skill_id() throws IOException {
		requestSpecificationPut();
	}

	@Then("User should not be able to update the Skill name")
	public void user_should_not_be_able_to_update_the_skill_name() throws IOException {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		System.out.println("Actual Response Status code=>  " + response.statusCode() + "  Expected Response Status code=>  " + expStatusCode);
		String responseBody = response.asPrettyString();
		System.out.println("Response Body is =>  " + responseBody);
		assertEquals(Integer.parseInt(expStatusCode),response.statusCode());
		System.out.println("Response Message =>  " + responseMessage);
	}
}



