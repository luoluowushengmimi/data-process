package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import cn.cnic.virostudio.process.CompositeProcessor;
public class AppTest {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	public static void main(String[] args) {
		Multimap<String,String> map = TreeMultimap.create();  
		map.put("locusId", "NZ_AAFL01000001");
		map.put("definition", "Campylobacter coli RM2228 cont202, whole genome shotgun sequence");
		map.put("keywords", "WGS; RefSeq");
		map.put("strain", "RM2228");
		map.put("dbXrefTxt", "GeneID:306254");
		map.put("bioprojectId", "PRJNA54141");
		map.put("projectId", "54141");
		loginfo.info("map: "+map);
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "genebank-job.xml" });
		CompositeProcessor app = context.getBean("main-processor", CompositeProcessor.class);
		Multimap<String,String> map2=app.process(map);
		loginfo.info("map2: "+map2);
	}

}
