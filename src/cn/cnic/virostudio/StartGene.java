package cn.cnic.virostudio;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cn.cnic.virostudio.job.JobController;

public class StartGene {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "gene-job-one.xml" });
		JobController job = context.getBean("job", JobController.class);
		job.doListener();
		context.close();
//		ClassPathXmlApplicationContext contextnew = new ClassPathXmlApplicationContext(
//				new String[] { "gene-job2.xml" });
//		JobController job2 = context2.getBean("job", JobController.class);
//		job2.doListener();
//		context2.close();
	}
}
