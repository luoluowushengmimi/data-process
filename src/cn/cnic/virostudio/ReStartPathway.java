package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.job.ReStep;
import cn.cnic.virostudio.job.Step;

public class ReStartPathway {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "pathway-job.xml" });
		ReStep step = context.getBean("restep", ReStep.class);
		loginfo.info("pathway总条数是： "+step.doStep(0));
		context.close();
	}

}
