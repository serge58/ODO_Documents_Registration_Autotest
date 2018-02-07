package common.httpclient;

public interface Services {
	public static final String EMPLOYEES = "employees";											// Получить информацию о работнике
	//filter=[{"property":"instate","value":true}]
	//limit=25
	//page=1
	//query=иванов п
	//sortDirection=ASC
	//sortProperty=full_name
	//start=0
	
	public static final String EMPLOYEE_NNN = "employees/nnn";									// Получить информацию о работнике, где NNN Id работника
	
	public static final String SECURITY_PROFILE = "security/profile";							// Получить информацию о профиле работника
	//empId=941
	//limit=25
	//page=1
	//start=0
	
	public static final String SECURITY_PROFILE_NNN = "security/profile/nnn";					// Получить информацию о профиле работника, где nnn Id работника
	
	public static final String SECURITY_PERMISSIONS = "security/permissions";					// Получить информацию о профиле подразделения
	//node=root
	//profileId=1627
	
	public static final String EMPLOYEE_DICTIONARY_MODULES = "employees/dictionary/modules";	// Получить список доступных модулей
	//limit=25
	//page=1
	//query	
	//start=0
	
	public static final String EMPLOYEE_RECEPTION = "employees/reception";						// Принять сотрудника на работу
	public static final String USER_DOCFLOW_DOCRECCARD = "user/docflow/docreccard";
	public static final String USER_DOCFLOW_DOCRECCARDS_SUMMARY_DETAILS = "user/docflow/docreccards/summary/details";
	public static final String EMPLOYEES_DEPARTMENT_TREE = "employees/departments/tree";		// Получить список подразделений
	public static final String EMPLOYEES_DEPARTMENT = "employees/departments";					// Получить информацию по подразделению
	
	public static final String USER_DICTIONARY_REGIONS = "user/dictionary/regions";				// Получить инфлормацию о регионах
	public static final String DECISIONS_IN_FIRST_INSTANCE = "user/docflow/personal-reception/decisions-in-first-instance"; // Суд первой инстанции
}
