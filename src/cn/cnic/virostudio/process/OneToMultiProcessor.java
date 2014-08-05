package cn.cnic.virostudio.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cnic.virostudio.rule.MatchRule;
import cn.cnic.virostudio.rule.ModifyData;

import com.google.common.collect.Multimap;

public class OneToMultiProcessor extends AbstractProcessor {

	private MatchRule matchRule;
	private List<ModifyData> modifies;

	public List<ModifyData> getModifies() {
		return modifies;
	}

	public void setModifies(List<ModifyData> modifies) {
		this.modifies = modifies;
	}

	public Multimap<String, String> process(Multimap<String, String> input) {
		String pname = matchRule.getPname();
		if (!shouldProcess(input))
			return input;
		for (String value : input.get(pname)) {
			if (this.isProcess(value)) {
				input = this.process(pname, value, input);
		}
		}
		return input;
	}

	public Multimap<String, String> process(String key, String value,
			Multimap<String, String> input) {
		for (ModifyData change : modifies) {
			String condition = change.getContent();
			Pattern pattern = Pattern.compile(condition);
			Matcher matcher = pattern.matcher(value);
			if (pattern.matches(condition, value)) {
				if (change.otype.equalsIgnoreCase("uri")) {
					String newOvalue = value.replaceAll(change.otrim, "");
					input.put(change.pnewName,
							change.oprefix + newOvalue.trim());
				} else { // if (!matchRule.otype.equalsIgnoreCase("uri")) {
					String newOvalue = value.replaceAll(change.otrim, "");
					input.put(change.pnewName, "\"" + change.oprefix
							+ newOvalue.trim() + "\"");
				}
			}
		}
		return input;
	}

	public boolean isProcess(String value) {
		if (matchRule.ofilter != null && !matchRule.ofilter.isEmpty()) {
			Pattern pattern = Pattern.compile(matchRule.ofilter);
			Matcher matcher = pattern.matcher(value);
			return pattern.matches(matchRule.ofilter, value);
		} else
			return true;
	}

	public MatchRule getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(MatchRule matchRule) {
		this.matchRule = matchRule;
	}

	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		String pname = matchRule.getPname();
		if (!input.containsKey(pname))
			return false;
		else
			return true;
	}

}
