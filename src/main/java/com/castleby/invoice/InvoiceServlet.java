package com.castleby.invoice;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.castleby.invoice.service.DefaultDataService;
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

    private WebApplicationContext appContext;
        
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        appContext = WebApplicationContextUtils
                .getWebApplicationContext(servletConfig.getServletContext());
        DefaultDataService defaultDataService = appContext.getBean(DefaultDataService.class);
        defaultDataService.init();
    }
}
