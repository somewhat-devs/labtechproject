<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<html>
<head>
<title>display data from the table using jsp</title>
<style>
	td{ padding: 10px; }
</style>
</head>
<body>
	<h2>Data from the table</h2>
	<h5>Query is to display all products based on user value</h5>
	<br>		<!-- text fields for user input which will be used as attributes for searching. -->
	Material No : 
	<input type="text" name="mateNo">
	<br><br>
	Product name : 
	<input type="text" name="prodName">
	<br><br>
	Packing : 
	<input type="text" name="packing">
	<br><br>
	HS code : 
	<input type="text" name="hsCode">
	<br><br>
	Section : 
	<input type="text" name="section">
	<br><br>
	<%
		try {
		String connectionURL = "jdbc:mysql://localhost:3306/test";	<!-- initializing connection to the mysql database. -->
		Connection connection = null;					<!-- initializing variables -->
		Statement statement, statementSect = null;
		ResultSet rs, rs2 = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();	<!-- defining class name for jdbc driver -->
		connection = DriverManager.getConnection(connectionURL, "root", "");	<!-- defining connection parameters, ...getConnection(connection string, username, password) -->
		statement = connection.createStatement(); statementSect = connection.createStatement();
										
											<!-- defining queries to run -->
		String QueryString = "SELECT * from product_table where "+						<!-- query 1: to get products having hscode = 7102 29 90  -->
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";		<!-- using nested query to retrieve the id of the above hscode from hscode_table -->
		
		String QueryString2 = "SELECT * from product_table where "+						<!-- query 2: to get products having particular hscode and section -->
				"section_id = " +									<!-- retreive section_id and hscode_id from section_table and hscode_table using nested queries -->
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";
		
		String QueryString3 = "SELECT * from product_table where "+						<!-- query 3: to get products having particular hscode, section and packing -->
				"section_id = "+									<!-- packing column is already a part of product_table, we can use direct comparision for packing value -->
				"packing = "+										<!-- retreive section_id and hscode_id from section_table and hscode_table using nested queries --> 
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";
		
		String QueryString4 = "SELECT * from product_table where "+						<!-- query 4: to get products having particular hscode, section, name and packing -->
				"section_id = "+									<!-- packing and product_name column is already a part of product_table, we can use direct comparision for packing value and name using 'LIKE' operator -->
				"packing = "+
				"product_name = "+
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";		<!-- retreive section_id and hscode_id from section_table and hscode_table using nested queries --> 
		
		rs = statement.executeQuery(QueryString);
	%>
	<table border=2>								<!-- creating table for display query results -->
		<%
			while (rs.next()) {
		%>
		<tr>
			<td><%=rs.getInt(1)%></td>					<!-- retrieving values of columns of each row, based on index of columns in the db -->
			<td><%=rs.getString(2)%></td>					<!-- 1 represent product_id, 2 represent material_no, and so on. -->
			<td><%=rs.getString(3)%></td>
			<td><%=rs.getString(4)%></td>
			<td><%=rs.getInt(5)%></td>
			<td><%=rs.getInt(6)%></td>
			<td>7102 29 90</td>
			<% 								
			String QueryStringSect = "SELECT section_name from section_table where section_id = "+rs.getInt(8)+";";
			rs2 = statementSect.executeQuery(QueryStringSect);
			while(rs2.next()){
			%>
			<td><%=rs2.getString(1) %></td> 
			<% } %>
		</tr>
		<%
			}
		%>
		<%
		rs2.close(); rs.close();
		statement.close();
		connection.close();
		} catch (Exception ex) {
		out.println("Unable to connect to database.");
		ex.printStackTrace();
		}
		%>
	</table>
</body>
</html>
