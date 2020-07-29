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
	<br>
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
		String connectionURL = "jdbc:mysql://localhost:3306/test";
		Connection connection = null;
		Statement statement, statementSect = null;
		ResultSet rs, rs2 = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "");
		statement = connection.createStatement(); statementSect = connection.createStatement();
		String QueryString = "SELECT * from product_table where "+
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";
		
		String QueryString2 = "SELECT * from product_table where "+
				"section = " +
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";
		
		String QueryString3 = "SELECT * from product_table where "+
				"section = "+
				"packing = "+
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";
		
		String QueryString4 = "SELECT * from product_table where "+
				"section = "+
				"packing = "+
				"product_name = "+
				"hscode_id = (select hscode_id from hscode_table where hscode = '7102 29 90')";
		
		
		
		rs = statement.executeQuery(QueryString);
	%>
	<table border=2>
		<%
			while (rs.next()) {
		%>
		<tr>
			<td><%=rs.getInt(1)%></td>
			<td><%=rs.getString(2)%></td>
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