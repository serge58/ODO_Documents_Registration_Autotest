package common.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpClient {
	static CloseableHttpClient httpClient;
	static BasicCookieStore cookieStore;
	static String baseURL;
	static String username;
	static String password;
	static boolean isLogin;
	
	String reasonPhrase;
	int statusCode;
	
	public HttpClient(String _baseURL, String _username, String _password) {
		username = _username;
		password = _password;
		baseURL = _baseURL;
	}

	public boolean Login() {
		cookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        
        try {
        	// Логинимся
        	HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI(baseURL + "j_spring_security_check"))
                    .addParameter("username", username)
                    .addParameter("password", password).addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();
        	
            CloseableHttpResponse response = httpClient.execute(login);
            
            reasonPhrase = response.getStatusLine().getReasonPhrase();
            statusCode = response.getStatusLine().getStatusCode();
            
            try {
                HttpEntity entity = response.getEntity();
                StatusLine statusLine = response.getStatusLine();
                
                isLogin = statusLine.getStatusCode() == 200 ? true : false;
                
                System.out.println("Login result: " + statusLine);
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
            
        } catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		} finally {
            //httpClient.close();
        }

        return isLogin;
	}
	
    public String ExecuteGetRequest(String service) throws IOException, URISyntaxException {
    	Login();
    	
		StringBuffer result = new StringBuffer();
    	
    	HttpGet httpget = new HttpGet(baseURL + service);
        
    	CloseableHttpResponse response = httpClient.execute(httpget);
        
        reasonPhrase = response.getStatusLine().getReasonPhrase();
        statusCode = response.getStatusLine().getStatusCode();
        
        try {
        	HttpEntity entity = response.getEntity();
            
            System.out.println("GET request result: " + response.getStatusLine());

			BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			EntityUtils.consume(entity);
         } finally {
            response.close();
            httpClient.close();
        }
    	
    	return result.toString();
    }
    
    public String ExecutePostRequest(String service, Object jsonObject) throws IOException, IOException, URISyntaxException {
    	//if (!isLogin) Login();
    	Login();
    	
    	StringBuffer result = new StringBuffer();
    	Gson gson = new Gson();
    	HttpPost post = new HttpPost(baseURL + service);
    	
    	StringEntity postingString = new StringEntity(gson.toJson(jsonObject), "UTF-8");
    	post.setEntity(postingString);
    	post.setHeader("Content-type", "application/json");
    	post.setHeader("Accept-Language", "en-US,en;q=0.8,ru;q=0.6");
    	post.setHeader("Accept", "*/*");
    	//post.setHeader("X-Requested-With", "XMLHttpRequest");
    	
    	CloseableHttpResponse response = httpClient.execute(post);
        
        reasonPhrase = response.getStatusLine().getReasonPhrase();
        statusCode = response.getStatusLine().getStatusCode();
        
    	try {
            HttpEntity entity = response.getEntity();
            System.out.println("POST request result: " + response.getStatusLine());
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
        } finally {
            response.close();
            httpClient.close();
        }
    	
    	return result.toString();
    }
    
    public String ExecutePutRequest(String service, Object jsonObject) throws IOException, IOException, URISyntaxException {
    	//if (!isLogin) Login();
    	Login();
    	
    	StringBuffer result = new StringBuffer();
    	Gson gson = new Gson();
    	HttpPut put = new HttpPut(baseURL + service);
    	
    	StringEntity postingString = new StringEntity(gson.toJson(jsonObject), "UTF-8");
    	put.setEntity(postingString);
    	put.setHeader("Content-type", "application/json");
    	
    	CloseableHttpResponse response = httpClient.execute(put);
        
        reasonPhrase = response.getStatusLine().getReasonPhrase();
        statusCode = response.getStatusLine().getStatusCode();
        
    	try {
            HttpEntity entity = response.getEntity();
            System.out.println("PUT request result: " + response.getStatusLine());
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
        } finally {
            response.close();
            httpClient.close();
        }
    	
    	return result.toString();
    }
    
    public String GetReasonPhrase() {
    	return reasonPhrase;
    }
    
	public int GetStatusCode() {
		return statusCode;
	}
    
	public String PrettyPrintJson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		String prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(new ObjectMapper().readValue(jsonString, Object.class));
    	System.out.println(prettyJson);
    	return prettyJson;
    }
}
