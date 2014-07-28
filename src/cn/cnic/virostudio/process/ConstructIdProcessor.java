package cn.cnic.virostudio.process;

import com.google.common.collect.Multimap;

import cn.cnic.virostudio.rule.MatchRule;
import cn.cnic.virostudio.rule.ModifyData;

public class ConstructIdProcessor extends AbstractProcessor{
	private MatchRule matchRule = new MatchRule();
	private ModifyData change = new ModifyData();
	public MatchRule getMatchRule() {
		return matchRule;
	}
	public void setMatchRule(MatchRule matchRule) {
		this.matchRule = matchRule;
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
		String pname = matchRule.getPname();
		String[] split_triples = input.get(pname).iterator().next()
				.split(change.osplitTag);
		input.removeAll(pname);
		input.put(pname, split_triples[1]);
		return input;
	}
	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		String pname = matchRule.getPname().trim();
		if(pname!=null&&pname!=""){
			return true;
		}
		else return false;
	}
	
	

}
