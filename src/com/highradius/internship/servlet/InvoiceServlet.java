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
@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InvoiceServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init(ServletConfig config) throws ServletException  {
		System.out.println("----------Initializing app-------------");
		try {
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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/add":
				addInvoice(request, response);
				break;
			case "/insert":
				deleteInvoice(request, response);
				break;
			case "/delete":
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
		// TODO Auto-generated method stub

	}

	private void deleteInvoice(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		// TODO Auto-generated method stub

	}

	private void addInvoice(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		// TODO Auto-generated method stub

	}

}
