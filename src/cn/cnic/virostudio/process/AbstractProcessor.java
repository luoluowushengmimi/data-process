package cn.cnic.virostudio.process;

import com.google.common.collect.Multimap;

public abstract class AbstractProcessor {

	/**
	 * map链处理
	 * 输入是一个map，输出是一个map
	 * @param input
	 * @return
	 */
	public abstract Multimap<String, String> process(Multimap<String, String> input);
	
	public abstract boolean shouldProcess(Multimap<String, String> input);

}
