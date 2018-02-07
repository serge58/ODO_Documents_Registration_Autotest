package common.httpclient.models;

import java.util.List;

public class Permissions {
	public Permissions(List<Number> checkIds, List<Number> uncheckIds) {
		super();
		this.checkIds = checkIds;
		this.uncheckIds = uncheckIds;
	}
	
	public Permissions() {}
	
	public List<Number> getCheckIds() {
		return checkIds;
	}
	
	public void setCheckIds(List<Number> checkIds) {
		this.checkIds = checkIds;
	}
	
	public List<Number> getUncheckIds() {
		return uncheckIds;
	}
	
	public void setUncheckIds(List<Number> uncheckIds) {
		this.uncheckIds = uncheckIds;
	}

	List<Number> checkIds;		// Установить разрешения
	List<Number> uncheckIds; 	// Сбросить разрешение
}