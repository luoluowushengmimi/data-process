package cn.cnic.virostudio.job;

import org.apache.log4j.Logger;


public class JobController {
	private Step step;
	private static Logger log = Logger.getLogger("");

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}
	
	/**
	 * 主控程序
	 * @throws Exception 
	 */
	public void doListener() throws Exception{
		int filenumber=0;
		log.info("进入到总控程序");
		while(true){
			long count=step.doStep(filenumber);
		int limit=step.getDataReader().getLimit();
		
		if(count<limit){
			break;
		}
		else{
			int offset=step.getDataReader().getOffset();
			offset=offset+step.getDataReader().getLimit();
			step.getDataReader().setOffset(offset);
		}
		}
	}

}
