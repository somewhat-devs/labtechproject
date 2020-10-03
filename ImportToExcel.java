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

@WebServlet("/ImportToExcel")
public class ImportToExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public ImportToExcel() {
    	
        super();
        
    }
    int discount=1;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			
			HSSFRow row1 = sheet.createRow(1);
	        HSSFCell cell11 = row1.createCell(1);
	        cell11.setCellValue(new HSSFRichTextString("Quot  No. KBI/Q-III/2020-21"));
	        HSSFCell cell14 = row1.createCell(4);
	        cell14.setCellValue(new HSSFRichTextString("Quotations: Date : 21-09-2020 15:07:25" ));
	        
	        HSSFRow row3 = sheet.createRow(3);
	        HSSFCell cell32 = row3.createCell(2);
	        cell32.setCellValue(new HSSFRichTextString("Dear Sir, In response to your enquiry, we are pleased to offer our rates as under:- "));
	        HSSFRow row4 = sheet.createRow(4);
	        HSSFCell cell42 = row4.createCell(2);
	        cell42.setCellValue(new HSSFRichTextString("Department of Pharmaceuticals"));
	        
	        String[] head = {"sno", "hsncode", "name", "make", "qty", "price", "discount", "gst","totalprice" };
	        
	        String[][] ar = { 
	        		{"1 ", "3821 00 00", "Penicillin Streptomycin Powder # w/ 5000 units Penicillin and 5 mg Streptomycin per ml in 0.9% normal saline when reconstituted with sterile tissue culture grade water to indicated volume", "Himedia", "1" , "4540" ,"0","18"},
	                {"2", " 3821 00 00",
	                		"L-Glutamine-Penicillin-Streptomycin Solution w/ 200mM LGlutamine", "Himedia", "1", "4300", "0", "18"}               		
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
	        
	        HSSFRow row13 = sheet.createRow(13);
	        row13.setHeightInPoints((short) 15);
			for (int i = 0; i < 9; i++) {
				HSSFCell cellhead = row13.createCell(i);
				cellhead.setCellValue(new HSSFRichTextString(head[(i)]));
				cellhead.setCellStyle(stylehead);
			}
			

	        int totalprice = 0;
	        float total=0;
	     
			
			for (int i = 0; i < ar.length; i++) {
				HSSFRow rowproduct = sheet.createRow(i+14);
				for (int j = 0; j < 10; j++) {
					if (j==2) {
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(new HSSFRichTextString(ar[i][j]));
						cellproduct.setCellStyle(styleprodname);
						sheet.setColumnWidth((j+1), 65 * 256);	
						continue;
					} if (j==1 || j==3) {
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(new HSSFRichTextString(ar[i][j]));
						cellproduct.setCellStyle(style);
						sheet.setColumnWidth((j+1), 15 * 256);	
						continue;
					} if (j==0 || j==4 || j==5 || j==6 || j==7) {
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(Double.valueOf((ar[i][j])));
						cellproduct.setCellStyle(style);
						sheet.setColumnWidth((j+1), 15 * 256);	
                        continue;
                                         
					} if (j==8 || (j==9) {
						if(discount == 1){ 
							float p1 = Float.valueOf(ar[i][5]) * Float.valueOf(ar[i][4]);
							float p2 = p1  - (p1  * (Float.valueOf(ar[i][6])/100));
							float p3 = p2  + (p2  * (Float.valueOf(ar[i][7])/100));
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(Float.valueOf(p3));
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 15 * 256);	
							totalprice += p3;
				    	}
				        	
				        	
				        else
				        {
				        	float p1 = Float.valueOf(ar[i][5]) * Float.valueOf(ar[i][4]);
							float p3 = p1  + (p1  * (Float.valueOf(ar[i][7])/100));
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(Float.valueOf(p3));
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 15 * 256);	
							totalprice += p3;
							
				       	}
				    total =  total+ totalprice;
					HSSFCell cellproduct = rowproduct.createCell(j+1);
					cellproduct.setCellValue(Float.valueOf(total));
					cellproduct.setCellStyle(style);
					sheet.setColumnWidth((j+1), 15 * 256);
					}
				}
			}
		
//	        
//	        HSSFRow row10 = sheet.createRow(10);
//	        HSSFCell cell102 = row10.createCell(2);
//	        cell102.setCellValue(new HSSFRichTextString("Terms and Conditions "));
//	        
//	        HSSFRow row11 = sheet.createRow(11);
//	        HSSFCell cell112 = row11.createCell(2);
//	        cell112.setCellValue(new HSSFRichTextString("1. The Intellectual Property disclosure will inform users that the contents, logo and other visual media you created is your property and is protected by copyright laws "));
//	        
//	        HSSFRow row12 = sheet.createRow(12);
//	        HSSFCell cell122 = row12.createCell(2);
//	        cell122.setCellValue(new HSSFRichTextString("2. A Termination clause will inform that users' accounts on your website and mobile app or users' access to your website and mobile (if users can't have an account with you) can be terminated in case of abuses or at your sole discretion "));
//
//	       	
	       	
	        
	        		
	        	
	        
	        
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
