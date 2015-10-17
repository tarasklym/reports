package com.castleby.invoice;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/*", name = "InvoiceServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = InvoiceUI.class, productionMode = false)
public class InvoiceServlet extends VaadinServlet {

}
