package cn.cnic.virostudio;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.cnic.virostudio.job.JobController;

public class StartGene {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context1 = new ClassPathXmlApplicationContext(
				new String[] { "gene-job1.xml" });
		JobController job1 = context1.getBean("job", JobController.class);
		job1.doListener();
		context1.close();
		ClassPathXmlApplicationContext context2 = new ClassPathXmlApplicationContext(
				new String[] { "gene-job2.xml" });
		JobController job2 = context2.getBean("job", JobController.class);
		job2.doListener();
		context2.close();
	}
}
