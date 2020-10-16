

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

@WebServlet("/Createpdf")
public class Createpdf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Createpdf() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		String action = request.getParameter("action");
		String[] tblheads = { "S. No.", "HSN Code", "Item name with description", "Make", "Qty.", "Unit Price",	"Discount", "GST","Total" };
//		String prodlist[][] = new Gson().fromJson(request.getParameter("prodlist"), String[][].class);
//		String termsNcondition = request.getParameter("termsNcondition");

		String action = "exportToPDF";
		String subject="Temporary Subject";
		String email1="manoj_maru@hotmail.com";
		String email2="rana_1990sagar@yahoo.com";
		String customername=request.getParameter("");
		String quotationno=request.getParameter("");
		String custaddress=request.getParameter("");
		double total=0;
		double p1=0,p2=0,p3=0;
		String prodlist[][] = { 
				{"2", "1234 00 00", "name fhwbdn", "himedia", "1", "200",	"0", "18",Double.toString(total)}
			};
		String termsNcondition = "temporary terms and condition";
		
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
				
				formatter = new SimpleDateFormat("dd MMMM yyyy");
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
			    Text text = new Text("Quotation  No :" + quotationno +"/t/t/t/t/t/t/t"+ formatter.format(date)).setFontSize(10);
				document.add(new Paragraph(text));
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
			    text = new Text("To:").setFontSize(10);
				document.add(new Paragraph(text));

				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				text = new Text(customername).setFontSize(10);
			    text = new Text(custaddress).setFontSize(10);
				document.add(new Paragraph(text));

				document.add(new Paragraph(" "));
				text = new Text("Subject:" +subject).setFontSize(10);
				document.add(new Paragraph(text));
				
				document.add(new Paragraph(" "));
				Paragraph paragraph = new Paragraph();
				paragraph.add(new Text("Dear Sir,\n").setFontSize(10));
				paragraph.add(new Text("In response to your enquiry, we are pleased to offer our rates as under:-\n")
						.setFontSize(10));
				document.add(paragraph);

				float[] pointColumnWidths = { 5, 5, 5, 5, 5, 5, 5, 5, 5 };
				Table table = new Table(pointColumnWidths);
				table.setWidth(UnitValue.createPercentValue(100));
				table.setMarginTop(30);

				for (int i = 0; i < 9; i++) {
					table.addCell(new Cell().add(new Paragraph(tblheads[i])).setTextAlignment(TextAlignment.CENTER)
							.setVerticalAlignment(VerticalAlignment.MIDDLE).setFontSize(10)
							.setFontColor(new DeviceRgb(0, 112, 192)).setPaddings(5, 2, 5, 2)
							.setBackgroundColor(new DeviceRgb(220, 233, 241))
							.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
				}
				
				for (int i = 0; i < prodlist.length; i++) {
					for (int j = 0; j < 9; j++) {
						if (j == 2) {
							table.addCell(new Cell().add(new Paragraph(prodlist[i][j])).setFontSize(10)
									.setPaddings(5, 2, 5, 2)
									.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
							continue;
						}
                        if(j==8){
                        	String quantity=Array.get(prodlist[i],4).toString();
        					int qty=Integer.parseInt(quantity);
        					System.out.println("qty: " +qty);
        					String UnitPrice=Array.get(prodlist[i],5).toString();
        					double price=Double.parseDouble(UnitPrice);
        					System.out.println("Price: " +price);
        					String Dis=Array.get(prodlist[i],6).toString();
        					double discount=Double.parseDouble(Dis);
        					System.out.println("Discount: " +discount);
        					String Gst=Array.get(prodlist[i],7).toString();
        					double gst=Double.parseDouble(Gst);
        					System.out.println("gst: " +gst);
							if(discount==0){
							 p1=price*qty;            //price with quantity
							 p3=p1+(p1*(gst/100));    //price with gst
							 total=(price*qty)+((price*qty)*gst/100);
							 System.out.println("Total : " +total);
							 
							}
							else{
								p1=price*qty;               //price with quantity
								p2=p1-(p1*(discount/100));  //price with discount
								p3=p2+(p2*(gst/100));       //price with gst
								total=(price*qty)-((price*qty)*(discount*100))+(((price*qty)-((price*qty)*(discount/100)))*(gst/100));
								System.out.println("Total : " +total);
								
							}
							
							table.addCell(new Cell().add(new Paragraph(Double.toString(total))));
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
				
				paragraph.add(new Text("Delivery : ").setFontSize(10));
				document.add(paragraph);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text("In about two weeks , from receipt of your order/payment. You may send your cheque for payment along with the order").setFontSize(10));
				document.add(paragraph);
				
				paragraph.add(new Text("Payment : ").setFontSize(10));
				document.add(paragraph);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text("Send the payment directly by RTGS/NEFT to our account and inform us accordingly,Our Bank details are as under;").setFontSize(10));
				document.add(paragraph);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text("STATE BANK OF INDIA,Subhash Road,Vile Parle(E) Br; Mumbai-400057.").setFontSize(10));
				paragraph.add(new Text("A/c No: 37046574015 /t/t/t/t/t IFS/RGTS Code No: SBIN0017610").setBold().setFontSize(10));
				document.add(paragraph);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text(" All quoted prices are valid for 30 days from date of quotation.").setFontSize(10));
				paragraph.add(new Text(" Please reference your Kasliwal Brothers quotation number on ypur purchase orders").setFontSize(10));
				paragraph.add(new Text(" Please mention your CST/TIN No./GST No. in your Purchase Order").setFontSize(10));
				paragraph.add(new Text(" If there is any Road Permit Entry/Exit form is required to ship thje material then please courier the same to below Address").setFontSize(10));
				paragraph.add(new Text(" Please mention Quotation number on your Purchase Order and send it to on"+email1 +"and copy to "+email2).setFontSize(10));
				document.add(paragraph);
				
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
				paragraph = new Paragraph();
				paragraph.add(new Text("SUBJECT TO INDORE JURISDICTION /t/t/t FOR KASLIWAL BROTHERS").setFontSize(10));
				paragraph.add(new Text("GST NO : 232323423I2J23I4IJ    /t/t/t Authorized Signatory,").setBold().setFontSize(10));
				document.add(paragraph);
				document.close();

				response.setContentType("text/html");
	            PrintWriter out = response.getWriter();
	            String resp = "";
	            resp += "pdf file generated at desktop!";
	            out.println(resp);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}}}