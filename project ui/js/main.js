$(document).ready(function() {
	
//  jquery for index.jsp ##############################################################################################################
	
//	creating list of brands, hsncode and section for validation
	var brandlist = [],
		hscodelist = [],
		sectionlist = [];
	$('#brandlist option').each(function() {
		brandlist.push(this.value);
	});
	
	$('#hscodelist option').each(function() {
		hscodelist.push(this.value);
	});
	
	$('#sectionlist option').each(function() {
		sectionlist.push(this.value);
	});

//	brand textfied value validaiton
	$('#brand').on('change', function() {
		if ($.inArray($(this).val(), brandlist) < 0 && !$(this).val() == "")
			$('#brand-errormsg').show();
		else
			$('#brand-errormsg').hide();
	});

//	hsncode textfied value validaiton
	$('#hscode').on('change', function() {
		if ($.inArray($(this).val(), hscodelist) < 0 && !$(this).val() == "")
			$('#hscode-errormsg').show();
		else
			$('#hscode-errormsg').hide();
	});

//	section textfied value validaiton
	$('#section').on('change', function() {
		if ($.inArray($(this).val(), sectionlist) < 0 && !$(this).val() == "")
			$('#section-errormsg').show();
		else
			$('#section-errormsg').hide();
	});
	
//	header link "go to Bill" scroll to bill block
	$('#billtable-btn').on('click', function(){
		$('html, body').animate({
	        scrollTop: $($('.row-div')[2]).offset().top
	    }, 500);
	});

//	clear individual input field on clicking clear icon
	$('.clr-srchinp-btn').on('click', function() {
		$(this).prev('input').val("");
		$('#' + $(this).prev('input').attr('id') + '-errormsg').hide();
	});
	
//	search for product on pressing enter in keyboard
	$('input').keypress(function(event) {
		if (event.keyCode == 13) 
			$('#resultbtn').click();
	});
	
//	clear product search table and empty all search textfields on clicking this button
	$('#clr-searchresult-btn').on('click', function(){
		$('.txtfld-tbl').find('td input').each(function(i,e){ $(e).val(""); });
		$('#custrate').val("none");
		$('#main-tbl').DataTable().clear().draw();		
	});
	
//	clear bill table and terms and condition textarea on clicking this button
	$('#clr-billresult-btn').on('click', function(){
		$('#bill-tbl tbody').children().not(':first').remove();
		$('#termsNcondition').val("");
		$('#billtbl-empty').show();
	});
	
//	define dataTable of search table
	$('#main-tbl').DataTable({
		"pageLength": 5,
		"pageType": "simple_numbers",
		"ordering": false,
		"searching": false,
        "columns": [
            { "data": "s_no" },				// target = 0
            { "data": "material_no" },		// target = 1
            { "data": "brand/make" },		// target = 2
            { "data": "packing" },			// target = 3
            { "data": "product_name" },		// target = 4
            { "data": "consumer_rate" },	// target = 5
            { "data": "gst_rate" },			// target = 6
            { "data": "hsn_code" },			// target = 7
            { "data": "section" },			// target = 8
            { "data": "add_item_to_Bill" }	// target = 9
        ],
		"columnDefs": [{ "width": "30%", "targets": 4 }, 
		               { "className": "td-center", "targets": [ 0, 9 ] }
		]
	}).draw(true);

//	implementation for "Get products" button to fetch searched products from database
	$('#resultbtn').click( function() {
		
//		search textfields validation
		if (($('#materialno').val() == "") &&
			($('#brand').val() == "") &&
			($('#productname').val() == "") &&
			($('#hscode').val() == "") &&
			($('#section').val() == "") &&
			($('#custrate').val() == "none")) {
			$('#submit-errormsg').show();
			return 1;
		} else {
			$('#submit-errormsg').hide();
			if (($.inArray($('#brand').val(), brandlist) < 0 && !$('#brand').val() == "") ||
				($.inArray($('#hscode').val(), hscodelist) < 0 && !$('#hscode').val() == "") ||
				($.inArray($('#section').val(), sectionlist) < 0 && !$('#section').val() == "")) {
				if ($.inArray($('#brand').val(), brandlist) < 0 && !$('#brand').val() == "")
					$('#brand-errormsg').show();
				if ($.inArray($('#hscode').val(), hscodelist) < 0 && !$('#hscode').val() == "")
					$('#hscode-errormsg').show();
				if ($.inArray($('#section').val(), sectionlist) < 0 &&!$('#section').val() == "")
					$('#section-errormsg').show();
				
				return 1;
			}
		}
		$('#brand-errormsg').hide();
		$('#hscode-errormsg').hide();
		$('#section-errormsg').hide();
		$('#load-result').show();

//		handling ajax to get searched products
		$.ajax({
			url: 'SearchService', 
			type: 'POST',	
			dataType: 'json',
			data: {
				materialno: $('#materialno').val(), 	
				brand: $('#brand').val(),
				productname: $('#productname').val(),
				hscode: $('#hscode').val(),
				section: $('#section').val(),
				custrate: $('#custrate').val(),
				action: 'searchProduct'
			},
			success: function(responseText) { 			
				var resp = "";
				var table = $('#main-tbl').DataTable();	
				table.clear().draw();
				if (responseText[0][0] == "none") {
//					if no products found
					$('.dataTables_empty').text("No results found.");
				} else {				
//					if products are found, add them to search table
					for (var i = 0; i < responseText.length; i++) { 
						table.row.add( {
							"DT_RowId":	"searchProd-" + responseText[i][0] ,
							"s_no": i+1 ,
				            "material_no": 	responseText[i][1].split("?").join("") ,
				            "brand/make":	responseText[i][2] ,
				            "packing": 		responseText[i][3].split("?").join("") ,
				            "product_name": responseText[i][4].split("?").join("") ,
				            "consumer_rate": responseText[i][5].split("?").join("") ,
				            "gst_rate": 	responseText[i][6] ,
				            "hsn_code": 	responseText[i][7] ,
				            "section": 		responseText[i][8] ,
				            "add_item_to_Bill": "<button class=\"btn btn-outline-success btn-sm add-btn\">add item</button>"
				        } );
					}
					table.draw();
				}
				$('#load-result').hide();
//				scroll to search table
				$('html, body').animate({
                    scrollTop: $($('.row-div')[1]).offset().top
                }, 500);
			},
			fail: function(){
				$(loadbtn).hide();
				alert("Some error occured!");
			}
		});
	});

//	implementation for add button of each product
	$(document).on("click", '.add-btn', function() { 										
		var n = $('#bill-tbl tbody').children().length;
		var mnid = $(this).parent().parent().attr("id");
		mnid = mnid.substring(mnid.indexOf('-')+1);
		if (n>0){
//			product validation if it already exist in bill or not
			if ($('#bill-tbl tr#billProd-'+mnid).length > 0){
				alert("item already added in bill!");
				return 1;
			}
		}
		var pn = $(this).parent().parent().find('td:nth-child(5)');
//		create table row with product id as rows id
		var item_tr = "<tr id='billProd-" + mnid + "'>"+
//		S no
		"<td class=\"td-center\">"+ n + "</td>" +			
//		hsncode editable field
		"<td><input type=\"text\" class=\"form-control form-control-sm\" value=\""+ pn.next().next().next().text() +"\"></td>" +
//		Item Name with Description
		"<td>"+ pn.text() + "</td>" +			
// 		Make/Brand
		"<td>"+ pn.prev().prev().text() + "</td>" +											
		
//		Quantity editable field
		"<td> <div class=\"input-group input-group-sm\">" +									
			"<div class=\"input-group-prepend\">" +
				"<button class=\"btn btn-sm btn-outline-secondary\" id=\"qty-decbtn\">-</button> </div>" +
			"<input type=\"number\" id=\"qty-input\" class=\"form-control form-control-sm bill-inputs qtyNprc\" value=\"1\" min=\"1\"> " +
			"<div class=\"input-group-append\">" +
				"<button class=\"btn btn-sm btn-outline-secondary\" id=\"qty-incbtn\">+</button> </div>" +
		"</div></td>" +																		
//		price/rate editable field
		"<td><input type=\"number\" class=\"form-control form-control-sm bill-inputs qtyNprc\" value=\""+ ( ($.isNumeric(pn.next().text())) ? pn.next().text() : 0 ) +"\"></td>" +										// Price/Rate
//		discount 
		"<td><div class=\"billinput-divs\">" +
		"<input type=\"number\" id=\"bil-input-disc\" class=\"form-control form-control-sm bill-inputs percent\" value=\"0\" min=\"0\" max=\"100\">"+ 
		"<label class=\"billinput-labels\" for=\"bill-input-disc\">%</label>" +
		"</div></td>" +						
//		gst rate editable field
		"<td><div class=\"billinput-divs\">" +
		"<input type=\"number\" id=\"bil-input-gst\" class=\"form-control form-control-sm bill-inputs percent\" value=\""+ ( ($.isNumeric(pn.next().next().text())) ? pn.next().next().text() : 0 ) +"\" min=\"0\" max=\"100\">" + 
		"<label class=\"billinput-labels\" for=\"bill-input-gst\">%</label>" +
		"</div></td>" +					
//		remove button for each product to delete it from bill table
		"<td class=\"td-center\"> <button class=\"btn btn-outline-danger btn-sm rem-btn\">remove</button> </td></tr>"	;													// remove button   - niharika
		
		$('#billtbl-empty').hide();
		$('#bill-tbl tbody').append(item_tr);
	});

//	implementation for remove button of each product in bill table
	$(document).on("click", '.rem-btn', function() { 
		$(this).parent().parent().nextAll().each(function(i, e){
			var cur_sno = $(e).find('td:first');
			cur_sno.text(parseInt(cur_sno.text())-1);
		});
		
		if ($('#bill-tbl tbody').children().length == 2){
			$(this).parent().parent().remove();
			$('#billtbl-empty').show();
		} else
			$(this).parent().parent().remove();
	});
	
//	implementation for increament and decreament buttons for quantity field of each product in bill table
	$(document).on("click", '#qty-incbtn', function() {
		++$(this).parent().prev('input').get(0).value;
		$(this).parent().prev('input').trigger("change");
	});
	$(document).on("click", '#qty-decbtn', function() {
		var inpVal = $(this).parent().next('input');
		if ( !(inpVal.val()<=1) ){ 
			--$(this).parent().next('input').get(0).value;
			inpVal.trigger("change");
		}
	});

//	quantity and price field validation, must be numeric
	$(document).on("change", '.bill-inputs.qtyNprc', function() {
		if ( !$.isNumeric($(this).val()) && !$(this).hasClass('invalid'))	$(this).addClass('invalid');
		else															 	$(this).removeClass('invalid');
	});

//	discount and gst field validation, must be numeric and in the range between 0 to 100
	$(document).on("change", '.bill-inputs.percent', function() {
		if ( (0 > $(this).val() || $(this).val() > 100 || $(this).val() == "") && !$(this).hasClass('invalid'))	
			$(this).addClass('invalid');
		else															 					
			$(this).removeClass('invalid');
	});
	
//	implementation for export to pdf and excel buttons 
	$('.exportbtn').click(function(){
//		validation of all textfields present in bill table for each product
		var invalidFlag = 0;
		if ($('#termsNcondition').val() == ""){
			$('#termsNcondition').addClass('invalid');
			invalidFlag = 1;
		} else if ($('#termsNcondition').hasClass('invalid')) 
			$('#termsNcondition').removeClass('invalid');
		
		$('.bill-inputs').each(function(i, e) {
			if ($(e).hasClass('invalid')) 
				invalidFlag = 1;
		});
		
		if (invalidFlag==1) {
			$('#exportbtn-errormsg').css("display", "block");
			return 1;
		} else {
			$('#exportbtn-errormsg').hide();
		}

//		generating json to send to export service
		var termsNcondition = $('#termsNcondition').val();
		var billProdList = [];
		$('#bill-tbl tbody tr').not(':first').each(function(i,e){ 
		    var temp = [];
		    $(e).children().each(function(i,e){
		        if (i==0 || i==2 || i==3) 			temp.push($(e).html());
		        if (i==1 || i==4 || i==5)			temp.push($(e).find('input').val());
		        if (i==6 || i==7) 					temp.push($(e).find('input').val()+"%");
		    });
		    billProdList.push(temp);
		});
		var exportbtnid = $(this).attr("id");
		if (exportbtnid == "exportbtn-pdf") {
			var dataType = 'application/pdf';	var action = 'exportToPDF';  var loadbtn = '#load-export-pdf';
		} else {
			var dataType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';	var action = 'exportToExcel';  var loadbtn = '#load-export-xlsx';
		}
		$(loadbtn).show();
		$.ajax({
			url: 'ExportService',
			type: 'POST',	
			data: {
				prodlist: JSON.stringify(billProdList),
				termsNcondition: termsNcondition,
				action: action
			},
			success: function(data) {
				$(loadbtn).hide();
				alert("File is saved at Desktop!");
			},
			fail: function(){
				$(loadbtn).hide();
				alert("Some error occured!");
			}
		});
	});
	
//	implementation for display of scroll to top icon on page scroll
	$(window).scroll(function() {
		if ( $(window).scrollTop() > 300 ) {
			$('#scrolltop-icon').fadeIn();
		} else {
			$('#scrolltop-icon').fadeOut();
		}
	});
//	scroll to top when clicked
	$('#scrolltop-icon').click(function() {
		$('html, body').animate({
			scrollTop: 0
		}, 500);
		return false;
	});
	
//##########################################################################################################################################
		
//  jquery for createnew.jsp ###############################################################################################################

//	create textfields validation must be numeric
	$(document).on("change", '.create-inputs-num', function() {
		if ( $(this).val()!="" && !$.isNumeric($(this).val()) && !$(this).hasClass('invalid') ){	
			$(this).addClass('invalid');
			$(this).parent().next('.errormsg').show();
		} else {								 	
			$(this).removeClass('invalid');
			$(this).parent().next('.errormsg').hide();
		}
	});
	
//	implementation for "create products" button to add new product to database
	$('#createbtn').on('click', function(){
		$('#load-create').show();
		var invalidFlag = 0;
		$('.create-inputs-num').each(function(i, e) {
			if ( $(e).val()!="" && !$.isNumeric($(e).val()) && !$(e).hasClass('invalid')) {	
				$(e).addClass('invalid');
				$(e).parent().next('.errormsg').show();
				invalidFlag = 1;
			} else if ($.isNumeric($(e).val()) && $(e).hasClass('invalid')) {	
				$(e).removeClass('invalid');
				$(e).parent().next('.errormsg').hide();
			}				
		});
		$('.create-inputs-req').each(function(i, e) {
			if ( $(e).val()=="" && !$(e).hasClass('invalid')) {	
				$(e).addClass('invalid');
				invalidFlag = 1;
			} else if ($(e).val()!="" && $(e).hasClass('invalid')) {
				$(e).removeClass('invalid');
			}				
		});
		$('.create-inputs-num, .create-inputs-req').each(function(i,e){
			if ($(e).hasClass('invalid'))	invalidFlag = 1;
		});
		
		if (invalidFlag==1) {
			return 1;
		}
		
//		handling ajax to send new product details to te database
		$.ajax({
			url: 'SearchService', 
			type: 'POST',
			data: {
				materialno: $('#materialno').val(), 	
				brand: $('#brand').val(),
				packing: $('#packing').val(),
				productname: $('#productname').val(),
				consumerrate: $('#consumerrate').val(),
				gstrate: $('#gstrate').val(),
				hscode: $('#hscode').val(),
				section: $('#section').val(),
				action: 'createProduct'
			},
			success: function(response){
				if (response == "success") { 					
					$('#load-create').hide();
					$('.txtfld-tbl').find('td input').each(function(i,e){ $(e).val(""); });	
					$('.txtfld-tbl tr').find('textarea').val("");
					alert("Product created succussfully!");
				} else
					alert("Something went wrong!");
			}
		});
	});
	
});