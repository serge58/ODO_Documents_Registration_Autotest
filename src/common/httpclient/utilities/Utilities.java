package common.httpclient.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utilities {
	public static int GetDepartmentID(String departmentList, String departmentName) {
		int departmentID = 0;
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(departmentList);
			JSONArray departList = (JSONArray) jsonObject.get("children");
			
			for (Object item: departList) {
				JSONObject json = (JSONObject) item;
				String name = (String) json.get("text");
				if (name.contentEquals(departmentName)) {
					departmentID = ((Long) json.get("id")).intValue();
					break;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return departmentID;
	}
	
	public static int GetEmployeeID(String employeeJSONString, String employeeName) {
		int employeeID = 0;
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(employeeJSONString);
			JSONArray employeeList = (JSONArray) jsonObject.get("records");
			
			for (Object employee: employeeList) {
				JSONObject json = (JSONObject) employee;
				String name = (String) json.get("fullName");
				if (name.contentEquals(employeeName)) {
					employeeID = ((Long) json.get("id")).intValue();
					break;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return employeeID;
	}
	
	public static String GetDeptShortName(String departmentInfo) {
		String shortDeptName = "";
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(departmentInfo);
			JSONArray departList = (JSONArray) jsonObject.get("records");
			JSONObject deptInfo = (JSONObject) departList.get(0);
			shortDeptName = (String) deptInfo.get("shortDeptName");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return shortDeptName;
	}
	
	public static Object GetProfileID(String jsonString) {
		Object result = "";
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
			JSONArray records = (JSONArray) jsonObject.get("records");
			JSONObject record = (JSONObject) records.get(0);
			JSONArray profiles = (JSONArray) record.get("profiles");
			JSONObject profile = (JSONObject) profiles.get(0);
			
			result = (Object) profile.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String GetErrorMessage(String result) {
		String errorMessage = "";
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
			errorMessage = (String) jsonObject.get("message");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return errorMessage;
	}
	
	public static Object GetFieldValue(String jsonString, String fieldName) {
		Object result = 0;
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
			JSONArray records = (JSONArray) jsonObject.get("records");
			
			if (!records.isEmpty()) {
				JSONObject record = (JSONObject) records.get(0);
				result = (Object) record.get(fieldName);
			}  
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
