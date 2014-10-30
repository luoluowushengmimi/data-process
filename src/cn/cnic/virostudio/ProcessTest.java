package cn.cnic.virostudio;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
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
		test.taxonomyTest();
		//test.genebankTest();
		//test.geneTest();
		//test.keggenzymeTest();
		//test.keggCompound();
		//test.addGenomeTitleTest();
		//test.pdbTest();
	}
	
	public void addGenomeTitleTest(){
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "add-genome-title.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("s", "http://gcm.wfcc.info/genome/NZ_AABF02000001");
		resultmap.put("comment", "Fusobacterium nucleatum subsp. vincentii ATCC 49256 Contig0377, whole genome shotgun sequence.");
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
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
		resultmap.put("taxid", "6");
		resultmap.put("nameclass", "scientific name");
		resultmap.put("taxname", "Azorhizobium");
		resultmap.put("nodeRank", "genus");
		resultmap.put("parentId", "335928");
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
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "enzyme-job.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("enzymeId", "1.1.1.1");
		resultmap.put("enzymeName", "ADH");
		resultmap.put("sysname", "");
		resultmap.put("class", "Oxidoreductases;Acting on the CH-OH group of donors;With NAD+ or NADP+ as acceptor");
		resultmap.put("reactionSource", "http://bds.csdb.cn/material/kegg/reaction/R07326");
		resultmap.put("substrate", "http://bds.csdb.cn/material/kegg/compound/C00003");
		resultmap.put("product", "http://bds.csdb.cn/material/kegg/compound/C00004");
		resultmap.put("comment", "A zinc protein. Acts on primary or secondary alcohols or hemi-acetals with very broad");
		resultmap.put("history", "EC 1.1.1.1 created 1961, modified 2011");
		resultmap.put("keggGeneStdId", "hsa:124");
	}

	
	public void keggCompound() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "compound-job-two.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("compoundId", "C00001");
		resultmap.put("compoundName", "C00001");
		resultmap.put("formula", "H2O");
		resultmap.put("remark", "Same as: D00046 G00000");
		resultmap.put("exactMass", "506.9957");
		resultmap.put("molWeight", "507.181");
		resultmap.put("comment", "Source: Streptomyces tsukubaensis [TAX:83656]4 Pyd is modified by vinyl. (allylmalonyl-CoA extension unit)");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}
	
	public void uniprot() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "uniprot-job.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("uniprotId", "1A1D_PYRAB");
		resultmap.put("accessionNumber", "G8ZFP4");
		resultmap.put("description", "RecName: Full=Putative 1-aminocyclopropane-1-carboxylate deaminase; Short=ACC deaminase; EC=3.5.99.7");
		resultmap.put("taxid", "272844");
		resultmap.put("dblinktype", "RefSeq");
		resultmap.put("dblinkVal", "3625414");
		//resultmap.put("dblinktype", "GO");
		//resultmap.put("dblinkVal", "GO:0051536");
//		resultmap.put("dblinktype", "Pfam");
//		resultmap.put("dblinkVal", "PF12837");
//		resultmap.put("dblinktype", "PDB");
//		resultmap.put("dblinkVal", "1J0A");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}
	
	public void pdbTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "pdb-job.xml" });
		Multimap<String, String> resultmap = ArrayListMultimap.create();
		resultmap.put("pdbid", "1YG0");
		resultmap.put("title", "SOLUTION STRUCTURE OF APO-COPP FROM HELICOBACTER PYLORI");
		resultmap.put("taxonNode", "http://bds.csdb.cn/material/taxonomy/85962");
		resultmap.put("dblinkType", "UNP");
		resultmap.put("dblinkval", "NRAM_IATRA");
		System.out.println("before process " + resultmap);
		CompositeProcessor processor = context.getBean("main-processor",
				CompositeProcessor.class);
		System.out.println("afer process " + processor.process(resultmap));
	}
}
