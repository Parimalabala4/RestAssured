Feature: Validating Place API's

Scenario Outline: Verify if Place is being successfully added using AddPlaceAPI
	Given Add Place Payload with "<name>" "<language>" "<address>"
	When user calls "AddPlaceAPI" with "POST" http request
	Then the API call is success with status code 200
	And "status" in response body is "OK"
	And "scope" in response body is "APP"
	And verify place_id created that maps to "<name>" using "getPlaceAPI"
	
	
Examples:
	| name 			|language 		| address					|
	| pari1 house	| French-IN		| 29, side layout, cohen 09	|
#	| pari2 house	| English		| 30, side layout, cohen 10	|
	