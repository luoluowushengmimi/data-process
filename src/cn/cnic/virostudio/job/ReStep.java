package cn.cnic.virostudio.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class ReStep {

	//private DataReader predatareader;
		private PreStep prestep;
		private DataReader dataReader;
		private CompositeProcessor processor;
		private DataWriter dataWriter;
		private static Logger logerr = Logger.getLogger("errLog");
		private static Logger loginfo = Logger.getLogger("infoLog");
		public PreStep getPrestep() {
			return prestep;
		}
		public void setPrestep(PreStep prestep) {
			this.prestep = prestep;
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
			if(prestep==null){
				return this.stepCaseOther(filenumber);
			}
			else{
				return this.stepCase(filenumber);
			}
		}
		/**
		 * stepcase 要做的事情是先读出s,然后再通过s构建新的查询语句，进行数据的处理工作。所以会有一个prestep的过程
		 * @param filenumber
		 * @return
		 * @throws Exception
		 */
		public  long stepCase(int filenumber) throws Exception{
			long count = 0;
			VirtGraph set = new VirtGraph (dataReader.getDataSource(), dataReader.getUserName(), dataReader.getPassWord());
			List<String> sublists=FileUtils.readLines(new File(prestep.getDataWriter().getFilePath()));
			List<String> transferlist = new ArrayList<String>();
			for(String s:sublists){
				  String query=dataReader.getSelectClause()+" from "+"<"+dataReader.getDataBase()+">"+" "+dataReader.getWhereClause().replace("?s", s);
				  //transferlist.add(query);
				  FileUtils.write(new File(dataWriter.getFilePath()+"/"+"querylist.txt"), query+"\r\n", true);
				  loginfo.info("dostep query:"+query);
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
					loginfo.info("已经处理完成查询语句为： "+query+" 的所有数据");
			}
			set.close();
			
//			for(String query:transferlist){
//				loginfo.info("dostep:"+query);
//				 Query sparql = QueryFactory.create(query);
//					VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
//					ResultSet results = vqe.execSelect();
//					loginfo.info("测试应该是读取成功");
//					while (results.hasNext()) {
//						loginfo.info("程序跑到第"+count+"个");
//						count++;
//						Multimap<String, String> resultmap = ArrayListMultimap.create();
//						QuerySolution result = results.nextSolution();
//						Iterator<String> iter=result.varNames();
//						while(iter.hasNext()){
//							String name=iter.next();
//							RDFNode node=result.get(name);
//							String nodevalue=node.toString();
//							if(nodevalue.endsWith(",")||nodevalue.endsWith(" ")||nodevalue.endsWith("|")){
//								nodevalue=nodevalue.substring(0, nodevalue.length()-1);
//							}
//							resultmap.put(name, nodevalue);
//						}
//						filenumber=this.getFileId(dataWriter.getFilePath(), filenumber);
//						System.out.println(resultmap);
//						new SingleThread(filenumber, dataWriter.getIdname(),resultmap,
//								processor, dataWriter).run();
//					}
//					vqe.close();
//					//loginfo.info("已经处理完成查询语句为： "+query+" 的所有数据");
//			}
//			
//					set.close();
//			
			return count;
			
		}
		/**
		 * 这段代码说的是step的另外一种情况，就是没有prestep,那么就是通过构建查询条件进行查询，只需要进行一次查询即可。
		 * @param filenumber
		 * @return
		 * @throws Exception
		 */
		public  long stepCaseOther(int filenumber) throws Exception{
			long count = 0;
			VirtGraph set = new VirtGraph (dataReader.getDataSource(), dataReader.getUserName(), dataReader.getPassWord());
			String query=null;
		if(dataReader.getLimit()==0&&dataReader.getOffset()==0){
			query=dataReader.getSelectClause()+" from "+"<"+dataReader.getDataBase()+">"+" "+dataReader.getWhereClause();
		}
		else if(dataReader.getLimit()!=0&&dataReader.getOffset()!=0) {
			query=dataReader.getSelectClause()+" from "+"<"+dataReader.getDataBase()+">"+" "+dataReader.getWhereClause()+" limit "+dataReader.getLimit()+" offset "+dataReader.getOffset();
		}
			loginfo.info(query);
			if(query==null){
				throw new Exception("查询语句有错误，请重新配置，主要是limit和offset,这个语句的规则是limit 和offset要么都设置，要么都不设置");
			}
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
			set.close();
			return count;
		}
}
