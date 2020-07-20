package servlet.menu;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class getUserList
 */
@WebServlet("/api/menu/getAllMeal")
public class GetAllMeal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAllMeal() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://106.13.201.225:3306/coffee?useSSL=false&serverTimezone=GMT","coffee","TklRpGi1");
			Statement stmt = conn.createStatement();
			String sql = "select * from meal";
			ResultSet rs = stmt.executeQuery(sql);
			JSONArray jsonarray = new JSONArray();
			JSONObject jsonobj = new JSONObject();
			JSONObject jsonobj2 = new JSONObject();
			while(rs.next()){
				jsonobj.put("mealId",rs.getString("mealId"));
				jsonobj.put("price",rs.getObject("price")==null?"":rs.getDouble("price"));
				jsonobj.put("amount",rs.getObject("amount")==null?"":rs.getInt("amount"));
				jsonobj.put("menuId",rs.getString("menuId")==null?"":rs.getString("menuId"));
				jsonobj.put("type",rs.getString("type")==null?"":rs.getString("type"));
				jsonobj.put("mealName",rs.getString("mealName")==null?"":rs.getString("mealName"));
				jsonobj.put("mealDetail",rs.getString("mealDetail"));
				jsonarray.add(jsonobj);
			}
			jsonobj2.put("success",true);
			jsonobj2.put("data",jsonarray);
			out = response.getWriter();
			out.println(jsonobj2);
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
