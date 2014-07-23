package cn.cnic.virostudio.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Multimap;

public class PostProcessor {
	protected List<String> removeKeys = new ArrayList<>();
	protected HashMap<String, String> addproperties = new HashMap<String, String>();

	public List<String> getRemoveKeys() {
		return removeKeys;
	}

	public void setRemoveKeys(List<String> removeKeys) {
		this.removeKeys = removeKeys;
	}

	public HashMap<String, String> getAddproperties() {
		return addproperties;
	}

	public void setAddproperties(HashMap<String, String> addproperties) {
		this.addproperties = addproperties;
	}

	/**
	 * 后处理，清理map
	 */
	public Multimap<String, String> getMap(Multimap<String, String> map) {
		for (String key : removeKeys)
			map.removeAll(key);
		if (addproperties != null) {
			for (String key : addproperties.keySet()) {
				String value = addproperties.get(key);
				map.put(key, value);
			}
		}
		return map;
	}

	// public Multimap<String, String> getMap(Multimap<String, String> map) {
	// for(String key : removeKeys) {
	// if(map.containsKey(key)){
	// return null;
	// }
	// }
	// return map;
	// }
}
