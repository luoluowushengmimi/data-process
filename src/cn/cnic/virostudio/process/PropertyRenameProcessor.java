package cn.cnic.virostudio.process;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			String oldOvalue=input.get(pname).iterator().next();
			String newOvalue=oldOvalue.replaceAll(change.otrim,"");
			input.put(change.pnewName, change.oprefix + newOvalue.trim());
		}
		else { // if (!matchRule.otype.equalsIgnoreCase("uri")) {
			String oldOvalue=input.get(pname).iterator().next();
			String newOvalue=oldOvalue.replaceAll(change.otrim,"");
			input.put(change.pnewName, "\"" + change.oprefix + newOvalue.trim() + "\"");
		}
		input.removeAll(pname);
	
		return input;
	}
	
	public boolean shouldProcess(Multimap<String, String> input) {
		String pname = matchRule.getPname();
		if(!input.containsKey(pname))
			return false;
		
		if (matchRule.ofilter != null && !matchRule.ofilter.isEmpty()) {
			String value=input.get(pname).iterator().next();
			 Pattern pattern=Pattern.compile(matchRule.ofilter);
			 Matcher matcher=pattern.matcher(value);
			 return pattern.matches(matchRule.ofilter, value);
		 //return input.get(pname).iterator().next().startsWith(matchRule.ofilter);
		}
		return true;
	}
}