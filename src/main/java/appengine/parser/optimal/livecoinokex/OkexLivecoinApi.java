package appengine.parser.optimal.livecoinokex;

import appengine.parser.optimal.livecoinokex.utils.DoubleUtil;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.optimal.utils.DataAnalyzerUtil;
import okhttp3.*;

import java.util.ArrayList;

public class OkexLivecoinApi {

    String printString = "";


    public String automate() {

        return getLastData(false);
    }

    private String getLastData(boolean isJson) {

        DataAnalyzerUtil dataAnalyzerUtil = new DataAnalyzerUtil();
        ArrayList<ResultOfCalculation> filteredResultOfCalculation =
                dataAnalyzerUtil.fetchDataFromTwoExchanges(Market.LIVECOIN, Market.OKEX);

        ArrayList<TradeDepth> allTradeDepths = new LivecoinUtil().getOrderBookAll();

        for (int i = 0; i < filteredResultOfCalculation.size(); i++) {
            ResultOfCalculation resultOfCalculation = filteredResultOfCalculation.get(i);
            if (resultOfCalculation.profitPercentage() > 0.5) {

                System.out.println(resultOfCalculation.toString());
                print(resultOfCalculation.toJSON(), isJson);

                TradeDepth tradeDepth = getTradeDepthForCoin(resultOfCalculation.getCoin(), allTradeDepths);

                if (tradeDepth != null) {

                    Double profit;


                    if (resultOfCalculation.getHighestSellCoin().getMarket() == Market.OKEX) {
                        profit = getMaxProfitAmount(resultOfCalculation, tradeDepth, true);
                    } else {
                        profit = getMaxProfitAmount(resultOfCalculation, tradeDepth, false);
                    }

                    String profitString = new DoubleUtil().priceFormatter(profit);

                    if (profit > 0.001) {

                        String text = "Profit : "+profitString+"\n"+resultOfCalculation.toString();
                        postOnSlack(text);
                    }

                    print("Profit - " + profitString+" BTC", isJson);
                    System.out.println("Profit - " + profitString);
                } else {
                    print("Trade depth null", isJson);
                    System.out.println("Trade depth null");
                }
            }

        }
        //return resultString(isJson);

        return "";
    }


    private Double getMaxProfitAmount(ResultOfCalculation resultOfCalculation, TradeDepth liveCointradedepth, boolean isSellingTradeOkex) {

        OkexUtil okexUtil = new OkexUtil();

        TradeDepth buyTradeDepth;
        TradeDepth sellTradeDepth;

        TradeDepth tradeDepthOkex = okexUtil.getOrderBook(resultOfCalculation.getUnderScoreBTCCoin());

        if (isSellingTradeOkex) {
            sellTradeDepth = tradeDepthOkex;
            buyTradeDepth = liveCointradedepth;
        } else {
            sellTradeDepth = liveCointradedepth;
            buyTradeDepth = tradeDepthOkex;
        }


        int buytradeIndex = 0;
        int selltradeIndex = 0;

        boolean noMoreProfitCanbeMade = false;

        Double profit = 0.0;

        while (!noMoreProfitCanbeMade) {

            if(buytradeIndex>buyTradeDepth.askList.size()||selltradeIndex>sellTradeDepth.bidList.size()){
                profit=profit*3;
                postOnSlack("Exceeded Order Book Profit "+buyTradeDepth.coin);
                return profit;
            }


            TradeDepth.Ask ourBuyTrade = buyTradeDepth.askList.get(buytradeIndex);
            TradeDepth.Bid ourSellTrade = sellTradeDepth.bidList.get(selltradeIndex);

            if (ourBuyTrade.price > ourSellTrade.price) {
                noMoreProfitCanbeMade = true;
                continue;
            }

            if (ourBuyTrade.amount < ourSellTrade.amount) {

                Double amount = ourBuyTrade.amount;
                profit += (ourSellTrade.price - ourBuyTrade.price) * amount;

                ourSellTrade.amount = ourSellTrade.amount - ourBuyTrade.amount;

                buytradeIndex++;

            }

            if (ourBuyTrade.amount > ourSellTrade.amount) {

                Double amount = ourSellTrade.amount;
                profit += (ourSellTrade.price - ourBuyTrade.price) * amount;

                ourBuyTrade.amount = ourBuyTrade.amount - ourSellTrade.amount;
                selltradeIndex++;
            }


        }


        return profit;

    }

    private void postOnSlack(String text) {

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{\"text\":\"" + text + "\"}");
            Request request = new Request.Builder()
                    .url("https://hooks.slack.com/services/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")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8fa86be2-d201-9b05-5249-2be48eeb8a59")
                    .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TradeDepth getTradeDepthForCoin(String coin, ArrayList<TradeDepth> allTradeDepths) {

        for (int i = 0; i < allTradeDepths.size(); i++) {

            TradeDepth tradeDepth = allTradeDepths.get(i);

            if (tradeDepth.coin.equalsIgnoreCase(coin)) {
                return tradeDepth;
            }

        }

        return null;
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
