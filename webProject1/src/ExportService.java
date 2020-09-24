import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;



@WebServlet("/ExportService")
public class ExportService extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    public ExportService() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		String[] tblheads = { "S. No.", "HSN Code", "Item name with description", "Make", "Qty.", "Price",	"Discount", "GST" };
		String prodlist[][] = new Gson().fromJson(request.getParameter("prodlist"), String[][].class);
		String termsNcondition = request.getParameter("termsNcondition");

		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HHmmss");
		Date date = new Date();
		String filename = "Quotation-" + formatter.format(date) ;
		File desktop = new File(System.getProperty("user.home"), "/Desktop");
		String url = request.getRequestURL().toString();
		URL imFile = new URL(url.substring(0, url.lastIndexOf('/')) + "/images/logo.jpg");
		
		if (action.equals("exportToPDF")) {
			try {
				
			    PdfWriter writer = new PdfWriter(desktop+"\\"+filename+".pdf");
				PdfDocument pdf = new PdfDocument(writer);
				pdf.addNewPage();
				pdf.getDocumentInfo().setCreator("Kasliwal Brothers Pharmaceuticals");
				pdf.getDocumentInfo().setAuthor("Kasliwal Brothers Pharmaceuticals");
				pdf.getDocumentInfo().addCreationDate();
				Document document = new Document(pdf, new PageSize(PageSize.A4));

				ImageData data = ImageDataFactory.create(imFile);
				Image image = new Image(data);
				image.setAutoScale(true);
				document.add(image);

				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				Text text = new Text("Quot  No. KBI/Q-III/2020-21").setFontSize(10);
				document.add(new Paragraph(text));

				formatter = new SimpleDateFormat("dd MMMM yyyy");
				text = new Text("Quotations: Date : " + formatter.format(date)).setFontSize(10);
				document.add(new Paragraph(text));

				document.add(new Paragraph(" "));
				Paragraph paragraph = new Paragraph();
				paragraph.add(new Text("Dear Sir,\n").setFontSize(10));
				paragraph.add(new Text("In response to your enquiry, we are pleased to offer our rates as under:-\n")
						.setFontSize(10));
				paragraph.add(new Text("Department of Pharmaceuticals").setBold().setFontSize(10));
				document.add(paragraph);

				float[] pointColumnWidths = { 5, 5, 5, 5, 5, 5, 5, 5 };
				Table table = new Table(pointColumnWidths);
				table.setWidth(UnitValue.createPercentValue(100));
				table.setMarginTop(30);

				for (int i = 0; i < 8; i++) {
					table.addCell(new Cell().add(new Paragraph(tblheads[i])).setTextAlignment(TextAlignment.CENTER)
							.setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(10)
							.setFontColor(new DeviceRgb(0, 112, 192)).setPaddings(5, 2, 5, 2)
							.setBackgroundColor(new DeviceRgb(220, 233, 241))
							.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
				}

				for (int i = 0; i < prodlist.length; i++) {
					for (int j = 0; j < 8; j++) {
						if (j == 2) {
							table.addCell(new Cell().add(new Paragraph(prodlist[i][j])).setFontSize(10)
									.setPaddings(5, 2, 5, 2)
									.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
							continue;
						}
						table.addCell(new Cell().add(new Paragraph(prodlist[i][j])).setFontSize(10)
								.setPaddings(5, 2, 5, 2).setTextAlignment(TextAlignment.CENTER)
								.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
					}
				}

				document.add(table);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text("Terms and Conditions : ").setFontSize(10));
				document.add(paragraph);
				paragraph = new Paragraph();
				paragraph.add(new Text(termsNcondition).setFontSize(10));
				document.add(paragraph);
				
				document.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equals("exportToExcel")) {
			try {
								
				HSSFWorkbook workbook = new HSSFWorkbook();
		        HSSFSheet sheet = workbook.createSheet();
		        
				InputStream is = imFile.openStream();
				byte[] bytes = IOUtils.toByteArray(is);
				int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
				is.close();
				CreationHelper helper = workbook.getCreationHelper();
				Drawing<?> drawing = sheet.createDrawingPatriarch();
				ClientAnchor anchor = helper.createClientAnchor();
				anchor.setCol1(2);
				anchor.setRow1(1);
				Picture pict = drawing.createPicture(anchor, pictureIdx);
				pict.resize(5);
		        
		        HSSFCellStyle style = workbook.createCellStyle();
		        HSSFFont font = workbook.createFont();
		        style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		        style.setFont(font);
		        
		        HSSFRow row7 = sheet.createRow(7);
		        row7.setHeightInPoints((short) 15);
		        HSSFCell cell71 = row7.createCell(1);
		        cell71.setCellValue(new HSSFRichTextString("Quot  No. KBI/Q-III/2020-21"));
		        cell71.setCellStyle(style);
		        HSSFCell cell75 = row7.createCell(5);
		        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		        cell75.setCellValue(new HSSFRichTextString("Quotations: Date : " + formatter.format(date)));
		        cell75.setCellStyle(style);
		        
		        HSSFRow row9 = sheet.createRow(9);
		        row9.setHeightInPoints((short) 15);
		        HSSFCell cell91 = row9.createCell(1);
		        cell91.setCellValue(new HSSFRichTextString("Dear Sir,"));
		        cell91.setCellStyle(style);
		        HSSFRow row10 = sheet.createRow(10);
		        row10.setHeightInPoints((short) 15);
		        HSSFCell cell101 = row10.createCell(1);
		        cell101.setCellValue(new HSSFRichTextString("In response to your enquiry, we are pleased to offer our rates as under:-"));
		        cell101.setCellStyle(style);
		        HSSFRow row11 = sheet.createRow(11);
		        row11.setHeightInPoints((short) 15);
		        HSSFCell cell111 = row11.createCell(1);
		        cell111.setCellValue(new HSSFRichTextString("Department of Pharmaceuticals"));
		        cell111.setCellStyle(style);	
		        
		        HSSFCellStyle stylehead = workbook.createCellStyle();
		        HSSFFont fonthead = workbook.createFont();
		        fonthead.setBold(true);
		        stylehead.setAlignment(HorizontalAlignment.CENTER);
		        style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		        stylehead.setFont(fonthead);
		        
		        HSSFCellStyle styleprodname = workbook.createCellStyle();
		        HSSFFont fontprodname = workbook.createFont();
		        styleprodname.setWrapText(true);
		        styleprodname.setFont(fontprodname);
		        
		        HSSFRow row13 = sheet.createRow(13);
		        row13.setHeightInPoints((short) 15);
				for (int i = 1; i < 9; i++) {
					HSSFCell cellhead = row13.createCell(i);
					cellhead.setCellValue(new HSSFRichTextString(tblheads[(i-1)]));
					cellhead.setCellStyle(stylehead);
				}
				
				for (int i = 0; i < prodlist.length; i++) {
					HSSFRow rowproduct = sheet.createRow(i+14);
					for (int j = 0; j < 8; j++) {
						if (j==2) {
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(new HSSFRichTextString(prodlist[i][j]));
							cellproduct.setCellStyle(styleprodname);
							sheet.setColumnWidth((j+1), 65 * 256);	
							continue;
						} if (j==1 || j==3) {
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(new HSSFRichTextString(prodlist[i][j]));
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 15 * 256);	
							continue;
						} if (j==0 || j==4 || j==5) {
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(Double.valueOf((prodlist[i][j])));
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 15 * 256);	
							continue;
						} else {
							HSSFCell cellproduct = rowproduct.createCell(j + 1);
							cellproduct.setCellValue(prodlist[i][j]);
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 15 * 256);
						}
					}
				}
				
		        HSSFRow rowtc1 = sheet.createRow(prodlist.length+17);
		        rowtc1.setHeightInPoints((short) 15);
		        HSSFCell celltc11 = rowtc1.createCell(3);
		        celltc11.setCellValue(new HSSFRichTextString("Terms and Conditions : "));
		        celltc11.setCellStyle(style);
		        HSSFRow rowtc2 = sheet.createRow(prodlist.length+18);
		        HSSFCell celltc12 = rowtc2.createCell(3);
		        celltc12.setCellValue(new HSSFRichTextString(termsNcondition));
		        celltc12.setCellStyle(styleprodname);
		        		        
		        File xlsfile = new File(desktop+"\\"+filename+".xls");
		        workbook.write(xlsfile); 
		        workbook.close();
		        			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		doGet(request, response);
	}

}


