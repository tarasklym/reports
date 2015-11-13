package com.castleby.invoice.pdf;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.castleby.invoice.service.EnglishNumberToWords;
import com.castleby.invoice.service.UkrainianNumberToWords;

public class DocumentUtils {

    private DocumentUtils(){}
    
    public final static String getId(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        return "" + year + "-" + month; 
    }
    
    public final static String getDate(Language language, Date date) {
        int style = DateFormat.LONG;
        Locale locale = new Locale(language.getValue());
        DateFormat df = DateFormat.getDateInstance(style, locale);
        return df.format(date);
    }
    
    public final static String getStringFromInt(Language language, long number) {
        String result = "";
        if (language == Language.ENG) {
            result = "" + number + " (" + EnglishNumberToWords.convert(number) + ") USD";
        } else if (language == Language.UKR) {
            result = "" + number + " (" + UkrainianNumberToWords.convert(number) + ") USD";
        } else {
            throw new IllegalArgumentException("Language is not supported");
        }
        return result;
    }
}
