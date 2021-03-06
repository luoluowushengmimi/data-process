package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.job.Step;
public class StartGenebank {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	public static void main(String[] args) throws Exception {
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new String[] { "genebank-job.xml" });
//		JobController job = context.getBean("job", JobController.class);
//		job.doListener();
//		context.close();
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "genebank-job.xml" });
		Step step = context.getBean("step", Step.class);
		loginfo.info("genebank总条数是： "+step.doStep(0));
		context.close();
	}
}
