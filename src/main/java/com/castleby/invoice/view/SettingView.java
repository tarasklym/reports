package com.castleby.invoice.view;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;
import com.castleby.invoice.entity.repo.InvoiceItemRepository;
import com.castleby.invoice.entity.repo.TaxRepository;
import com.castleby.invoice.view.InvoiceItemWindow.SaveListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;

@SuppressWarnings("serial")
@SpringView(name=SettingView.NAME)
public class SettingView extends VerticalLayout implements View {

    public static final String NAME = "settings";
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;
    @Autowired
    private TaxRepository taxRepository;
    private InvoiceItemWindow window;
    private BeanItemContainer<InvoiceItem> container = new BeanItemContainer<>(InvoiceItem.class);
    private static final String DELETE_PROPERTY = "delete";
    
    @Override
    public void enter(ViewChangeEvent event) {
    }
    
    @PostConstruct
    private void init() {
        setSpacing(true);
        setMargin(true);
        addGrid();
        addNewItemButton();
    }

    private void addGrid() {
        Grid grid = new Grid(createGridContainer());
        grid.setColumns("description", "amount", DELETE_PROPERTY);
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
        List<InvoiceItem> defaultItems = invoiceItemRepository.findByType(ItemType.DEFAULT);
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

    private void addNewItemButton() {
        window = new InvoiceItemWindow(taxRepository);
        Button addNew = new Button("Add new default item", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                window.setItem(new InvoiceItem());
                UI.getCurrent().addWindow(window);
            }
        });
        window.addSaveListener(new SaveListener() {
            @Override
            public void save(InvoiceItem item) {
                item.setType(ItemType.DEFAULT);
                item = invoiceItemRepository.saveAndFlush(item);
                container.removeItem(item);
                container.addBean(item);
            }
        });
        addNew.setIcon(FontAwesome.PLUS);
        addComponent(addNew);
    }

}
