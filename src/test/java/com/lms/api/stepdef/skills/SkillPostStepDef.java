package com.lms.api.stepdef.skills;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.lms.api.dbmanager.Dbmanager;
import com.lms.api.utilities.ExcelReaderUtil;
import com.lms.api.utilities.PropertiesReaderUtil;
import org.json.JSONObject;

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

public class SkillPostStepDef {
	
	RequestSpecification requestSpec;
	Response response;
	String path;
	Scenario scenario;
	ExcelReaderUtil excelSheetReaderUtil;
	String sheetPost;
	Properties properties;
	Dbmanager dbmanager;

	public SkillPostStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
		dbmanager = new Dbmanager();
	}
	
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
	this.scenario=scenario;
	sheetPost=properties.getProperty("sheetPost");
	excelSheetReaderUtil =new ExcelReaderUtil(properties.getProperty("skills.excel.path"));
	excelSheetReaderUtil.readSheet(sheetPost);
	
	}
	
	public void requestSpecificationPost() throws IOException {
		requestSpec.header("Content-Type", "application/json");
		String bodyExcel=excelSheetReaderUtil.getDataFromExcel(scenario.getName(),"Body");
		requestSpec.body(bodyExcel).log().body();
		try {
		assertThat(bodyExcel, matchesJsonSchemaInClasspath("skill-schema.json"));
		System.out.println("Validated the schema");
		}
		catch(AssertionError ex) {
		System.out.print("Json validation failed.. ");	
		}		
		response = requestSpec.when().post(path);
	}
	
	@Given("User is on POST method with endpoint url Skills")
	public void user_is_on_post_method_with_endpoint_url_skills() throws IOException {
		RestAssured.baseURI=properties.getProperty("base_uri");
		requestSpec=RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
				    properties.getProperty("password"));
		path=properties.getProperty("skills.endpoint.Post");
		System.out.println("Path for Post is "+ path);
	}

	@When("User sends request with inputs  skill name")
	public void user_sends_request_with_inputs_skill_name() throws IOException {
		requestSpecificationPost();
				
	}

	@Then("User is able to create a new Skill id")
	public void user_is_able_to_create_a_new_skill_id() throws IOException, SQLException {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		String responseBody = response.asPrettyString();
		System.out.println("Actual Response Status code=>  " + response.statusCode() + "  Expected Response Status code=>  " + expStatusCode);
		System.out.println("Response Body is =>  " + responseBody);
		assertThat(responseBody, matchesJsonSchemaInClasspath("responseskill-schema.json"));
		System.out.println("Validated the Response Schema");
		assertEquals(Integer.parseInt(expStatusCode),response.statusCode());
		System.out.println("Response Message =>  " + responseMessage);
		
		JSONObject obj = new JSONObject(responseBody);
		String skill_id= obj.get("skill_id").toString();
		dbmanager.dbvalidation(responseBody, skill_id);
	}

	@When("User sends request with blank inputs")
	public void user_sends_request_with_blank_inputs() throws IOException {
		requestSpecificationPost();
	}

	@When("User sends request with boolean as skill name")
	public void user_sends_request_with_boolean_as_skill_name() throws IOException {
		requestSpecificationPost();
	}

	@When("User sends request with integer as  skill name")
	public void user_sends_request_with_integer_as_skill_name() throws IOException {
		requestSpecificationPost();
	}

	@When("User sends request with Alphanumeric as skill name")
	public void user_sends_request_with_alphanumeric_as_skill_name() throws IOException {
		requestSpecificationPost();
	}
	
	@Then("User cannot create a new Skill id")
	public void user_cannot_create_a_new_skill_id() throws IOException {
		String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
		String responseMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
		String responseBody = response.asPrettyString();
		System.out.println("Actual Response Status code=>  " + response.statusCode() + "  Expected Response Status code=>  " + expStatusCode);
		System.out.println("Response Body is =>  " + responseBody);
		assertEquals(Integer.parseInt(expStatusCode),response.statusCode());
		System.out.println("Response Message =>  " + responseMessage);
	}
	

}
