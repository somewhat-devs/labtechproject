import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ExportToExcel")
public class ImportExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ImportExcel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = "D:\\colly\\SEM 7\\training 7sem 2020\\db schema\\item master list.xlsx";

        List<List<String>> productList = new ArrayList<>();
        List<String> brandList = new ArrayList<>();		brandList.add("NULL");
        List<String> hsncodeList = new ArrayList<>();	hsncodeList.add("NULL");
        List<String> sectionList = new ArrayList<>();	sectionList.add("NULL");
                
        try {
        	OPCPackage pkg = OPCPackage.open(new File(filename));
        	XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        	
        	int sheet_count = workbook.getNumberOfSheets();
			for (int k = 0; k < sheet_count; k++) {
				XSSFSheet sheet = workbook.getSheetAt(k);

				Iterator<Row> rows = sheet.rowIterator();
				rows.next();
				int cellcount, pnnull_fl=0; 
				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					List<String> data = new ArrayList<>();

					for (cellcount = 0; cellcount < 8; cellcount++) {
						Cell cell = (Cell) row.getCell(cellcount);
						if (cell == null || cell.getCellType() == CellType.BLANK) {
							if (cellcount == 3) {
								pnnull_fl=1;
							} else if (cellcount == 1 || cellcount == 6 || cellcount == 7) {
								data.add("1");
							} else {
								data.add("NULL");
							}
						} else {
							CellType type = cell.getCellType();
							if (type == CellType.STRING) {
								String str = cell.getRichStringCellValue().toString();
								if (str.equals("")) {
									if (cellcount == 3) {
										pnnull_fl=1;
									} else if (cellcount == 1 || cellcount == 6 || cellcount == 7) {
										data.add("1");
									}else {
										data.add("NULL");
									}
								} else {
									if (cellcount == 1) {
										if (brandList.contains(str)) {
											data.add(String.valueOf(brandList.indexOf(str)+1));
										} else {
											brandList.add(str);
											data.add(String.valueOf(brandList.size()));
										}
									} else if (cellcount == 6) {
										if (hsncodeList.contains(str)) {
											data.add(String.valueOf(hsncodeList.indexOf(str)+1));
										} else {
											hsncodeList.add(str);
											data.add(String.valueOf(hsncodeList.size()));
										}
									} else if (cellcount == 7) {
										if (sectionList.contains(str)) {
											data.add(String.valueOf(sectionList.indexOf(str)+1));
										} else {
											sectionList.add(str);
											data.add(String.valueOf(sectionList.size()));
										}
									} else if (cellcount == 3) {
										str = str.replaceAll("\'", "\'\'");
										data.add(str);
									} else {
										data.add(str);
									}
								}
							} else if (type == CellType.NUMERIC) {
								data.add(String.valueOf((int) cell.getNumericCellValue()));
							} else {
								continue;
							}
						}
					}
					if (pnnull_fl==0) productList.add(data);
				}
			}
            workbook.close();
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String resp = "";
                       
//            System.out.print(productList.size());
            
            List<String> db_sql = new ArrayList<String>();		String insert_str ="";
            db_sql.add("CREATE DATABASE labtech2; ");
            db_sql.add("USE DATABASE labtech2; ");
            
            db_sql.add("DROP TABLE IF EXISTS product_table; ");
            db_sql.add("DROP TABLE IF EXISTS hsncode_table; "); 
            db_sql.add("DROP TABLE IF EXISTS section_table; ");
            db_sql.add("DROP TABLE IF EXISTS brand_table; ");
            
            db_sql.add("CREATE TABLE section_table (" + 
            		"section_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " + 
            		"section_name VARCHAR(40)" + 
            		") ENGINE=InnoDB; ");
            
            db_sql.add("CREATE TABLE hsncode_table (" + 
            		"hsncode_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " + 
            		"hsncode VARCHAR(20)" + 
            		") ENGINE=InnoDB; ");
            
            db_sql.add("CREATE TABLE brand_table (" + 
            		"brand_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " + 
            		"brand VARCHAR(20)" + 
            		") ENGINE=InnoDB; ");
            
            db_sql.add("CREATE TABLE product_table (" + 
            		"product_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " + 
            		"material_no VARCHAR(30), " + 
            		"brand_id INT UNSIGNED, " +
            		"packing VARCHAR(100), " + 
            		"product_name TEXT, " + 
            		"consumer_rate VARCHAR(20), " + 
            		"gst_rate TINYINT, " + 
            		"hsncode_id INT UNSIGNED, " + 
            		"section_id INT UNSIGNED, " + 
            		"CONSTRAINT fk_brand" + 
            		"    FOREIGN KEY (brand_id) REFERENCES brand_table (brand_id), " + 
            		"CONSTRAINT fk_hscode " + 
            		"    FOREIGN KEY (hsncode_id) REFERENCES hsncode_table (hsncode_id), " + 
            		"CONSTRAINT fk_section" + 
            		"    FOREIGN KEY (section_id) REFERENCES section_table (section_id), " + 
            		"FULLTEXT(product_name) " + 
            		") ENGINE=InnoDB; ");
          
            db_sql.add("INSERT INTO brand_table(brand) VALUES(null); ");
            for (int j=1; j<brandList.size(); j++) {
            	db_sql.add("INSERT INTO brand_table(brand) VALUES('"+ brandList.get(j) +"'); ");
            }

            db_sql.add("INSERT INTO hsncode_table(hsncode) VALUES(null); ");
            for (int j=1; j<hsncodeList.size(); j++) {
            	db_sql.add("INSERT INTO hsncode_table(hsncode) VALUES('"+ hsncodeList.get(j) +"'); ");
            }

            db_sql.add("INSERT INTO section_table(section_name) VALUES(null); ");
            for (int j=1; j<sectionList.size(); j++) {
            	db_sql.add("INSERT INTO section_table(section_name) VALUES('"+ sectionList.get(j) +"'); ");
            }

            for (int i=0; i<productList.size(); i++) {
                insert_str = "";
            	insert_str += "INSERT INTO product_table(material_no, brand_id, packing, product_name, consumer_rate, gst_rate, hsncode_id, section_id) VALUES (";
            	List<String> t = productList.get(i);
            	for (int j=0; j<t.size(); j++) {
            		if (j==0) {
            			if (t.get(j).equals("NULL")) 	insert_str += "NULL" ;
            			else 							insert_str += "'"+t.get(j)+"'";
            		}
            		else if (j==1 || j==5 || j==6 || j==7) {
            			insert_str += ", "+ t.get(j) ;
            		} else {
            			if (t.get(j).equals("NULL")) 	insert_str += ", NULL" ;
            			else 							insert_str += ", '"+t.get(j)+"'";
            		}
                }
            	insert_str += "); ";
            	db_sql.add(insert_str);
            }
            
//            FileWriter myWriter = new FileWriter("D://db_queries.sql");
//            for (int i=0; i<db_sql.size(); i++) {
//            	myWriter.write(db_sql.get(i));            	 
//               	myWriter.write("\n"); 
//            }
//            myWriter.close();
            
//            String connectionURL = "jdbc:mysql://localhost:3306/labtech2";
//    		Connection connection = null;
//    		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//    		connection = DriverManager.getConnection(connectionURL, "root", "");
//    		Statement statement = connection.createStatement();
//    		for (int i=0; i<db_sql.size(); i++) {
//    			statement.addBatch(db_sql.get(i));
//    		}
//    		statement.executeBatch();
    	    
            resp += "queries generated!\n";
            resp += productList.size()+ " products are added to db. ";
            out.println(resp);
            
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
