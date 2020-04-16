package demo;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import com.xx.POJO.DeletePlace;
public class DemoBasic {

	public static void main(String[] args) {

				//given - all input details 
				//when - Submit the API -resource,http method
				//Then - validate the response

		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200)
		.body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
		System.out.println(response);
		
		JsonPath js = new JsonPath(response);
		String Placeid = js.getString("place_id");
		
		System.out.println(Placeid);
		
		// Update with new address --> get place to check if new address is present in response -->
		//Update place
		
		String newAddress = "70 summer walk, aftrica";
		given().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+Placeid+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}").
		when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//get place
	String getPLaceResponse =given().queryParam("key", "qaclick123")
		.queryParam("place_id", Placeid)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
	
	JsonPath js1 = new JsonPath(getPLaceResponse);
	String ActualAddress =  js1.getString("address");
	
	Assert.assertEquals(ActualAddress, newAddress);
	System.out.println("ActualAddress : "+ActualAddress+" and newAddress : "+newAddress);
	
	//Delete that place_id with POJO
	DeletePlace d1=new DeletePlace();
	d1.setPlaceId(Placeid);

	given().queryParam("key", "qaclick123")
	.header("Content-Type","application/json")
	.body(d1)
	.when().post("maps/api/place/delete/json")
	.then().log().all().assertThat().statusCode(200);
	
	 // try to get place_id which is deleted
		given().queryParam("key", "qaclick123").queryParam("place_id", Placeid)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(404);
}
}
