package cn.cnic.virostudio.process;

import java.util.List;
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
	/**
	 * 预处理，从BindingSet获取map
	 */
//	public Multimap<String, String> getMap(BindingSet set) {
//		
//		Multimap<String, String> result = ArrayListMultimap.create();
//		for(String key : set.getBindingNames()) {
//			result.put(key, set.getValue(key).stringValue().replace("\"", ""));
//		}
//		loginfo.info("map: "+result);
//		return this.getFilterResult(result);
//		//return result;
//	}
	
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
			loginfo.info("map" +map +"是否保留用作处理数据：" +tag);
		}
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
		//info.info("一个留下的条件的初始tag：" +tag);
		String mapValue=map.get(condition.getKey()).iterator().next().trim();
		//info.info("mapValue:" +mapValue);
		String conditionValue=condition.getValue().trim();
		//info.info("conditionValue:" +conditionValue);
		if(condition.getMatchRule().equalsIgnoreCase("start")&&!conditionValue.contains(",")){
			//info.info("condition 进入start and without ,:" +conditionValue);
			tag=this.isStartSave(mapValue, conditionValue);
			//info.info(" 进入start and without , " +tag);
		}
		else if(condition.getMatchRule().equalsIgnoreCase("equal")&&!conditionValue.contains(",")){
			//info.info("condition 进入equal and without ,:" +conditionValue);
			tag=this.isEqualSave(mapValue, conditionValue);
			//info.info(" 进入equal and without , 的tag 是: " +tag);
		}
		else if(condition.getMatchRule().equalsIgnoreCase("start")&&conditionValue.contains(",")){
			//info.info("condition 进入start and with |:" +conditionValue);
			tag=this.isStartSave(mapValue, conditionValue.split(","));
			//info.info(" 进入start and with , 的tag 是: " +tag);
		}
		else if(condition.getMatchRule().equalsIgnoreCase("equal")&&conditionValue.contains(",")){
			//info.info("condition 进入equal and with ," +conditionValue);
			tag=this.isEqualSave(mapValue, conditionValue.split(","));
			//info.info(" 进入equal and with , 的tag 是: " +tag);
		}
		return tag;
		
	}
	public boolean isStartSave(String mapValue,String[] conditionValue){
		boolean tag=false;
		for(String con:conditionValue){
			//info.info("con: "+con);
			//info.info("this.isStartSave: "+this.isStartSave(mapValue, con));
			if(this.isStartSave(mapValue, con)){
				tag=true;
			}
		}
		return tag;
	}
	public boolean isEqualSave(String mapValue,String[] conditionValue){
		boolean tag=false;
		for(String con:conditionValue){
			if(this.isStartSave(mapValue, con)){
				tag=true;
			}
		}
		return tag;
	}
	
	public boolean isStartSave(String mapValue,String conditionValue){
		boolean tag=false;
		if(mapValue.startsWith(conditionValue)){
			tag=true;
		}
		return tag;
	}
	public boolean isEqualSave(String mapValue,String conditionValue){
		boolean tag=false;
		if(mapValue.equalsIgnoreCase(conditionValue)){
			tag=true;
		}
		return tag;
	}
	
	
}
