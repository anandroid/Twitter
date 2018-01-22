package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalnotifyNotifytype;
import appengine.parser.mysqlmodels.enums.OptimalupdateOperation;
import appengine.parser.optimal.objects.Notify;
import appengine.parser.optimal.objects.NotifyType;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.utils.DataBaseConnector;
import com.google.gson.Gson;
import org.jooq.*;

import java.sql.Timestamp;
import java.util.ArrayList;

import static appengine.parser.mysqlmodels.Tables.*;

/**
 * Created by anand.kurapati on 15/01/18.
 */
public class DataAnalyzer {

    String printString = "\n";

    public String coinAnalyzer(String[] coins, boolean isJson) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        for (String coin : coins) {
            coin = coin.toUpperCase();
            Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                    where(OPTIMALJSON.COINLABEL.eq(coin)).limit(500).fetch();

            for (int i = 0; i < result.size(); i++) {
                ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
                resultOfCalculation.setTimestamp(result.get(i).value2());
                if (isJson) {
                    print(resultOfCalculation.toJSON(), isJson);
                } else {
                    print(resultOfCalculation.toString(), isJson);
                }
            }
        }
        return resultString(isJson);
    }

    public String getDataFromLastUpdate(boolean isJson) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();

        return getDataFromTime(updatedTimeRecord.value1(), isJson);
    }

    public String getDataFromTime(Timestamp timeStamp, boolean isJson) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                where(OPTIMALJSON.TIME.greaterOrEqual(timeStamp)).and(OPTIMALJSON.COINLABEL.isNotNull()).limit(500).fetch();
        for (int i = 0; i < result.size(); i++) {
            ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
            resultOfCalculation.setTimestamp(result.get(i).value2());
            if (isJson) {
                print(resultOfCalculation.toJSON(), isJson);
            } else {
                print(resultOfCalculation.toString(), isJson);
            }
        }
        return resultString(isJson);
    }

    public ArrayList<ResultOfCalculation> getDataFromLastUpdate() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();
        return getDataFromTime(updatedTimeRecord.value1());
    }

    public ArrayList<ResultOfCalculation> getDataFromTime(Timestamp timeStamp) {

        ArrayList<ResultOfCalculation> resultOfCalculations = new
                ArrayList<>();

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                where(OPTIMALJSON.TIME.greaterOrEqual(timeStamp)).and(OPTIMALJSON.COINLABEL.isNotNull()).limit(500).fetch();
        for (int i = 0; i < result.size(); i++) {
            ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
            resultOfCalculation.setTimestamp(result.get(i).value2());
            resultOfCalculations.add(resultOfCalculation);
        }
        return resultOfCalculations;
    }

    public String getDataFromTimeAndCoin(Timestamp timeStamp, String[] coins, boolean isJson) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        for (String coin : coins) {
            coin = coin.toUpperCase();
            Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                    where(OPTIMALJSON.TIME.greaterThan(timeStamp)).and(OPTIMALJSON.COINLABEL.eq(coin)).limit(500).fetch();
            for (int i = 0; i < result.size(); i++) {
                ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
                resultOfCalculation.setTimestamp(result.get(i).value2());
                if (isJson) {
                    print(resultOfCalculation.toJSON(), isJson);
                } else {
                    print(resultOfCalculation.toString(), isJson);
                }
            }
        }
        return resultString(isJson);
    }

    public Notify getDataFromNotify(Notify notify) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record6<String, Timestamp, Double, String, String, OptimalnotifyNotifytype>> result =
                dslContext.select(OPTIMALNOTIFY.COINLABEL, OPTIMALNOTIFY.TIME, OPTIMALNOTIFY.PROFIT,
                        OPTIMALNOTIFY.FROMMARKET, OPTIMALNOTIFY.TOMARKET, OPTIMALNOTIFY.NOTIFYTYPE).from(OPTIMALNOTIFY).
                        where(OPTIMALNOTIFY.COINLABEL.eq(notify.coinlabel).and(OPTIMALNOTIFY.FROMMARKET.eq(notify.frommarket.name())).
                                and(OPTIMALNOTIFY.TOMARKET.eq(notify.tomarket.name()))).fetch();

        if (result.size() > 1) {
            //should not occur here
        }

        if (result.size() == 1) {

            Record6<String, Timestamp, Double, String, String, OptimalnotifyNotifytype> record = result.get(0);
            Notify oldnotify = new Notify(record.value1(), record.value2(), record.value3(), record.value4(), record.value5(),
                    NotifyType.valueOf(record.value6().toString()));

            return oldnotify;
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
