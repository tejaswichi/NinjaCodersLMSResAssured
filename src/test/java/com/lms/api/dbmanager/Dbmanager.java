package com.lms.api.dbmanager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Dbmanager {

	
	String url = "jdbc:postgresql://localhost:5432/LMS_DB";
    String user = "postgres";
    String password = "Dunu12345";
    Statement stmt;
    Connection con;
    ResultSet rs;
    private String query;
  
    
    public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}	
	
    public void createConnection() throws SQLException {
    	con=DriverManager.getConnection(url,user,password);
    	System.out.println("Connection Created ");    	
    }
	
    public ResultSet executeQuery() throws SQLException {
    	stmt=con.createStatement();
    	rs = stmt.executeQuery(query);
       	System.out.println("Query Executed ");
    	return rs;
    }
    public void closeConnection() throws SQLException {
        con.close();
        System.out.println("Connection Closed ");  
    }
    
    
    public void dbvalidation(String responseBody, String id) throws SQLException{
		
		String query = "select * from tbl_lms_skill_master where skill_id ='" + id  + "'";
		setQuery(query);
		createConnection();
		ResultSet rs = executeQuery();
		
		boolean dbValidated = true;
		
		while (rs.next()) {
			String skillId = rs.getString(1);
			 String skill_name = rs.getString(2);
			String creation_time = rs.getString(3);		        
		     String last_mod_time=rs.getString(4);
			   
		        ArrayList<String> resultlist= new ArrayList<String>();
		        resultlist.add(skillId);
		        resultlist.add(skill_name);		        
		        resultlist.add(creation_time);
		        resultlist.add(last_mod_time);
		        System.out.println("Record from Database : " + resultlist);
		
			
			if (responseBody.contains(skillId)) {
			  dbValidated = true;	
			}
			else {
				dbValidated = false;
				//break;
			}		
		}	
		
		if (dbValidated) {
			System.out.println(" DB validation success ");
		}
		else {
			System.out.println(" DB validation failed !! ");
		}
		
		
		// last step
		closeConnection();
		
	}
  

   
}



