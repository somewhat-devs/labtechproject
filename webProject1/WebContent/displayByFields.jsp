<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson" %>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Display by fields</title>
<script src="js/jquery-3.5.0.min.js"></script>
<script type="text/javascript" src="js/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="css/datatables.min.css"/>
<style>
body {
	margin: 3%;
	background: #f8f8f8;
}
.txtfld-tbl, #main-tbl{
	width: 100%;
}
.txtfld-tbl td{
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
.errormsg{
	color: red;
    font-size: 12px;
    display: none;
}
#loadimg{
	width: 25px;
    display: none;
	margin-inline-start: 10px; 
}
</style>
</head>
<body>
	<h2>Quotation Builder.</h2> <br><br>
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
	<td><label for="materialno">Material No</label>											<!-- creating textFields and dropdowns  -->
	<input type="text" id="materialno" class="form-control" name="materialno">
	</td>

	<td><label for="packing">Packing</label>
	<input type="text" id="packing" list="packinglist" class="form-control" name="packing" >
	<datalist id="packinglist" >															<!-- fetch distinct packing names from db and inserting them to options of dropdowns -->
		<%
		statement = connection.createStatement();
		String QueryStringFields = "select distinct packing from product_table";
		rs = statement.executeQuery(QueryStringFields);
		while (rs.next()){
			%>
			<option value="<%=rs.getString(1) %>"><%=rs.getString(1) %></option>
			<%
		}
		rs.close(); statement.close();
	%>
	</datalist>
	<span id="packing-errormsg" class="errormsg">*Enter value only from list.</span></td>

	<td><label for="productname">Product Name</label>
	<input type="text" id="productname" class="form-control" name="productname"></td></tr>
	
	<tr><td><label for="hscode">HS code:</label>
	<input type="text" id="hscode" list="hscodelist" class="form-control" name="hscode" >
	<datalist id="hscodelist">																	<!-- fetch distinct hscodes from db and inserting them to options of dropdowns -->
		<%
		statement = connection.createStatement();
		QueryStringFields = "select hscode from hscode_table";
		rs = statement.executeQuery(QueryStringFields);
		while (rs.next()){
			%>
			<option value="<%=rs.getString(1) %>"><%=rs.getString(1) %></option>
			<%
		}
		rs.close(); statement.close();
	%>
	</datalist>
	<span id="hscode-errormsg" class="errormsg">*Enter value only from list.</span></td>

	<td><label for="section">Section:</label>
	<input type="text" id="section" list="sectionlist" class="form-control" name="section" >
	<datalist id="sectionlist" >																<!-- fetch distinct section names from db and inserting them to options of dropdowns -->
		<%
		statement = connection.createStatement();
		QueryStringFields = "select section_name from section_table";
		rs = statement.executeQuery(QueryStringFields);
		while (rs.next()){
			%>
			<option value="<%=rs.getString(1) %>"><%=rs.getString(1) %></option>
			<%
		}
		rs.close(); statement.close();
	%>
	</datalist>
	<span id="section-errormsg" class="errormsg">*Enter value only from list.</span></td><td></td></tr>
	</table>
	<br>
	<button id="resultbtn" class="btn btn-primary">Get products</button>
	<span id="submit-errormsg" class="errormsg">*Please fill atleast one of the inputs.</span>
	<img id="loadimg" src="images/load.gif">
	<br><br><br>
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
		
		var packinglist = [], hscodelist = [], sectionlist = [];
		$('#packinglist option').each(function(){ packinglist.push(this.value); });
		$('#hscodelist option').each(function(){ hscodelist.push(this.value); });
		$('#sectionlist option').each(function(){ sectionlist.push(this.value); });
		
		$('#packing').on('change', function(){
			if ($.inArray($(this).val(), packinglist) < 0 && !$(this).val()=="")		$('#packing-errormsg').show();
			else	$('#packing-errormsg').hide();
		});
		$('#hscode').on('change', function(){
			if ($.inArray($(this).val(), hscodelist) < 0 && !$(this).val()=="")		$('#hscode-errormsg').show();
			else	$('#hscode-errormsg').hide();
		});
		$('#section').on('change', function(){
			if ($.inArray($(this).val(), sectionlist) < 0 && !$(this).val()=="")		$('#section-errormsg').show();
			else	$('#section-errormsg').hide();
		});

		$('#resultbtn').click(function(){
			if (($('#materialno').val()=="") && ($('#packing').val()=="") && ($('#productname').val()=="") && ($('#hscode').val()=="") && ($('#section').val()) == "" ){ 
				$('#submit-errormsg').show();
				return 1;
			} else {
				$('#submit-errormsg').hide();
			}
			$('#loadimg').show();
			
			$.ajax({
				url : 'SearchService',								// sending ajax request, url = name of java page
				type : 'POST',										// datatype = type of data returned from server	
				dataType : 'json',
				data : {
					materialno : $('#materialno').val(),			// sending textField values 
					packing : $('#packing').val(),
					productname : $('#productname').val(),
					hscode : $('#hscode').val(),
					section : $('#section').val(),					
				},
				success : function(responseText) {					// creating table from the json data got from server
//  					console.log(responseText);
					var resp = ""; 
					if ( responseText[0][0] == "none" ){
						resp += "No results found!";
					} else {
						resp += "<table id='main-tbl' class='table table-sm table-bordered'>"+			// inserting data to resp string which would be inserted into a div
						"<thead><tr><th>S. No.</th>"+
						"<th>Material No</th>"+
						"<th>Packing</th>"+
						"<th>Product Name</th>"+
						"<th>Consumer rate</th>"+
						"<th>GST rate</th>"+
						"<th>HS code</th>"+
						"<th>Section</th>"+
						"</tr></thead><tbody>";
						for (var i=0; i<responseText.length; i++){
							resp += "<tr id='prod-"+ (i+1) +"'><td>"+(i+1)+"</td>";
							for (var j=0; j<7; j++){
								resp+="<td>"+ responseText[i][j] +"</td>"
							}
							resp+="</tr>";
						}
						resp+= "</tbody></table>";
					}
					$('#resulttext').html(resp);
					$('#loadimg').hide();
					createTbl();
				}
			});
		});
		
		function createTbl(){
			$('#main-tbl').DataTable({
				"pageLength": 10,
				"pageType": "simple_numbers",
				"ordering": true,
				"searching": false,
				"columnDefs": [
			        { "targets": [1, 2, 3, 6, 7], "orderable": false}
			    ]
			}).draw(true);
		}
	});
</script>
</html> 

