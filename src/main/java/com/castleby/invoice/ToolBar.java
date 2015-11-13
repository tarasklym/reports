package com.castleby.invoice;

import com.castleby.invoice.view.SettingView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class ToolBar extends HorizontalLayout {

    public ToolBar() {
        setSpacing(true);
        setMargin(true);
        setWidth("100%");
        
        addSettingButton();
    }

    private void addSettingButton() {
        Button settings = new Button("", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().getNavigator().navigateTo(SettingView.NAME);
            }
        });
        settings.setIcon(FontAwesome.GEARS);
        addComponent(settings);
        setComponentAlignment(settings, Alignment.MIDDLE_RIGHT);
    }
}
