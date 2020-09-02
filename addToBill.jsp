<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/datatables.min.css" />
<style>
body {
	margin: 3%;
	background: #f8f8f8;
}

.txtfld-tbl, #main-tbl {
	width: 100%;
}

.txtfld-tbl td {
	padding-right: 5%;
	padding-bottom: 2%;
	vertical-align: top;
}

.table {
	background: #fff;
	box-shadow: 1px 1px 8px grey;
}

th {
	text-align: center;
	vertical-align: middle;
}

.errormsg {
	color: red;
	font-size: 12px;
	display: none;
}

#loadimg {
	width: 25px;
	display: none;
	margin-inline-start: 10px;
}

.clr1-btn {
	cursor: pointer;
	padding: 7px;
	font-size: 13px;
}
</style>
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
		connection = DriverManager.getConnection(connectionURL, "root", "Dhruv@1113");
	%>

	<div class="row">
		<div class="col-lg-12">
			<table class="txtfld-tbl">
				<tr>
					<td><label for="materialno">Material No</label> <!-- creating textFields and dropdowns  -->
						<div class="input-group">
							<input type="text" id="materialno" class="form-control"
								name="materialno" autofocus>
							<div class="input-group-text clr1-btn">&#x2716</div>
						</div></td>

					<td colspan="2"><label for="productname">Product Name</label>
						<div class="input-group">
							<input type="text" id="productname" class="form-control"
								name="productname">
							<div class="input-group-text clr1-btn">&#x2716</div>
						</div></td>
				</tr>

				<tr>
					<td><label for="hscode">HSN:</label>
						<div class="input-group">
							<input type="text" id="hscode" list="hscodelist"
								class="form-control" name="hscode">
							<div class="input-group-text clr1-btn">&#x2716</div>
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
								class="form-control" name="section">
							<div class="input-group-text clr1-btn">&#x2716</div>
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

					<td><label for="custrate">Price range:</label> <select
						id="custrate" class="form-control" name="custrate">
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
			<span id="submit-errormsg" class="errormsg">*Please fill
				At least one of the inputs.</span> <img id="loadimg" src="images/load.gif">
			<br>	
			<div> 
			<br>
					<button id="clearbutton" class="input-group-text clr1-btn">Clear</button>
					
					
					
			<br>
			</div>										<!-- dhruv -->
			<br>
			<br>
			<h4>Search Results</h4>
			<br>
			<div id="resulttext"></div>
		</div>
	</div> <br><br>
	<div class="row">
		<div class="col-lg-12">
			<h4>Quotation Bill</h4>
			<br>
			<div> remove all btn </div>									<!-- muskan -->
			<br>
			<table id="bill-tbl" class="table table-bordered">
				<!-- table for adding products in bill -->
				<thead>
					<tr>
						<th>S no.</th>
						<th>Item Name with Description</th>
						<th>Make/Brand</th>
						<th>Price/Rate</th>
						<th>GST</th>
						<th>Discount</th>
						<th>Quantity</th>
						<th>remove item</th>				
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
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

<script>
$(document).ready(function() {
	var packinglist = [],
		hscodelist = [],
		sectionlist = [];
	$('#hscodelist option').each(function() {
		hscodelist.push(this.value);
	});
	
	$('#sectionlist option').each(function() {
		sectionlist.push(this.value);
	});

	$('#hscode').on('change', function() {
		if ($.inArray($(this).val(), hscodelist) < 0 && !$(this).val() == "")
			$('#hscode-errormsg').show();
		else
			$('#hscode-errormsg').hide();
	});
	
	$('#section').on('change', function() {
		if ($.inArray($(this).val(), sectionlist) < 0 && !$(this).val() == "")
			$('#section-errormsg').show();
		else
			$('#section-errormsg').hide();
	});

	$('.clr1-btn').on('click', function() {
		$(this).prev('input').val("");
		$('#' + $(this).prev('input').attr('id') + '-errormsg').hide();
	});

	$('input').keypress(function(event) {
		if (event.keyCode == 13) 
			$('#resultbtn').click();
	});

	$('#resultbtn').click( function() {
		if (($('#materialno').val() == "") &&
			($('#productname').val() == "") &&
			($('#hscode').val() == "") &&
			($('#section').val() == "") &&
			($('#custrate').val() == "none")) {
			$('#submit-errormsg').show();
			return 1;
		} else {
			$('#submit-errormsg').hide();
			if (($.inArray($('#hscode').val(), hscodelist) < 0 && !$('#hscode').val() == "") ||
				($.inArray($('#section').val(), sectionlist) < 0 && !$('#section').val() == "")) {
				if ($.inArray($('#hscode').val(), hscodelist) < 0 && !$('#hscode').val() == "")
					$('#hscode-errormsg').show();
				if ($.inArray($('#section').val(), sectionlist) < 0 &&!$('#section').val() == "")
					$('#section-errormsg').show();
				
				return 1;
			}
		}
		$('#hscode-errormsg').hide();
		$('#section-errormsg').hide();
		$('#loadimg').show();

		$.ajax({
			url: 'SearchService', // sending ajax request, url = name of java page
			type: 'POST', // datatype = type of data returned from server	
			dataType: 'json',
			data: {
				materialno: $('#materialno').val(), 	// sending textField values 
				productname: $('#productname').val(),
				hscode: $('#hscode').val(),
				section: $('#section').val(),
				custrate: $('#custrate').val()
			},
			success: function(responseText) { 			// creating table from the json data got from server
				// console.log(responseText);
				var resp = "";
				if (responseText[0][0] == "none") {
					resp += "No results found!";
				} else {
					resp += "<table id='main-tbl' class='table table-sm table-bordered'>" + // inserting data to resp string which would be inserted into a div
						"<thead><tr><th>S. No.</th>" +
						"<th>Material No</th>" +
						"<th>Packing</th>" +
						"<th>Product Name</th>" +
						"<th>Consumer rate</th>" +
						"<th>GST rate</th>" +
						"<th>HSN</th>" +
						"<th>Section</th>" +
						"<th>Qty.</th>" +
						"<th>add item to Bill</th>" +
						"</tr></thead><tbody>";
					for (var i = 0; i < responseText.length; i++) {
						resp += "<tr id='prod-" + (i + 1) + "'>" +
						"<td>" + (i + 1) + "</td>";
						for (var j = 0; j < 7; j++) {
							resp += "<td>" + responseText[i][j] + "</td>"
						}
						resp += "<td>" + 									// block for creating qty input field    - kavya
							"<div class=\"qty-btn\">qty.</div>" +
							"</td>";
						resp += "<td>" + 						
							"<button class=\"btn btn-outline-success btn-sm add-btn\">add item</button>" +
							"</td>";
						resp += "</tr>";
					}
					resp += "</tbody></table>";
				}
				$('#resulttext').html(resp);
				$('#loadimg').hide();
				createTbl();
			}
		});
	});

	$(document).on("click", '.add-btn', function() { 				// block for managing action of qty button
		$(this).text("added");
		$(this).prop("disabled",true);
		var n = $('#bill-tbl tbody').children().length;
		var pn = $(this).parent().parent().find('td:nth-child(4)');
		var item_tr = "<tr><td>"+ (n+1) + "</td>";											// S. no
		item_tr += "<td>"+ pn.text() + "</td>" +											// Item Name with Description
		"<td></td>"	+																		// Make/Brand
		"<td>"+ pn.next().text() + "</td>" +												// Price/Rate
		"<td>"+ pn.next().next().text() + "</td>" + 										// GST
		"<td> discount input  </td>"	+													// Discount  - Parul
		"<td>"+ $(this).parent().parent().find('td:nth-child(9)').html(); +"</td>" +		// Quantity
		"<td>  remove btn  </td></tr>"	;													// remove button   - niharika
		$('#bill-tbl tbody').append(item_tr);
	});

	$(document).on("click", '.rem-btn', function() { 				// block for managing action of add button

	});

	function createTbl() {
		$('#main-tbl').DataTable({
			"pageLength": 5,
			"pageType": "simple_numbers",
			"ordering": true,
			"searching": false,
			"columnDefs": [{
				"targets": [1, 2, 3, 6, 7, 8, 9],
				"orderable": false
			}]
		}).draw(true);
	}

	$('#clearbutton').click(function() {
		$('#materialno').val("");
		$('#productname').val("");
		$('#hscode').val("");
		$('#section').val("");
		$('#custrate').val();
	});
	
});
</script>
</html>

