package cn.cnic.virostudio.step;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.log4j.Logger;
import org.springframework.batch.item.util.FileUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class DataWriterToFile implements DataWriter {
	private String filePath;
	private String sprefix;
	private String idname;
	private HashMap<String, String> namespace;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSprefix() {
		return sprefix;
	}

	public void setSprefix(String sprefix) {
		this.sprefix = sprefix;
	}

	public HashMap<String, String> getNamespace() {
		return namespace;
	}

	public void setNamespace(HashMap<String, String> namespace) {
		this.namespace = namespace;
	}

	public void setIdname(String idname) {
		this.idname = idname;
	}
	
	@Override
	public String getIdname() {
		// TODO Auto-generated method stub
		return idname;
	}

	
	/**
	 * 
	 * @param map
	 *            将一个读取结果的数据处理以后放入一个文件里
	 */
	public void write(String id,Multimap<String, String> map) {
		id=id.replaceAll("\"", "");
		Model model = this.constructFileContent(id, map);
		File directory = new File(filePath);
		if (!directory.exists()) {
			loginfo.info("创建文件夹： "+filePath);
			directory.mkdirs();
		}
		if (filePath.endsWith("/")) {
			filePath = filePath.substring(0, filePath.length() - 1);
		}
		File file = new File(filePath + "/" + id+".nt");
		FileOutputStream fout;
		try {
			loginfo.info("创建文件： "+filePath + "/" + id+".nt");
			fout = new FileOutputStream(file, true);
			RDFDataMgr.write(fout, model, RDFFormat.NT);
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			logerr.error("写入文件： "+filePath + "/" + id+"  失败！"+e.getMessage());
		}

	}

	public Model constructFileContent(String id,
			Multimap<String, String> map) {
		Model model = this.createModel();
		Resource resource = model.createResource(sprefix + id);
		for (String key : map.keySet()) {
			for (String value : map.get(key)) {
				Property p = this.constructPronouce(key, model);
				if (this.isURL(value)) {
					Resource constructObject = this.constructObject(value,
							model);
					resource.addProperty(p, constructObject);
				} else {
					resource.addProperty(p, value.replaceAll("\"", ""));
				}
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
	
//public static void main(String[] args) {
//	Multimap<String, String> result = ArrayListMultimap.create();
//	HashMap<String, String> namespace =new HashMap<String, String> ();
//	namespace.put("dwc", "http://rs.tdwg.org/dwc/terms/");
//	namespace.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
//	namespace.put("taxon", "http://gcm.wfcc.info/taxonomy/");
//	result.put("rdfs:label", "\""+"106003"+"\"");
//	result.put("dwc:parentNameUsageID", "taxon:231509");
//	result.put("dwc:taxonRank", "\""+"species"+"\"");
//	result.put("dwc:scientificName", "\""+"Rhodotorula buffonii"+"\"");
//	loginfo.info(result);
//	DataWriterToFile ww=new DataWriterToFile();
//	ww.setNamespace(namespace);
//	ww.setFilePath("D:/taxonomy/");
//	ww.setSprefix("http://gcm.wfcc.info/taxonomy/");
//	ww.setIdname("");
//	ww.write("1", result);
//}


}
