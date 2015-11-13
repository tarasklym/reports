package com.castleby.invoice.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.castleby.invoice.dropbox.DropboxService;
import com.castleby.invoice.entity.Invoice;
import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;
import com.castleby.invoice.entity.repo.InvoiceItemRepository;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class InvoiceDocument {
    
    public static final String DATE_EN_PROPERTY = "date_en";
    public static final String DATE_UA_PROPERTY = "date_ua";
    public static final String SALARY_EN_PROPERTY = "salary_en";
    public static final String SALARY_UA_PROPERTY = "salary_ua";
    public static final String SPENDING_EN_PROPERTY = "spending_en";
    public static final String SPENDING_UA_PROPERTY = "spending_ua";
    public static final String ID_EN_PROPERTY = "id_en";
    public static final String ID_UA_PROPERTY = "id_ua";
    public static final String TOTAL_PROPERTY = "total";
    
    private static final String TEMPLATE_NAME = "invoice.jrxml";
    private static final String REPORT_NAME = "/invoice.pdf";
    private Invoice invoice;
    @Autowired
    private InvoiceItemRepository itemRepository;
    @Autowired
    private DropboxService dropboxService;
    
    public void generate() {
        Calendar calendar = Calendar.getInstance();
        Date date = invoice.getDate();
        calendar.setTime(date);
        String path = "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put(ID_UA_PROPERTY, DocumentUtils.getId(date));
        properties.put(ID_EN_PROPERTY, DocumentUtils.getId(date));
        properties.put(DATE_UA_PROPERTY, DocumentUtils.getDate(Language.UKR, date));
        properties.put(DATE_EN_PROPERTY, DocumentUtils.getDate(Language.ENG, date));
        int salary = 0;
        int other = 0;
        for (InvoiceItem item : itemRepository.findByInvoice(invoice)) {
            if (item.getType() == ItemType.SALARY) {
                salary += item.getAmount();
            } else {
                other += item.getAmount();
            }
        }
        properties.put(SPENDING_UA_PROPERTY, DocumentUtils.getStringFromInt(Language.UKR, other));
        properties.put(SPENDING_EN_PROPERTY, DocumentUtils.getStringFromInt(Language.ENG, other));
        properties.put(SALARY_UA_PROPERTY, DocumentUtils.getStringFromInt(Language.UKR, salary));
        properties.put(SALARY_EN_PROPERTY, DocumentUtils.getStringFromInt(Language.ENG, salary));
        properties.put(TOTAL_PROPERTY, salary + other);
        PdfGenerator pdfGenerator = new PdfGenerator(TEMPLATE_NAME, REPORT_NAME, properties);
        ByteArrayOutputStream stream = pdfGenerator.getStream();
        byte[] byteArray = stream.toByteArray();
        dropboxService.save(path + REPORT_NAME, byteArray.length, new ByteArrayInputStream(byteArray));
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
