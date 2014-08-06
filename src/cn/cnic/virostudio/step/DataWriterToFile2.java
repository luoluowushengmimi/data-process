package cn.cnic.virostudio.step;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.log4j.Logger;
import org.springframework.batch.item.util.FileUtils;

import cn.cnic.virostudio.triple.ConstructTriples;
import cn.cnic.virostudio.triple.Triple;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class DataWriterToFile2 implements DataWriter {
	private String filePath;
	private HashMap<String, String> namespace;
	private ConstructTriples constructTriples;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public HashMap<String, String> getNamespace() {
		return namespace;
	}

	public void setNamespace(HashMap<String, String> namespace) {
		this.namespace = namespace;
	}

	public ConstructTriples getConstructTriples() {
		return constructTriples;
	}

	public void setConstructTriples(ConstructTriples constructTriples) {
		this.constructTriples = constructTriples;
	}

	public void write(int fileId,ArrayList<Multimap<String, String>> listmap) throws Exception{
		List<Triple> triples=this.constructTriples(listmap);
		this.write(fileId, triples);
	}
	
	public List<Triple> constructTriples(ArrayList<Multimap<String, String>> listmap) throws Exception{
	ArrayList<Triple> list=new ArrayList<Triple>();
	for(Multimap<String, String> map:listmap){
		List<Triple> listp=constructTriples.constructTriplesFromOneMap(map);
		list.addAll(listp);
	}
	return list;
}
	/**
	 * 
	 * @param map
	 *            将一个读取结果的数据处理以后放入一个文件里
	 */
	public void write(int fileId,List<Triple> triples) {
		Model model = this.constructFileContent(triples);
		File directory = new File(filePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0, filePath.length() - 1);
		}
		File file = new File(filePath + "/" + fileId+".nt");
		FileOutputStream fout;
		try {
			loginfo.info("创建文件： "+filePath + "/" + fileId+".nt");
			fout = new FileOutputStream(file, true);
			RDFDataMgr.write(fout, model, RDFFormat.NT);
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			logerr.error("写入文件： "+filePath + "/" + fileId+"  失败！"+e.getMessage());
		}

	}

	public Model constructFileContent(
			List<Triple> triples) {
		Model model = this.createModel();
		for(Triple triple:triples){
			String subject=triple.getS();
			Resource resource = model.createResource(subject);
			Property p = this.constructPronouce(triple.getP(), model);
			if (this.isURL(triple.getO())) {
				Resource constructObject = this.constructObject(triple.getO(),
						model);
				resource.addProperty(p, constructObject);
			} else {
				resource.addProperty(p, triple.getO().replaceAll("\"", ""));
			}
		}
		return model;
	}

	public Resource constructObject(String value, Model model) {
		Resource resource = null;
		value=value.replace("\"", "");
		/**
		 * taxon:1234这样的宾语，处理方式
		 */
		String[] couples = value.split(":");
		String prefix = couples[0];
		String content = couples[1];
		if (prefix.startsWith("http") || prefix.startsWith("HTTP")
				|| prefix.startsWith("https") || prefix.startsWith("HTTPS")) {
			resource = model.createResource(value);
			return resource;
		}
		if (!namespace.containsKey(prefix))
			throw new NullPointerException();
		String oprefix = namespace.get(prefix);
		if (!oprefix.endsWith("/")) {
			oprefix = oprefix + "/";
		}
		value = oprefix + content;
		resource = model.createResource(value);
		return resource;

	}

	public boolean isURL(String value) {
		if (value.contains("\"")) {
			return false;
		} 
		else{
		return true;
		}
	}

	public Property constructPronouce(String key, Model model) {
		Property p = null;
		if (key.startsWith("http:") || key.startsWith("HTTP:")
				|| key.startsWith("https:") || key.startsWith("HTTPS:")) {
			p = model.createProperty(key);
			return p;
		} else if (key.contains(":")) {
			String[] couples = key.split(":");
			String prefix = couples[0];
			String prenounce = couples[1];
			if (!namespace.containsKey(prefix))
				throw new NullPointerException();
			p = model.createProperty(namespace.get(prefix), prenounce);
		}
		return p;
	}

	/**
	 * 将所有的prefix放入，其中namespace就是所有的prefix的集合
	 * 
	 * @return
	 */
	public Model createModel() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefixes(namespace);
		return model;
	}

	@Override
	public void write(int fileId, String id, Multimap<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIdname() {
		// TODO Auto-generated method stub
		return null;
	}

}
