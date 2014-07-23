package cn.cnic.virostudio.rule;

public class ModifyData {
	public String pnewName = "";
	public String oprefix = "";
	public String osplitTag="";
	/**
	 * content 针对多对一的情况，这时候配置文件如下
	 * <property name="content" value="taxname.value" />
	 * 就是要明确得到的值是taxname这个key对应的value
	 */
	public String content="";
	public String otype="";

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

	public String getOsplitTag() {
		return osplitTag;
	}

	public void setOsplitTag(String osplitTag) {
		this.osplitTag = osplitTag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOtype() {
		return otype;
	}

	public void setOtype(String otype) {
		this.otype = otype;
	}
	
	
}