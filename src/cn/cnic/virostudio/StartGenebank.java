package cn.cnic.virostudio;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import cn.cnic.virostudio.job.JobController;
import cn.cnic.virostudio.process.CompositeProcessor;
public class StartGenebank {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "genebank-job.xml" });
		JobController job = context.getBean("job", JobController.class);
		job.doListener();
	}
}