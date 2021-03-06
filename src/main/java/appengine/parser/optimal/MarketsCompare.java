package appengine.parser.optimal;

import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.Notify;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.optimal.utils.DataAnalyzerUtil;

import java.util.ArrayList;

public class MarketsCompare {

    String printString = "";

    public String getLastData(String firstMarket,String secondMarket,boolean isJson) {

        DataAnalyzerUtil dataAnalyzerUtil = new DataAnalyzerUtil();
        ArrayList<ResultOfCalculation> filteredResultOfCalculation =
                dataAnalyzerUtil.fetchDataFromTwoExchanges(Market.valueOf(firstMarket), Market.valueOf(secondMarket));
        for (int i = 0; i < filteredResultOfCalculation.size(); i++) {
            ResultOfCalculation resultOfCalculation = filteredResultOfCalculation.get(i);
            print(resultOfCalculation.toJSON(), isJson);
        }
        return resultString(isJson);
    }


    public String getCoinNotifyData(String firstMarket, String secondMarket ,String coinlabel, boolean isJson) {
        coinlabel = coinlabel.toUpperCase();

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<Notify> notifyArrayList = dataAnalyzer.getDataFromNotify(coinlabel, Market.valueOf(firstMarket),
                Market.valueOf(secondMarket));

        for (int i = 0; i < notifyArrayList.size(); i++) {
            Notify notify = notifyArrayList.get(i);
            print(notify.toJSON(), isJson);
        }

        return resultString(isJson);
    }


    private String resultString(boolean isJson) {

        if (printString.length() > 2 && printString.charAt(printString.length() - 2) == ',') {
            printString = printString.substring(0, printString.length() - 2);
            printString += "\n";
        }

        if (isJson) {
            return "[" + printString + "]";
        } else {
            return printString;
        }
    }


    private void print(String text, boolean isJson) {
        printString += text;
        if (isJson) {
            printString += ",";
        }
        printString += "\n";
    }

}
