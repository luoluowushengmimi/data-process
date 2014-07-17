package cn.cnic.virostudio.thread;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import cn.cnic.virostudio.process.CompositeProcessor;
import cn.cnic.virostudio.step.DataWriter;
import com.google.common.collect.Multimap;


public class ItemThread implements Runnable {
	private String idname ;
	private Multimap<String, String> map;
	private CompositeProcessor processor;
	private DataWriter dataWriter;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	
	public ItemThread(String idname, Multimap<String, String> map,
			CompositeProcessor processor, DataWriter dataWriter) {
		super();
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
	public CompositeProcessor getProcessor() {
		return processor;
	}
	public void setProcessor(CompositeProcessor processor) {
		this.processor = processor;
	}
	@Override
	public void run() {
		Multimap<String, String> processmap=processor.process(map);
		loginfo.info("after process map: "+processmap );
		if(processmap==null) return;
		String id=processmap.get(idname).iterator().next();
		loginfo.info("id: "+id );
		if(id==null) {
			logerr.error("id: "+id +"不存在！");
			throw new RuntimeException();
			}
		dataWriter.write(id, processmap);
	}
	
}
