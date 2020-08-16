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

		String Query 1 = "SELECT * from product_table where "+				
		"material_no like '%TC%' and "+
		"packing = '100ml' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '1701 99 90') and " +
		"section_id = (select section_id from section_table where section_name = 'CELL STRUCTURE') and " +
		"product_name = Sucrose";
                
                String Query 2 = "SELECT * from product_table where "+				
		"material_no like '%TC%' and "+
		"packing = '500gm' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3504 00 10') and " +
		"section_id = (select section_id from section_table where section_name = 'CELL STRUCTURE') and " +
		"product_name like '%erum%' ";
                
                String Query 3 = "SELECT * from product_table where "+				
		"material_no like '%TC%' and "+
		"packing = '100ml' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3504 00 10') and " +
		"section_id = (select section_id from section_table where section_name = 'CELL STRUCTURE') and " +
		"product_name like '%erum%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'CHEMICALS') and " +
		"product_name like '%CNC%' ";
		
		String Query 3 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'LABORATORY AIDS') and " +
		"product_name like '%CNC%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'MICROBIOLOGY') and " +
		"product_name like '%CNC%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'MOLECUlAR BIOLOGY') and " +
		"product_name like '%CNC%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'PLANT TISSUE CULTURE') and " +
		"product_name like '%CNC%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'RPM & PLATE') and " +
		"product_name like '%CNC%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'MS2760F-25NO' and "+
		"packing = '25nos' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'Viral Transport Media') and " +
		"product_name like '%Transport Medium%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'MS2760G-25NO' and "+
		"packing = '25nos' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'Viral Transport Media') and " +
		"product_name like '%Transport Medium%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'MS2760H-25NO' and "+
		"packing = '25nos' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'Viral Transport Media') and " +
		"product_name like '%Transport Medium%' ";
		
		String Query 2 = "SELECT * from product_table where "+ 
		"material_no = 'PHS001-5VL' and "+
		"packing = '5vl' and "+
		"hscode_id = (select hscode_id from hscode_table where hscode = '3822 00 90') and " +
		"section_id = (select section_id from section_table where section_name = 'PLANT TISSUE CULTURE') and " +
		"product_name like '%CNC%' ";

		
