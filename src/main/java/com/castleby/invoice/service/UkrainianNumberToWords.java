package com.castleby.invoice.service;

import java.text.DecimalFormat;

public class UkrainianNumberToWords {

    private static final String[] numNames = {
      "",
      " один",
      " два",
      " три",
      " чотири",
      " п'ять",
      " шість",
      " сім",
      " вісім",
      " дев'ять",
      " десять",
      " одинадцять",
      " дванадцять",
      " тринадцять",
      " чотирнадцять",
      " п'ятнадцять",
      " шістнадцять",
      " сімнадцять",
      " вісімнадцять",
      " дев'ятнадцять"
    };
    

    private static final String[] tensNames = {
      "",
      " десять",
      " двадцять",
      " тридцять",
      " сорок",
      " п'ятдесят",
      " шістдесят",
      " сімдесят",
      " вісімдесят",
      " девяносто"
    };

    private static final String[] hundredNames = {
      "",
      " сто",
      " двісті",
      " триста",
      " чотириста",
      " п'ятсот",
      " шістсот",
      " сімсот",
      " вісімсот",
      " девятсот"
    };

    private UkrainianNumberToWords() {}

    private static String convertLessThanOneThousand(int number) {
      String soFar;

      if (number % 100 < 20){
        soFar = numNames[number % 100];
        number /= 100;
      }
      else {
        soFar = numNames[number % 10];
        number /= 10;

        soFar = tensNames[number % 10] + soFar;
        number /= 10;
      }
      if (number == 0) return soFar;
      return " " + hundredNames[number] + soFar;
    }


    public static String convert(long number) {
      // 0 to 999 999 999 999
      if (number == 0) { return "нуль"; }

      String snumber = Long.toString(number);

      // pad with "0"
      String mask = "000000000000";
      DecimalFormat df = new DecimalFormat(mask);
      snumber = df.format(number);

      // XXXnnnnnnnnn
      int billions = Integer.parseInt(snumber.substring(0,3));
      // nnnXXXnnnnnn
      int millions  = Integer.parseInt(snumber.substring(3,6));
      // nnnnnnXXXnnn
      int hundredThousands = Integer.parseInt(snumber.substring(6,9));
      // nnnnnnnnnXXX
      int thousands = Integer.parseInt(snumber.substring(9,12));

      String tradBillions;
      switch (lastNumer(billions)) {
      case 0:
        tradBillions = "";
        break;
      case 1 :
        tradBillions = convertLessThanOneThousand(billions)
        + " мільярд ";
        break;
      case 2: case 3: case 4:  
          tradBillions = convertLessThanOneThousand(billions)
          + " мільярди ";
          break;
      default :
        tradBillions = convertLessThanOneThousand(billions)
        + " мільярдів ";
      }
      String result =  tradBillions;

      String tradMillions;
      switch (lastNumer(millions)) {
      case 0:
        tradMillions = "";
        break;
      case 1 :
        tradMillions = convertLessThanOneThousand(millions)
           + " мільйон ";
        break;
      case 2 : case 3 : case 4 :
          tradMillions = convertLessThanOneThousand(millions)
             + " мільйони ";
          break;
      default :
        tradMillions = convertLessThanOneThousand(millions)
           + " мільйонів ";
      }
      result =  result + tradMillions;

      String tradHundredThousands;
      switch (lastNumer(hundredThousands)) {
      case 0:
        tradHundredThousands = "";
        break;
      case 1 :
        tradHundredThousands = "одна тисяча ";
        break;
      case 2 : case 3 : case 4 :
          tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " тисячі ";
          break;
      default :
        tradHundredThousands = convertLessThanOneThousand(hundredThousands)
           + " тисяч ";
      }
      result =  result + tradHundredThousands;

      String tradThousand;
      tradThousand = convertLessThanOneThousand(thousands);
      result =  result + tradThousand;

      // remove extra spaces!
      return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
    
    private static int lastNumer(int number) {
        int result = number;
        if (number > 100) {
            result = number % 100;
            result = result % 10;
        } else if (number > 20){
            result = number % 10;
        }
        return result == 0 ? number : result;
    }
}
