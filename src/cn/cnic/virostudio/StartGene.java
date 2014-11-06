package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.job.Step;

public class StartGene {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "gene-job-one.xml" });
		Step step = context.getBean("step", Step.class);
		loginfo.info("genome中包含gene 总条数是： "+step.doStep(0));
		context.close();
		ClassPathXmlApplicationContext con = new ClassPathXmlApplicationContext(
				new String[] { "gene-job-two.xml"});
		Step steptwo = con.getBean("step", Step.class);
		loginfo.info("gene数据总条数是： "+steptwo.doStep(0));
		con.close();
	}
}
