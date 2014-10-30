package cn.cnic.virostudio;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.job.Step;



public class Start {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "taxonomy-job.xml" });
		Step step = context.getBean("step", Step.class);
		step.doStep(0);
		context.close();
	}
}
