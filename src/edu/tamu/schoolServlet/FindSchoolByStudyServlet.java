package edu.tamu.schoolServlet;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;



@WebServlet("/FindSchoolByStudyServlet")
public class FindSchoolByStudyServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	final String JDBC_DRIVER="com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://kona.education.tamu.edu:3306/studymanagement";
	final String user = "simstudent";
	final String password = "simstudent";
	
	
    public FindSchoolByStudyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	
    
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		String studyName = request.getParameter("studyName");
    	
		try {
			Class.forName(JDBC_DRIVER);
			Connection conn = (Connection) DriverManager.getConnection(DB_URL,user,password);
			
			String sql = "select study_key from study where study_name=?";
			PreparedStatement statement = (PreparedStatement) conn.prepareStatement(sql);
			
			statement.setString(1, studyName);
			ResultSet rs = statement.executeQuery();
			
			//return json data:
			
			
			String studyKey = "";
			if(rs.next()) {
				studyKey = rs.getString("study_key");
			}
			rs.close();
			
			String sql1 = "select schoolname from school where study_key=?";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql1);
			ps.setString(1, studyKey);
			ResultSet rs1 = ps.executeQuery();
			
			JSONArray jarray = new JSONArray();
			while(rs1.next()) {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("schoolname", rs1.getString("schoolname"));
				jarray.add(jsonobj);
			}
			
			if(ps != null) {
				ps.close();
			}
			
			if(rs1 != null) {
				rs1.close();
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jarray.toString());
			
			// then close the database connection
			if(conn != null) {
				conn.close();
			}
			if(statement != null) {
				statement.close();
			}
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException | ClassNotFoundException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
