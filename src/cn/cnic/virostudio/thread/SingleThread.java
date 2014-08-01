package cn.cnic.virostudio.thread;

import org.apache.log4j.Logger;
import cn.cnic.virostudio.process.CompositeProcessor;
import cn.cnic.virostudio.step.DataWriter;

import com.google.common.collect.Multimap;

public class SingleThread {
	private int fileId;
	private String idname ;
	private Multimap<String, String> map;
	private CompositeProcessor processor;
	private DataWriter dataWriter;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	
	public SingleThread(int fileId,String idname, Multimap<String, String> map,
			CompositeProcessor processor, DataWriter dataWriter) {
		super();
		this.fileId=fileId;
		this.idname = idname;
		this.map = map;
		this.processor = processor;
		this.dataWriter = dataWriter;
	}
	public String getIdname() {
		return idname;
	}
	public void setIdname(String idname) {
		this.idname = idname;
	}
	
	public Multimap<String, String> getMap() {
		return map;
	}
	public void setMap(Multimap<String, String> map) {
		this.map = map;
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
	public void run() {
		Multimap<String, String> processmap=processor.process(map);
		loginfo.info("after process map: "+processmap );
		if(processmap==null) return;
		String id=processmap.get(idname).iterator().next();
		//loginfo.info("id: "+id );
		if(id==null) {
			logerr.error("id: "+id +"不存在！");
			throw new RuntimeException();
			}
		dataWriter.write(fileId,id, processmap);
	}
	

}
