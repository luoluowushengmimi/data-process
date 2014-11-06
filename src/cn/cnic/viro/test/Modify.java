package cn.cnic.viro.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import cn.cnic.virostudio.job.Step;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class Modify {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	private String dataSource;
	private String dataBase;
	private String selectClause;
	private String whereClause;
	private String userName;
	private String passWord;
	private int limit;
	private int offset;
	private ArrayList<Mode> modes;
	
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

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


	public String getDataBase() {
		return dataBase;
	}


	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
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

	public ArrayList<Mode> getModes() {
		return modes;
	}

	public void setModes(ArrayList<Mode> modes) {
		this.modes = modes;
	}

	public void doListener() {
		int number=0;
		while(true){
			String query=selectClause+" from <"+dataBase+"> "+whereClause+" limit "+limit+" offset "+offset;
			loginfo.info("query "+query);
			int count=this.doAction(query);
			number=number+count;
			loginfo.info("all number: "+number);
		if(count<limit){
			break;
		}
		else{
			offset=offset+limit;
		}
		}
	}


	public int doAction(String query){
		List<HashMap<String,String>> lists=this.doSelect(query);
		Results result=this.constructDeleteInsertResults(lists, modes);
		loginfo.info("old triple and new triple:  "+result.toString());
		this.insertNews(result.getNews());
		this.deleteolds(result.getOlds());
		return lists.size();
	}
	
	public void deleteolds(List<Triple> olds){
		//VirtGraph graph=new VirtGraph (dataBase,dataSource, userName, passWord);
		VirtGraph graph=new VirtGraph (dataBase,dataSource, userName, passWord);
		graph.getBulkUpdateHandler().delete(olds);
		graph.close();
	}
	
	
	public void insertNews(List<Triple> news){
		VirtGraph graph=new VirtGraph (dataBase,dataSource, userName, passWord);
		graph.getBulkUpdateHandler().add(news);
		graph.close();
	}
	
	public Results constructDeleteInsertResults(List<HashMap<String,String>> lists,ArrayList<Mode> modes){
		List<Triple> olds=new ArrayList<Triple>();
		List<Triple> news=new ArrayList<Triple>();
		for(HashMap<String,String> map:lists){
			//String subject=map.get("s");
			for(Mode mode:modes){
				String subject=this.subjectValue(map.get("s"),mode);
				String key=mode.getMatchRule().getPname();
				String oldp=mode.getChange().getPoldName();
				String newp=mode.getChange().getPnewName();
				String oldvalue=map.get(key);
				String newvalue=this.confirmOvalue(oldvalue, mode);
				if(newvalue==null){
					throw new RuntimeException("ModifyData 配置错误，请重新配置，要么就是Otrim和oprefix都有值，要么是都没有值");
				}
				Triple tripleold=this.constructTriple(subject, oldp, newvalue);
				Triple triplenew=this.constructTriple(subject, newp, newvalue);
				olds.add(tripleold);
				news.add(triplenew);
			}
		}
		Results result=new Results(olds,news);
		return result;
	}
	
	public String confirmOvalue(String oldvalue,Mode mode){
		if(mode.getChange().getOprefix()==null||mode.getChange().getOtrim()==null){
			return oldvalue;
		}
		else if(mode.getChange().getOprefix().isEmpty()||mode.getChange().getOtrim().isEmpty()){
			return oldvalue;
		}
		else if(!mode.getChange().getOprefix().isEmpty()&&!mode.getChange().getOtrim().isEmpty()){
			return oldvalue.replaceAll(mode.getChange().getOtrim(), mode.getChange().getOprefix());
		}
		else return null;
	}
	

	public List<HashMap<String,String>> doSelect(String query){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		VirtGraph set = new VirtGraph (dataSource, userName, passWord);
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			HashMap<String,String> map=new HashMap<String,String>();
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
				map.put(name, nodevalue);
			}
			list.add(map);
		}
		vqe.close();
		set.close();
		return list;
	}
	
	public Triple constructTriple(String s,String p,String o){
		Node sub = Node.createURI(s);
		Node predicate = Node.createURI(p);
		Node obj=null;
		if (o.trim().startsWith("http") || o.trim().startsWith("HTTP")
				|| o.trim().startsWith("https") || o.trim().startsWith("HTTPS")) {
			 obj = Node.createURI(o);
		}
		else{
			 obj = Node.createLiteral(o);
		}
		return new Triple(sub, predicate, obj);
	}
	
	public void deleteTriples(List<Triple> triples){
		VirtGraph graph = new VirtGraph (dataBase,dataSource, userName, passWord);
		graph.getBulkUpdateHandler().delete(triples);
	}
	
	public String subjectValue(String subject,Mode mode){
		if(mode.getChange().getSubjectoldPrefix()==null||mode.getChange().getSubjectnewPrefix()==null){
			return subject;
		}
		else if(mode.getChange().getSubjectoldPrefix().isEmpty()||mode.getChange().getSubjectnewPrefix().isEmpty()){
			return subject;
		}
		else if(!mode.getChange().getSubjectoldPrefix().isEmpty()&&!mode.getChange().getSubjectnewPrefix().isEmpty()){
			return subject.replaceAll(mode.getChange().getSubjectoldPrefix(), mode.getChange().getSubjectnewPrefix());
		}
		else return null;
	}
	
	public static void main(String[] args) {
		//Modify modify=new Modify();
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				//new String[] { "genome-modify-job.xml" });
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "modify-enzyme-job.xml" });
		Modify modify = context.getBean("modify", Modify.class);
		modify.doListener();
		context.close();
		//modify.constructTriple("http://gcm.wfcc.info/genome/NZ_AABF02000001", "http://gcm.wdcm.org/gcm/tostrain", "ATCC 49256");
//		ArrayList<Triple> list=new ArrayList<Triple>();
//		list.add(modify.constructTriple("http://gcm.wfcc.info/genome/NZ_AABF02000001", "http://gcm.wdcm.org/gcm/tostrain", "ATCC 49256"));
//		modify.deleteolds(list);
	}
}
