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
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@WebServlet("/CreateExcel")
public class CreateExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public CreateExcel() {
        super();
    }

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
	        
	        String[][] ar = {{"sno", "hsncode", "name", "make", "qty", "price", "discount", "gst"}, 
	        		{"1 ", "3821 00 00", "Penicillin Streptomycin Powder # w/ 5000 units Penicillin and 5 mg Streptomycin per ml in 0.9% normal saline when reconstituted with sterile tissue culture grade water to indicated volume", "Himedia", "1" , "4540" ,"0%","18%"},
	                {"2", " 3821 00 00", "00 00\r\n" + 
	                		"L-Glutamine-Penicillin-Streptomycin Solution w/ 200mM LGlutamine, 10,000 units/ml Penicillin and 10 mg/ml Streptomycin in 0.9% normal saline ", "Himedia", "1", "4300", "0%", "18%"}
	                		
	        };
	        
	        for (int i=0; i<3; i++) {
	        	HSSFRow row = sheet.createRow(i+6);
	        	for (int j=0; j<8; j++) {
	        		HSSFCell cell = row.createCell(j+2);
	    	        cell.setCellValue(new HSSFRichTextString(ar[i][j]));
	        	}
	        }
	        
	        HSSFRow row10 = sheet.createRow(10);
	        HSSFCell cell102 = row10.createCell(2);
	        cell102.setCellValue(new HSSFRichTextString("Terms and Conditions "));
	        
	        HSSFRow row11 = sheet.createRow(11);
	        HSSFCell cell112 = row11.createCell(2);
	        cell112.setCellValue(new HSSFRichTextString("1. The Intellectual Property disclosure will inform users that the contents, logo and other visual media you created is your property and is protected by copyright laws "));
	        
	        HSSFRow row12 = sheet.createRow(12);
	        HSSFCell cell122 = row12.createCell(2);
	        cell122.setCellValue(new HSSFRichTextString("2. A Termination clause will inform that users' accounts on your website and mobile app or users' access to your website and mobile (if users can't have an account with you) can be terminated in case of abuses or at your sole discretion "));

	                
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
