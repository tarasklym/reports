package com.castleby.invoice.view;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(name=MainView.NAME)
public class MainView extends VerticalLayout implements View {

    public static final String NAME = "";

    @Override
    public void enter(ViewChangeEvent event) {
    }
    
    @PostConstruct
    private void init() {
        setSpacing(true);
        setMargin(true);
    }
}
