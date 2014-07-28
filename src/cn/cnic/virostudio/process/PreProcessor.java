package cn.cnic.virostudio.process;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import cn.cnic.virostudio.rule.FilterCondition;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class PreProcessor {
	private List<FilterCondition> conditions;
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	
	public List<FilterCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<FilterCondition> conditions) {
		this.conditions = conditions;
	}
	
	public Multimap<String, String> getFilterResult(Multimap<String, String> map){
		if(conditions==null||conditions.size()==0)
			return map;
		if(this.isSave(map, conditions)){
			return map;
		}
		else{
			return null;
		}
	}
	
	public boolean isSave(Multimap<String, String> map,List<FilterCondition> conditions){
		boolean tag=true;
		for(FilterCondition condition:conditions){
			//info.info("condition:" +condition.getKey()+ " "+condition.getValue()+" "+condition.getMatchRule());
			boolean temp=this.isSave(map, condition);
			tag=tag&&temp;
		}
		loginfo.info("map" +map +"是否保留用作处理数据：" +tag);
		return tag;
	}
	/**
	 * 判断该map是否保留,满足条件的留下
	 * 例如：
	 * NC_011839 GeneID:7265989 Lactobacillus gasseri plasmid pLgLA39, complete sequence. 保留 
	 * @param map
	 * @param condition  一个留下的条件
	 * @return
	 */
	public boolean isSave(Multimap<String, String> map,FilterCondition condition){
		boolean tag=false;
		if(!map.containsKey(condition.getKey())) return tag;
		String mapValue=map.get(condition.getKey()).iterator().next().trim();
		String regEx=condition.getValue().trim();
		Pattern pattern=Pattern.compile(regEx);
		Matcher matcher=pattern.matcher(mapValue);
		return pattern.matches(regEx, mapValue);
		
	}
}
