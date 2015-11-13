package com.castleby.invoice.pdf;

import java.util.Calendar;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class TestDocumentUtil {

    @Test
    public void testIdProperty() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 1);
        
        Assert.assertEquals(DocumentUtils.getId(calendar.getTime()), "2015-12");
    }
    
    @Test
    public void testDateProperty() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 1);
        
        Assert.assertEquals(DocumentUtils.getDate(Language.ENG, calendar.getTime()), "December 1, 2015");
        Assert.assertEquals(DocumentUtils.getDate(Language.UKR, calendar.getTime()), "1 грудня 2015");
    }
    
    @Test
    public void testIntConvertorProperty() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 11, 1);
        
        Assert.assertEquals(DocumentUtils.getStringFromInt(Language.ENG, 2324), "2324 (two thousand three hundred twenty four) USD");
        Assert.assertEquals(DocumentUtils.getStringFromInt(Language.UKR, 3578), "3578 (три тисячі п'ятсот сімдесят вісім) USD");
    }
}
