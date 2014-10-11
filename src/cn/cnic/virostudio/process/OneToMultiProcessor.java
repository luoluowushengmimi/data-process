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
/**
 * 这个类是这样的，具体需求是在compound数据 remark字段，由于郭没有完全把数据打散造成的
 * 首先在处理的时候配置文件已经将"Same as: "去掉，并且去掉了前后引号，以空格作为了splitTag,
 *将数据 "Same as: D02324 G00316 G07287 G07471 G08476 G10593" 打散成了
 *[D02324,G00316,G07287,G07471,G08476,G10593] 对这个数组里面的每一个内容进行过滤和判断条件，满足条件a 将数据组里的数据转换成什么，满足条件b，将数据里满足条件的内容转化成什么，具体的
 *内容，请详见compound-job的配置。
 * @author lenovo
 *
 */
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
