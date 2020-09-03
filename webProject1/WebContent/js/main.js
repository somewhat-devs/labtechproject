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

	$('.clr-srchinp-btn').on('click', function() {
		$(this).prev('input').val("");
		$('#' + $(this).prev('input').attr('id') + '-errormsg').hide();
	});

	$('input').keypress(function(event) {
		if (event.keyCode == 13) 
			$('#resultbtn').click();
	});
	
	$('#clr-searchresult-btn').on('click', function(){
		$('.txtfld-tbl').find('td input').each(function(i,e){ $(e).val(""); });
		$('#custrate').val("none");
		$('#main-tbl').DataTable().clear().draw();		
	});
	
	$('#clr-billresult-btn').on('click', function(){
		$('#bill-tbl tbody tr').each( function(i, e){ 
			$(e).remove(); 
		});
	});
	
	$('#main-tbl').DataTable({
		"pageLength": 5,
		"pageType": "simple_numbers",
		"ordering": true,
		"searching": false,
        "columns": [
            { "data": "s_no" },
            { "data": "material_no" },
            { "data": "packing" },
            { "data": "product_name" },
            { "data": "consumer_rate" },
            { "data": "gst_rate" },
            { "data": "hsn_code" },
            { "data": "section" },
            { "data": "add_item_to_Bill" }
        ],
		"columnDefs": [{ "targets": [1, 2, 3, 6, 7, 8], "orderable": false },
		               { "width": "30%", "targets": 3 }, 
		               { "className": "td-center", "targets": [ 0, 8 ] }
		]
	}).draw(true);

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
		$('#load-result').show();

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
//				 console.log(responseText);
				var resp = "";
				var table = $('#main-tbl').DataTable();
				if (responseText[0][0] == "none") {
					$('.dataTables_empty').text("No results found.");
				} else {					
					for (var i = 0; i < responseText.length; i++) { 
						table.row.add( {
							"DT_RowId":	"searchProd-" + responseText[i][0] ,
							"s_no": i+1 ,
				            "material_no": 	responseText[i][0] ,
				            "packing": 		responseText[i][1] ,
				            "product_name": responseText[i][2] ,
				            "consumer_rate": responseText[i][3] ,
				            "gst_rate": 	responseText[i][4] ,
				            "hsn_code": 	responseText[i][5] ,
				            "section": 		responseText[i][6] ,
				            "add_item_to_Bill": "<button class=\"btn btn-outline-success btn-sm add-btn\">add item</button>"
				        } );
					}
					table.draw();
				}
				$('#load-result').hide();
			}
		});
	});

	$(document).on("click", '.add-btn', function() { 										// block for managing action of add button
		var n = $('#bill-tbl tbody').children().length;
		var mnid = $(this).parent().parent().attr("id");
		if (n>0){
			mnid = mnid.substring(mnid.indexOf('-')+1);
			if ($('#bill-tbl tr#billProd-'+mnid).length > 0){
				alert("item already added in bill!");
				return 1;
			}
		}
		var pn = $(this).parent().parent().find('td:nth-child(4)');
		var item_tr = "<tr id='billProd-" + pn.prev().prev().text() + "'>"+
		"<td class=\"td-center\">"+ (n+1) + "</td>" +										// S. no
		"<td>"+ pn.next().next().next().text() +"</td>" +									// HSN code
		"<td>"+ pn.text() + "</td>" +														// Item Name with Description
		"<td></td>"	+																		// Make/Brand
		
		"<td> <div class=\"input-group input-group-sm\">" +												// Quantity
			"<div class=\"input-group-prepend\">" +
				"<button class=\"btn btn-sm btn-outline-secondary\" id=\"qty-decbtn\">-</button> </div>" +
			"<input type=\"number\" id=\"qty-input\" class=\"form-control form-control-sm bill-inputs\" value=\"1\" min=\"1\"> " +
			"<div class=\"input-group-append\">" +
				"<button class=\"btn btn-sm btn-outline-secondary\" id=\"qty-incbtn\">+</button> </div>" +
		"</div></td>" +																		
		
		"<td>"+ pn.next().text() + "</td>" +												// Price/Rate
		"<td> <div class=\"billinput-divs\"><input type=\"number\" id=\"bil-input-disc\" class=\"form-control form-control-sm bill-inputs\" placeholder=\"disc?\" value=\"0\" min=\"0\" max=\"100\">"+ 
		"<label class=\"billinput-labels\" for=\"bill-input-disc\">%</label></div></td>" +						// Discount  - Parul
		"<td> <div class=\"billinput-divs\"><input type=\"number\" id=\"bil-input-gst\" class=\"form-control form-control-sm bill-inputs\" placeholder=\"gst?\" value=\"0\" min=\"0\" max=\"100\">" + 
		"<label class=\"billinput-labels\" for=\"bill-input-gst\">%</label></div></td>" +						// GST
		
		"<td class=\"td-center\"> <button class=\"btn btn-outline-danger btn-sm rem-btn\">remove</button> </td></tr>"	;													// remove button   - niharika
		
		$('#bill-tbl tbody').append(item_tr);
	});

	$(document).on("click", '.rem-btn', function() { 									
		$(this).parent().parent().remove();
	});
	
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

	$(document).on("change", '.bill-inputs', function() {
		if ( !$.isNumeric($(this).val()) && !$(this).hasClass('invalid'))	$(this).addClass('invalid');
		else															 	$(this).removeClass('invalid');
	});
	
	$('#exportbtn').click(function(){
		var invalidFlag = 0;
		$('.bill-inputs').each(function(i, e) {
			if ($(e).hasClass('invalid')) {
				invalidFlag = 1;
				$('#exportbtn-errormsg').show();
				return 1;
			}
		});
		if (invalidFlag==0)				
			$('#exportbtn-errormsg').hide();
		
		$('#load-export').show();
		var billProdList = [];
		$('#bill-tbl tbody tr').each(function(i,e){ 
		    var temp = [];
		    $(e).children().each(function(i,e){
		        if (i==0 || i==1 || i==2 || i==5) temp.push($(e).html());
		        if (i==3) temp.push("labtech");
		        if (i==4 || i==6 || i==7) temp.push($(e).find('input').val());
		    });
		    billProdList.push(temp);
		});
//		console.log(billProdList);
		$.ajax({
			url: 'ExportService',
			type: 'POST',	
			dataType: 'application/pdf',
			data: {
				prodlist: JSON.stringify(billProdList)
			},
			success: function(data) { 		
			},
			fail: function(){
				alert("Some error occured!");
			}
		});
		$('#load-export').hide();
	});
});