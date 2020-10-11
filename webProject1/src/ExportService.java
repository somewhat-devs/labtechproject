import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
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
		String[] tblheads = { "S. No.", "Item name with description", "Make", "Qty.", "Unit Price",	"Discount", "GST", "Price \n(incl. GST)", "HSN Code" };
		String prodlist[][] = new Gson().fromJson(request.getParameter("prodlist"), String[][].class);
		String otherfields[] = new Gson().fromJson(request.getParameter("otherfields"), String[].class);
		int discount = Integer.parseInt(otherfields[4]);
				
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
				Text text = new Text("Quotation  No: "+ otherfields[0]).setFontSize(10);
				document.add(new Paragraph(text));
				
				text = new Text("Customer Name: "+ otherfields[1]).setFontSize(10);
				document.add(new Paragraph(text));
				
				text = new Text("Subject: "+ otherfields[2]).setFontSize(10);
				document.add(new Paragraph(text));
				
				formatter = new SimpleDateFormat("dd MMMM yyyy");
				text = new Text("Quotations: Date : " + formatter.format(date)).setFontSize(10);
				document.add(new Paragraph(text));

				document.add(new Paragraph(" "));
				Paragraph paragraph = new Paragraph();
				paragraph.add(new Text("Dear Sir,\n").setFontSize(10));
				paragraph.add(new Text("In response to your enquiry, we are pleased to offer our rates as under:-\n")
						.setFontSize(10));
				document.add(paragraph);

				float[] pointColumnWidths;
				if (discount == 1) 
					pointColumnWidths = new float[]{ 5, 5, 5, 5, 5, 5, 5, 5, 5 };
				else
					pointColumnWidths = new float[]{ 5, 5, 5, 5, 5, 5, 5, 5 };
					
				Table table = new Table(pointColumnWidths);
				table.setWidth(UnitValue.createPercentValue(100));
				table.setMarginTop(30);

				for (int i = 0; i < 9; i++) {
					if (i==5) 
						if (discount == 0) 
							continue;
					
					table.addCell(new Cell().add(new Paragraph(tblheads[i]))
							.setTextAlignment(TextAlignment.CENTER)
							.setVerticalAlignment(VerticalAlignment.MIDDLE)
							.setFontSize(10)
							.setFontColor(new DeviceRgb(0, 112, 192))
							.setPaddings(5, 2, 5, 2)
							.setBackgroundColor(new DeviceRgb(220, 233, 241))
							.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
				}

				for (int i = 0; i < prodlist.length; i++) {
					for (int j = 0; j < 9; j++) {
						if (j == 1) {
							table.addCell(new Cell().add(new Paragraph(prodlist[i][j]))
									.setFontSize(10)
									.setPaddings(5, 2, 5, 2)
									.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
							continue;
						} 
						if (j == 5) {
							if (discount == 0) {
								continue;
							}
						}	
						if (j==5 || j==6) {
							table.addCell(new Cell().add(new Paragraph(prodlist[i][j]+"%"))
									.setFontSize(10)
									.setPaddings(5, 2, 5, 2)
									.setTextAlignment(TextAlignment.CENTER)
									.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
							continue;
						}
						table.addCell(new Cell().add(new Paragraph(prodlist[i][j]))
								.setFontSize(10)
								.setPaddings(5, 2, 5, 2)
								.setTextAlignment(TextAlignment.CENTER)
								.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
					}
				}
				for (int j = 0; j < 8; j++) {
					if (j==4) {
						table.addCell(new Cell(1, 2).add(new Paragraph("Total Price: "))
								.setFontSize(10)
								.setPaddings(5, 2, 5, 2)
								.setTextAlignment(TextAlignment.CENTER)
								.setBorder(new SolidBorder(new DeviceRgb(255, 255, 255), 1F)));
						continue;
					}
					if (j==5) {
						if (discount == 1) {
							table.addCell(new Cell().add(new Paragraph(""))
									.setBorder(new SolidBorder(new DeviceRgb(255, 255, 255), 1F)));	
							continue;
						} else {
							continue;
						}
					}
					if (j==6) {
						table.addCell(new Cell().add(new Paragraph(String.valueOf(otherfields[5])))
								.setFontSize(10)
								.setPaddings(5, 2, 5, 2)
								.setTextAlignment(TextAlignment.CENTER)
								.setBorder(new SolidBorder(new DeviceRgb(255, 255, 255), 1F)));
						continue;
					}
					table.addCell(new Cell().add(new Paragraph(""))
							.setBorder(new SolidBorder(new DeviceRgb(255, 255, 255), 1F)));			
				}
				
				document.add(table);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text("Terms and Conditions : ").setFontSize(10));
				document.add(paragraph);
				paragraph = new Paragraph();
				paragraph.add(new Text(otherfields[3]).setFontSize(10));
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
		        int rowcount = 7;
		        
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
		        
		        HSSFRow rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        HSSFCell cellint = rowint.createCell(1);
		        cellint.setCellValue(new HSSFRichTextString("Quotation  No: "+otherfields[0]));
		        cellint.setCellStyle(style);
		        ++rowcount;

		        rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        cellint = rowint.createCell(1);
		        cellint.setCellValue(new HSSFRichTextString("Customer Name: "+ otherfields[1]));
		        cellint.setCellStyle(style);
		        ++rowcount;

		        rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        cellint = rowint.createCell(1);
		        cellint.setCellValue(new HSSFRichTextString("Subject: "+ otherfields[2]));
		        cellint.setCellStyle(style);
		        ++rowcount;
		        
		        rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        cellint = rowint.createCell(1);
		        formatter = new SimpleDateFormat("dd MMMM yyyy");
		        cellint.setCellValue(new HSSFRichTextString("Quotations: Date : " + formatter.format(date)));
		        cellint.setCellStyle(style);
		        rowcount += 2;
		        		        
		        rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        cellint = rowint.createCell(1);
		        cellint.setCellValue(new HSSFRichTextString("Dear Sir,"));
		        cellint.setCellStyle(style);
		        ++rowcount;
		        rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        cellint = rowint.createCell(1);
		        cellint.setCellValue(new HSSFRichTextString("In response to your enquiry, we are pleased to offer our rates as under:-"));
		        cellint.setCellStyle(style);
		        rowcount += 2;	
		        
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
		        
		        HSSFRow rowhead = sheet.createRow(rowcount);
		        rowhead.setHeightInPoints((short) 15);
				for (int i = 1; i < 10; i++) {
					if (i==6) 
						if (discount == 0) 
							continue;
					HSSFCell cellhead;
					if (i==7 || i==8 || i==9) {
						if (discount == 0)
							cellhead = rowhead.createCell(i-1);
						else
							cellhead = rowhead.createCell(i);
					} else {
						cellhead = rowhead.createCell(i);
					}
					cellhead.setCellValue(new HSSFRichTextString(tblheads[(i-1)]));
					cellhead.setCellStyle(stylehead);
				}
		        ++rowcount;

				if (discount == 0)
					sheet.setColumnWidth((7), 18 * 256);	
				else
					sheet.setColumnWidth((8), 18 * 256);
				
				for (int i = 0; i < prodlist.length; i++) {
					HSSFRow rowproduct = sheet.createRow(i+rowcount);
					for (int j = 0; j < 9; j++) {
						if (j==1) {
							HSSFCell cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(new HSSFRichTextString(prodlist[i][j]));
							cellproduct.setCellStyle(styleprodname);
							sheet.setColumnWidth((j+1), 65 * 256);	
							continue;
						} if (j==2 || j==8) {							
							HSSFCell cellproduct;
							if (j==8) {
								if (discount == 0)
									cellproduct = rowproduct.createCell(j);
								else
									cellproduct = rowproduct.createCell(j+1);
							} else 
								cellproduct = rowproduct.createCell(j+1);
							cellproduct.setCellValue(new HSSFRichTextString(prodlist[i][j]));
							cellproduct.setCellStyle(style);
							sheet.setColumnWidth((j+1), 10 * 256);	
							continue;
						} if (j==6 || j==7) {							
							HSSFCell cellproduct; 
							if (discount == 0)
								cellproduct = rowproduct.createCell(j);
							else
								cellproduct = rowproduct.createCell(j+1);
							if (j==6)
								cellproduct.setCellValue(new HSSFRichTextString(prodlist[i][j]+"%"));
							else
								cellproduct.setCellValue(Double.valueOf(prodlist[i][j]));
							cellproduct.setCellStyle(style);
							continue;
						} if (j==5) {
							if (discount == 0)
								continue;
							else {
								HSSFCell cellproduct = rowproduct.createCell(j+1);
								cellproduct.setCellValue(new HSSFRichTextString(prodlist[i][j]+"%"));
								cellproduct.setCellStyle(style);
								sheet.setColumnWidth((j+1), 12 * 256);
								continue;
							}
								
						}						
						HSSFCell cellproduct = rowproduct.createCell(j+1);
						cellproduct.setCellValue(Double.valueOf(prodlist[i][j]));
						cellproduct.setCellStyle(style);
						sheet.setColumnWidth((j+1), 12 * 256);
					}
				}
				rowcount += prodlist.length;	int tpcell;
				if (discount == 0)
					tpcell = 6;
				else
					tpcell = 7;
				rowint = sheet.createRow(rowcount);
		        rowint.setHeightInPoints((short) 15);
		        cellint = rowint.createCell(tpcell);
		        cellint.setCellValue(new HSSFRichTextString("Total Price "));
		        cellint.setCellStyle(style);
		        ++tpcell;
		        cellint = rowint.createCell(tpcell);
		        cellint.setCellValue(Double.valueOf(otherfields[5]));
		        cellint.setCellStyle(style);								
		        rowcount += 2;
				
		        HSSFRow rowtc1 = sheet.createRow(prodlist.length+rowcount);
		        rowtc1.setHeightInPoints((short) 15);
		        HSSFCell celltc11 = rowtc1.createCell(2);
		        celltc11.setCellValue(new HSSFRichTextString("Terms and Conditions : "));
		        celltc11.setCellStyle(style);
		        ++rowcount;
		        HSSFRow rowtc2 = sheet.createRow(prodlist.length+rowcount);
		        HSSFCell celltc12 = rowtc2.createCell(2);
		        celltc12.setCellValue(new HSSFRichTextString(otherfields[3]));
		        celltc12.setCellStyle(styleprodname);
		        		        
		        File xlsfile = new File(desktop+"\\"+filename+".xls");
		        workbook.write(xlsfile); 
		        workbook.close();
		        			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (action.equals("exportToWord")) {
			try {
				
				XWPFDocument doc = new XWPFDocument(); 
				XWPFParagraph par = doc.createParagraph();
				XWPFRun run = par.createRun();
		        String filePath = getServletContext().getRealPath("/");
				File imgFile = new File(filePath +"/images/logo.jpg");
				run.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_JPEG, "logo.jpg", Units.pixelToEMU(600), Units.pixelToEMU(100)); 
				
	            XWPFParagraph paragraph = doc.createParagraph();  
	            run = paragraph.createRun(); 
	            
	            run.addBreak();
				run.setText("Quotation  No: "+ otherfields[0]); 
	            run.addBreak(); run.addBreak();
				
				run.setText("Customer Name: "+ otherfields[1]); 
	            run.addBreak(); run.addBreak();
				
				run.setText("Subject: "+ otherfields[2]); 
	            run.addBreak(); run.addBreak();
	            
	    		formatter = new SimpleDateFormat("dd MMMM yyyy");
				run.setText("Quotations: Date : "+formatter.format(date));
				run.addBreak(); run.addBreak();
				
				run.setText("Dear Sir,\n");
				run.addBreak();
				run.setText("In response to your enquiry, we are pleased to offer our rates as under:-\n");
				run.addBreak(); run.addBreak();
				
				int colind;
				XWPFTable tab = doc.createTable(1, 1);
	            XWPFTableRow row = tab.getRow(0); 
	            for (int i = 0; i < 9; i++) {
	            	if (i==0) {
	            		XWPFParagraph p1 = row.getCell(0).getParagraphs().get(0);
	    				p1.setAlignment(ParagraphAlignment.CENTER);
	    				XWPFRun r1 = p1.createRun();
	    				r1.setText(tblheads[i]);
	    				r1.setBold(true);
	    				continue;
	            	}            		
					if (i==5) 
						if (discount == 0) 
							continue;
					XWPFParagraph p1 = row.addNewTableCell().getParagraphs().get(0);
					p1.setAlignment(ParagraphAlignment.CENTER);
					XWPFRun r1 = p1.createRun();
					r1.setText(tblheads[i]);
    				r1.setBold(true);
				}
	            
	            for (int i = 0; i < prodlist.length; i++) {
	            	row = tab.createRow();
					for (int j = 0; j < 9; j++) {
						if (j == 1) {
							row.getCell(j).setText(prodlist[i][j]);;
							continue;
						} 
						if (j == 5) {
							if (discount == 0) {
								continue;
							}
						}	
						if (j==5 || j==6) {
							if (j==6) {
								if (discount==1)
									colind = j;
								else 
									colind = j-1;
							} else {
								colind = j;
							}
							XWPFParagraph p1 = row.getCell(colind).getParagraphs().get(0);
							p1.setAlignment(ParagraphAlignment.CENTER);
							XWPFRun r1 = p1.createRun();
							r1.setText(prodlist[i][j]+"%");
							continue;
						}
						if (j==7 || j==8) {
							if (discount==1)
								colind = j;
							else 
								colind = j-1;
							XWPFParagraph p1 = row.getCell(colind).getParagraphs().get(0);
							p1.setAlignment(ParagraphAlignment.CENTER);
							XWPFRun r1 = p1.createRun();
							r1.setText(prodlist[i][j]);
							continue;
						}
						XWPFParagraph p1 = row.getCell(j).getParagraphs().get(0);
						p1.setAlignment(ParagraphAlignment.CENTER);
						XWPFRun r1 = p1.createRun();
						r1.setText(prodlist[i][j]);
					}
				}
	            
	            row = tab.createRow();
	            for (int j = 0; j < 9; j++) {
					if (j==5) {
						if (discount == 1) {
							row.getCell(j)
								.getParagraphs().get(0)
								.createRun()
								.setText("");
							continue;
						} else {
							continue;
						}
					}
					if (j==6) {
						if (discount==1)
							colind = j;
						else 
							colind = j-1;
						XWPFParagraph p1 = row.getCell(colind).getParagraphs().get(0);
						p1.setAlignment(ParagraphAlignment.CENTER);
						XWPFRun r1 = p1.createRun();
						r1.setText("Total Price: ");
						continue;
					}
					if (j==7 || j==8) {
						if (discount==1)
							colind = j;
						else 
							colind = j-1;	
						if (j==7) {
							XWPFParagraph p1 = row.getCell(colind).getParagraphs().get(0);
							p1.setAlignment(ParagraphAlignment.CENTER);
							XWPFRun r1 = p1.createRun();
							r1.setText(otherfields[5]);
						} else {
							row.getCell(colind)
								.getParagraphs().get(0)
								.createRun()
								.setText("");
						}					
						continue;
					}
					row.getCell(j)
						.getParagraphs().get(0)
						.createRun()
						.setText("");		
				}
	            
	            paragraph = doc.createParagraph(); 
	            paragraph.setWordWrapped(true);
	            run = paragraph.createRun(); 
	            run.addBreak(); run.addBreak();
				run.setText("Terms and Conditions : ");
				run.addBreak();
				run.setText(otherfields[3]);
				run.addBreak();
		        			
				File docfile = new File(desktop+"\\"+filename+".doc");
				FileOutputStream out = new FileOutputStream(docfile);
		        doc.write(out); 
				doc.close();
				out.flush();
				out.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		doGet(request, response);
	}

}


