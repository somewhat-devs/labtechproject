<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson" %>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Display by fields (temp)</title>
<!-- <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"> -->
<script src="js/jquery-3.5.0.min.js"></script>
<style>
body {
	margin: 3%;
	background: #f8f8f9;
}
.txtfld-tbl{
	width: 100%;
}
.txtfld-tbl td{
	padding-right: 5%;
    padding-bottom: 2%;
}
.table {
	background: #fff;
}
th {
	text-align: center;
}
</style>
</head>
<body>
	<h2>Displaying results using textfields.</h2> <br><br>
	<%
	try {
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "");
	%>

	<table class="txtfld-tbl"><tr>
	<td><label for="materialno">Material No</label>					<!-- creating textFields and dropdowns  -->
	<input type="text" id="materialno" class="form-control" name="materialno" ></td>

	<td><label for="packing">Packing</label>
	<select name="packing" id="packing" class="form-control" >
		<%
		statement = connection.createStatement();
		String QueryStringFields = "select distinct packing from product_table";
		rs = statement.executeQuery(QueryStringFields);
		out.println("<option value=\"none\">any</option>");
		while (rs.next()){
			%>
			<option value="<%=rs.getString(1) %>"><%=rs.getString(1) %></option>
			<%
		}
		rs.close(); statement.close();
	%>
	</select></td>

	<td><label for="productname">Product Name</label>
	<input type="text" id="productname" class="form-control" name="productname"></td></tr>
	
	<tr><td><label for="hscode">HS code:</label>
	<select name="hscode" id="hscode" class="form-control" >
		<%
		statement = connection.createStatement();
		QueryStringFields = "select hscode from hscode_table";
		rs = statement.executeQuery(QueryStringFields);
		out.println("<option value=\"none\">any</option>");
		while (rs.next()){
			%>
			<option value="<%=rs.getString(1) %>"><%=rs.getString(1) %></option>
			<%
		}
		rs.close(); statement.close();
	%>
	</select></td>

	<td><label for="section">Section:</label>
	<select name="section" id="section" class="form-control" >
		<%
		statement = connection.createStatement();
		QueryStringFields = "select section_name from section_table";
		rs = statement.executeQuery(QueryStringFields);
		out.println("<option value=\"none\">any</option>");
		while (rs.next()){
			%>
			<option value="<%=rs.getString(1) %>"><%=rs.getString(1) %></option>
			<%
		}
		rs.close(); statement.close();
	%>
	</select></td><td></td></tr>
	</table>
	<br>
	<br>
	<button id="resultbtn" class="btn btn-primary">Get products</button>
	<br><br>
	<div id="resulttext"></div>

	<%		
		connection.close();
	} catch(Exception e){
		e.printStackTrace();
	}
	%>

</body>

<script>
	$(document).ready(function(){
		$('#resultbtn').click(function(){
			$.ajax({
				url : 'SearchService',								// sending ajax request, url = name of java page
				type : 'POST',										// datatype = type of data returned from server	
				dataType : 'json',
				data : {
					materialno : $('#materialno').val(),			// sending textField values 
					packing : $('#packing option').filter(':selected').val(),
					productname : $('#productname').val(),
					hscode : $('#hscode option').filter(':selected').val(),
					section : $('#section option').filter(':selected').val(),					
				},
				success : function(responseText) {					// creating table from the json data got from server
					console.log(responseText);
					var resp = "<table class='table table-sm table-bordered'>"+
					"<tr><th>S. No.</th>"+
					"<th>Material No</th>"+
					"<th>Packing</th>"+
					"<th>Product Name</th>"+
					"<th>Consumer rate</th>"+
					"<th>GST rate</th>"+
					"<th>HS code</th>"+
					"<th>Section</th></tr>";
					for (var i=0; i<responseText.length; i++){
						resp += "<tr><td>"+(i+1)+"</td>";
						for (var j=0; j<7; j++){
							resp+="<td>"+ responseText[i][j] +"</td>"
						}
						resp+="</tr>";
						
					}
					resp+= "</table>";
					$('#resulttext').html(resp);
				}
			});
		});
	});
</script>
</html> 

<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson" %>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Display by fields (temp)</title>
<script src="js/jquery-3.5.0.min.js"></script>
<style> 
td { padding: 5px; }
</style>
</head>
<body>
<h2>Displaying results using textfields.</h2> <br><br>
	
	<label for="packing">Packing: </label>					<!-- creating textFields and dropdowns  -->
	<input type="text" id="packing" name="packing" >	<!-- 4x250 no -->
	
	<label for="hscode">HS code: </label>					<!-- creating textFields and dropdowns  -->
	<input type="text" id="hscode" name="hscode" >		<!-- 8421 21 90 --> 
	
	<br>
	<br>
	<button id="resultbtn">Get products</button>
	<br><br>
	<div id="resulttext"></div>
	
</body>
<script>
	$(document).ready(function(){
		$('#resultbtn').click(function(){
			$.ajax({
				url : 'SearchService',								// sending ajax request, url = name of java page
				type : 'POST',										// datatype = type of data returned from server	
				dataType : 'json',
				data : {
					packing : $('#packing').val(),					// sending textField values 
					hscode : $('#hscode').val()			
				},
				success : function(responseText) {					// creating table from the json data got from server
					console.log(responseText);
					var resp = "";
					resp += "<p>data of responseText: </p>"
					for (var i=0; i<responseText.length; i++){
						resp += "<p>"+responseText[i]+"</p>";
					}
					$('#resulttext').html(resp);
				}
			});
		});
	});
</script>
</html> --%>