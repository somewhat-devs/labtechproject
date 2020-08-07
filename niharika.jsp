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
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";	// initializing connection to the mysql database.
		Connection connection = null;					// initializing variables
		Statement statement, statementSect, statementHscode = null;
		ResultSet rs, rs2, rs3= null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();	// defining class name for jdbc driver
		connection = DriverManager.getConnection(connectionURL, "root", "root");	// defining connection parameters, ...getConnection(connection string, username, password)
		statement = connection.createStatement(); 
		statementSect = connection.createStatement();
		statementHscode = connection.createStatement();
										
											// defining queries to run
		String QueryString = "SELECT * from product_table where packing = '20plts'";
		rs = statement.executeQuery(QueryString);
	%>
 <table border=2>								        <!-- creating table for display query results -->
		<%
		int i=1;
			while (rs.next()) {
		%>
		<tr>
			<td><%=i %></td>
			<td><%=rs.getInt(1)%></td>					<!-- retrieving values of columns of each row, based on index of columns in the db -->
			<td><%=rs.getString(2)%></td>					<!-- 1 represent product_id, 2 represent material_no, and so on. -->
			<td><%=rs.getString(3)%></td>
			<td><%=rs.getString(4)%></td>
			<td><%=rs.getInt(5)%></td>
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
		
		%>
	</table>
		 <%	
		rs.close();	               // closing all database conneciton variables
		statement.close();
		connection.close();
		} catch (Exception ex) {                 // managing db connection errors
		out.println("Unable to connect to database.");
		ex.printStackTrace();
		}
		%>
	</table>
</body>
</html>