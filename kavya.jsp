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
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";			// initializing connection to the mysql database.
		Connection connection = null;
		Statement statement, statementSect, statementHscode = null;
		ResultSet rs, rs3, rs2 = null;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "");
		statement = connection.createStatement();
		statementSect = connection.createStatement();
		statementHscode = connection.createStatement();
		
		
		
String QueryString1 ="SELECT * from product_table where "+
                     "product_name = 'N,N-Dimethyldodecylamine' and "+ 
                     "hscode_id = (select hscode_id from hscode_table where hscode='2921 19 90')";   

String QueryString2 ="SELECT * from product_table where "+
                     "section_id = (select section_id from section_table where section_name = 'MOLECULAR BIOLOGY') and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='3923 90 90')";   

String QueryString3 ="SELECT * from product_table where "+
                     "packing = '1 kit for 10 subcultures'and "+ 
                     "hscode_id = (select hscode_id from hscode_table where hscode='3821 00 00')";

String QueryString4 ="SELECT * from product_table where "+
                     "material_no ='LA006-50NO' and "+ 
                     "hscode_id = (select hscode_id from hscode_table where hscode='7326 90 99')";

String QueryString5 ="SELECT * from product_table where "+
                     "section_id =(select section_id from section_table where section_name = 'Viral Transport Media') and "+
                     "packing ='25nos' and "+   
                     "hscode_id = (select hscode_id from hscode_table where hscode='3821 00 00')";

String QueryString6 ="SELECT * from product_table where "+
                     "section_id =(select section_id from section_table where section_name = 'CELL CULTURE') and "+
                     "material_no ='100gm' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='2917 19 90')";

String QueryString7 ="SELECT * from product_table where "+
                     "section_id =(select section_id from section_table where section_name = 'MICROBIOLOGY') and "+
                     "product_name like'%Casitose, Type-II%' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='3504 00 10')";

String QueryString8 ="SELECT * from product_table where "+
                     "packing ='50c' and "+
                     "material_no ='9610-1100-50C' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='4823 20 00')"; 

String QueryString9 ="SELECT * from product_table where "+
                     "packing ='10gm'and "+
                     "product_name like'%Pyridoxine hydrochloride%' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='2936 25 00')";

String QueryString10 = "SELECT * from product_table where "+
                     "material_no ='10slants' and "+
                     "product_name like '%Gelatin Agar Slant%' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='3821 00 00')";

String QueryString11 ="SELECT * from product_table where "+
                     "section_id =(select section_id from section_table where section_name ='MOLECULAR BIOLOGY') and "+
                     "material_no ='LA949-1NO' and "+
                     "packing ='1 no' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='9027 50 90')";

String QueryString12 ="SELECT * from product_table where "+
                     "section_id =(select section_id from section_table where section_name ='CHEMICALS') and "+
                     "material_no ='RM8875-1G' and "+
                     "product_name like '%Rhodium 5% on carbon%' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='7102 29 90')";

String QueryString13 ="SELECT * from product_table where "+
                     "product_name like '%Vacuum-driven Filters PVDF hydrophilic membrane%' and "+
                     "material_no ='VD18-12NO' and "+
                     "packing ='12 no' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='8421 19 99')";

String QueryString14 ="SELECT * from product_table where "+
                     "section_id =(select section_id from section_table where section_name ='CHEMICALS''PLANT TISSUE CULTURE') and "+
                     "product_name like '%Street Medium w/ Vitamins & Sucrose; w/o Agar%' and "+
                     "packing ='25lt' and "+
                     "hscode_id = (select hscode_id from hscode_table where hscode='3821 00 00')";

String[] QueryArr = {QueryString1, QueryString2, QueryString3, QueryString4, QueryString5, QueryString6, 
				QueryString7, QueryString8, QueryString9, QueryString10, QueryString11, QueryString12, QueryString13, QueryString14};
		int qno = 13;
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
