package pricelist;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

@WebServlet("/CreateExcel1")
public class CreateExcel1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public CreateExcel1() {
    	
        super();
        
    }
    int discount=1;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			
		
			
			HSSFRow row1 = sheet.createRow(1);
	        HSSFCell cell12 = row1.createCell(2);
	        cell12.setCellValue(new HSSFRichTextString("Quot  abc-123"));
	        
	        HSSFRow row3 = sheet.createRow(3);
	        HSSFCell cell32= row3.createCell(2);
	        cell32.setCellValue(new HSSFRichTextString("To: "));
	        
	        HSSFRow row4 = sheet.createRow(4);
	        HSSFCell cell42= row4.createCell(2);
	        cell42.setCellValue(new HSSFRichTextString("Navin florine Int. Ltd.(Kind Att. Mr. Madhusudan Sarwate)Indore, MP  "));
	        		
	        
	        HSSFCell cell14 = row1.createCell(4);
	        cell14.setCellValue(new HSSFRichTextString("21-09-2020 15:07:25" ));
	        
	       
	        HSSFRow row6 = sheet.createRow(6);
	        HSSFCell cell62 = row6.createCell(2);
	        cell62.setCellValue(new HSSFRichTextString("Subject:  temporary subjec" ));
	        
	        HSSFRow row8 = sheet.createRow(8);
	        HSSFCell cell82 = row8.createCell(2);
	        cell82.setCellValue(new HSSFRichTextString("Dear Sir, In response to your enquiry, we are pleased to offer our rates as under:- "));
	       
	        		
	       
	        
	        String[] head = {"sno",  "name", "make", "qty", "price", "discount", "gst", "Price (incl. GST)","hsn code","total price"  };
	        
	        String[][] ar = { 
	        		{"1 ",  "Penicillin Streptomycin Powder # w/ 5000 units Penicillin and 5 mg Streptomycin per ml in 0.9% normal saline when reconstituted with sterile tissue culture grade water to indicated volume", "Himedia", "1" , "9070" ,"10","18","9632.34","3821 00 00"},
	                       		
	        };
	        
	        HSSFCellStyle stylehead = workbook.createCellStyle();
	        HSSFFont fonthead = workbook.createFont();
	        fonthead.setBold(true);
	        stylehead.setAlignment(HorizontalAlignment.CENTER);
	        stylehead.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
	        stylehead.setFont(fonthead);
	        
	        HSSFCellStyle styleprodname = workbook.createCellStyle();
	        HSSFFont fontprodname = workbook.createFont();
	        styleprodname.setWrapText(true);
	        styleprodname.setFont(fontprodname);
	        
	        HSSFCellStyle style = workbook.createCellStyle();
	        HSSFFont font = workbook.createFont();
	        style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
	        style.setFont(font);
	        
	        HSSFRow row10 = sheet.createRow(10);
	        row10.setHeightInPoints((short) 15);
			for (int i = 0; i < 10; i++) {
				HSSFCell cellhead = row10.createCell(i);
				cellhead.setCellValue(new HSSFRichTextString(head[(i)]));
				cellhead.setCellStyle(stylehead);
			}
			


	     
			
			for (int i = 0; i < ar.length; i++) {
				HSSFRow rowproduct = sheet.createRow(i+14);
				for (int j = 0; j < 10; j++) {
					if (j==1) {
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(new HSSFRichTextString(ar[i][j]));
						cellproduct.setCellStyle(styleprodname);
						sheet.setColumnWidth((j+1), 65 * 256);	
						continue;
					} if (j==2 || j==8) {
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(new HSSFRichTextString(ar[i][j]));
						cellproduct.setCellStyle(style);
						sheet.setColumnWidth((j+1), 15 * 256);	
						continue;
					} if (j==0 || j==3 || j==4 || j==5 || j==6 || j==7) {
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(Double.valueOf((ar[i][j])));
						cellproduct.setCellStyle(style);
						sheet.setColumnWidth((j+1), 15 * 256);	
                        continue;
                                         
					} if (j==9) {
						if(discount == 1){ 
							float p1 = Float.valueOf(ar[i][5]) * Float.valueOf(ar[i][4]);
							float p2 = p1  - (p1  * (Float.valueOf(ar[i][6])/100));
							float p3 = p2  + (p2  * (Float.valueOf(ar[i][7])/100));
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(Float.valueOf(p3));
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 15 * 256);	
							
				    	}
				        	
				        	
				        else
				        {
				        	float p1 = Float.valueOf(ar[i][5]) * Float.valueOf(ar[i][4]);
							float p3 = p1  + (p1  * (Float.valueOf(ar[i][7])/100));
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(Float.valueOf(p3));
							cellproduct.setCellStyle(style);
							
							
				       	}
				    
					}
				}
			}
		
	        
	        HSSFRow row20 = sheet.createRow(20);
	        HSSFCell cell202 = row20.createCell(2);
            cell202.setCellValue(new HSSFRichTextString("Terms and Conditions "));
	        
	        HSSFRow row21 = sheet.createRow(21);
	        HSSFCell cell212 = row21.createCell(2);
	        cell212.setCellValue(new HSSFRichTextString("1) All quotated prices are valid for 30 daysfrom date of quotation. Freight charge Rs. one thousand(1000) extra. If order value less than Rs. ten thousand(10000)"));
	     	        
	        HSSFRow row22 = sheet.createRow(22);
            HSSFCell cell222 = row22.createCell(2);
	        cell222.setCellValue(new HSSFRichTextString("2)Amount exclude any applicable taxes. Applicable taxes will be separately stated on the invoice at time of billing. "));

	        HSSFRow row23 = sheet.createRow(23);
            HSSFCell cell232 = row23.createCell(2);
	        cell232.setCellValue(new HSSFRichTextString("3)All prices in this quote/document are exclusive of ST and any other applicable taxes,by whatever name called,unless otherwise mutually agereed in writing by the parties. Customer agrees to pay all lawfull and applicable taxes,whether imposed now or i the future,by the relevant authorities in accordance with the relevant legislation, rules and regulations.GST will be charged as per mentioned above.Octroi,if and as applicable will be charges extra at the time of delivery/invoicing and octroi is to be paid by consignee directly to the Octroi authority. "));

	        HSSFRow row24 = sheet.createRow(24);
	        HSSFCell cell242 = row24.createCell(2);
            cell242.setCellValue(new HSSFRichTextString("4)Please refrence your Kasliwal Brothers quotation number on your purchase orders.Returns or claims for loss or damage will not be accepted after 15 days from the date of factory shipment.All retrns must be pre-authorised in writing.Cheques to be made in favour of Kasliwal Brothers,Indore."));
	        
            HSSFRow row25 = sheet.createRow(25);
	        HSSFCell cell252 = row25.createCell(2);
            cell252.setCellValue(new HSSFRichTextString("5)Please mention your CST/TIN No./GST No. in your Purchase Order."));
	        
            HSSFRow row26 = sheet.createRow(26);
	        HSSFCell cell262 = row26.createCell(2);
            cell262.setCellValue(new HSSFRichTextString("6)If there is any Road permit/Entry/Exit form is required to ship the material then please courier yhe same to below Address:"));
	        
            HSSFRow row27 = sheet.createRow(27);
	        HSSFCell cell272 = row27.createCell(2);
            cell272.setCellValue(new HSSFRichTextString("7)Please mention Quotation number on your Purchase Order and send it to on manoj_maru@hotmail.com and copy to rana_1990sagar@yahoo.com "));
	        
            HSSFRow row28 = sheet.createRow(28);
	        HSSFCell cell282 = row28.createCell(2);
            cell282.setCellValue(new HSSFRichTextString(" Payment Terms:30 Days subject to Credit Approval."));
	        
            HSSFRow row29 = sheet.createRow(29);
	        HSSFCell cell292 = row29.createCell(2);
            cell292.setCellValue(new HSSFRichTextString("Incoterms:DDP Delivered Duty Paid "));
	        
            HSSFRow row32 = sheet.createRow(32);
	        HSSFCell cell322= row32.createCell(2);
            cell322.setCellValue(new HSSFRichTextString("Payment:"));
	        
            HSSFRow row33 = sheet.createRow(33);
	        HSSFCell cell332= row33.createCell(2);
            cell332.setCellValue(new HSSFRichTextString(" point 1    "));
	        
            HSSFRow row34 = sheet.createRow(34);
	        HSSFCell cell342= row34.createCell(2);
            cell342.setCellValue(new HSSFRichTextString(" point 2      "));
	        
            HSSFRow row37 = sheet.createRow(37);
	        HSSFCell cell373= row37.createCell(3);
            cell373.setCellValue(new HSSFRichTextString("BANK details 1"));
	        
           
	        HSSFCell cell375= row37.createCell(5);
            cell375.setCellValue(new HSSFRichTextString("BANK details 1"));
	        		
            HSSFRow row38 = sheet.createRow(38);
	        HSSFCell cell383= row38.createCell(3);
            cell383.setCellValue(new HSSFRichTextString("BANK details 2")); 	
	        
            HSSFCell cell385= row38.createCell(5);
            cell385.setCellValue(new HSSFRichTextString("BANK details 2")); 	
            
            HSSFRow row40 = sheet.createRow(40);
	        HSSFCell cell402= row40.createCell(2);
            cell402.setCellValue(new HSSFRichTextString("Point 3")); 	
            
            HSSFRow row41 = sheet.createRow(41);
	        HSSFCell cell412= row41.createCell(2);
            cell412.setCellValue(new HSSFRichTextString("Point 4")); 	
            

            HSSFRow row42 = sheet.createRow(42);
	        HSSFCell cell422= row42.createCell(2);
            cell422.setCellValue(new HSSFRichTextString("Point 5")); 	
            

            HSSFRow row43 = sheet.createRow(43);
	        HSSFCell cell432= row43.createCell(2);
            cell432.setCellValue(new HSSFRichTextString("Point 6")); 	
            
            
            HSSFRow row46 = sheet.createRow(46);
	        HSSFCell cell462= row46.createCell(2);
            cell462.setCellValue(new HSSFRichTextString("Please mention Quotation number on your Purchase Order and send it to on _____________________________and copy to___________________________")); 	
            
            HSSFRow row50 = sheet.createRow(50);
	        HSSFCell cell503= row50.createCell(3);
            cell503.setCellValue(new HSSFRichTextString("SUBJECT TO INDORE JURISDICTION	")); 	 
	        
            HSSFCell cell506= row50.createCell(6);
            cell506.setCellValue(new HSSFRichTextString("FOR KASLIWAL BROTHERS")); 	 
            
            HSSFRow row52 = sheet.createRow(52);
	        HSSFCell cell523= row52.createCell(3);
            cell523.setCellValue(new HSSFRichTextString("GST NO. 232323423I2J23I4IJ	")); 	 
	        
            HSSFCell cell526= row52.createCell(6);
            cell526.setCellValue(new HSSFRichTextString("Authorized Signatory,")); 
	        
            
	        
	        
	        workbook.write(new FileOutputStream(new File("D://temp.xls")));
	        workbook.close();
	        
	        
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
