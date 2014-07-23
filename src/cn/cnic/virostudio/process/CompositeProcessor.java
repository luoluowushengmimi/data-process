package cn.cnic.virostudio.process;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.google.common.collect.Multimap;

public class CompositeProcessor extends AbstractProcessor{
	protected PreProcessor preProcessor;
	//protected PostProcessor postProcessor;
	protected List<AbstractProcessor> processors = new ArrayList<>();
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");

	public PreProcessor getPreProcessor() {
		return preProcessor;
	}

	public void setPreProcessor(PreProcessor preProcessor) {
		this.preProcessor = preProcessor;
	}

	public List<AbstractProcessor> getProcessors() {
		return processors;
	}

	public void setProcessors(List<AbstractProcessor> processors) {
		this.processors = processors;
	}

//	public Multimap<String, String> process(BindingSet tuple) {
//		return this.process(preProcessor.getMap(tuple));
//	}

	@Override
	public Multimap<String, String> process(Multimap<String, String> input) {
		
		if(input==null) return null;
		input=preProcessor.getFilterResult(input);
		if(input==null) return null;
		Multimap<String, String> result = input;
		for(AbstractProcessor processor : processors) {
			result = processor.process(result);
		}
		return result;
	}

	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		// TODO Auto-generated method stub
		return false;
	}
}
