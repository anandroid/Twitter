package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalnotifyNotifytype;
import appengine.parser.optimal.objects.*;
import appengine.parser.utils.DataBaseConnector;
import okhttp3.*;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static appengine.parser.mysqlmodels.Tables.OPTIMALJSON;
import static appengine.parser.mysqlmodels.Tables.OPTIMALNOTIFY;

/**
 * Created by anand.kurapati on 17/01/18.
 */
public class Notifier {


    public String fetch() {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<ResultOfCalculation> resultOfCalculationList = dataAnalyzer.getDataFromLastUpdate();


        for (int i = 0; i < resultOfCalculationList.size(); i++) {

            ResultOfCalculation resultOfCalculation = resultOfCalculationList.get(i);

            Notify newnotify = new Notify(resultOfCalculation.getCoin(), resultOfCalculation.getTimeStamp(),
                    resultOfCalculation.profitPercentage(), resultOfCalculation.getLowestBuyCoin().getMarket(),
                    resultOfCalculation.getHighestSellCoin().getMarket(), null);

            Notify oldnotify = dataAnalyzer.getDataFromNotify(newnotify);

            if (oldnotify == null) {
                newnotify.setNotifyType(NotifyType.NEWRAISE);
            } else {

                Double oldProfit = oldnotify.profit;
                Double newProfit = newnotify.profit;

                if (newProfit > oldProfit + percentageOf(oldProfit, 10)) {
                    newnotify.setNotifyType(NotifyType.RAISEINCREASE);
                }

                if (newProfit < oldProfit - percentageOf(oldProfit, 10)) {
                    newnotify.setNotifyType(NotifyType.RAISEDECREASE);
                }

                if (newProfit < 2) {
                    newnotify.setNotifyType(NotifyType.EQUAL);
                }
            }
            if (newnotify.notifyType != null) {
                insertResultInDB(newnotify);
                postOnSlack(newnotify.toString());
            }

        }

        return "ok";

    }

    public String fetchOkexBinance() {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<ResultOfCalculation> resultOfCalculationList = dataAnalyzer.getDataFromLastUpdate();

        for (int i = 0; i < resultOfCalculationList.size(); i++) {

            ResultOfCalculation resultOfCalculation = resultOfCalculationList.get(i);

            boolean isPaired = false;

            if (resultOfCalculation.getLowestBuyCoin().getMarket() == Market.BINANCE) {
                if (resultOfCalculation.getHighestSellCoin().getMarket() == Market.OKEX) {
                    isPaired = true;
                } else {
                    List<CoinMarket> otherCoinMarketsList = resultOfCalculation.getAllOtherMarkets();
                    for (int j = 0; j < otherCoinMarketsList.size(); j++) {
                        CoinMarket otherCoinMarket = otherCoinMarketsList.get(j);

                        if (otherCoinMarket.getMarket() == Market.OKEX) {
                            resultOfCalculation.setHighestSellCoin(otherCoinMarket);
                            isPaired = true;
                            break;
                        }
                    }
                }
            } else if (resultOfCalculation.getLowestBuyCoin().getMarket() == Market.OKEX) {
                if (resultOfCalculation.getHighestSellCoin().getMarket() == Market.BINANCE) {
                    isPaired = true;
                } else {
                    List<CoinMarket> otherCoinMarketsList = resultOfCalculation.getAllOtherMarkets();

                    for (int j = 0; j < otherCoinMarketsList.size(); j++) {
                        CoinMarket otherCoinMarket = otherCoinMarketsList.get(j);
                        if (otherCoinMarket.getMarket() == Market.BINANCE) {
                            resultOfCalculation.setHighestSellCoin(otherCoinMarket);
                            isPaired = true;
                            break;
                        }
                    }
                }
            } else {
                List<CoinMarket> otherCoinMarketsList = resultOfCalculation.getAllOtherMarkets();
                CoinMarket firstCoinMarket = null;
                CoinMarket secondCoinMarket = null;
                for (int j = 0; j < otherCoinMarketsList.size(); j++) {
                    CoinMarket coinMarket = otherCoinMarketsList.get(j);
                    if (coinMarket.getMarket() == Market.OKEX) {
                        firstCoinMarket = coinMarket;
                    }
                    if (coinMarket.getMarket() == Market.BINANCE) {
                        secondCoinMarket = coinMarket;
                    }
                }
                if (firstCoinMarket != null && secondCoinMarket != null) {

                    resultOfCalculation.setLowestBuyCoin(firstCoinMarket);
                    resultOfCalculation.setHighestSellCoin(secondCoinMarket);

                    Double firstProfitPercentage = resultOfCalculation.profitPercentage();

                    resultOfCalculation.setLowestBuyCoin(secondCoinMarket);
                    resultOfCalculation.setHighestSellCoin(firstCoinMarket);

                    Double secondProfitPercentage = resultOfCalculation.profitPercentage();

                    if (firstProfitPercentage > secondProfitPercentage) {
                        resultOfCalculation.setLowestBuyCoin(firstCoinMarket);
                        resultOfCalculation.setHighestSellCoin(secondCoinMarket);
                    } else {
                        //its already set previously
                    }
                    isPaired = true;
                }

            }

            if (isPaired && resultOfCalculation.profitPercentage() > 1) {

                Notify newnotify = getNotifyFromResultOfCalculation(resultOfCalculation);

                Notify oldnotify = dataAnalyzer.getDataFromNotify(newnotify);

                if (oldnotify == null) {
                    newnotify.setNotifyType(NotifyType.NEWRAISE);
                } else {

                    Double oldProfit = oldnotify.profit;
                    Double newProfit = newnotify.profit;

                    if (newProfit > oldProfit + percentageOf(oldProfit, 10)) {
                        newnotify.setNotifyType(NotifyType.RAISEINCREASE);
                    }

                    if (newProfit < oldProfit - percentageOf(oldProfit, 10)) {
                        newnotify.setNotifyType(NotifyType.RAISEDECREASE);
                    }

                    if (newProfit < 2) {
                        newnotify.setNotifyType(NotifyType.EQUAL);
                    }
                }
                if (newnotify.notifyType != null) {
                    insertResultInDB(newnotify);
                    postOnSlacKOkexBinance(newnotify.toString());
                }


            }


        }

        return "ok";

    }

    public Notify getNotifyFromResultOfCalculation(ResultOfCalculation resultOfCalculation) {
        Notify notify = new Notify(resultOfCalculation.getCoin(), resultOfCalculation.getTimeStamp(),
                resultOfCalculation.profitPercentage(), resultOfCalculation.getLowestBuyCoin().getMarket(),
                resultOfCalculation.getHighestSellCoin().getMarket(), null);
        return notify;
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

    private void postOnSlacKOkexBinance(String text) {

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

    public Double percentageOf(Double value, int times) {

        Double dtimes = new Double(times);
        return (value * dtimes) / 100;
    }


    public void fetchAndNotify() {

        long twoMinsBack = System.currentTimeMillis() - 60 * 1000 * 2;

        Timestamp timestamp = new Timestamp(twoMinsBack);

    }


    private void insertResultInDB(Notify notify) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALNOTIFY, OPTIMALNOTIFY.COINLABEL, OPTIMALNOTIFY.PROFIT, OPTIMALNOTIFY.FROMMARKET,
                OPTIMALNOTIFY.TOMARKET, OPTIMALNOTIFY.NOTIFYTYPE)
                .values(notify.coinlabel, notify.profit, notify.frommarket.name(), notify.tomarket.name(),
                        OptimalnotifyNotifytype.valueOf(notify.notifyType.name())).onDuplicateKeyUpdate()
                .set(OPTIMALNOTIFY.TIME, getCurrentTime())
                .set(OPTIMALNOTIFY.NOTIFYTYPE, OptimalnotifyNotifytype.valueOf(notify.notifyType.name()))
                .set(OPTIMALNOTIFY.PROFIT, notify.profit)
                .execute();
    }

    private Timestamp getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String result = sdf.format(calendar.getTime());
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp;
    }


    private void insertResultInDB(ResultOfCalculation resultOfCalculation) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALJSON, OPTIMALJSON.COINLABEL, OPTIMALJSON.JSON)
                .values(resultOfCalculation.getCoin(), resultOfCalculation.toJSON()).execute();
    }
}
