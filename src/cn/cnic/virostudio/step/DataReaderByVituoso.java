package cn.cnic.virostudio.step;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.*;

public class DataReaderByVituoso implements DataReader {
	private String dataSource;
	private String dataBase;
	private String selectClause;
	private String whereClause;
	private int limit;
	private int offset;
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

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
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
	/**
	 * Executes a SPARQL query against a virtuoso url and prints results.
	 */
	public List<Multimap<String,String>> getQueryResult(){
		List <Multimap<String, String>> list = new ArrayList<Multimap<String, String>>();
		VirtGraph set = new VirtGraph (dataSource, userName, passWord);
		String query=selectClause+" from "+"<"+dataBase+">"+" "+whereClause+" limit "+limit+" offset "+offset;
		loginfo.info(query);
				//Query sparql = QueryFactory.create("SELECT * from <test> WHERE {  ?s ?p ?o  } limit 100");
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		loginfo.info("测试应该是读取成功");
		while (results.hasNext()) {
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
			list.add(resultmap);
		}
		vqe.close();
		set.close();
		return list;
	}
}
