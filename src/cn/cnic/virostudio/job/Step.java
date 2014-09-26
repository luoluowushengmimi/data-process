package cn.cnic.virostudio.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

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
	private DataReader predatareader;
	private DataReader dataReader;
	private CompositeProcessor processor;
	private DataWriter dataWriter;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	
	public DataReader getPredatareader() {
		return predatareader;
	}

	public void setPredatareader(DataReader predatareader) {
		this.predatareader = predatareader;
	}

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


	public int getFileId(String filepath, int filenumber) {
		if (!filepath.endsWith("/"))
			filepath = filepath + "/";
		while(true){
			File file = new File(filepath + filenumber + ".nt");
			double length = (double) (file.length() / 1024.0 / 1024.0);
			if (length >=300.0) {
				filenumber++;
			}
			else{
				return filenumber;
			}
		}
	}
	
	public  long doStep(int filenumber) throws Exception{
		long count = 0;
		VirtGraph set = new VirtGraph (dataReader.getDataSource(), dataReader.getUserName(), dataReader.getPassWord());
		String query=null;
		ArrayList<Multimap<String, String>> maps=this.getPrequery();
		for(Multimap<String, String> map:maps){
			 System.out.println(map.get("s"));
			 String s=map.get("s").toString().replaceAll("\\[", "<").replaceAll("\\]", ">");
			 query=dataReader.getSelectClause()+" from "+"<"+dataReader.getDataBase()+">"+" "+ dataReader.getWhereClause().replace("?s", s);;
			 loginfo.info("dostep:"+query);
			 Query sparql = QueryFactory.create(query);
				VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
				ResultSet results = vqe.execSelect();
				loginfo.info("测试应该是读取成功");
				while (results.hasNext()) {
					loginfo.info("程序跑到第"+count+"个");
					count++;
					Multimap<String, String> resultmap = ArrayListMultimap.create();
					QuerySolution result = results.nextSolution();
					Iterator<String> iter=result.varNames();
					while(iter.hasNext()){
						String name=iter.next();
						RDFNode node=result.get(name);
						String nodevalue=node.toString();
						if(nodevalue.endsWith(",")||nodevalue.endsWith(" ")||nodevalue.endsWith("|")){
							nodevalue=nodevalue.substring(0, nodevalue.length()-1);
						}
						resultmap.put(name, nodevalue);
					}
					filenumber=this.getFileId(dataWriter.getFilePath(), filenumber);
					System.out.println(resultmap);
					new SingleThread(filenumber, dataWriter.getIdname(),resultmap,
							processor, dataWriter).run();
				}
				vqe.close();
		}
				set.close();
		
		return count;
	}
	
	public ArrayList<Multimap<String, String>>  getPrequery(){
		long count = 0;
		ArrayList<Multimap<String, String>> maps=new ArrayList<Multimap<String, String>>();
		VirtGraph set = new VirtGraph (predatareader.getDataSource(), predatareader.getUserName(), predatareader.getPassWord());
		String query=predatareader.getSelectClause()+" from "+"<"+predatareader.getDataBase()+">"+" "+predatareader.getWhereClause();
		
		loginfo.info("prequery:"+query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		loginfo.info("测试应该是读取成功");
		while (results.hasNext()) {
			loginfo.info("prequery 程序跑到第"+count+"个");
			count++;
			Multimap<String, String> resultmap = ArrayListMultimap.create();
			QuerySolution result = results.nextSolution();
			Iterator<String> iter=result.varNames();
			while(iter.hasNext()){
				String name=iter.next();
				RDFNode node=result.get(name);
				String nodevalue=node.toString();
				if(nodevalue.endsWith(",")||nodevalue.endsWith(" ")||nodevalue.endsWith("|")){
					nodevalue=nodevalue.substring(0, nodevalue.length()-1);
				}
				loginfo.info("premap "+" name: " +name+" nodevalue: "+nodevalue);
				resultmap.put(name, nodevalue);
			}
			
			maps.add(resultmap);
		}
		vqe.close();
		set.close();
		return maps;
	}
	
}
