package cn.cnic.virostudio.thread;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.cnic.virostudio.process.CompositeProcessor;
import cn.cnic.virostudio.step.DataWriter;
import cn.cnic.virostudio.step.DataWriterToFile2;
import cn.cnic.virostudio.triple.ConstructTriples;
import cn.cnic.virostudio.triple.Triple;

import com.google.common.collect.Multimap;

public class SingleThread2 {

	private int fileId;
	private ArrayList<Multimap<String, String>>listmap;
	private CompositeProcessor processor;
	private DataWriter dataWriter;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	

	public SingleThread2(int fileId,
			ArrayList<Multimap<String, String>> listmap,
			CompositeProcessor processor, DataWriter dataWriter) {
		super();
		this.fileId = fileId;
		this.listmap = listmap;
		this.processor = processor;
		this.dataWriter = dataWriter;
	}
	
	public int getFileId() {
		return fileId;
	}


	public void setFileId(int fileId) {
		this.fileId = fileId;
	}


	public ArrayList<Multimap<String, String>> getListmap() {
		return listmap;
	}


	public void setListmap(ArrayList<Multimap<String, String>> listmap) {
		this.listmap = listmap;
	}


	public CompositeProcessor getProcessor() {
		return processor;
	}
	public void setProcessor(CompositeProcessor processor) {
		this.processor = processor;
	}

	public DataWriter getDataWriter() {
		return dataWriter;
	}

	public void setDataWriter(DataWriter dataWriter) {
		this.dataWriter = dataWriter;
	}


	public void run() throws Exception {
		ArrayList<Multimap<String, String>> listmap=this.AfterProcessMaps();
		dataWriter.write(fileId, listmap);
	}
	
	
	public ArrayList<Multimap<String, String>> AfterProcessMaps(){
		int counts=0;
		ArrayList<Multimap<String, String>> maps=new ArrayList<Multimap<String, String>>();
		for(Multimap<String, String> map:listmap){
			counts++;
			Multimap<String, String> processmap=processor.process(map);
			loginfo.info("处理后的map：  "+processmap);
			maps.add(processmap);
		}
		return maps;
	}
	
	
}
