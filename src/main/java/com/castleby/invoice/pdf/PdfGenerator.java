package com.castleby.invoice.pdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class PdfGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerator.class);
    private static final String TEMPLATE_PATH = "/templates/"; 
    private String templateName;
    private String reportName;
    private Map<String, Object> propertyMap;
    
    public PdfGenerator(String templateName, String reportName, Map<String, Object> propertyMap) {
        this.templateName = templateName;
        this.reportName = reportName;
        this.propertyMap = propertyMap;
    }
    
    public void getFile() {
        try {
            JasperReport jasperReport = JasperCompileManager
                    .compileReport(PdfGenerator.class.getResourceAsStream(TEMPLATE_PATH + templateName));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, propertyMap,
                    new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, reportName);
        } catch (JRException e) {
            LOGGER.error("Exception during report generation");
        }        
    }
    
    public OutputStream getStream() {
        OutputStream outStream = new ByteArrayOutputStream();
        try {
            JasperReport jasperReport = JasperCompileManager
                    .compileReport(PdfGenerator.class.getResourceAsStream(TEMPLATE_PATH + templateName));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, propertyMap,
                    new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (JRException e) {
            LOGGER.error("Exception during report generation");
        }
        return outStream;
    }
}
