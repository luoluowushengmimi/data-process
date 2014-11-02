package cn.cnic.viro.test;

public class MatchRule {
	public String pname = "";
	public String ofilter = "";

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getOfilter() {
		return ofilter;
	}

	public void setOfilter(String ofilter) {
		this.ofilter = ofilter;
	}


	public MatchRule() {
	}
	public MatchRule(String pname, String ofilter, String otype) {
		this.pname = pname;
		this.ofilter = ofilter;
	}
}