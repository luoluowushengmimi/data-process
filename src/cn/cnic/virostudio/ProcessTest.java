package cn.cnic.virostudio;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.job.Step;
import cn.cnic.virostudio.process.CompositeProcessor;
import cn.cnic.virostudio.step.DataWriter;
import cn.cnic.virostudio.step.DataWriterToFile;

public class ProcessTest {
	private DataWriterToFile dataWriter;

	public static void main(String[] args) throws Exception {
		ProcessTest test=new ProcessTest();
		//test.taxonomyTest();
		//test.genebankTest();
		test.geneTest();
	}

	public void genebankTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "genebank-job.xml" });
		//JobController job = context.getBean("job", JobController.class);
		//job.doListener();
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("locusId", "NZ_AAFL01000001");
		resultmap.put("dbXrefTxt", "taxon:306254");
		resultmap.put("seqMax", "380315");
		resultmap.put("seqMin", "379860");
		resultmap.put("locationTxt", "3204533..3204904");
		resultmap
				.put("definition",
						"Campylobacter coli RM2228 cont202, whole genome shotgun sequence");
		resultmap.put("keywords", "WGS; RefSeq.");
		resultmap.put("projectId", "179761");
		resultmap.put("bioprojectId", "PRJNA179761");
		resultmap.put("strain", "RM2228");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}
	
	public void taxonomyTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "taxonomy-job.xml" });
		//JobController job = context.getBean("job", JobController.class);
		//job.doListener();
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("taxid", "1");
		resultmap.put("nameclass", "hello");
		resultmap.put("taxname", "root");
		resultmap.put("nodeRank", "superfamily");
		resultmap.put("parentId", "7434");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}

	
	public void geneTest(){
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "gene-job.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("locusId", "NC_000870");
		resultmap.put("locus_tag", "ToYLCTvsBgp1");
		resultmap.put("seqMax", "1062");
		resultmap.put("seqMin", "346");
		resultmap.put("locationTxt", "346..1062");
		resultmap.put("featureTagName", "gene");
		resultmap.put("dbXrefTxt", "GeneID:2046");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",CompositeProcessor.class);
		Multimap<String, String> processmap=processor.process(resultmap);
		System.out.println("after process " + processmap);
//			if(processmap==null) return;
//			String id=processmap.get("dbXrefTxt").iterator().next();
//			if(id==null) {
//				throw new RuntimeException();
//				}
//			dataWriter=new DataWriterToFile();
//			dataWriter.setFilePath("D:/gene/");
//			dataWriter.setSprefix("http://gcm.wfcc.info/gene/");
//			dataWriter.setIgnorekey("dbXrefTxt");
//			HashMap<String, String> namespace=new HashMap<String, String>();
//			namespace.put("dc", "http://purl.org/dc/elements/1.1/");
//			namespace.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
//			namespace.put("GCM", "http://gcm.wdcm.org/gcm/");
//			dataWriter.setNamespace(namespace);
//			dataWriter.write(id, processmap);
	}
}
