
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/SearchService")
public class SearchService extends HttpServlet {
	private static final long serialVersionUID = 1L;								// declaring database connection variables
	private static String connectionURL = "jdbc:mysql://localhost:3306/labtech";
	private static Connection connection = null;
	private static Statement statement, statementSect, statementHscode = null;
	private static ResultSet rs, rs3, rs2 = null;

	public SearchService() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();				// initializing database connection and statements
			connection = DriverManager.getConnection(connectionURL, "root", "");	
			statement = connection.createStatement();
			statementSect = connection.createStatement();
			statementHscode = connection.createStatement();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		String materialno = request.getParameter("materialno").trim();				// get form parameters from request
		String packing = request.getParameter("packing").trim();
		String productname = request.getParameter("productname").trim();
		String hscode = request.getParameter("hscode").trim();
		String section = request.getParameter("section").trim();					// check which attributes are provided by user, based on that query is written and executed.
		
		try {
		if (!materialno.equals("") && 
			packing.equals("none") && 
			productname.equals("") && 
			hscode.equals("none") && 
			section.equals("none")) 
				materialNo(request, response, materialno);
		
		if (materialno.equals("") && 
			!packing.equals("none") && 
			!productname.equals("") && 
			hscode.equals("none") && 
			section.equals("none")) 						// 25x1 no , vented cap
				packing_productname(request, response, packing, productname);
		
		if (materialno.equals("") && 
			!packing.equals("none") && 
			!productname.equals("") && 
			!hscode.equals("none") && 
			!section.equals("none"))
				packing_productname_hscode_section(request, response, packing, productname, hscode, section);
		
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

//		log("- "+materialno+" - "+packing+" - "+productname+" - "+hscode+" - "+section+" - "+action);
	}

	private void materialNo(HttpServletRequest request, HttpServletResponse response, String materialno)
			throws SQLException, IOException, ServletException {
		
		List<List<String>> responseArr = new ArrayList<List<String>>();
		
		try {
			String QueryString = "SELECT * from product_table where material_no like '%"+ materialno +"%';";		
			
			rs = statement.executeQuery(QueryString);							// executing query and adding data to list 
			if (rs.isBeforeFirst()) {
				while(rs.next()) {
					List<String> temp = new ArrayList<String>();
					for (int i=2; i<=6; i++) {	temp.add(rs.getString(i));	}
					
					String QueryStringHscode = "SELECT hscode from hscode_table where hscode_id = " + rs.getInt(7) + ";";
					rs3 = statementHscode.executeQuery(QueryStringHscode);
					while (rs3.next()) { temp.add(rs3.getString(1)); }
					rs3.close(); 
					
					String QueryStringSect = "SELECT section_name from section_table where section_id = " + rs.getInt(8) + ";";
					rs2 = statementSect.executeQuery(QueryStringSect);
					while (rs2.next()) { temp.add(rs2.getString(1)); }
					rs2.close(); 
					
					responseArr.add(temp);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			statementHscode.close(); statementSect.close(); statement.close();
			connection.close();
		}
		
		String resjson = new Gson().toJson(responseArr);				// sending respond back to client page displayByField after converting it to json
		response.setContentType("application/json");
		response.getWriter().write(resjson);
	}

	private void packing_productname(HttpServletRequest request, HttpServletResponse response, String packing, String productname)
			throws SQLException, IOException, ServletException {
		
		List<List<String>> responseArr = new ArrayList<List<String>>();
		
		try {
			String QueryString = "SELECT * from product_table where packing = '"+ packing +"' and product_name like '%"+ productname +"%';";		
			rs = statement.executeQuery(QueryString);							// executing query and adding data to list 
			if (rs.isBeforeFirst()) {
				while(rs.next()) {
					List<String> temp = new ArrayList<String>();
					for (int i=2; i<=6; i++) {	temp.add(rs.getString(i));	}
					
					String QueryStringHscode = "SELECT hscode from hscode_table where hscode_id = " + rs.getInt(7) + ";";
					rs3 = statementHscode.executeQuery(QueryStringHscode);
					while (rs3.next()) { temp.add(rs3.getString(1)); }
					rs3.close(); 
					
					String QueryStringSect = "SELECT section_name from section_table where section_id = " + rs.getInt(8) + ";";
					rs2 = statementSect.executeQuery(QueryStringSect);
					while (rs2.next()) { temp.add(rs2.getString(1)); }
					rs2.close(); 
					
					responseArr.add(temp);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			statementHscode.close(); statementSect.close(); statement.close();
			connection.close();			
		}
		String resjson = new Gson().toJson(responseArr);				// sending respond back to client page displayByField after converting it to json
		response.setContentType("application/json");
		response.getWriter().write(resjson);
	}

	private void packing_productname_hscode_section(HttpServletRequest request, HttpServletResponse response, String packing, String productname, String hscode, String section)
			throws SQLException, IOException, ServletException {

		List<List<String>> responseArr = new ArrayList<List<String>>();
		
		try {
			String QueryString = "SELECT * from product_table where packing = '"+ packing +"' and product_name like '%"+ productname +"%' and "+
								 "hscode_id = (select hscode_id from hscode_table where hscode = '"+ hscode +"') and " + 
								 "section_id = (select section_id from section_table where section_name = '"+ section +"');";		
			rs = statement.executeQuery(QueryString);							// executing query and adding data to list 
			if (rs.isBeforeFirst()) {
				while(rs.next()) {
					List<String> temp = new ArrayList<String>();
					for (int i=2; i<=6; i++) {	temp.add(rs.getString(i));	}
					temp.add(hscode); 
					temp.add(section);
					
					responseArr.add(temp);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			statement.close();
			connection.close();			
		}
		String resjson = new Gson().toJson(responseArr);				// sending respond back to client page displayByField after converting it to json
		response.setContentType("application/json");
		response.getWriter().write(resjson);
	}

} 


/*
 * import java.io.IOException; import java.sql.SQLException; import
 * java.util.ArrayList; import java.util.List; import java.sql.*;
 * 
 * import javax.servlet.ServletException; import
 * javax.servlet.annotation.WebServlet; import javax.servlet.http.HttpServlet;
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse;
 * 
 * import com.google.gson.Gson;
 * 
 * @WebServlet("/SearchService") public class SearchService extends HttpServlet
 * { private static final long serialVersionUID = 1L;
 * 
 * public SearchService() { super(); }
 * 
 * protected void doPost(HttpServletRequest request, HttpServletResponse
 * response) throws ServletException, IOException { doGet(request, response); }
 * 
 * protected void doGet(HttpServletRequest request, HttpServletResponse
 * response) throws ServletException, IOException {
 * 
 * String action = ""; // get form parameters from request String packing =
 * request.getParameter("packing").trim(); String hscode =
 * request.getParameter("hscode").trim(); // check which attributes are provided
 * by user, based on that query is written and executed.
 * 
 * if (!packing.equals("") && hscode.equals("")) action = "pk";
 * 
 * if (packing.equals("") && !hscode.equals("")) action = "hs";
 * 
 * if (!packing.equals("") && !hscode.equals("")) action = "pk+hs";
 * 
 * try { switch (action) { // linking queries based on value of action case
 * "pk": packing(request, response, packing); break; case "hs": hscode(request,
 * response, hscode); break; case "pk+hs": packing_hscode(request, response,
 * packing, hscode); break; }
 * 
 * } catch (Exception ex) { throw new ServletException(ex); }
 * 
 * }
 * 
 * private void packing(HttpServletRequest request, HttpServletResponse
 * response, String packing) throws SQLException, IOException, ServletException
 * {
 * 
 * List<String> responseArr = new ArrayList<String>();
 * 
 * String res = "packing was given by user."; responseArr.add(res);
 * 
 * String resjson = new Gson().toJson(responseArr); // sending respond back to
 * client page displayByField after converting it to json
 * response.setContentType("application/json");
 * response.getWriter().write(resjson); }
 * 
 * private void hscode(HttpServletRequest request, HttpServletResponse response,
 * String hscode) throws SQLException, IOException, ServletException {
 * 
 * List<String> responseArr = new ArrayList<String>();
 * 
 * String res = "hscode was given by user."; responseArr.add(res);
 * 
 * String resjson = new Gson().toJson(responseArr); // sending respond back to
 * client page displayByField after converting it to json
 * response.setContentType("application/json");
 * response.getWriter().write(resjson); }
 * 
 * private void packing_hscode(HttpServletRequest request, HttpServletResponse
 * response, String packing, String hscode) throws SQLException, IOException,
 * ServletException {
 * 
 * List<String> responseArr = new ArrayList<String>();
 * 
 * String res = "packing was given by user."; responseArr.add(res); res =
 * "hscode was given by user."; responseArr.add(res);
 * 
 * String resjson = new Gson().toJson(responseArr); // sending respond back to
 * client page displayByField after converting it to json
 * response.setContentType("application/json");
 * response.getWriter().write(resjson); }
 * 
 * }
 */