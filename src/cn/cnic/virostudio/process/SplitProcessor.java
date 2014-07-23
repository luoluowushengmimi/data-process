package cn.cnic.virostudio.process;
import cn.cnic.virostudio.rule.MatchRule;
import cn.cnic.virostudio.rule.ModifyData;
import com.google.common.collect.Multimap;

/**
 *s "http://example/12"
 *p definition
 *o wgs;os wgs
 *
 *s:http://hello/12
 *p rdfs:comments
 *o wgs
 *
 *s:http://hello/12
 *p rdfs:comments
 *o os wgs
 */
public class SplitProcessor extends AbstractProcessor {
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
		if (!input.containsKey(pname))
			return input;

		String[] split_triples = input.get(pname).iterator().next()
				.split(change.osplitTag);
		input.removeAll(pname);
		for (String split_triple : split_triples) {
			input.put(change.pnewName, "\""+split_triple.trim()+"\"");
		}
		input.removeAll(pname);
		return input;
	}

	@Override
	public boolean shouldProcess(Multimap<String, String> input) {
		// TODO Auto-generated method stub
		return false;
	}
}
