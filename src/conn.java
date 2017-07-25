import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class conn {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Driver ���� �ε�, �� �������� DriverManager�� �ڵ����� ��ü�� ������.
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.0.51:3306/jiradb", "root", "illootech");
			
			Connection conn_ora = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.24:1521:xe", "SCOTT", "TIGER");
			/* connection�� ������ db�� ��� ���� ����.
             * ������ �ʵ�� ���, ����� �̸�, �н����� ��
			 */
			Statement statement = connection.createStatement();
			Statement stmt2 = conn_ora.createStatement();
			// connection ��ü�� ���ؼ�, ���ǹ� ����� ���� statement ��ü ����
			String sql = "";
			sql = "select	empno,	ename,	job, mgr,	sal,comm,	deptno	from emp";
//			String sql = "INSERT INTO test_table VALUES(1, 'title!', 'JDBC is so difficult. :(');";
			stmt2.executeUpdate(sql);
			
			ResultSet res = stmt2.executeQuery(sql);
			
			ResultSetMetaData r = res.getMetaData() ; 
			
			while(res.next()) {
				int ID = Integer.parseInt(res.getString("EMPNO"));
				String AUTHOR= res.getString("ENAME");
				String worklogbody = res.getString("JOB");
				
				System.out.println(ID + " " + AUTHOR + " " + worklogbody);
				System.out.println(r.getColumnType(1));
			}
			 
//			sql = "SELECT * FROM worklog";
//			ResultSet resultSet = statement.executeQuery(sql);
			
//			while(resultSet.next()) {
//				int ID = Integer.parseInt(resultSet.getString("ID"));
//				String AUTHOR= resultSet.getString("AUTHOR");
//				String worklogbody = resultSet.getString("worklogbody");
//				
//				System.out.println(ID + " " + AUTHOR + " " + worklogbody);
//			}
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
	}


}
