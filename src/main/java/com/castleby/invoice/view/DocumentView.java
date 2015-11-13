package com.castleby.invoice.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.castleby.invoice.dropbox.DropboxService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringView(name=DocumentView.NAME)
public class DocumentView extends VerticalLayout implements View {

    public static final String NAME = "document";
    
    @Autowired
    private DropboxService dropboxService;
    
    BrowserFrame pdf = new BrowserFrame();
    
    @PostConstruct
    public void init() {
        setSizeFull();
        pdf.setSizeFull();
        addComponent(pdf);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        String path = "/" + event.getParameters();
        String name = StringUtils.substringAfterLast(path, "/");
        Resource pdfFile = new StreamResource(new StreamSource() {
            
            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(dropboxService.getFile(path).toByteArray());
            }
        }, name);
        pdf.setSource(pdfFile);
    }

}
