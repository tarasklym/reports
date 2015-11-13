package com.castleby.invoice.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.castleby.invoice.dropbox.DropboxService;
import com.castleby.invoice.entity.Invoice;
import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.repo.InvoiceItemRepository;
import com.castleby.invoice.entity.repo.InvoiceRepository;
import com.castleby.invoice.entity.repo.TaxRepository;
import com.castleby.invoice.pdf.ActDocument;
import com.castleby.invoice.pdf.InvoiceDocument;
import com.castleby.invoice.service.EntityBuilderService;
import com.castleby.invoice.upload.DropboxReceiver;
import com.castleby.invoice.view.InvoiceItemWindow.SaveListener;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

@SuppressWarnings("serial")
@SpringView(name=InvoiceView.NAME)
public class InvoiceView extends VerticalLayout implements View {

    public static final String NAME = "invoice";
    private static final String DELETE_PROPERTY = "delete";

    @Autowired
    private DropboxReceiver receiver;
    @Autowired
    private DropboxService dropboxService;
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private EntityBuilderService builderService;
    @Autowired
    private InvoiceDocument invoiceDocument;
    @Autowired
    private ActDocument actDocument;
    @Autowired TaxRepository taxRepository;
    
    private InvoiceItemWindow window;
    private BeanItemContainer<InvoiceItem> container = new BeanItemContainer<>(InvoiceItem.class);
    private Invoice invoice;
    private Grid grid = new Grid();
    private String path;

    @PostConstruct
    private void init() {
        setSpacing(true);
        addTopBar();
        addListComponent();
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        path = event.getParameters();
        String[] split = StringUtils.split(path, "/");
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1]);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month - 1 , 1);
        Date startDate = cal.getTime();
        LocalDate initial = LocalDate.of(year, month, 1);
        cal.set(year, month - 1, initial.lengthOfMonth());
        Date endDate = cal.getTime();
        invoice = invoiceRepository.findByDateBetween(startDate, endDate);
        if (invoice == null) {
            invoice = builderService.buildInvoice();
            cal.set(year, month - 1, initial.lengthOfMonth());
            invoice.setDate(cal.getTime());
            invoice = invoiceRepository.saveAndFlush(invoice);
        }
        grid.setContainerDataSource(createGridContainer());
    }
    
    private void addListComponent() {
        grid.setContainerDataSource(createGridContainer());
        grid.setColumns("description", "netto", "extraTaxRate", "amount", "type", DELETE_PROPERTY);
        grid.setWidth("700px");
        grid.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    UI.getCurrent().addWindow(window);
                    BeanItem<InvoiceItem> item = container.getItem(event.getItemId());
                    window.setItem(item.getBean());
                }
            }
        });
        addRemoveHandler(grid);
        addFooter(grid);
        addComponent(grid);
    }
    
    private void addRemoveHandler(Grid grid) {
        grid.getColumn(DELETE_PROPERTY).setRenderer(new ButtonRenderer(new RendererClickListener() {
            @Override
            public void click(RendererClickEvent event) {
                container.removeItem(event.getItemId());
                invoiceItemRepository.delete((InvoiceItem) event.getItemId());
            }
        }));
    }
    
    private GeneratedPropertyContainer createGridContainer() {
        List<InvoiceItem> defaultItems = getInvoiceItems();
        container.removeAllItems();
        container.addAll(defaultItems);
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(container);
        gpc.addGeneratedProperty(DELETE_PROPERTY, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Delete";
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        return gpc;
    }

    private List<InvoiceItem> getInvoiceItems() {
        List<InvoiceItem> items = new ArrayList<>(); 
        if (invoice != null) {
            items = invoiceItemRepository.findByInvoice(invoice);
        }
        return items;
    }
    
    private void addFooter(Grid grid) {
        final FooterRow footer = grid.addFooterRowAt(0);
        updateTotalAmount(footer);
        container.addItemSetChangeListener(new ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(ItemSetChangeEvent event) {
                updateTotalAmount(footer);
            }
        });
    }

    private void updateTotalAmount(final FooterRow footer) {
        double totalAmount = 0;
        for (InvoiceItem item : getInvoiceItems()) {
            totalAmount += item.getAmount();
        }
        footer.getCell("description").setText("Total amount: " + totalAmount);
    }
    
    private void addTopBar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        addNewItemButton(layout);
        addGenerateReportButton(layout);
        addDownloadActButton(layout);
        addDownloadInvoiceButton(layout);
        addUploadButton(layout);
        addComponent(layout);
    }

    private void addNewItemButton(HorizontalLayout layout) {
        Button addButton = new Button("add", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(window);
                window.setItem(new InvoiceItem());
            }
        });
        window = new InvoiceItemWindow(taxRepository);
        window.addSaveListener(new SaveListener() {
            @Override
            public void save(InvoiceItem item) {
                item = invoiceItemRepository.saveAndFlush(item);
                container.removeItem(item);
                container.addBean(item);
            }
        });
        layout.addComponent(addButton);
    }
    
    private void addGenerateReportButton(HorizontalLayout layout) {
        Button generateButton = new Button("generate");
        generateButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                invoiceDocument.setInvoice(invoice);
                invoiceDocument.generate();
                actDocument.setInvoice(invoice);
                actDocument.generate();
            }
        });
        layout.addComponent(generateButton);
    }
    
    private void addDownloadInvoiceButton(HorizontalLayout layout) {
        Button downloadButton = new Button("Download Invoice");
        FileDownloader fileDownloader = new FileDownloader(new StreamResource(new StreamSource() {
            @Override
            public InputStream getStream() {
                byte[] byteArray = dropboxService.getFile("/", "invoice.pdf").toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
        }, "invoice.pdf"));
        fileDownloader.extend(downloadButton);
        layout.addComponent(downloadButton);
    }

    private void addDownloadActButton(HorizontalLayout layout) {
        Button downloadButton = new Button("Download Act");
        FileDownloader fileDownloader = new FileDownloader(new StreamResource(new StreamSource() {
            @Override
            public InputStream getStream() {
                byte[] byteArray = dropboxService.getFile("/", "act.pdf").toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
        }, "act.pdf"));
        fileDownloader.extend(downloadButton);
        layout.addComponent(downloadButton);
    }
    
    private void addUploadButton(HorizontalLayout layout) {
        Upload upload = new Upload(null, receiver);
        upload.addSucceededListener(receiver);
        upload.addStartedListener(new StartedListener() {
            @Override
            public void uploadStarted(StartedEvent event) {
                receiver.setFolderPath(path);
            }
        });
        upload.setImmediate(true);
        upload.setButtonCaption("upload signed");
        layout.addComponent(upload);
    }
    
}
