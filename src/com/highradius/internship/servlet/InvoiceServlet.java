package com.highradius.internship.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.highradius.internship.dao.InvoiceDAO;
import com.highradius.internship.model.Invoice;

/**
 * Servlet implementation class InvoiceServlet
 */
@WebServlet("/")
public class InvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvoiceServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		System.out.println("----------Initializing app-------------");
		try {
			//comment this if not required
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			invoiceDAO.loadCSVonStartup();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		System.out.println("----------App initialized successfully-------------");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		//String action = request.getServletPath();
		String action = request.getPathInfo();

		try {
			switch (action) {
			case "/add":
				addInvoice(request, response);
				break;
			case "/delete":
				deleteInvoice(request, response);
				break;
			case "/search":
				searchInvoice(request, response);
				break;
			case "/update":
				updateInvoice(request, response);
				break;
			default:
				listInvoices(request, response);
				break;
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	private void listInvoices(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {

		List<Invoice> invoiceList = new ArrayList<Invoice>();
		try {
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			invoiceList = invoiceDAO.listInvoices();
		} catch (SQLException e) {
			throw e;
		}
		Gson gson = new Gson();
		String data = gson.toJson(invoiceList);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(data);
		out.flush();
	}

	private void updateInvoice(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// TODO Auto-generated method stub

	}

	private void searchInvoice(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {

		// hardcoding for testing
		//String invoiceId = "1928501760";
		String invoiceId = request.getParameter("invoiceId");
		List<Invoice> invoiceList = new ArrayList<Invoice>();
		try {
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			invoiceList = invoiceDAO.searchInvoice(invoiceId);
		} catch (SQLException e) {
			throw e;
		}
		Gson gson = new Gson();
		String data = gson.toJson(invoiceList);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(data);
		out.flush();
	}

	private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// hardcoding for testing
		//String docId = "1928501756";
		String[] docList =  (request.getParameter("docList")).split("\\,");
		//List<Invoice> invoiceList = new ArrayList<Invoice>();
		boolean isSuccess = false;
		try {
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			isSuccess = invoiceDAO.deleteInvoice(docList);
		} catch (SQLException e) {
			throw e;
		}
		Gson gson = new Gson();
		String data = gson.toJson(isSuccess);
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(data);
		out.flush();

	}

	private void addInvoice(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// TODO Auto-generated method stub

	}

}
