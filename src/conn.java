import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class conn {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Driver ���� �ε�, �� �������� DriverManager�� �ڵ����� ��ü�� ������.

			Connection conn_mysql = DriverManager.getConnection("jdbc:mysql://192.168.0.51:3306/jiradb", "root",
					"illootech");

			Connection conn_ora = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.14:7521:orcl", "jira1",
					"jira1");
			/*
			 * connection�� ������ db�� ��� ���� ����. ������ �ʵ�� ���, ����� �̸�, �н����� ��
			 */
			Statement stmt_mysql = conn_mysql.createStatement();
			Statement stmt_ora = conn_ora.createStatement();
			// connection ��ü�� ���ؼ�, ���ǹ� ����� ���� statement ��ü ����
			String sql = "";

			// ******       ���̺�� List�� ��� 		********************************************************************************
			sql = "show tables";
			// stmt_mysql.executeQuery(sql);
			ResultSet res = stmt_mysql.executeQuery(sql);
			String t1 = "";
			ArrayList table_names = new ArrayList();
			while (res.next()) {
				t1 = res.getString(1);
				table_names.add(t1);
			}
			int tbnum = table_names.size();
			int insert_res = 0;
			for (int i = 0; i < tbnum; i++) {
				
				// ***************** ���̺� ��Ÿ���� ��ȸ ******************************************************************************
				 sql = "select * from "+table_names.get(i);
				 res = stmt_mysql.executeQuery(sql);
				 ResultSetMetaData r = res.getMetaData() ;
				 HashMap<String , String> dataType = new HashMap<String , String>();
				 
				 System.out.println("===================================");
				 for(int j=1;j <=r.getColumnCount(); j++){
					 System.out.println("column size : "+r.getColumnDisplaySize(j));	 
//					 System.out.println(r.getColumnTypeName(j));
					 dataType.put(r.getColumnName(j), r.getColumnTypeName(j));
				 }	 
				 
				 Iterator<String> it = dataType.keySet().iterator();
				
				 System.out.println("table name : "+ table_names.get(i));
				 StringBuilder dsql = new StringBuilder("insert into ");
				 dsql.append(table_names.get(i)+"(");
				 while(it.hasNext()){
					 String key = it.next();
					 System.out.print("key: "+key);
					 System.out.print(", value: "+dataType.get(key)+'\n'); 
					 
					 dsql.append(key+", ");
					 
				 }
				 dsql.deleteCharAt(dsql.length()-2);
				 dsql.append(")");
				 dsql.append(" values (");
				 
				 
				 //*************  ���̺� ���� ��ȸ  *****************************************************************************
				 sql = "select count(*) from "+table_names.get(i) ;
				 res = stmt_mysql.executeQuery(sql);
				 int cnt = 0;
				 while(res.next()){
					 System.out.println("count : "+res.getString("count(*)"));
				 if(res.getInt("count(*)") == 0 ){
					 System.out.println("Data�� �����ϴ�.");
//					 continue;
				 }else{
					 cnt++;
				 }
				 }
				 if(cnt !=0){
				 sql = "select * from "+table_names.get(i) ;
				 res = stmt_mysql.executeQuery(sql);
				 ArrayList dataValue = new ArrayList();
				 Iterator<String> it2 = dataType.keySet().iterator();
				 while(res.next()){
					 while(it2.hasNext()){
						 String key = it2.next();
						 
						 System.out.println("dataType : "+dataType.get(key));
						 if(dataType.get(key) == "DECIMAL" || dataType.get(key) == "TINYINT" || dataType.get(key) == "INT" || dataType.get(key) == "BIGINT"){
							 if( res.getBigDecimal(key).toString().equals("")  ||  res.getBigDecimal(key) == null){
								 dataValue.add("");
								 System.out.println("res.getBigDecimal(key) == null");
							 }else{
								 dataValue.add( res.getBigDecimal(key));
							 }
						 }else if(dataType.get(key) == "VARCHAR" || dataType.get(key) == "CHAR"  ){
//							 if( res.getString(key).isEmpty() || res.getString(key) == null){
//								 dataValue.add(" ");
//								 System.out.println("res.getString(key) == null ==>("+res.getString(key).isEmpty()+")");
//							 }else{
								 dataValue.add( "\'"+res.getString(key)+"\'");
//							 }
							 
						 }else if(dataType.get(key) == "DATETIME" ){
							 if( res.getDate(key).equals("") ||  res.getDate(key) == null ){
								 dataValue.add("");
								 System.out.println("res.getDate(key) == null ");
							 }else{
								 dataValue.add( res.getDate(key));
							 }
							 
						 }else if(dataType.get(key) == "BLOB" ){
							 if( res.getBlob(key).length() == 0 || res.getBlob(key) == null){
								 dataValue.add("");
								 System.out.println("res.getBlob(key) == null");
							 }else{
								 dataValue.add( res.getBlob(key));
							 }
							 
						 }
					 }
				 }
				System.out.println("////////////////////////////		��ȸ��		//////////////////////////////////////"); 
				
				//��ȸ�� ArrayList�� ���
				for(int k=0;k<dataValue.size();k++){
				 System.out.println(dataValue.get(k));
				 dsql.append(dataValue.get(k)+", ");
				}
				dsql.deleteCharAt(dsql.length()-2);
				dsql.append(")");
				System.out.println("************** ������ ������  ******************");
				System.out.println(dsql);
				 
				 //*************  ���̺� ���� insert  ***************************************************************************** 
				if(stmt_ora.executeUpdate(dsql.toString()) != 0){
					insert_res++; 
				}
				 }
			}// ���̺� name ���� �ݺ��� ��
			System.out.println("************************************");
			System.out.println("table count : " + tbnum);
			System.out.println("insert count : "+ insert_res);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}

                                    
