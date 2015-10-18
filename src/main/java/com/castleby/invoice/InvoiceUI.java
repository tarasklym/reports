package com.castleby.invoice;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author taras.klym
 *
 */
@SuppressWarnings("serial")
@Theme("invoicer")
@Widgetset("com.castleby.invoice.InvoiceWidgetset")
@SpringUI(path="")
public class InvoiceUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(new Button("test"));
        setContent(layout);
    }
}
