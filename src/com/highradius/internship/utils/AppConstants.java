package com.highradius.internship.utils;

public final class AppConstants {
	//connections details
	public static final String URL = "jdbc:mysql://localhost:3306/";
	public static final String USER = "root";
	public static final String PASS = "root";
	public static final String DBNAME = "h2h_internship";
	
	//startup file location
	public static final String CSV = "C:\\\\csvfile\\\\1806138.csv";
	public static final int BATCHSIZE = 500;
	
	//DB queries
	public static final String LOADCSVQUERY = "INSERT  INTO invoice_details (business_code,cust_number,name_customer,clear_date,business_year,doc_id,posting_date,document_create_date,due_in_date,invoice_currency,document_type,posting_id,area_business,total_open_amount,baseline_create_date,cust_payment_terms,invoice_id,isOpen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String LISTINVIOCES = "SELECT * FROM invoice_details LIMIT 50";
	
}
