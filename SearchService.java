import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private Object crate;

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
		
			String action = "", QueryString = "";
			String materialno = request.getParameter("materialno").trim();				// get form parameters from request
			String packing = request.getParameter("packing").trim();
			String productname = request.getParameter("productname").trim();
			String hscode = request.getParameter("hscode").trim();
			String section = request.getParameter("section").trim();		// check which attributes are provided by user, based on that query is written and executed.
			String customerrate = request.getParameter("crate").trim();
			
			if (!materialno.equals(""))
				action += "mn";
			if (!packing.equals(""))
				action += "pk";
			if (!productname.equals(""))
				action += "pn";
			if (!hscode.equals(""))
				action += "hs";
			if (!section.equals(""))
				action += "st";
			if (!crate.equals(""))
				action += "cr";

			switch (action) {
			//niharika
			case "pk":
				QueryString = "SELECT * from product_table where packing = '"+ packing ;
				break;
							
			case "pkcr":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and customer_rate= '"+crate;
				break;
			
			case "pkpn":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and product_name like '%"+ productname +"%';";		
				break;
			
			case "pkcrpn":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and customer_rate= '"+ crate +"'and product_name like '%"+ productname +"%';";		
				break;
				
			case "pkpnst":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and section_id = (select section_id from section_table where section_name = '"+ section +"') and product_name like '%"+ productname +"%';";		
				break;
			
			case "pkst":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and section_id = (select section_id from section_table where section_name = '"+ section +"') and customer_rate= '\"+ crate +\"';";		
				break;
			
			case "mnpkpnst":
				QueryString = "SELECT * from product_table where material_no like '%"+ materialno +"%' and packing = '"+ packing +"' and product_name like '%"+ productname +"%' and and section_id = (select section_id from section_table where section_name = '"+ section +"');";		
				break;
			
			case "mnpnhsst":
				QueryString = "SELECT * from product_table where material_no like '%"+ materialno +"%' and hscode_id = (select hscode_id from hscode_table where hscode = '"+ hscode +"') and section_id = (select section_id from section_table where section_name = '"+ section +"') and product_name like '%"+ productname +"%';";		
				break;
			
			case "pkmnpnst":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and material_no like '%"+ materialno +"%' and section_id = (select section_id from section_table where section_name = '"+ section +"') and product_name like '%"+ productname +"%';";
				break;
			
			case "pkpnhs":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and hscode_id = (select hscode_id from hscode_table where hscode = '"+ hscode +"') and product_name like '%"+ productname +"%';";		
				break;
			
			case "pnhscrpkst":
				QueryString = "SELECT * from product_table where hscode_id = (select hscode_id from hscode_table where hscode = '"+ hscode +"') and packing = '"+ packing +"' and section_id = (select section_id from section_table where section_name = '"+ section +"') and customer_rate= '"+crate +"' and product_name like '%"+ productname +"%';";		
				break;
			
			case "pkmnpkpnhsst":
				QueryString = "SELECT * from product_table where packing = '"+ packing +"' and material_no like '%"+ materialno +"%' and packing = '"+ packing +"' and hscode_id = (select hscode_id from hscode_table where hscode = '"+ hscode +"') and section_id = (select section_id from section_table where section_name = '"+ section +"') and product_name like '%"+ productname +"%';";	
				break;
			}

			List<List<String>> responseArr = new ArrayList<List<String>>();
			
			rs = statement.executeQuery(QueryString);
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
			} else {
				responseArr.add(Arrays.asList("none"));
			}
			
			statementHscode.close(); statementSect.close(); statement.close();
			connection.close();
			
			String resjson = new Gson().toJson(responseArr);				// sending respond back to client page displayByField after converting it to json
			response.setContentType("application/json");
			response.getWriter().write(resjson);
			
		} catch (SQLException ex) { 
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		log("- "+materialno+" - "+packing+" - "+productname+" - "+hscode+" - "+section+" - "+action);
	}

}