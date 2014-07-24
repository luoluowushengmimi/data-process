package cn.cnic.virostudio.process;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

public class PostProcessor {
	protected List<String> removeKeys = new ArrayList<>();
	public List<String> getRemoveKeys() {
		return removeKeys;
	}
	public void setRemoveKeys(List<String> removeKeys) {
		this.removeKeys = removeKeys;
	}
	/**
	 * 后处理，清理map
	 */
	public Multimap<String, String> getMap(Multimap<String, String> map) {
		for(String key : removeKeys) 
				map.removeAll(key);
			return map;
	}
	
}
