<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<html>
<head>
<title>display by queries</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<style>
body{
	margin: 3%;
	background: #f8f8f9;
}
.table{
	background: #fff;
}
td {
	padding: 10px;
}
th{
	text-align: center;
}
</style>
</head>
<body>
	<h2>Data from the table</h2>
	<br>
	<%
		try {
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";			// initializing connection to the mysql database.
		Connection connection = null;
		Statement statement, statementSect, statementHscode = null;
		ResultSet rs, rs3, rs2 = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "root");
		statement = connection.createStatement();
		statementSect = connection.createStatement();
		statementHscode = connection.createStatement();
		
		String QueryString1 = "SELECT * from product_table where packing = '20plts'";
		// creating query strings
		String QueryString2 = "SELECT * from product_table where "+						// 1 for single input type query
		"packing = '20plts' and " +									// 2, 3, 4, 5 for 2 inputs type query
		"Consumer_rate = '1285'";
		
		String QueryString3 = "SELECT * from product_table where "+
		"packing = '6x500ml' and " +
		"product_name like '%bicarbonate%'";
		
		String QueryString4 = "SELECT * from product_table where "+						// 1 for single input type query
		"packing = '50plts' and " +									// 2, 3, 4, 5 for 2 inputs type query
		"Consumer_rate = '3310' and product_name like '%Soybean Casein Digest Agar Plate%' ";
				
		String QueryString5 = "SELECT * from product_table where "+
		"packing = '50plts' and " +	
		"section_id = (select section_id from section_table where section_name = 'RPM & PLATE') and " +
		"product_name like '%EMB Agar Plate%'";
		
		String QueryString6 = "SELECT * from product_table where "+						// 6, 7, 8, 9, 10, 11 for 3 input type query
		"packing = '5lt' and " +
		"product_name like '%Vitamins%'";
		
		String QueryString7 = "SELECT * from product_table where "+
		"packing = '100plt' and " +
		"hscode_id = (select hscode_id from hscode_table where hscode = '3821 00 00') and " +
		"product_name like '%Agar Plate%'";
		
		String QueryString8 = "SELECT * from product_table where "+
		"packing = '1 no' and " +
		"material_no = 'LA211-1NO' and " +
		"section_id = (select section_id from section_table where section_name = 'LABORATORY AIDS') and " +
		"product_name like '%Test Tube%'";
		
		String QueryString9 = "SELECT * from product_table where "+
		"packing = '50plts' and " +
		"hscode_id = (select hscode_id from hscode_table where hscode = '3821 00 00') and " +
		"product_name like '%Soybean%'";
		
		String QueryString10 = "SELECT * from product_table where "+
		"packing = '50plts' and " +
		"section_id = (select section_id from section_table where section_name = 'RPM & PLATE') and " +
		"product_name like '%Soybean%'";
		
		
		String QueryString11 = "SELECT * from product_table where "+						// 12 for 5 input type query
		"packing = '5vl' and "+
		"material_no = 'PHS001-5VL' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'PLANT TISSUE CULTURE') and " +
		"product_name like '%CNC%' ";
				
		String QueryString12 = "SELECT * from product_table where "+						// 12 for 5 input type query
				"packing = '5vl' and "+
				"material_no = 'PHS001-5VL' and "+
				"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
				"section_id = (select section_id from section_table where section_name = 'PLANT TISSUE CULTURE') and " +
				"product_name like '%CNC%' and"+ "consumer_rate='2255' ";
		
						
		String[] QueryArr = {QueryString1, QueryString2, QueryString3, QueryString4, QueryString5, QueryString6, 
				QueryString7, QueryString8, QueryString9, QueryString10, QueryString11, QueryString12};
		int qno = 8;
		rs = statement.executeQuery(QueryArr[qno-1]);								// qno reperesent the current executing queryString in the array QueryArr
	%>	
		fetching result based on query :
		<br>
		<h4><%=QueryArr[qno-1]%></h4>
		<%
		if (!rs.isBeforeFirst() ) {										// checking if 0 rows are not returned from database
			out.println("No results found!");
		}else{
		%>
	<br>
	<br>
	<table class="table table-sm table-bordered">									<!-- creating table to disply rows -->
		<tr>
			<th> S. No.</th>
			<th> Material No</th>
			<th> Packing</th>
			<th> Product Name</th>
			<th> Consumer rate</th>
			<th> GST rate</th>
			<th> HS code</th>
			<th> Section </th>
		</tr>		
		
		<%
			int i=1;
			while (rs.next()) {
		%>
		<tr>
			<td><%=i %></td>
			<td><%=rs.getString(2)%></td>
			<td><%=rs.getString(3)%></td>
			<td><%=rs.getString(4)%></td>
			<td><%=rs.getString(5)%></td>
			<td><%=rs.getInt(6)%></td>
			<%
			String QueryStringHscode = "SELECT hscode from hscode_table where hscode_id = " + rs.getInt(7) + ";";
			rs3 = statementHscode.executeQuery(QueryStringHscode);
			while (rs3.next()) {
			%>
				<td><%=rs3.getString(1)%></td>
			<%
			}
			%>
			<%
			String QueryStringSect = "SELECT section_name from section_table where section_id = " + rs.getInt(8) + ";";
			rs2 = statementSect.executeQuery(QueryStringSect);
			while (rs2.next()) {
			%>
				<td><%=rs2.getString(1)%></td>
			<%
			}
			%>
		</tr>
		<%
			i++;	 
			rs2.close();
			rs3.close();
			}	
		}
		%>
	</table>
		 <%	
		rs.close();											// finally closing all db variables and db connection
		statement.close();
		statementSect.close();
		statementHscode.close();
		connection.close();
	} catch (Exception ex) {
		out.println("Unable to connect to database.");
		ex.printStackTrace();
	}
		%>
</body>
</html>