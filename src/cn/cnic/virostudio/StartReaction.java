package cn.cnic.virostudio;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.cnic.virostudio.job.JobController;

public class StartReaction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "reaction-job.xml" });
		JobController job = context.getBean("job", JobController.class);
		job.doListener();
		context.close();
	}

}
