package com.castleby.invoice;

import org.springframework.beans.factory.annotation.Autowired;

import com.castleby.invoice.component.tree.MonthsTree;
import com.castleby.invoice.dropbox.DropboxService;
import com.castleby.invoice.view.MainView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author taras.klym
 *
 */
@SuppressWarnings("serial")
@Widgetset("com.castleby.invoice.InvoiceWidgetset")
@Theme("invoicer")
@SpringUI(path="")
@Title("Reporter")
@Push
public class InvoiceUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private DropboxService dropboxService;
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setSizeFull();
        setContent(mainLayout);
        addLeftLayout(mainLayout);
        addRightLayout(mainLayout);
    }

    private void addLeftLayout(final HorizontalLayout mainLayout) {
        MonthsTree tree = new MonthsTree(dropboxService);
        Panel panel = new Panel(tree);
        panel.setHeight("100%");
        panel.setWidth("300px");
        mainLayout.addComponent(panel);
    }

    private void addRightLayout(final HorizontalLayout mainLayout) {
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setSizeFull();
        mainLayout.addComponent(rightLayout);
        mainLayout.setExpandRatio(rightLayout, 2);
        
        addToolbar(rightLayout);
        addNavigationLayout(rightLayout);
    }
    
    private void addToolbar(VerticalLayout rightLayout) {
        rightLayout.addComponent(new ToolBar());
    }
    
    private void addNavigationLayout(VerticalLayout rightLayout) {
        VerticalLayout navigatorLayout = new VerticalLayout();
        navigatorLayout.setSizeFull();
        rightLayout.addComponent(navigatorLayout);
        rightLayout.setExpandRatio(navigatorLayout, 2);
        
        Navigator navigator = new Navigator(this, navigatorLayout);
        navigator.addProvider(viewProvider);
        navigator.navigateTo(MainView.NAME);
    }
}
