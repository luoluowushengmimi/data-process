package cn.cnic.virostudio.process;



import cn.cnic.virostudio.rule.MatchRule;
import cn.cnic.virostudio.rule.ModifyData;
import com.google.common.collect.Multimap;

public class PropertyRenameProcessor extends AbstractProcessor {
	
	private MatchRule matchRule = new MatchRule();
	private ModifyData change = new ModifyData();
	public ModifyData getChange() {
		return change;
	}

	public void setChange(ModifyData change) {
		this.change = change;
	}

	public MatchRule getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(MatchRule matchRule) {
		this.matchRule = matchRule;
	}
	
	@Override
	public Multimap<String, String> process(Multimap<String, String> input) {
		String pname = matchRule.getPname();
		if(!shouldProcess(input))
			return input;
		
		if(change.otype.equalsIgnoreCase("uri")) {
			input.put(change.pnewName, change.oprefix + input.get(pname).iterator().next());
		}
		else { // if (!matchRule.otype.equalsIgnoreCase("uri")) {
			input.put(change.pnewName, "\"" + change.oprefix + input.get(pname).iterator().next() + "\"");
		}
		input.removeAll(pname);
	
		return input;
	}
	
	public boolean shouldProcess(Multimap<String, String> input) {
		String pname = matchRule.getPname();
		if(!input.containsKey(pname))
			return false;
		
		if (matchRule.ofilter != null && !matchRule.ofilter.isEmpty()) {
		 return input.get(pname).iterator().next().startsWith(matchRule.ofilter);
		}
		return true;
	}
}