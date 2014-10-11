package cn.cnic.virostudio.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
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

public class PreStep {

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



	public  void doPreStep() throws Exception{
		ArrayList<Multimap<String, String>> maps=this.getPrequery();
		for(Multimap<String, String> map:maps){
			//loginfo.info("主语是： "+map.get("s"));
			 String s=map.get("s").toString().replaceAll("\\[", "<").replaceAll("\\]", ">");
			//String query=dataReader.getSelectClause()+" from "+"<"+dataReader.getDataBase()+">"+" "+ dataReader.getWhereClause().replace("?s", s);;
			 //loginfo.info("doprestep:"+query);
			 FileUtils.write(new File(dataWriter.getFilePath()), s+"\r\n", true);
		}
		
	}
	
	
	/**
	 * 构造查询条件，得到数据的主语
	 * @return
	 */
	public ArrayList<Multimap<String, String>>  getPrequery(){
		long count = 0;
		ArrayList<Multimap<String, String>> maps=new ArrayList<Multimap<String, String>>();
		VirtGraph set = new VirtGraph (dataReader.getDataSource(), dataReader.getUserName(), dataReader.getPassWord());
		String query=dataReader.getSelectClause()+" from "+"<"+dataReader.getDataBase()+">"+" "+dataReader.getWhereClause();
		loginfo.info("prequery:"+query);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		//loginfo.info("测试应该是读取成功");
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
				//loginfo.info("premap "+" name: " +name+" nodevalue: "+nodevalue);
				resultmap.put(name, nodevalue);
			}
			
			maps.add(resultmap);
		}
		vqe.close();
		set.close();
		return maps;
	}
}
