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
        String reportName = "HelloJasper.pdf";
        PdfGenerator pdfGenerator = new PdfGenerator(HELLOJASPER_JRXML, reportName, properties);
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
