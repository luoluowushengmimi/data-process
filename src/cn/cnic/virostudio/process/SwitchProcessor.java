package cn.cnic.virostudio.process;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Multimap;

public class SwitchProcessor extends AbstractProcessor{

	protected PreProcessor preProcessor;
	protected PostProcessor postProcessor;
	protected List<AbstractProcessor> processors = new ArrayList<>();

	public List<AbstractProcessor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<AbstractProcessor> processors) {
		this.processors = processors;
	}
	
	public PreProcessor getPreProcessor() {
		return preProcessor;
	}

	public void setPreProcessor(PreProcessor preProcessor) {
		this.preProcessor = preProcessor;
	}

	public PostProcessor getPostProcessor() {
		return postProcessor;
	}

	public void setPostProcessor(PostProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}

	/**
	 * 顺序处理所有单元
	 */


	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		//return true;
		for (AbstractProcessor pro : processors) {
			if (pro.shouldProcess(input)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Multimap<String, String> process(Multimap<String, String> input) {
		Multimap<String, String> result = input;
		for (AbstractProcessor processor : processors) {
			if (processor.shouldProcess(input)){
				result= processor.process(input);
				return result;
			}
		}
		return input;
	}
}
