package cn.cnic.virostudio.count;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

import cn.cnic.virostudio.job.JobController;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class CountAll {
	private String dataSource;
	private String dataBase;
	private String selectClause;
	private String whereClause;
	private String userName;
	private String passWord;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getSelectClause() {
		return selectClause;
	}

	public void setSelectClause(String selectClause) {
		this.selectClause = selectClause;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String  getQueryResult() {
		String count="";
		// VirtGraph set = new VirtGraph ("jdbc:virtuoso://192.168.94.50:1111",
		// "dba", "dba");
		VirtGraph set = new VirtGraph(dataSource, userName, passWord);
		String query = selectClause + " from " + "<" + dataBase + ">" + " "
				+ whereClause;
		loginfo.info(query);
		// Query sparql =
		// QueryFactory.create("SELECT * from <test> WHERE {  ?s ?p ?o  } limit 100");
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(
				sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			Iterator<String> iter=result.varNames();
			while(iter.hasNext()){
				String name=iter.next();
				RDFNode node=result.get(name);
				String nodevalue=node.toString();
				count=nodevalue;
			}
		}
		return count;
	}
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "count-job.xml"});
		CountAll count = context.getBean("datareaderenzyme", CountAll.class);
		loginfo.info(count.getQueryResult());
		context.close();
	}
}
