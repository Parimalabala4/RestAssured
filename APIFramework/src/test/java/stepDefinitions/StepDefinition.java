package stepDefinitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StepDefinition extends Utils{
	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;

	TestDataBuild data = new TestDataBuild();
	@Given("Add Place Payload with {string} {string} {string}")
	public void add_Place_Payload_with(String name, String language, String address) throws IOException {
	   res=given().spec(requestSpecification())
				.body(data.addPlacePayload(name,language,address));		
	}

	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String HttpMethod) {
		
		APIResources resourceAPI= APIResources.valueOf(resource);
		System.out.println(resourceAPI.getResource());		
		
		resspec =new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		if(HttpMethod.equalsIgnoreCase("POST"))
			response =res.when().post(resourceAPI.getResource());
		else if(HttpMethod.equalsIgnoreCase("GET"))
			response =res.when().get(resourceAPI.getResource());
				
	}

	@Then("the API call is success with status code {int}")
	public void the_API_call_is_success_with_status_code(Integer int1) {
	  
	   assertEquals( response.getStatusCode(), 200);
	}
	@And("{string} in response body is {string}")
	public void in_response_body_is(String keyValue, String expectedvalue) {	     	   
		   assertEquals(getJsonPath(response, keyValue), expectedvalue);
	}
	
	@Then("verify place_id created that maps to {string} using {string}")
	public void verify_place_id_created_that_maps_to_using(String expectedName , String resource) throws IOException {
		String place_id= getJsonPath(response,"place_id");
		
		 res=given().spec(requestSpecification()).queryParam("place_id", place_id);
		 user_calls_with_http_request( resource, "GET");
		 String Actualname = getJsonPath(response,"name");
		 assertEquals(Actualname, expectedName);
	}
}
