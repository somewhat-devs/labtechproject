<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert into Product Bill</title>
<script src="js/jquery-3.5.0.min.js"></script>
<script type="text/javascript" src="js/datatables.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/datatables.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<h2>Quotation Builder.</h2>
	<br>
	<br>
	<%
		try {
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "");
	%>

	<div class="row">
		<div class="col-lg-12">
			<table class="txtfld-tbl">
				<tr>
					<td><label for="materialno">Material No</label> <!-- creating textFields and dropdowns  -->
						<div class="input-group">
							<input type="text" id="materialno" class="form-control"
								name="materialno" placeholder="Enter material no..." autofocus>
							<div class="input-group-text clr-srchinp-btn">&#x2716</div>
						</div></td>

					<td colspan="2"><label for="productname">Product Name</label>
						<div class="input-group">
							<input type="text" id="productname" class="form-control"
								name="productname" placeholder="Enter product no..." >
							<div class="input-group-text clr-srchinp-btn">&#x2716</div>
						</div></td>
				</tr>

				<tr>
					<td><label for="hscode">HSN code:</label>
						<div class="input-group">
							<input type="text" id="hscode" list="hscodelist"
								class="form-control" name="hscode" placeholder="Choose HSN code..." >
							<div class="input-group-text clr-srchinp-btn">&#x2716</div>
						</div> <datalist id="hscodelist">
							<!-- fetch distinct hscodes from db and inserting them to options of dropdowns -->
							<%
								statement = connection.createStatement();
							String QueryStringFields = "select hscode from hscode_table";
							rs = statement.executeQuery(QueryStringFields);
							while (rs.next()) {
							%>
							<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
							<%
								}
							rs.close();
							statement.close();
							%>
						</datalist> <span id="hscode-errormsg" class="errormsg">*Enter value
							only from list.</span></td>

					<td><label for="section">Section:</label>
						<div class="input-group">
							<input type="text" id="section" list="sectionlist"
								class="form-control" name="section" placeholder="Choose section name..." >
							<div class="input-group-text clr-srchinp-btn">&#x2716</div>
						</div> <datalist id="sectionlist">
							<!-- fetch distinct section names from db and inserting them to options of dropdowns -->
							<%
								statement = connection.createStatement();
							QueryStringFields = "select section_name from section_table";
							rs = statement.executeQuery(QueryStringFields);
							while (rs.next()) {
							%>
							<option value="<%=rs.getString(1)%>"><%=rs.getString(1)%></option>
							<%
								}
							rs.close();
							statement.close();
							%>
						</datalist> <span id="section-errormsg" class="errormsg">*Enter value
							only from list.</span></td>

					<td><label for="custrate">Price range:</label> 
					<select	id="custrate" class="form-control" name="custrate">
							<option value="none">0 - 1,00,000+</option>
							<option value="1">&lt; 500</option>
							<option value="2">500 - 1,000</option>
							<option value="3">1,000 - 5,000</option>
							<option value="4">5,000 - 10000</option>
							<option value="5">10,000 - 25,000</option>
							<option value="6">25,000 - 1,00,000</option>
							<option value="7">&gt; 1,00,000</option>
					</select></td>
				</tr>
			</table>
			<br>
			<button id="resultbtn" class="btn btn-primary">Get products</button>
			<span id="submit-errormsg" class="errormsg">*Please fill atleast one of the inputs.</span> 
			<img id="load-result" class="loadimg" src="images/load.gif">
			<br><br>
			<hr>
			<br><br>
			<div class="row">
				<div class="col-lg-2"><h4>Search Results</h4></div>
				<div class="col-lg-10"><button class="btn btn-outline-dark btn-sm" id="clr-searchresult-btn">clear search items</button></div>
			</div>
			<br>
			<div id="resulttext">
				<table id='main-tbl' class='table table-sm table-bordered'>
				<thead><tr>
					<th>S No</th>
					<th>Material No</th>
					<th>Packing</th>
					<th>Product Name</th>
					<th>Consumer rate</th>
					<th>GST rate</th>
					<th>HSN code</th>
					<th>Section</th>
					<th>add item to Bill</th>
				</tr></thead>
				<tbody>
				</tbody>
				</table>
			</div>
		</div>
	</div> 
	<%
		connection.close();
	} catch (Exception e) {
	e.printStackTrace();
	out.println("Unable to connect to database.");
	}
	%>
	<br><br><hr>
	<br><br>
	<div class="row">
		<div class="col-lg-12">
			<div class="row">
				<div class="col-lg-2"><h4>Quotation Bill</h4></div>
				<div class="col-lg-10"><button class="btn btn-outline-dark btn-sm" id="clr-billresult-btn">clear bill items</button></div>
			</div>							
			<br>
			<table id="bill-tbl" class="table table-bordered">
				<!-- table for adding products in bill -->
				<thead>
					<tr>
						<th>S no.</th>
						<th>HSN code</th>
						<th style="width: 450px;">Item Name with Description</th>
						<th>Make/Brand</th>
						<th class="input-cols">Quantity</th>
						<th>Price/Rate</th>
						<th class="input-cols">Discount</th>
						<th class="input-cols">GST</th>
						<th>remove item</th>				
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<br>
	<button id="exportbtn" class="btn btn-success">export to pdf</button>
	<span id="exportbtn-errormsg" class="errormsg">*Please fill all inputs properly.</span>
	<img id="load-export" class="loadimg" src="images/load.gif">
	<br><br>
</body>

</html>

