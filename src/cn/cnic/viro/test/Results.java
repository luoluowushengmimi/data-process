package cn.cnic.viro.test;

import java.util.List;

import com.hp.hpl.jena.graph.Triple;

public class Results {

  public Results(List<Triple> olds, List<Triple> news) {
		super();
		this.olds = olds;
		this.news = news;
	}

private List<Triple> olds;
  
  private List<Triple> news;

@Override
public String toString() {
	
	StringBuilder sb=new StringBuilder();
	sb.append("old triples: ");
	for(Triple triple:olds){
		sb.append(triple.getSubject().getURI()+"  ");
		sb.append(triple.getPredicate().getURI()+"  ");
		if(triple.getObject().isLiteral()){
			sb.append(triple.getObject().getLiteral().toString());
		}
		else{
			sb.append(triple.getObject().getURI());
		}
		sb.append("\r\n");
	}
	sb.append("new triples: ");
	for(Triple triple:news){
		sb.append(triple.getSubject().getURI()+"  ");
		sb.append(triple.getPredicate().getURI()+"  ");
		if(triple.getObject().isLiteral()){
			sb.append(triple.getObject().getLiteral().toString());
		}
		else{
			sb.append(triple.getObject().getURI());
		}
		sb.append("\r\n");
	}
	return sb.toString();
}

public List<Triple> getOlds() {
	return olds;
}


public List<Triple> getNews() {
	return news;
}
}
