package servlet.menu;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.*;
// import net.sf.json.JSONObject;

/**
 * Servlet implementation class UpLoadImg
 */
@WebServlet("/api/menu/uploadImg")
@MultipartConfig
public class UpLoadImg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpLoadImg() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
    	response.setHeader("Allow", "POST");
    	response.sendError(405);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* 设置响应头部 */
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		/* 读取请求内容 */
		request.setCharacterEncoding("UTF-8");
		String mealId = request.getParameter("mealId");
		
		Part part=request.getPart("file");
		String realFileName = part.getSubmittedFileName();
		String type = realFileName.substring(realFileName.lastIndexOf("."));			
		String fileName = UUID.randomUUID().toString() + type;
		String path=getServletContext().getRealPath("/");
		path += ".." + File.separator + "Attachment" + File.separator + "MenuImage" + File.separator + fileName;
		part.write(path);
		
		Connection conn = null;
		Statement stmt = null;
		try {
			/* 连接数据库 */
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://106.13.201.225:3306/coffee?serverTimezone=GMT", "coffee", "TklRpGi1");
			stmt = conn.createStatement();
			
			/* 构建SQL语句 */
			String sql = "UPDATE meal SET pictureUrl=? WHERE mealId=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, path);
			ps.setString(2, mealId);
			
			
			/* 执行SQL语句 */
			ps.executeUpdate();
			
			/* 处理执行结果 */
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("success", true);
			responseJson.addProperty("msg", "上传成功");
			out.println(responseJson);
		} catch (SQLException e) {
			e.printStackTrace();
			/* 处理执行结果 */
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("success",false);
			responseJson.addProperty("msg", e.getMessage());
			out.print(responseJson);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.fillInStackTrace();
		} finally {
			/* 无论如何关闭连接 */
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out.close();
	}

}
