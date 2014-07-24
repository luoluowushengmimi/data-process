package cn.cnic.virostudio.process;

import com.google.common.collect.Multimap;

import cn.cnic.virostudio.rule.MatchRule;
import cn.cnic.virostudio.rule.ModifyData;

public class AddPropertyProcessor extends AbstractProcessor{

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
		
		String pname = matchRule.getPname().trim();
		if (pname != null && pname != "") {
			if (change.otype.equalsIgnoreCase("uri")) {
				input.put(change.pnewName, change.oprefix
						+ input.get(pname).iterator().next().replaceAll("\"", ""));
			} else {
				input.put(change.pnewName,
						"\"" + change.oprefix
								+ input.get(pname).iterator().next().replaceAll("\"", "") + "\"");
			}
		}
		else{
			input.put(change.pnewName, change.content);
		}
		return input;
	}

	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		// TODO Auto-generated method stub
		return false;
	}

}
