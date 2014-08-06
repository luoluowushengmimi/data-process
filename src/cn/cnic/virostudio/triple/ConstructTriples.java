package cn.cnic.virostudio.triple;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.cnic.virostudio.rule.ConstructTripleRule;

import com.google.common.collect.Multimap;

public class ConstructTriples {
	private static Logger logerr = Logger.getLogger("errLog");
	private static Logger loginfo = Logger.getLogger("infoLog");
	private List<ConstructTripleRule> rules;
	public List<ConstructTripleRule> getRules() {
		return rules;
	}
	public void setRules(List<ConstructTripleRule> rules) {
		this.rules = rules;
	}
	
   public Triple constructTriple(Multimap<String, String> map,String key,ConstructTripleRule rule){
		String id=map.get(rule.getIdname()).iterator().next().replace("\"", "");
		String subject=rule.getSprefix()+id;
		Triple triple=new Triple(subject,key,map.get(key).iterator().next());
		return triple;
   }
      
	
	public List<Triple> constructTriplesFromOneMap(Multimap<String, String> map) throws Exception{
	ArrayList<Triple> list=new ArrayList<Triple>();
	for (String key : map.keySet()) {
		ConstructTripleRule rule=this.verifyconstructTripleRule(key);
		if(rule==null){
			throw new Exception("配置ConstructTripleRule 有错，无法构建triple,错误信息是key："+key+"不存在，请查看原因");
		}
		if(!map.containsKey(rule.getIdname()))
			throw new Exception("配置ConstructTripleRule 有错，无法构建triple,错误信息是配置idname："+rule.getIdname()+"在map中不存在，请查看原因");
		list.add(this.constructTriple(map, key, rule));
	}
	return list;
}
	

//   
	/**
	 * 判断map里面的字段是否在配置的ConstructTripleRule list中，如果有，则返回这个ConstructTripleRule，以便于下面一步的构建，如果没有则返回null说明配置错误，配置错误的话，则构建整体失败，
	 * 需要重新配置才能启动任务。
	 * @param key
	 * @return
	 */
	public ConstructTripleRule verifyconstructTripleRule(String key){
		for(ConstructTripleRule rule:rules){
			ArrayList<String> keyList=(ArrayList<String>) rule.getKeys();
			for(String configKey:keyList){
				if(configKey.endsWith(key)){
					return rule;
				}
			}
		}
		return null;
	}
}
