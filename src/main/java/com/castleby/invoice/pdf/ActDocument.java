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
import com.castleby.invoice.entity.repo.InvoiceItemRepository;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class ActDocument {

    public static final String DATE_EN_PROPERTY = "date_en";
    public static final String DATE_UA_PROPERTY = "date_ua";
    public static final String PERIOD_EN_PROPERTY = "period_en";
    public static final String PERIOD_UA_PROPERTY = "period_ua";
    public static final String AMOUNT_EN_PROPERTY = "amount_en";
    public static final String AMOUNT_UA_PROPERTY = "amount_ua";
    
    private static final String TEMPLATE_NAME = "act.jrxml";
    private static final String REPORT_NAME = "/act.pdf";
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
        String dateUa = DocumentUtils.getDate(Language.UKR, invoice.getDate());
        String dateEn = DocumentUtils.getDate(Language.ENG, invoice.getDate());
        properties.put(ActDocument.DATE_UA_PROPERTY, dateUa);
        properties.put(ActDocument.DATE_EN_PROPERTY, dateEn);

        calendar.setTime(invoice.getDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String periodUa = DocumentUtils.getDate(Language.UKR, calendar.getTime()) + " по " + dateUa;
        String periodEn = DocumentUtils.getDate(Language.ENG, calendar.getTime()) + " till " + dateEn;
        properties.put(ActDocument.PERIOD_UA_PROPERTY, periodUa);
        properties.put(ActDocument.PERIOD_EN_PROPERTY, periodEn);
        
        int spendings = 0;
        for (InvoiceItem item : itemRepository.findByInvoice(invoice)) {
            spendings += item.getAmount();
        }
        properties.put(ActDocument.AMOUNT_UA_PROPERTY, DocumentUtils.getStringFromInt(Language.UKR, spendings));
        properties.put(ActDocument.AMOUNT_EN_PROPERTY, DocumentUtils.getStringFromInt(Language.ENG, spendings));
        
        PdfGenerator pdfGenerator = new PdfGenerator(TEMPLATE_NAME, REPORT_NAME, properties);
        ByteArrayOutputStream stream = pdfGenerator.getStream();
        byte[] byteArray = stream.toByteArray();
        dropboxService.save(path + REPORT_NAME, byteArray.length, new ByteArrayInputStream(byteArray));
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
