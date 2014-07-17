package cn.cnic.virostudio.process;

import java.util.List;
import cn.cnic.virostudio.rule.MatchRule;
import cn.cnic.virostudio.rule.ModifyData;

import com.google.common.collect.Multimap;

public class CombineProcessor extends AbstractProcessor{
	private List<MatchRule> matchRules ;
	private ModifyData change ;

	public List<MatchRule> getMatchRules() {
		return matchRules;
	}

	public void setMatchRules(List<MatchRule> matchRules) {
		this.matchRules = matchRules;
	}

	public ModifyData getChange() {
		return change;
	}

	public void setChange(ModifyData change) {
		this.change = change;
	}

	@Override
	public Multimap<String, String> process(Multimap<String, String> input) {
		if(!shouldProcess(input))
			return input;
		String pnewname;
		String value;
		if(change.getPnewName().endsWith(".value")){
			 pnewname=input.get(change.getPnewName().split(".value")[0]).iterator().next();
		}
		else{
			 pnewname=change.getPnewName();
		}
		if(change.getContent().endsWith(".value")){
			value=input.get(change.getContent().split(".value")[0]).iterator().next();
		}
		else{
			value=change.getContent();
		}
		input.put(pnewname, this.constructObjectThroughOtype(value));
		return input;
	}
	
	public String constructObjectThroughOtype(String value){
		if(change.getOtype().equalsIgnoreCase("uri")){
			value=value;
		}
		else{
			value="\""+value+"\"";
		}
		return value;
	}

	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		for(MatchRule rule:matchRules){
			String pname = rule.getPname();
			if(!input.containsKey(pname))
				return false;
			if (rule.ofilter != null && !rule.ofilter.isEmpty()) {
			 return input.get(pname).iterator().next().startsWith(rule.ofilter);
			}
		}
		return true;
	}
	
}
