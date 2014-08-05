package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.job.Step;

public class StartPathway {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//				new String[] { "pathway-job.xml" });
//		JobController job = context.getBean("job", JobController.class);
//		job.doListener();
//		context.close();
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "pathway-job.xml" });
		Step step = context.getBean("step", Step.class);
		loginfo.info("pathway总条数是： "+step.doStep(0));
		context.close();
	}

}
