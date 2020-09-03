import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
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

@WebServlet("/ExportService")
public class ExportService extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
    public ExportService() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String prodlist[][] = new Gson().fromJson(request.getParameter("prodlist"), String[][].class);
			
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HHmmss");
			Date date = new Date();
			String filename = "Quotation-" + formatter.format(date) + ".pdf";
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline;filename=\""+ filename +"\"");

			PdfWriter writer = new PdfWriter(response.getOutputStream());
			PdfDocument pdf = new PdfDocument(writer);
			pdf.addNewPage();
			pdf.getDocumentInfo().setCreator("Kasliwal Brothers Pharmaceuticals");
			pdf.getDocumentInfo().setAuthor("Kasliwal Brothers Pharmaceuticals");
			pdf.getDocumentInfo().addCreationDate();
			Document document = new Document(pdf, new PageSize(PageSize.A4));

			String url = request.getRequestURL().toString();
			URL imFile = new URL(url.substring(0, url.lastIndexOf('/')) + "/images/logo.jpg");
			ImageData data = ImageDataFactory.create(imFile);
			Image image = new Image(data);
			image.setAutoScale(true);
			document.add(image);

			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			Text text = new Text("Quot  No. KBI/Q-III/2020-21").setFontSize(10);
			document.add(new Paragraph(text));

			formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			text = new Text("Quotations: Date : " + formatter.format(date)).setFontSize(10);
			document.add(new Paragraph(text));

			document.add(new Paragraph(" "));
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Text("Dear Sir,\n").setFontSize(10));
			paragraph.add(new Text("In response to your enquiry, we are pleased to offer our rates as under:-\n")
					.setFontSize(10));
			paragraph.add(new Text("Department of Pharmaceuticals").setBold().setFontSize(10));
			document.add(paragraph);

			float[] pointColumnWidths = { 10, 10, 10, 10, 10, 10, 10, 10 };
			Table table = new Table(pointColumnWidths);
			table.setWidth(UnitValue.createPercentValue(100));
			table.setMarginTop(30);

			String [] tblheads = {"S. No.", "HSN Code.", "Item name with description", "Make", "Qty.", "Price", "Discount", "GST"};
			for (int i=0; i<8; i++) {
				table.addCell(new Cell().add(new Paragraph(tblheads[i]))
						.setTextAlignment(TextAlignment.CENTER)
						.setVerticalAlignment(VerticalAlignment.MIDDLE)
						.setFontSize(10).setFontColor(new DeviceRgb(0, 112, 192))
						.setPaddings(5, 2, 5, 2)
						.setBackgroundColor(new DeviceRgb(220, 233, 241))
						.setBorder(new SolidBorder(new DeviceRgb(216, 216, 216), 1F)));
			}

			for (int i = 0; i < prodlist.length; i++) {
				for (int j = 0; j < 8; j++) {
					if (j == 2) {
						table.addCell(new Cell().add(new Paragraph(prodlist[i][j]))
								.setFontSize(10)
								.setPaddings(5, 2, 5, 2)
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

			document.add(table);
			document.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		doGet(request, response);
	}

}


