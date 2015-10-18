package com.castleby.invoice;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.spring.server.SpringVaadinServlet;

/**
 * 
 * @author taras.klym
 *
 */
@WebServlet(urlPatterns = "/*", name = "InvoiceServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = InvoiceUI.class, productionMode = false)
@SuppressWarnings("serial")
public class InvoiceServlet extends SpringVaadinServlet {

}
