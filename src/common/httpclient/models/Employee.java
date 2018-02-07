package common.httpclient.models;

public class Employee {
	public boolean isReception() {
		return reception;
	}
	public void setReception(boolean reception) {
		this.reception = reception;
	}
	public String getTabNumber() {
		return tabNumber;
	}
	public void setTabNumber(String tabNumber) {
		this.tabNumber = tabNumber;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMidName() {
		return midName;
	}
	public void setMidName(String midName) {
		this.midName = midName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLastId() {
		return lastId;
	}
	public void setLastId(String lastId) {
		this.lastId = lastId;
	}
	public String getHiddenLogin() {
		return hiddenLogin;
	}
	public void setHiddenLogin(String hiddenLogin) {
		this.hiddenLogin = hiddenLogin;
	}
	public boolean isDontCreateAccount() {
		return dontCreateAccount;
	}
	public void setDontCreateAccount(boolean dontCreateAccount) {
		this.dontCreateAccount = dontCreateAccount;
	}
	public Number getDeptId() {
		return deptId;
	}
	public void setDeptId(Number deptId) {
		this.deptId = deptId;
	}
	
	public Employee() {
		
	};
	
	public Number getActive() {
		return active;
	}
	public void setActive(Number active) {
		this.active = active;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasword() {
		return pasword;
	}
	public void setPasword(String pasword) {
		this.pasword = pasword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Number getDurationType() {
		return durationType;
	}
	public void setDurationType(Number durationType) {
		this.durationType = durationType;
	}

	public Number getPositionId() {
		return positionId;
	}
	public void setPositionId(Number positionId) {
		this.positionId = positionId;
	}

	public Number getTypeId() {
		return typeId;
	}
	public void setTypeId(Number typeId) {
		this.typeId = typeId;
	}

	public Employee(boolean reception, String tabNumber, String lastName, String firstName, String midName,
			String fullName, String email, String phone, String comment, String lastId, String hiddenLogin,
			boolean dontCreateAccount, Number deptId) {
		super();
		this.reception = reception;
		this.tabNumber = tabNumber;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midName = midName;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.comment = comment;
		this.lastId = lastId;
		this.hiddenLogin = hiddenLogin;
		this.dontCreateAccount = dontCreateAccount;
		this.deptId = deptId;
	}

	private boolean reception = true; 			// Приём или увольнение
	private String tabNumber = "";				// Табельный номер
	private String lastName;					// Фамилия
	private String firstName;					// Имя
	private String midName = "";				// Отчество
	private String fullName;					// Отображаемое имя
	private String email = "";					// email
	private String phone = "";					// Номер телефона
	private String comment = "";				// Примечание
	private String lastId = "";					// 
	private String hiddenLogin = "";			// 
	private boolean dontCreateAccount = true;	// Не создавать учётную запись для данного работника
	private Number deptId;						// ID подразделения
	
	private Number active = 1;					// Учётная запись
	private String login;
	private String pasword;
	private String confirmPassword;
	
	private Number durationType;				// Назначение
	private Number positionId;
	private Number typeId;
}