<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create new product</title>
<script src="js/jquery-3.5.0.min.js"></script>
<script type="text/javascript" src="js/datatables.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/datatables.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<h2>Create new product.</h2>
	<%
		try {
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "");
	%>
	
	<div class="row row-div">
		<div class="col-lg-12">
			<p class="redtext">* required fields</p>
			<table class="txtfld-tbl">
				<tr>
					<td><label for="materialno">Material No/Catalog Id: <span class="redtext">*</span></label> <!-- creating textFields and dropdowns  -->
						<div class="input-group">
							<input type="text" id="materialno" class="form-control create-inputs-req"
								name="materialno" placeholder="Enter material no..." autofocus>
						</div>
					</td>
						
					<td><label for="brand">Brand/Make:</label>
						<div class="input-group">
							<input type="text" id="brand" class="form-control"  list="brandlist"
							name="brand" placeholder="Enter brand/make..." >
						</div>
						<datalist id="brandlist">
							<%
								statement = connection.createStatement();
							String QueryStringFields = "select brand from brand_table";
							rs = statement.executeQuery(QueryStringFields);		rs.next();
							while (rs.next()) {
							%>
							<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
							<%
								}
							rs.close();
							statement.close();
							%>
						</datalist>
					</td>
					
					<td><label for="packing">packing:</label>
						<div class="input-group">
							<input type="text" id="packing" class="form-control" 
							name="packing" placeholder="Enter packing type..." >
						</div>
					</td>
				</tr>
				
				<tr>
					<td colspan="3"><label for="productname">Product Name: <span class="redtext">*</span></label>
						<div class="input-group">
							<textarea id="productname" class="form-control create-inputs-req" name="productname" 
							placeholder="Enter product description..." rows="2" ></textarea>
						</div>
					</td>
				</tr>

				<tr>
					<td><label for="consumerrate">Consumer rate: <span class="redtext">*</span></label>
						<div class="input-group">
							<input type="text" id="consumerrate" class="form-control create-inputs-num" 
							name="consumerrate" placeholder="Enter consumer rate..." >
						</div>
						<span class="errormsg">*only numbers allowed</span>
					</td>

					<td><label for="gstrate">GST rate:</label>
						<div class="input-group">
							<input type="text" id="gstrate" class="form-control create-inputs-num" 
							name="gstrate" placeholder="Enter GST rate..." >
						</div>
						<span class="errormsg">*only numbers allowed</span>
					</td>
				</tr>

				<tr>
					<td><label for="hscode">HSN code:</label>
						<div class="input-group">
							<input type="text" id="hscode"class="form-control" list="hscodelist"
							name="hscode" placeholder="Enter HSN code..." >
						</div>						
						<datalist id="hscodelist">
							<%
								statement = connection.createStatement();
							QueryStringFields = "select hsncode from hsncode_table";
							rs = statement.executeQuery(QueryStringFields);		rs.next();
							while (rs.next()) {
							%>
							<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
							<%
								}
							rs.close();
							statement.close();
							%>
						</datalist>
					</td>

					<td><label for="section">Section:</label>
						<div class="input-group">
							<input type="text" id="section" class="form-control" list="sectionlist"
							name="section" placeholder="Enter section name..." >
						</div>
						<datalist id="sectionlist">
							<%
								statement = connection.createStatement();
							QueryStringFields = "select section_name from section_table";
							rs = statement.executeQuery(QueryStringFields);		rs.next();
							while (rs.next()) {
							%>
							<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
							<%
								}
							rs.close();
							statement.close();
							%>
						</datalist>
					</td>
				</tr>
			</table>
			<br>
			<button id="createbtn" class="btn btn-primary">Create</button>
			<span id="create-errormsg" class="errormsg">*Please fill atleast one of the inputs.</span> 
			<img id="load-create" class="load-icon" src="images/load.gif">
		</div>
	</div>
	
	<%
		connection.close();
	} catch (Exception e) {
		e.printStackTrace();
		out.println("Unable to connect to database.");
	}
	%>
</body>
</html>