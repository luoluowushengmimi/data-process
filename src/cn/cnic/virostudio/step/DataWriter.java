package cn.cnic.virostudio.step;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.virostudio.triple.Triple;

import com.google.common.collect.Multimap;

public interface DataWriter {
	public void write(int fileId,String id,Multimap<String, String> map);
	
	public String getIdname();
	
	public String getFilePath() ;
	
	public void write(int fileId,ArrayList<Multimap<String, String>> map) throws Exception;
}
