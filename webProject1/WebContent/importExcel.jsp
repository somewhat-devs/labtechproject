<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Import product</title>
<script src="js/jquery-3.5.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<div class="row row-div">
		<div class="col-lg-12">
			<div class="row">
				<div class="col-lg-11">
					<h2>Import product by excel file.</h2>
				</div>
				<div class="col-lg-1">
					<a href="index.jsp"><button class="btn btn-outline-dark">back</button></a>
				</div>
			</div>
			<br>
			<div>
				<h5>The excel file must be defined as per following instructions:</h5>
				<p>
					1. I<sup>st</sup> row must have column names as per respective
					order in each sheet. <br> "Catalogue ID", "Brand", "Packing",
					"Product Name", "Consumer Rate", "GST Rate", "HSN Code", "Section"
				</p>
				<p>2. Catelogue ID and Product Name couldn't be empty be empty
					in each row.</p>
				<p>3. Catelogue ID value shouldn't exceed length greater than 30
					characters.</p>
			</div>
			<br>
			<form id="importForm" enctype="multipart/form-data">
				<div class="row">
					<div class="col-lg-4"></div>
					<div class="col-lg-4">
						<input id="importFile" class="form-control" name="importFile"
							type="file" multiple accept=".xls">
					</div>
					<div class="col-lg-4"></div>
				</div>
				<br>
				<div class="row">
					<div class="col-lg-3"></div>
					<div class="col-lg-3">
						<input id="scanimport" type="submit"
							class="form-control btn btn-primary" value="Scan excel file">
					</div>
					<div class="col-lg-3">
						<button id="contimport" class="form-control btn btn-success"
							disabled="true">Continue Import</button>
					</div>
					<div class="col-lg-3"></div>
				</div>
			</form>
			<br>
			<br> <img id="load-export-pdf" class="load-icon"
				src="images/load.gif">
			<div id="resp"></div>
			<br>
		</div>
	</div>

</body>

<script>

	$(document).ready(function() {
		var uploadedFilePath;
	
		$('#importForm').on('submit', function(e) {
            e.stopPropagation();
            e.preventDefault();
            $('.load-icon').show();
            var data = new FormData(this);
            $.ajax({
                url :  'ImportService',
                type : 'POST',
                data : data,
                cache : false,
                processData : false,
                contentType : false,
                success : function(data) {
            		$('.load-icon').hide();
                    $('#resp').html(data[1]);
                    uploadedFilePath = data[0];
                    if (data[2] == "0")
                    	$('#contimport').prop('disabled', false);                    	
                },
                fail : function(){
                	alert("Something went wrong!");
                }
           });
        });
        
        $('#contimport').on('click', function(e) {
        	$('#resp').html("");
            $('.load-icon').show();
            $.ajax({
                url :  'ImportService',
                type : 'POST',
                data : {
                	action: "contimport",
                	uploadedFilePath: uploadedFilePath
                },
                success : function(data) {  
            		$('.load-icon').hide();                  
                    $('#resp').html(data+" products are added to database successfully.");
                },
                fail : function(){
                	alert("Something went wrong!");
                }
           }); 
        });
        
	});
</script>
</html>