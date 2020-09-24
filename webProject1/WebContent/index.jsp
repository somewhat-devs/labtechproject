<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Quotation Builder</title>
<script src="js/jquery-3.5.0.min.js"></script>
<script type="text/javascript" src="js/datatables.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/datatables.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	
	<%
	/* Database connection details */
		try {
		String connectionURL = "jdbc:mysql://localhost:3306/labtech";
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, "root", "");
	%>
	
	<a href="#" id="scrolltop-icon"><img src="images/scrolltop.png"></a>

	<!-- Product Search block starts here -->
	<div class="row row-div">
		<div class="col-lg-12">
		
			<!-- header links -->
			<div class="row">
				<div class="col-lg-6"><h2>Quotation Builder.</h2></div>
				<div class="col-lg-2">
					<button id="billtable-btn" class="btn btn-default head-links">go to <b>Bill</b></button>
				</div>
				<div class="col-lg-2">
					<button id="createnew-btn" class="btn btn-default head-links"><a href="createnew.jsp" target="_blank">Add new product</a></button>
				</div>
				<div class="col-lg-2">
					<button id="importbtn-xlsx" class="btn btn-default head-links" hidden="true">import products by excel</button>
				</div>
			</div>
			<br><br>
			
			<!-- Textfields for searching of product -->
			<div class="row">
				<div class="col-lg-12">
					<table class="txtfld-tbl">
						<tr>
							<td colspan="2"><label for="materialno">Material No/Catalog Id:</label>
								<div class="input-group">
									<input type="text" id="materialno" class="form-control"
										name="materialno" placeholder="Enter material no..." autofocus>
									<div class="input-group-text clr-srchinp-btn">&#x2716</div>
								</div></td>
								
							<td><label for="brand">Brand/Make:</label>
								<div class="input-group">
									<input type="text" id="brand" list="brandlist"
										class="form-control" name="brand" placeholder="Choose brand/make..." >
									<div class="input-group-text clr-srchinp-btn">&#x2716</div>
								</div> <datalist id="brandlist">
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
								</datalist> <span id="brand-errormsg" class="errormsg">*Enter value
									only from list.</span></td>	
							
						</tr>
						
						<tr>
							<td colspan="3"><label for="productname">Product Name:</label>
								<div class="input-group">
									<input type="text" id="productname" class="form-control"
										name="productname" placeholder="Enter product keyword..." >
									<div class="input-group-text clr-srchinp-btn">&#x2716</div>
								</div>
							</td>
						</tr>
		
						<tr>
							<td><label for="hscode">HSN code:</label>
								<div class="input-group">
									<input type="text" id="hscode" list="hscodelist"
										class="form-control" name="hscode" placeholder="Choose HSN code..." >
									<div class="input-group-text clr-srchinp-btn">&#x2716</div>
								</div> <datalist id="hscodelist">
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
								</datalist> <span id="hscode-errormsg" class="errormsg">*Enter value
									only from list.</span></td>
		
							<td><label for="section">Section:</label>
								<div class="input-group">
									<input type="text" id="section" list="sectionlist"
										class="form-control" name="section" placeholder="Choose section name..." >
									<div class="input-group-text clr-srchinp-btn">&#x2716</div>
								</div> <datalist id="sectionlist">
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
					
					<!-- "Get products" search button -->
					<button id="resultbtn" class="btn btn-primary">Get products</button>
					<span id="submit-errormsg" class="errormsg">*Please fill atleast one of the inputs.</span> 
					<img id="load-result" class="load-icon" src="images/load.gif">
				</div>
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
	<!-- Product Search block ends here -->
	
	<hr>
	
	<!-- Search results block starts here -->
	<div class="row row-div">
		<div class="col-lg-12">
			<div class="row">
				<div class="col-lg-2"><h4>Search Results</h4></div>
				<div class="col-lg-10"><button class="btn btn-outline-dark btn-sm" id="clr-searchresult-btn">clear search items</button></div>
			</div>
		<br>
			<div class="row">
				<div class="col-lg-12">
					<div id="resulttext">
						<table id='main-tbl' class='table table-sm table-bordered'>
							<thead>
								<tr>
									<th>S No</th>
									<th>Material No</th>
									<th>Brand/Make</th>
									<th>Packing</th>
									<th>Product Name</th>
									<th>Consumer rate</th>
									<th>GST rate</th>
									<th>HSN code</th>
									<th>Section</th>
									<th>add item to Bill</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div> 
	<!-- Search results block ends here -->
	
	<hr>

	<!-- Product Bill block starts here -->
	<div class="row row-div">
		<div class="col-lg-12">
			<div class="row">
				<div class="col-lg-2">
					<h4>Quotation Bill</h4>
				</div>
				<div class="col-lg-10">
					<button class="btn btn-outline-dark btn-sm" id="clr-billresult-btn">clear bill items</button>
				</div>
			</div>
			<br>
			<div class="row">
				<table id="bill-tbl" class="table table-bordered">
					<!-- table for adding products in bill -->
					<thead>
						<tr>
							<th>S no.</th>
							<th style="width: 120px;">HSN code</th>
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
						<tr>
							<td id="billtbl-empty" colspan="9">Their are no products currently on the Bill.</td>
						</tr>
					</tbody>
				</table>
			</div>
			<br>
			<div class="row">
				<div class="col-lg-7">
					<div class="form-group termsNcondition-div">
						<label for="comment">Terms and Conditions:</label>
						<textarea class="form-control" rows="5" id="termsNcondition"
							placeholder="Add Terms and Conditions to your quotation..."></textarea>
					</div>
				</div>
				<div class="col-lg-1"></div>
				<div class="col-lg-2">
					<button id="exportbtn-pdf" class="btn btn-dark exportbtn">export to pdf</button>
					<img id="load-export-pdf" class="load-icon" src="images/load.gif">
				</div>
				<div class="col-lg-2">
					<button id="exportbtn-xlsx" class="btn btn-dark exportbtn">export to excel</button>
					<span id="exportbtn-errormsg" class="errormsg">*Please fill all inputs properly.</span> 
						<img id="load-export-xlsx" class="load-icon" src="images/load.gif">
				</div>
			</div>
		</div>
	</div>
	<!-- Product Bill block ends here -->
</body>

</html>

