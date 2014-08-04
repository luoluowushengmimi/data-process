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
		//test.geneTest();
		//test.keggenzymeTest();
		test.keggCompound();
	}

	public void genebankTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "genebank-job.xml" });
		//JobController job = context.getBean("job", JobController.class);
		//job.doListener();
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("locusId", "NZ_AAFL01000001");
		resultmap.put("dbXrefTxt", "taxon:306254");
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
				new String[] { "gene-job-one.xml" });
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
		ApplicationContext context2 = new ClassPathXmlApplicationContext(
				new String[] { "gene-job-two.xml" });
		Multimap<String, String> resultmap2 = ArrayListMultimap.create();
		resultmap2.put("geneId", "1010183");
		resultmap2.put("taxonSource", "http://bds.csdb.cn/material/taxonomy/1140");
		resultmap2.put("symbol", "anL02");
		resultmap2.put("description", "putative replication-associated protein");
		resultmap2.put("geneType", "protein-coding");
		System.out.println("before process " + resultmap2);
		CompositeProcessor processor2 = context2.getBean("main-processor",CompositeProcessor.class);
		System.out.println("after process " + processor2.process(resultmap2));
		
	}
	
	public void keggenzymeTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "enzyme-job.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("enzymeId", "1.1.1.1");
		resultmap.put("enzymeName", "ADH");
		resultmap.put("sysname", "alcohol:NAD+ oxidoreductase");
		resultmap.put("class", "Oxidoreductases;Acting on the CH-OH group of donors;With NAD+ or NADP+ as acceptor");
		resultmap.put("reactionSource", "http://bds.csdb.cn/material/kegg/reaction/R07326");
		resultmap.put("substrate", "http://bds.csdb.cn/material/kegg/compound/C00003");
		resultmap.put("product", "http://bds.csdb.cn/material/kegg/compound/C00004");
		resultmap.put("comment", "A zinc protein. Acts on primary or secondary alcohols or hemi-acetals with very broad");
		resultmap.put("history", "EC 1.1.1.1 created 1961, modified 2011");
		resultmap.put("keggGeneStdId", "hsa:124");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}

	
	public void keggCompound() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "compound-job.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("compoundId", "C00001");
		resultmap.put("compoundName", "C00001");
		resultmap.put("formula", "H2O");
		resultmap.put("remark", "Same as: G08646");
		resultmap.put("exactMass", "506.9957");
		resultmap.put("molWeight", "507.181");
		resultmap.put("comment", "Source: Streptomyces tsukubaensis [TAX:83656]4 Pyd is modified by vinyl. (allylmalonyl-CoA extension unit)");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}
}
