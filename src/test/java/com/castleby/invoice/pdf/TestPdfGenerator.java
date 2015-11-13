package com.castleby.invoice.pdf;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class TestPdfGenerator {

    private static final String HELLOJASPER_JRXML = "hellojasper.jrxml";

    @Test
    public void testFileGenerator() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("Author", "taras");
        String reportName = "HelloJasper.pdf";
        PdfGenerator pdfGenerator = new PdfGenerator(HELLOJASPER_JRXML, reportName, properties);
        pdfGenerator.getFile();
        File inputFile = new File(reportName);
        Assert.assertTrue(inputFile.exists());
    }
    
    @Test
    public void testActGenerator() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ActDocument.DATE_UA_PROPERTY, "20 жовтня 2015 р.");
        properties.put(ActDocument.DATE_EN_PROPERTY, "20 October 2015");
        properties.put(ActDocument.PERIOD_UA_PROPERTY, "3 жовтня 2015 р. по 20 жовтня 2015 р.");
        properties.put(ActDocument.PERIOD_EN_PROPERTY, "October 3, 2015 till October 20, 2015. ");
        properties.put(ActDocument.AMOUNT_UA_PROPERTY, "3710 (три тисячі двісті девяносто) доларів США.");
        properties.put(ActDocument.AMOUNT_EN_PROPERTY, "3710 (three thousand two hundred ninety) US dollars.");
        String reportName = "act.pdf";
        PdfGenerator pdfGenerator = new PdfGenerator("act.jrxml", reportName, properties);
        pdfGenerator.getFile();
        File inputFile = new File(reportName);
        Assert.assertTrue(inputFile.exists());
    }
    
    @Test
    public void testInvoiceGenerator() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(InvoiceDocument.ID_UA_PROPERTY, "2015-10");
        properties.put(InvoiceDocument.ID_EN_PROPERTY, "2015-10");
        properties.put(InvoiceDocument.DATE_UA_PROPERTY, "20 жовтня 2015 р.");
        properties.put(InvoiceDocument.DATE_EN_PROPERTY, "20 October 2015");
        properties.put(InvoiceDocument.SPENDING_UA_PROPERTY, "360 (триста шістдесят) USD");
        properties.put(InvoiceDocument.SPENDING_EN_PROPERTY, "360 (three hundred and sixty) USD");
        properties.put(InvoiceDocument.SALARY_UA_PROPERTY, "3350 (три тисячі триста п’ятдесят) USD");
        properties.put(InvoiceDocument.SALARY_EN_PROPERTY, "3350 (three thousand three hundred and fifty) USD");
        properties.put(InvoiceDocument.TOTAL_PROPERTY, 3350);
        String reportName = "invoice.pdf";
        PdfGenerator pdfGenerator = new PdfGenerator("invoice.jrxml", reportName, properties);
        pdfGenerator.getFile();
        File inputFile = new File(reportName);
        Assert.assertTrue(inputFile.exists());
    }
    
    @Test
    public void testStreamGenerator() {
        Map<String, Object> properties = new HashMap<>();
        String reportName = "HelloJasper.pdf";
        PdfGenerator pdfGenerator = new PdfGenerator(HELLOJASPER_JRXML, reportName, properties);
        OutputStream stream = pdfGenerator.getStream();
        Assert.assertNotNull(stream);
    }
}
