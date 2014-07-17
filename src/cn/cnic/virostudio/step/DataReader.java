package cn.cnic.virostudio.step;

import java.util.List;
import com.google.common.collect.Multimap;

public interface DataReader {

	public List<Multimap<String, String>> getQueryResult();

	public String getDataSource();

	public void setDataSource(String dataSource);

	public String getSelectClause();

	public void setSelectClause(String selectClause);

	public String getWhereClause();

	public void setWhereClause(String whereClause);

	public int getLimit();

	public void setLimit(int limit);

	public int getOffset();

	public void setOffset(int offset);

	public String getDataBase();

	public void setDataBase(String dataBase);

	public String getUserName();

	public void setUserName(String userName);

	public String getPassWord();

	public void setPassWord(String passWord);

}
