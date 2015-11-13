package com.castleby.invoice.service;

import org.testng.annotations.Test;

import com.castleby.invoice.TestBaseClass;

@Test
public class TestEnglishNumberToWords extends TestBaseClass {
    
    @Test
    public void testConvertor() {
        System.out.println("*** " + EnglishNumberToWords.convert(0));
        System.out.println("*** " + EnglishNumberToWords.convert(1));
        System.out.println("*** " + EnglishNumberToWords.convert(16));
        System.out.println("*** " + EnglishNumberToWords.convert(100));
        System.out.println("*** " + EnglishNumberToWords.convert(118));
        System.out.println("*** " + EnglishNumberToWords.convert(200));
        System.out.println("*** " + EnglishNumberToWords.convert(219));
        System.out.println("*** " + EnglishNumberToWords.convert(800));
        System.out.println("*** " + EnglishNumberToWords.convert(801));
        System.out.println("*** " + EnglishNumberToWords.convert(1316));
        System.out.println("*** " + EnglishNumberToWords.convert(1000000));
        System.out.println("*** " + EnglishNumberToWords.convert(2000000));
        System.out.println("*** " + EnglishNumberToWords.convert(3000200));
        System.out.println("*** " + EnglishNumberToWords.convert(700000));
        System.out.println("*** " + EnglishNumberToWords.convert(9000000));
        System.out.println("*** " + EnglishNumberToWords.convert(9001000));
        System.out.println("*** " + EnglishNumberToWords.convert(123456789));
        System.out.println("*** " + EnglishNumberToWords.convert(2147483647));
        System.out.println("*** " + EnglishNumberToWords.convert(3000000010L));
        
        /*
         *** zero
         *** one
         *** sixteen
         *** one hundred
         *** one hundred eighteen
         *** two hundred
         *** two hundred nineteen
         *** eight hundred
         *** eight hundred one
         *** one thousand three hundred sixteen
         *** one million
         *** two millions
         *** three millions two hundred
         *** seven hundred thousand
         *** nine millions
         *** nine millions one thousand
         *** one hundred twenty three millions four hundred
         **      fifty six thousand seven hundred eighty nine
         *** two billion one hundred forty seven millions
         **      four hundred eighty three thousand six hundred forty seven
         *** three billion ten
         **/
    }

}
