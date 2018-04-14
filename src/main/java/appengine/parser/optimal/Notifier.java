package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalnotifyNotifytype;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.Notify;
import appengine.parser.optimal.objects.NotifyType;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.optimal.utils.DataAnalyzerUtil;
import appengine.parser.utils.DataBaseConnector;
import okhttp3.*;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.util.ArrayList;

import static appengine.parser.mysqlmodels.Tables.OPTIMALJSON;
import static appengine.parser.mysqlmodels.Tables.OPTIMALNOTIFY;
import static appengine.parser.utils.TimeUtils.getCurrentTime;

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
                    resultOfCalculation.getLowestBuyCoin().getOurBuyPrice(), resultOfCalculation.getHighestSellCoin().getMarket(),
                    resultOfCalculation.getHighestSellCoin().getOurSellPrice(), null);

            Notify oldnotify = dataAnalyzer.getDataFromLastNotify(newnotify);

            modifyNotifyType(newnotify, oldnotify);

            if (newnotify.notifyType != null) {
                insertResultInDB(newnotify);
                if (!(newnotify.profit < 0 && oldnotify.profit < 0)) {
                    postOnSlack(newnotify.toString());
                }
            }

        }

        return "ok";

    }

    public String fetchOkexBinance() {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<ResultOfCalculation> resultOfCalculationList = dataAnalyzer.getDataFromLastUpdate();

        DataAnalyzerUtil dataAnalyzerUtil = new DataAnalyzerUtil();
        ArrayList<ResultOfCalculation> filterResultOfCalculationList =
                dataAnalyzerUtil.fetchDataFromTwoExchanges(Market.BINANCE, Market.OKEX);

        for (int i = 0; i < filterResultOfCalculationList.size(); i++) {

            ResultOfCalculation resultOfCalculation = filterResultOfCalculationList.get(i);

            if (resultOfCalculation.profitPercentage() > 1) {

                Notify newnotify = getNotifyFromResultOfCalculation(resultOfCalculation);

                Notify oldnotify = dataAnalyzer.getDataFromLastNotify(newnotify);

                modifyNotifyType(newnotify, oldnotify);

                if (newnotify.notifyType != null) {
                    insertResultInDB(newnotify);
                    postOnSlacKOkexBinance(newnotify.toString());
                }
            }

        }

        return "ok";

    }

    private void modifyNotifyType(Notify newnotify, Notify oldnotify) {
        if (oldnotify == null) {
            if (newnotify.profit < 2) {
                newnotify.setNotifyType(NotifyType.EQUAL);
            } else {
                newnotify.setNotifyType(NotifyType.NEWRAISE);
            }
        } else {

            Double oldProfit = oldnotify.profit;
            Double newProfit = newnotify.profit;

            if (oldnotify.notifyType == NotifyType.EQUAL) {
                newnotify.setNotifyType(NotifyType.NEWRAISE);
            } else if (newProfit > oldProfit + percentageOf(oldProfit, 10)) {
                newnotify.setNotifyType(NotifyType.RAISEINCREASE);
            } else if (newProfit < oldProfit - percentageOf(oldProfit, 10)) {
                newnotify.setNotifyType(NotifyType.RAISEDECREASE);
            } else if (newProfit < 2) {
                newnotify.setNotifyType(NotifyType.EQUAL);
            }
        }
    }


    public Notify getNotifyFromResultOfCalculation(ResultOfCalculation resultOfCalculation) {
        Notify notify = new Notify(resultOfCalculation.getCoin(), resultOfCalculation.getTimeStamp(),
                resultOfCalculation.profitPercentage(), resultOfCalculation.getLowestBuyCoin().getMarket(),
                resultOfCalculation.getLowestBuyCoin().getOurBuyPrice(), resultOfCalculation.getHighestSellCoin().getMarket(),
                resultOfCalculation.getHighestSellCoin().getOurSellPrice(), null);
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
                OPTIMALNOTIFY.BUYPRICE, OPTIMALNOTIFY.TOMARKET, OPTIMALNOTIFY.SELLPRICE, OPTIMALNOTIFY.NOTIFYTYPE)
                .values(notify.coinlabel, notify.profit, notify.frommarket.name(), notify.buyprice, notify.tomarket.name(),
                        notify.sellprice, OptimalnotifyNotifytype.valueOf(notify.notifyType.name())).onDuplicateKeyUpdate()
                .set(OPTIMALNOTIFY.TIME, getCurrentTime())
                .set(OPTIMALNOTIFY.PROFIT, notify.profit)
                .set(OPTIMALNOTIFY.BUYPRICE, notify.buyprice)
                .set(OPTIMALNOTIFY.SELLPRICE, notify.sellprice)
                .execute();
    }


    private void insertResultInDB(ResultOfCalculation resultOfCalculation) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALJSON, OPTIMALJSON.COINLABEL, OPTIMALJSON.JSON)
                .values(resultOfCalculation.getCoin(), resultOfCalculation.toJSON()).execute();
    }
}
