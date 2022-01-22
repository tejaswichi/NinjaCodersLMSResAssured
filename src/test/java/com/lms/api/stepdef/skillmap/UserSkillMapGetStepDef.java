package com.lms.api.stepdef.skillmap;
import java.io.IOException;
import java.util.Properties;

import com.lms.api.utilities.ExcelReaderUtil;
import com.lms.api.utilities.PropertiesReaderUtil;
import org.testng.Assert;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserSkillMapGetStepDef {
	
	
	RequestSpecification RequestSpec;
	Response response;
	String path;
	String sheetGet;

	ExcelReaderUtil excelSheetReaderUtil;
	Scenario scenario;

	Properties properties;

	public UserSkillMapGetStepDef() {
		PropertiesReaderUtil propUtil = new PropertiesReaderUtil();
		properties = propUtil.loadProperties();
	}

	// Before annotation from io cucumber
	// Scenario class will give us information at the runtime like the scenario
	// name, getid() or isFailed()
	@Before
	public void initializeDataTable(Scenario scenario) throws Exception {
		this.scenario = scenario;
		sheetGet = properties.getProperty("sheetGet");
		excelSheetReaderUtil = new ExcelReaderUtil("src/test/resources/excel/data_UserSkillMap.xls");
		excelSheetReaderUtil.readSheet(sheetGet);

	}
	

@Given("User is on GETall Method")
public void user_is_on_getall_method() throws IOException{
	
	RestAssured.baseURI = properties.getProperty("base_uri");

	RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
			properties.getProperty("password"));
	// RequestSpec = RestAssured.given().auth().preemptive().basic("username","password");

	path = "/UserSkills";
	
   
}

@When("User sends request")
public void user_sends_request() {
	
    
	//response = RequestSpec.get(path);
	response = RequestSpec.request(Method.GET,path);
	
}

@Then("User receives status code with valid json schemaforall")
public void user_receives_status_code_with_valid_json_schemaforall() throws IOException {
	
	String responseBody = response.prettyPrint();
	String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
	System.out.println("Expected response code: " + expStatusCode);
	System.out.println("Response Status code is =>  " + response.statusCode());
	int statuscode = response.statusCode();
	Assert.assertEquals(Integer.parseInt(expStatusCode),statuscode);

	System.out.println("Response Body is =>  " + responseBody);
	assertThat(responseBody,matchesJsonSchemaInClasspath("userSkillMap_schemaAll.json"));
	System.out.println("Validated the schema");
   
}

@Given("User is on GET Methods")
public void user_is_on_get_methods() throws IOException{
	
	RestAssured.baseURI = properties.getProperty("base_uri");

	RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
			properties.getProperty("password"));
	// RequestSpec = RestAssured.given().auth().preemptive().basic("username","password");

	//path = "/UserSkills/";
	path = properties.getProperty("skillmap.endpoint.get");
	
   
}


@When("User sends request with Valid id")
public void user_sends_request_with_valid_id() throws IOException {
	
	requestSpecificationGET();
   
}
@Then("User receives status code with valid json schemas")
public void user_receives_status_code_with_valid_json_schemas() throws IOException {
	
	String responseBody = response.prettyPrint();
	String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
	System.out.println("Expected response code: " + expStatusCode);
	System.out.println("Response Status code is =>  " + response.statusCode());
	int statuscode = response.statusCode();
	Assert.assertEquals(Integer.parseInt(expStatusCode),statuscode);

	System.out.println("Response Body is =>  " + responseBody);
	assertThat(responseBody,matchesJsonSchemaInClasspath("userSkillMap_schema.json"));
	System.out.println("Validated the schema");
   
}
	
@When("User sends request with Invalid id")
public void user_sends_request_with_invalid_id() throws IOException {
	
	requestSpecificationGET();
}

@Then("User gets Response as Bad Request")
public void user_gets_response_as_bad_request() throws IOException {
	
	String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
	String expMessage = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "Message");
	System.out.println("Expected response code: " + expStatusCode + "Expected message is: " + expMessage);
	
    System.out.println("Response Status code is =>  " + response.statusCode());
	int statuscode = response.statusCode();
	Assert.assertEquals(Integer.parseInt(expStatusCode),statuscode);
	
    String responseBody = response.prettyPrint();
	System.out.println("Response Body is =>  " + responseBody);
	
	
    
}



@When("User sends request with Blank id")
public void user_sends_request_with_blank_id()throws IOException {
	requestSpecificationGET();
}

@When("User sends request  with AlphaNumeric id")
public void user_sends_request_with_alpha_numeric_id()throws IOException {
	
	requestSpecificationGET();
}

@When("User sends request with Decimal as id")
public void user_sends_request_with_decimal_as_id()throws IOException {
	
	requestSpecificationGET();
}
	

public void requestSpecificationGET() throws IOException {
	
String UserSkillsId = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "UserSkills");
	
	//response = RequestSpec.get(path);
    RequestSpec.log().all();
	response = RequestSpec.request(Method.GET,path+UserSkillsId);
}
	

@Given("User  sets GET request with a valid endpoint as url\\/UserSkillsMap")
public void user_sets_get_request_with_a_valid_endpoint_as_url_user_skills_map() {
	
	RestAssured.baseURI = properties.getProperty("base_uri");

	RequestSpec = RestAssured.given().auth().preemptive().basic(properties.getProperty("username"),
			properties.getProperty("password"));
	// RequestSpec = RestAssured.given().auth().preemptive().basic("username","password");

	//path = "/UserSkillsMap";
	path = properties.getProperty("/UserSkillsMap");
   
}

@Then("User receives status code with valid json schemaforallSkills")
public void user_receives_status_code_with_valid_json_schemaforall_skills() throws IOException {
   
	String responseBody = response.prettyPrint();
	String expStatusCode = excelSheetReaderUtil.getDataFromExcel(scenario.getName(), "StatusCode");
	System.out.println("Expected response code: " + expStatusCode);
	System.out.println("Response Status code is =>  " + response.statusCode());

	System.out.println("Response Body is =>  " + responseBody);
	assertThat(responseBody,matchesJsonSchemaInClasspath("userSkillMap_schemaGetAllSkills.json"));
	System.out.println("Validated the schema");
	
}

@When("User sends request with query param")
public void user_sends_request_with_query_param() throws IOException {
    
	requestSpecificationGET();
}




}
