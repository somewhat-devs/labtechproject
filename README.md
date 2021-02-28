# Quotation Builder

### Problem Statement

This project is QUOTATION BUILDER software for controlling the billing and maintaining the database of the company.

The project Quotation Builder is developed on Eclipse and My SQL Workbench, which mainly focuses on searching for a product using Material No., Brand, Product Name, HSN Code, and Section, adding products in the bill, adding data or new products in the database using the data inputs as Material No., Brand, Product Name, HSN Code, and Section and importing the quotation to pdf, word or excel format.

The project has three major modules:
  1. Go to bill- search for product, select the product, the bill gets generated
  2. Add new products- enter details of products, create product
  3. Excel import- upload a file of products with all details as the database, details gets added to database

### Functional Requirements

1. Create an backend layout where the products will be stored in the database with the following parameters:
* catalogue number
* Brand
* packing
* product name
* gst rate
* hsncode
* section

2. Create a frontend phase where the user can select the item from the backend by some fields such as Catalogue number, Name, HSN Code, Section, Brand, Packing.

3. When a user enters the data in the frontend so automatically the remaining data should be fetched. For Example : User in the front end has entered the Name of the product so automatically the remaining data should be fetched from database.

4. Adding all the items, we should have an option to generate it in the excel format, PDF format or Word format.

5. User be able to add products to the database by providing all required fields.

### Technology Used

* Web Presentation: HTML, CSS
* Client side Scripting: Javascript
* Libraries: Apache POI, iTextPdf, DataTables.js,
* Bootstrap
* Programming Language: Java
* Web based Technologies: JNDI, Servlets, JSP
* Database Connectivity API: JDBC
* Backend Database: SQL Workbench
* Database Language: MYSQL
* Operating System: Windows 10
* J2EE Web/Application Server: Tomcat
* IDEs: Eclipse
* Browser: Chrome/Mozilla

