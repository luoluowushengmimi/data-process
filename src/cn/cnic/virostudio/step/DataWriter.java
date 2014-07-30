package cn.cnic.virostudio.step;

import com.google.common.collect.Multimap;

public interface DataWriter {
	public void write(int fileId,String id,Multimap<String, String> map);
	
	public String getIdname();
	
	public String getFilePath() ;


}
