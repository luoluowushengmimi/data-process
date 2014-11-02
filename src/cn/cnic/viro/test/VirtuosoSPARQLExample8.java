/*
 *  $Id: VirtuosoSPARQLExample8.java,v 1.2 2008/06/19 07:39:35 source Exp $
 *
 *  This file is part of the OpenLink Software Virtuoso Open-Source (VOS)
 *  project.
 *
 *  Copyright (C) 1998-2008 OpenLink Software
 *
 *  This project is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation; only version 2 of the License, dated June 1991.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

//package virtuoso.jena.driver;
package cn.cnic.viro.test;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.*;

public class VirtuosoSPARQLExample8 {

	/**
	 * Executes a SPARQL query against a virtuoso url and prints results.
	 */
	public static void main(String[] args) {

/*			STEP 1			*/
		VirtGraph set = new VirtGraph ("Example8","jdbc:virtuoso://10.0.10.14:1111", "dba", "dba");

/*			STEP 2			*/
//System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
//                String str = "CLEAR GRAPH <http://test1>";
//                VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
//                vur.exec();    
		//set.clear();

//System.out.println("\nexecute: INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }");
//                str = "INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }";
//                vur = VirtuosoUpdateFactory.create(str, set);
//                vur.exec();                  

/*			STEP 3			*/
/*		Select all data in virtuoso	*/
//System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
//		Query sparql = QueryFactory.create("SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");

/*			STEP 4			*/
//		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
//
//		ResultSet results = vqe.execSelect();
//		while (results.hasNext()) {
//			QuerySolution rs = results.nextSolution();
//		    RDFNode s = rs.get("s");
//		    RDFNode p = rs.get("p");
//		    RDFNode o = rs.get("o");
//		    System.out.println(" { " + s + " " + p + " " + o + " . }");
//		}
		 String  str = "DELETE FROM GRAPH <http://gcm.wfcc.info/> { <http://gcm.wfcc.info/keggcompound/R00021> ?p ?o.} where{<http://gcm.wfcc.info/keggcompound/R00021> ?p ?o.}";
		 VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
         System.out.println("\nexecute: DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }");
                vur = VirtuosoUpdateFactory.create(str, set);
                vur.exec();                  

//System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
//		vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
//                results = vqe.execSelect();
//		while (results.hasNext()) {
//			QuerySolution rs = results.nextSolution();
//		    RDFNode s = rs.get("s");
//		    RDFNode p = rs.get("p");
//		    RDFNode o = rs.get("o");
//		    System.out.println(" { " + s + " " + p + " " + o + " . }");
		//}
   
//		VirtuosoSPARQLExample8 ex=new VirtuosoSPARQLExample8();
//		try {
//			ex.constructDeleteLan(ex.sublist("d:/subjectlist.txt"));
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void constructDeleteLan(List<String> sublist){
		VirtGraph set = new VirtGraph ("Example8","jdbc:virtuoso://10.0.10.14:1111", "dba", "dba");
		for(String s:sublist){
			String str="DELETE FROM GRAPH <http://gcm.wfcc.info/> {<"+s+"> ?p ?o.}"+" where{<"+s+"> ?p ?o.}" ;
			 VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
			 System.out.println("DELETE FROM GRAPH <http://gcm.wfcc.info/> {<"+s+"> ?p ?o.}"+" where{<"+s+"> ?p ?o.}");
             vur = VirtuosoUpdateFactory.create(str, set);
             vur.exec();  
		}
		set.close();
	}
	
	
	public List<String> sublist(String filepath) throws IOException{
		return FileUtils.readLines(new File(filepath));
	}
	
	
}
