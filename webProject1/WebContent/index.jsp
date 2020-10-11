<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="javax.naming.InitialContext" %>
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
		try {
			InitialContext initialContext = new InitialContext();
			String connectionURL = (String) initialContext.lookup("java:comp/env/connectionURL");
			String classforName = (String) initialContext.lookup("java:comp/env/classforName");
			String username = (String) initialContext.lookup("java:comp/env/username");
			String password = (String) initialContext.lookup("java:comp/env/password");
			Connection connection = null;
			Class.forName(classforName).newInstance();
			connection = DriverManager.getConnection(connectionURL, username, password);
			Statement statement = null;
			ResultSet rs = null;		
	%>
	
	<a href="#" id="scrolltop-icon"><img src="images/scrolltop.png"></a>

	<!-- Product Search block starts here -->
	<div class="row row-div">
		<div class="col-lg-12">
		
			<!-- header links -->
			<div class="row">
				<div class="col-lg-3">
					<img id="main-logo" src="images/main-logo.png">
				</div>
				<div id="main-head" class="col-lg-6"><h1>Quotation Builder.</h1></div>
				<div class="col-lg-3"></div>
			</div>
			<br>
			<div class="row header">
				<div class="col-lg-3"></div>
				<div class="col-lg-2">
					<button id="billtable-btn" class="btn btn-outline-dark btn-sm head-links">go to Bill</button>
				</div>
				<div class="col-lg-2">
					<a href="createnew.jsp"><button id="createnew-btn" class="btn btn-outline-success btn-sm head-links">add new product</button></a>
				</div>
				<div class="col-lg-2">
					<a href="importExcel.jsp"><button id="importbtn-xlsx" class="btn btn-outline-success btn-sm head-links">excel import</button></a>
				</div>
				<div class="col-lg-3"></div>
			</div>
			<br><br>
			
			<!-- Textfields for searching of product -->
			<div class="row billfields-tbl">
				<br>
				<div class="col-lg-12">
					<div class="row">
						<div class="col-lg-5 fields">
							<label for="quotationno">Quotation No:</label>
							<div class="input-group">
								<input type="text" id=quotationno class="form-control"
									name="quotationno" placeholder="Enter quotation no..." autofocus>
								<img class="input-group-text clr-srchinp-btn"
									src="images/clear.png">
							</div>
						</div>
						<div class="col-lg-2"></div>
						<div class="col-lg-5 fields">
							<label for="customername">Customer Name:</label>
							<div class="input-group">
								<input type="text" id=customername class="form-control"
									name="customername" placeholder="Enter customer name...">
								<img class="input-group-text clr-srchinp-btn"
									src="images/clear.png">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 fields">
							<label for="subject">Subject:</label>
							<div class="input-group">
								<textarea class="form-control" rows="2" id="subject" name="subject"
									placeholder="Add Subject to the Quotation..."></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
			<br><br>
			<div class="row srchfields-tbl">
				<div class="col-lg-12">
					<div class="row">
						<div class="col-lg-8 fields">
							<label for="materialno">Material No/Catalog Id:</label>
							<div class="input-group">
								<input type="text" id="materialno" class="form-control"
									name="materialno" placeholder="Enter material no...">
								<img class="input-group-text clr-srchinp-btn" src="images/clear.png">
							</div>
						</div>
						
						<div class="col-lg-4 fields">
							<label for="brand">Brand/Make:</label>
								<div class="input-group">
									<input type="text" id="brand" list="brandlist"
										class="form-control" name="brand" placeholder="Choose brand/make..." >
									<img class="input-group-text clr-srchinp-btn" src="images/clear.png">
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
									only from list.</span>
							
						</div>
					</div>
					
					<div class="row">						
						<div class="col-lg-12 fields">
							<label for="productname">Product Name:</label>
								<div class="input-group">
									<input type="text" id="productname" class="form-control"
										name="productname" placeholder="Enter product keyword..." >
									<img class="input-group-text clr-srchinp-btn" src="images/clear.png">
								</div>
						</div>
					</div>
					
					<div class="row">		
						<div class="col-lg-2"></div>				
						<div class="col-lg-4 fields">
							<label for="hscode">HSN code:</label>
								<div class="input-group">
									<input type="text" id="hscode" list="hscodelist"
										class="form-control" name="hscode" placeholder="Choose HSN code..." >
									<img class="input-group-text clr-srchinp-btn" src="images/clear.png">
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
									only from list.</span>
						</div>
						
						<div class="col-lg-4 fields">
							<label for="section">Section:</label>
								<div class="input-group">
									<input type="text" id="section" list="sectionlist"
										class="form-control" name="section" placeholder="Choose section name..." >
									<img class="input-group-text clr-srchinp-btn" src="images/clear.png">
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
									only from list.</span>
						</div>
						<div class="col-lg-2"></div>
					</div>
					<br>
					
					<!-- "Get products" search button -->
					<div class="row">
						<div class="col-lg-4"></div>
						<div class="col-lg-4">
							<button id="resultbtn" class="btn btn-primary btn-lg">GET PRODUCTS</button>
							<img id="load-result" class="load-icon" src="images/load.gif">
						</div>
						<div class="col-lg-4">
							<span id="submit-errormsg" class="errormsg">*Please fill atleast one of the inputs.</span> 
						</div>
					</div>
					<br><br>
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
		
	<!-- Search results block starts here -->
	<div class="row row-div">
		<div class="col-lg-12">
			<div class="row">
				<div class="col-lg-2"><h4>Search Results</h4></div>
				<div class="col-lg-10"><button class="btn btn-outline-danger btn-sm" id="clr-searchresult-btn">clear search items</button></div>
			</div>
		<br><br>
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
				<div class="col-lg-3">
					<h4>Quotation Generated</h4>
				</div>
				<div class="col-lg-9">
					<button class="btn btn-outline-danger btn-sm" id="clr-billresult-btn">clear bill items</button>
				</div>
			</div>
			<br><br>
			<div class="row">
				<table id="bill-tbl" class="table table-bordered">
					<!-- table for adding products in bill -->
					<thead>
						<tr>
							<th>S no.</th>
							<th style="width: 400px;">Item Name with Description</th>
							<th>Make/Brand</th>
							<th class="input-cols">Quantity</th>
							<th class="input-cols">Unit Price</th>
							<th style="width: 120px;"> Discount
								<input type="checkbox" id="discount-enbl" value="discount-enbl" checked> </th>
							<th class="input-cols">GST</th>
							<th class="input-cols">Price (incl. GST)</th>
							<th style="width: 120px;">HSN code</th>
							<th>remove item</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td id="billtbl-empty" colspan="10">Their are no products currently on the Bill.</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="6"></th>
							<th class="input-cols">Total Price</th>
							<th id="billtotal" class="input-cols td-center">0.0</th>
							<th colspan="2"></th>
						</tr>
					</tfoot>
				</table>
			</div>
			<br>
			<div class="row">
				<div class="col-lg-7">
					<div class="form-group termsNcondition-div">
						<label for="termsNcondition">Terms and Conditions:</label>
						<textarea class="form-control" rows="5" id="termsNcondition"
							placeholder="Add Terms and Conditions to your quotation..."></textarea>
					</div>
				</div>
				<div class="col-lg-5">
					<br>
					<div class="row">
						<div class="col-lg-3"></div>
						<div class="col-lg-2"><img class="export-icon" src="images/pdf.png"></div>
						<div class="col-lg-6">
							<button id="exportbtn-pdf" class="btn btn-dark exportbtn">export to pdf</button>
							<img id="load-export-pdf" class="load-icon" src="images/load.gif">
							<br><br>
						</div>
					</div>
					<div class="row">	
						<div class="col-lg-3"></div>				
						<div class="col-lg-2"><img class="export-icon" src="images/word.png"></div>
						<div class="col-lg-6">
							<button id="exportbtn-doc" class="btn btn-dark exportbtn">export to word</button>
							<img id="load-export-doc" class="load-icon" src="images/load.gif">
							<br><br>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-3"></div>
						<div class="col-lg-2"><img class="export-icon" src="images/excel.png"></div>
						<div class="col-lg-6">
							<button id="exportbtn-xls" class="btn btn-dark exportbtn">export to excel</button>							
							<img id="load-export-xls" class="load-icon" src="images/load.gif">
							<br><br>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-2"></div>
						<div class="col-lg-10">
						<span id="exportbtn-errormsg" class="errormsg">*Some
								inputs are missing or Quotation table is empty.</span> 
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Product Bill block ends here -->
</body>

</html>

