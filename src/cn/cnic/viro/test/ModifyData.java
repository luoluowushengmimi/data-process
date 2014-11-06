package cn.cnic.viro.test;

public class ModifyData {
	private String poldName="";
	public String pnewName = "";
	private String subjectoldPrefix="";
	private String subjectnewPrefix="";
	public String oprefix = "";
	public String otrim="";

	public ModifyData() {
	}

	public String getPnewName() {
		return pnewName;
	}

	public void setPnewName(String pnewName) {
		this.pnewName = pnewName;
	}

	public String getOprefix() {
		return oprefix;
	}

	public void setOprefix(String oprefix) {
		this.oprefix = oprefix;
	}

	public String getOtrim() {
		return otrim;
	}

	public void setOtrim(String otrim) {
		this.otrim = otrim;
	}

	public String getPoldName() {
		return poldName;
	}

	public void setPoldName(String poldName) {
		this.poldName = poldName;
	}

	public String getSubjectoldPrefix() {
		return subjectoldPrefix;
	}

	public void setSubjectoldPrefix(String subjectoldPrefix) {
		this.subjectoldPrefix = subjectoldPrefix;
	}

	public String getSubjectnewPrefix() {
		return subjectnewPrefix;
	}

	public void setSubjectnewPrefix(String subjectnewPrefix) {
		this.subjectnewPrefix = subjectnewPrefix;
	}
	
	
}