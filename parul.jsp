<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<html>
<head>
<title>display by queries</title>
<link rel="stylesheet" type="text/css">
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
		String connectionURL = "jdbc:mysql://localhost:3307/labtech";			// initializing connection to the mysql database.
		Connection connection = null;
		Statement statement, statementSect, statementHscode = null;
		ResultSet rs, rs3, rs2 = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "root@123");
		statement = connection.createStatement();
		statementSect = connection.createStatement();
		statementHscode = connection.createStatement();
