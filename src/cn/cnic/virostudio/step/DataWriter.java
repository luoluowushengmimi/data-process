package cn.cnic.virostudio.step;

import com.google.common.collect.Multimap;

public interface DataWriter {
	public void write(String id,Multimap<String, String> map);
	
	public String getIdname();

}
