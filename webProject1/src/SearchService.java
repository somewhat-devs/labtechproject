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
		
			String QueryString = "SELECT * from product_table where ", custrateqr = "consumer_rate <> 'On Request' and cast(consumer_rate as unsigned) ";
			String materialno = request.getParameter("materialno").trim();				// get form parameters from request
			String productname = request.getParameter("productname").trim();
			String hscode = request.getParameter("hscode").trim();
			String section = request.getParameter("section").trim();					// check which attributes are provided by user, based on that query is written and executed.
			String custrate = request.getParameter("custrate").trim();	
			int qrfor1 = 0 ;		
			
			if (!materialno.equals("")) {
				if (qrfor1 == 0) 	qrfor1 = 1;
				else 				QueryString += "and ";
				
				QueryString += "material_no like '%"+ materialno +"%' ";
			}
			if (!productname.equals("")){
				if (qrfor1 == 0) 	qrfor1 = 1;
				else 				QueryString += "and ";
				
				QueryString += "product_name like '%"+ productname +"%' ";
			}
			if (!hscode.equals("")){
				if (qrfor1 == 0) 	qrfor1 = 1;
				else 				QueryString += "and ";
				
				QueryString += "hscode_id = (select hscode_id from hscode_table where hscode = '"+ hscode +"')";
			}
			if (!section.equals("")){
				if (qrfor1 == 0) 	qrfor1 = 1;
				else 				QueryString += "and ";
				
				QueryString += "section_id = (select section_id from section_table where section_name = '"+ section +"') ";
			}
			if (!custrate.equals("none")) {
				
				switch (custrate) {
				case "1":	custrateqr += "< 500 ";						break;
				case "2":	custrateqr += "between 500 and 1000 ";		break;
				case "3":	custrateqr += "between 1000 and 5000 ";		break;
				case "4":	custrateqr += "between 5000 and 10000 ";	break;
				case "5":	custrateqr += "between 10000 and 25000 ";	break;
				case "6":	custrateqr += "between 25000 and 100000 ";	break;
				case "7":	custrateqr += "> 100000 ";					break;
				}

				if (qrfor1 == 0) 	qrfor1 = 1;
				else 				QueryString += "and ";
				
				QueryString += custrateqr;
			}
			QueryString += ";";
			
//			log("- "+materialno+" - "+packing+" - "+productname+" - "+hscode+" - "+section+" - "+custrateqr+" - "+QueryString);

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

	}

}
