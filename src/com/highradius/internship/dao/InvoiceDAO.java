package com.highradius.internship.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.highradius.internship.model.Invoice;
import com.highradius.internship.utils.AppConstants;
import com.highradius.internship.utils.DatabaseConnection;

public class InvoiceDAO {

	// constructor
	public InvoiceDAO() {
	}

	// loadCSVonStartup
	// function to load data on server startup
	public void loadCSVonStartup() {
		
		String sql = AppConstants.LOADCSVQUERY;
		String csvFilePath = AppConstants.CSV;
		int batchSize = AppConstants.BATCHSIZE;
		Connection connection = null;
		// date formats
		DateFormat dateFormatHMS = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		try {
			DatabaseConnection dbconn = new DatabaseConnection();
			connection = dbconn.initializeDatabase();
			connection.setAutoCommit(false);
			// creating prepared statement
			PreparedStatement statement = connection.prepareStatement(sql);

			BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
			String lineText = null;
			int count = 0;
			lineReader.readLine(); // skip header line
			while ((lineText = lineReader.readLine()) != null) {
				String[] data = lineText.split(",");
				String businessCode = data[0];
				String custNumber = data[1];
				String nameCustomer = data[2];
				String clearDate = data[3];
				String businessYear = data[4];
				String docId = data[5];
				String postingDate = data[6];
				String documentCreateDate = data[7];
				String dueInDate = data[9];
				String invoiceCurrency = data[10];
				String documentType = data[11];
				String postingId = data[12];
				String areaBusiness = data[13];
				String totalOpenAmount = data[14];
				String baselineCreateDate = data[15];
				String custPaymentTerms = data[16];
				String invoiceId = data[17];
				String isOpen = data[18];

				statement.setString(1, businessCode);
				statement.setString(2, custNumber);
				statement.setString(3, nameCustomer);

				if (nullableCheck(clearDate) != null) {
					// Date dClear_date = dateFormatHMS.parse(clear_date);
					Timestamp clearDateTs = new Timestamp(dateFormatHMS.parse(clearDate).getTime());
					statement.setTimestamp(4, clearDateTs);
				} else {
					statement.setTimestamp(4, null);
				}

				statement.setFloat(5, Float.parseFloat(numberNullCheck(businessYear).toString()));

				// Double lDoc_id = Double.parseDouble(doc_id);
				// System.out.println("doc id: "+lDoc_id);
				statement.setDouble(6, Double.parseDouble(docId));

				if (nullableCheck(postingDate) != null) {
					Date dPostingDate = new Date(dateFormat.parse(postingDate).getTime());
					statement.setDate(7, dPostingDate);
				} else {
					statement.setDate(7, null);
				}

				if (nullableCheck(documentCreateDate) != null) {
					LocalDate dDocumentCreateDate = LocalDate.parse(
							Integer.valueOf(documentCreateDate.split("\\.")[0]).toString(),
							DateTimeFormatter.BASIC_ISO_DATE);
					statement.setDate(8, java.sql.Date.valueOf(dDocumentCreateDate));
				} else {
					statement.setDate(8, null);
				}

				if (nullableCheck(dueInDate) != null) {
					LocalDate dDueInDate = LocalDate.parse(Integer.valueOf(dueInDate.split("\\.")[0]).toString(),
							DateTimeFormatter.BASIC_ISO_DATE);
					statement.setDate(9, java.sql.Date.valueOf(dDueInDate));
				} else {
					statement.setDate(9, null);
				}

				statement.setString(10, invoiceCurrency);
				statement.setString(11, documentType);

				Float fPostingId = Float.parseFloat(numberNullCheck(postingId).toString());
				statement.setFloat(12, fPostingId);

				statement.setString(13, areaBusiness);

				Float fTotalOpen = Float.parseFloat(numberNullCheck(totalOpenAmount).toString());
				statement.setFloat(14, fTotalOpen);

				if (nullableCheck(baselineCreateDate) != null) {
					LocalDate dBaselineCreateDate = LocalDate.parse(
							Integer.valueOf(baselineCreateDate.split("\\.")[0]).toString(),
							DateTimeFormatter.BASIC_ISO_DATE);
					statement.setDate(15, java.sql.Date.valueOf(dBaselineCreateDate));
				} else {
					statement.setDate(15, null);
				}

				statement.setString(16, custPaymentTerms);

				Float fInvoiceId = Float.parseFloat(numberNullCheck(invoiceId).toString());
				statement.setFloat(17, fInvoiceId);

				Float fIsOpen = Float.parseFloat(numberNullCheck(isOpen).toString());
				statement.setFloat(18, fIsOpen);

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
			
			System.out.println("execution completed" + "\n");

		} catch (IOException ex) {
			System.out.println("IO Exception Encountered" + "\n");
			System.err.println(ex);
		} catch (SQLException ex) {
			printSQLException(ex);

			try {
				System.out.println("Trying to Rollback" + "\n");
				connection.rollback();
			} catch (SQLException e) {
				printSQLException(ex);
			}
		} catch (Exception ex) {
			System.out.println("Generic Exception Encountered" + "\n");
			ex.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				printSQLException(e);
			}
			
		}

	}
	
	//function to list 50 invoices
	public List<Invoice> listInvoices() throws SQLException {
		String sql = AppConstants.LISTINVIOCES;
		List<Invoice> invoiceList = new ArrayList<Invoice>();
		Connection connection = null;

		try {
			connection = new DatabaseConnection().initializeDatabase();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				Invoice invoice = setModel(rs);
				invoiceList.add(invoice);
			}
		} catch (SQLException ex) {
			printSQLException(ex);

			try {
				System.out.println("Trying to Rollback" + "\n");
				connection.rollback();
			} catch (SQLException e) {
				printSQLException(ex);
			}
		} catch (Exception ex) {
			System.out.println("Generic Exception Encountered" + "\n");
			ex.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				printSQLException(e);
			}
			
		}

		return invoiceList;
	}

	
	// utils - start
	//for loading CSV
	private Float numberNullCheck(String ob) {
		if (ob == null || ob.trim().isEmpty()) {
			return new Float(0);
		} else {
			return Float.parseFloat(ob);
		}
	}

	private Object nullableCheck(String ob) {
		if (ob == null || ob.trim().isEmpty()) {
			return null;
		} else {
			return ob;
		}
	}
	
	//for setting model
	
	private Invoice setModel(ResultSet rs) {
		Invoice inv = new Invoice();
		
		try {
			inv.setBusinessCode(rs.getString("business_code"));
			inv.setCustNumber(rs.getString("cust_number"));
			inv.setNameCustomer(rs.getString("name_customer"));
			inv.setClearDate(rs.getTimestamp("clear_date"));
			inv.setBusinessYear(rs.getFloat("business_year"));
			inv.setDocId(rs.getDouble("doc_id"));
			inv.setPostingDate(rs.getDate("posting_date"));
			inv.setDocumentCreateDate(rs.getDate("document_create_date"));
			inv.setDueInDate(rs.getDate("posting_date"));
			inv.setInvoiceCurrency(rs.getString("invoice_currency"));
			inv.setDocumentType(rs.getString("document_type"));
			inv.setPostingId(rs.getFloat("posting_id"));
			inv.setAreaBusiness(rs.getString("area_business"));
			inv.setTotalOpenAmount(rs.getFloat("total_open_amount"));
			inv.setBaselineCreateDate(rs.getDate("baseline_create_date"));
			inv.setCustPaymentTerms(rs.getString("cust_payment_terms"));
			inv.setInvoiceId(rs.getFloat("invoice_id"));
			inv.setIsOpen(rs.getFloat("isOpen"));
		} catch (SQLException ex) {
			printSQLException(ex);
		} catch (Exception ex) {
			System.out.println("Generic Exception Encountered" + "\n");
			ex.printStackTrace();
		}
		return inv;
	}
	
	//printing formatted SQL exception
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState() + "\n");
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode() + "\n");
				System.err.println("Message: " + e.getMessage() + "\n");
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t + "\n");
					t = t.getCause();
				}
			}
		}
	}
	
	// utils - end
}
