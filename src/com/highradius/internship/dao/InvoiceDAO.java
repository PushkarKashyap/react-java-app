package com.highradius.internship.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.highradius.internship.utils.AppConstants;
import com.highradius.internship.utils.DatabaseConnection;

public class InvoiceDAO {
	private static Float numberNullCheck(String ob) {
		if (ob == null || ob.trim().isEmpty()) {
			return new Float(0);
		} else {
			return Float.parseFloat(ob);
		}
	}

	private static Object nullableCheck(String ob) {
		if (ob == null || ob.trim().isEmpty()) {
			return null;
		} else {
			return ob;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csvFilePath = AppConstants.CSV;
		int batchSize = AppConstants.BATCHSIZE;
		Connection connection = null;
		// date formats
		DateFormat dateFormatHMS = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		try {
			DatabaseConnection dbconn = new DatabaseConnection(AppConstants.URL, AppConstants.DBNAME, AppConstants.USER, AppConstants.PASS);
			connection = dbconn.initializeDatabase();
			connection.setAutoCommit(false);
			// creating prepared statement
			String sql = AppConstants.LOADCSVQUERY;
			PreparedStatement statement = connection.prepareStatement(sql);

			BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
			String lineText = null;
			int count = 0;
			lineReader.readLine(); // skip header line
			while ((lineText = lineReader.readLine()) != null) {
				String[] data = lineText.split(",");
				String business_code = data[0];
				String cust_number = data[1];
				String name_customer = data[2];
				String clear_date = data[3];
				String business_year = data[4];
				String doc_id = data[5];
				String posting_date = data[6];
				String document_create_date = data[7];
				String due_in_date = data[9];
				String invoice_currency = data[10];
				String document_type = data[11];
				String posting_id = data[12];
				String area_business = data[13];
				String total_open_amount = data[14];
				String baseline_create_date = data[15];
				String cust_payment_terms = data[16];
				String invoice_id = data[17];
				String isOpen = data[18];

				statement.setString(1, business_code);
				statement.setString(2, cust_number);
				statement.setString(3, name_customer);

				if (nullableCheck(clear_date) != null) {
					// Date dClear_date = dateFormatHMS.parse(clear_date);
					Timestamp clear_date_ts = new Timestamp(dateFormatHMS.parse(clear_date).getTime());
					statement.setTimestamp(4, clear_date_ts);
				} else {
					statement.setTimestamp(4, null);
				}

				statement.setFloat(5, Float.parseFloat(numberNullCheck(business_year).toString()));

				// Double lDoc_id = Double.parseDouble(doc_id);
				// System.out.println("doc id: "+lDoc_id);
				statement.setDouble(6, Double.parseDouble(doc_id));

				if (nullableCheck(posting_date) != null) {
					Date dPosting_date = new Date(dateFormat.parse(posting_date).getTime());
					statement.setDate(7, dPosting_date);
				} else {
					statement.setDate(7, null);
				}

				if (nullableCheck(document_create_date) != null) {
					LocalDate dDocument_create_Date = LocalDate.parse(
							Integer.valueOf(document_create_date.split("\\.")[0]).toString(),
							DateTimeFormatter.BASIC_ISO_DATE);
					statement.setDate(8, java.sql.Date.valueOf(dDocument_create_Date));
				} else {
					statement.setDate(8, null);
				}

				if (nullableCheck(due_in_date) != null) {
					LocalDate dDue_in_date = LocalDate.parse(Integer.valueOf(due_in_date.split("\\.")[0]).toString(),
							DateTimeFormatter.BASIC_ISO_DATE);
					statement.setDate(9, java.sql.Date.valueOf(dDue_in_date));
				} else {
					statement.setDate(9, null);
				}

				statement.setString(10, invoice_currency);
				statement.setString(11, document_type);

				Float fPosting_id = Float.parseFloat(numberNullCheck(posting_id).toString());
				statement.setFloat(12, fPosting_id);

				statement.setString(13, area_business);

				Float fTotal_open = Float.parseFloat(numberNullCheck(total_open_amount).toString());
				statement.setFloat(14, fTotal_open);

				if (nullableCheck(baseline_create_date) != null) {
					LocalDate dBaseline_create_date = LocalDate.parse(
							Integer.valueOf(baseline_create_date.split("\\.")[0]).toString(),
							DateTimeFormatter.BASIC_ISO_DATE);
					statement.setDate(15, java.sql.Date.valueOf(dBaseline_create_date));
				} else {
					statement.setDate(15, null);
				}

				statement.setString(16, cust_payment_terms);

				Float fInvoice_id = Float.parseFloat(numberNullCheck(invoice_id).toString());
				statement.setFloat(17, fInvoice_id);

				Float fIs_open = Float.parseFloat(numberNullCheck(isOpen).toString());
				statement.setFloat(18, fIs_open);

				statement.addBatch();
				count++;
				if (count % batchSize == 0) {
					System.out.println("batch size: " + count);
					statement.executeBatch();
				}

			}

			lineReader.close();

			// execute the remaining queries
			statement.executeBatch();

			connection.commit();
			connection.close();
			System.out.println("execution completed" + "\n");

		} catch (IOException ex) {
			System.out.println("IO Exception Encountered" + "\n");
			System.err.println(ex);
		} catch (SQLException ex) {
			System.out.println("SQL Exception Encountered" + "\n");
			ex.printStackTrace();

			try {
				System.out.println("Trying to Rollback" + "\n");
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println("Generic Exception Encountered" + "\n");
			ex.printStackTrace();
		}

	}

}
