package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cn.cnic.virostudio.job.JobController;

public class ReStartGene {

	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	public static void main(String[] args) throws Exception {
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new String[] { "enzyme-job.xml" });
//		JobController job = context.getBean("job", JobController.class);
//		job.doListener();
//		context.close();
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "gene-job-one.xml" });
		JobController job = context.getBean("job", JobController.class);
		job.doListener();
		context.close();
	}

}
