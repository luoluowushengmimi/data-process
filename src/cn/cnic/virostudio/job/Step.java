package cn.cnic.virostudio.job;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import com.google.common.collect.Multimap;
import cn.cnic.virostudio.process.CompositeProcessor;
import cn.cnic.virostudio.step.DataReader;
import cn.cnic.virostudio.step.DataWriter;
import cn.cnic.virostudio.thread.SingleThread;

/**
 * 配置资源的类
 * 
 * @author Administrator
 * 
 */
public class Step {
	private DataReader dataReader;
	private CompositeProcessor processor;
	private DataWriter dataWriter;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	public DataReader getDataReader() {
		return dataReader;
	}

	public void setDataReader(DataReader dataReader) {
		this.dataReader = dataReader;
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

	public int doStep(int filenumber) {
		int count = 0;
		//loginfo.info("传入的filenumber----------------------- :"+filenumber);
		List<Multimap<String, String>> result = dataReader.getQueryResult();
		for (int i = 0; i < result.size(); i++) {
			count++;
			filenumber=this.getFileId(dataWriter.getFilePath(), filenumber);
			//loginfo.info("最终确定的filenumber----------------------- :"+filenumber);
			new SingleThread(filenumber, dataWriter.getIdname(), result.get(i),
					processor, dataWriter).run();
		}
		System.gc();
		return count;
	}

	public int getFileId(String filepath, int filenumber) {
		if (!filepath.endsWith("/"))
			filepath = filepath + "/";
		while(true){
			File file = new File(filepath + filenumber + ".nt");
			double length = (double) (file.length() / 1024.0 / 1024.0);
			if (length >=100.0) {
				filenumber++;
			}
			else{
				return filenumber;
			}
		}
	}
}
