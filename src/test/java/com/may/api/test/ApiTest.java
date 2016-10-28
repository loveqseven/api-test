package com.may.api.test;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import static com.jayway.restassured.config.EncoderConfig.encoderConfig;
import static com.jayway.restassured.RestAssured.config;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static com.jayway.restassured.http.ContentType.JSON;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiTest {
 
    private String access_Token = "240ee4366a786377484f9868c09431b6b257c4fa";
    private String APIUrl = "https://developer.github.com/v3/gists";
	
	static private int list_id   = 0 ; 

	
	@Test
	public void test2GetLists(){
		   given().
		           headers("X-access-token",access_Token).
	       when().
                   get(APIUrl + "/gists").
           then().
                  statusCode(200);
	}

    @Test
    public void test3DeleteAList(){
        given().
                headers("X-access-token",access_Token).
                when().
                delete(APIUrl + "/gists/:"+list_id).
                then().
                statusCode(200);
    }


	@Test
	public void test1CreateList(){
		   SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       Date now = new Date();
	       String strDate = sdfDate.format(now);
		   String listName = "ECLIPSE-list-" + strDate ; 
	       
		   Map<String, Object>  jsonAsMap = new HashMap<>();
		   jsonAsMap.put("title", listName );
	       given().headers("X-access-token",access_Token).
	               contentType(JSON).
	               body(jsonAsMap).
	       when().
                   post(APIUrl + "/gists").
           then().
                   statusCode(201);
	       
	       
	        final String body = with().headers("X-Access-Token",access_Token).
                                      body("{\n" +
                                              "            \"description\": \"the description for this gist\",\n" +
                                              "                \"public\": true,\n" +
                                              "                \"files\": {\n" +
                                              "            \"file1.txt\": {\n" +
                                              "                \"content\": \"String file contents\"\n" +
                                              "            }\n" +
                                              "        }\n" +
                                              "        }   ").

                                      contentType(JSON).
                                when().
                                      get(APIUrl + "/gists").asString();

             System.out.println("create-a-gist "+ body);
             
             final JsonPath jsonPath = new JsonPath(body);
             final List <Integer> lists = jsonPath.getList("id");
             list_id =  lists.get( lists.size() - 1);
             System.out.println ("Newly Created List Id: " + list_id );
    }
}



