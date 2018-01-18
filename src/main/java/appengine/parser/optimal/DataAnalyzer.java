package appengine.parser.optimal;

import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.utils.DataBaseConnector;
import com.google.gson.Gson;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;

import java.sql.Timestamp;

import static appengine.parser.mysqlmodels.Tables.OPTIMALJSON;

/**
 * Created by anand.kurapati on 15/01/18.
 */
public class DataAnalyzer {

    String printString = "\n";
    public String coinAnalyzer(String[] coins, boolean isJson) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        for (String coin : coins) {
            coin = coin.toUpperCase();
            Result<Record1<String>> result = dslContext.select(OPTIMALJSON.JSON).from(OPTIMALJSON).
                    where(OPTIMALJSON.COINLABEL.eq(coin)).fetch();

            for (int i = 0; i < result.size(); i++) {
                ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
                if (isJson) {
                    print(resultOfCalculation.toJSON());
                } else {
                    print(resultOfCalculation.toString());
                }
            }
        }
        return printString;
    }

    public String getDataFromTime(Timestamp timeStamp,boolean isJson) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record1<String>> result = dslContext.select(OPTIMALJSON.JSON).from(OPTIMALJSON).
                where(OPTIMALJSON.TIME.greaterThan(timeStamp)).fetch();
        for (int i = 0; i < result.size(); i++) {
            ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
            if (isJson) {
                print(resultOfCalculation.toJSON());
            } else {
                print(resultOfCalculation.toString());
            }
        }
        return printString;
    }

    public String getDataFromTimeAndCoin(Timestamp timeStamp, String[] coins ,boolean isJson) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        for (String coin : coins) {
            coin = coin.toUpperCase();
            Result<Record1<String>> result = dslContext.select(OPTIMALJSON.JSON).from(OPTIMALJSON).
                    where(OPTIMALJSON.TIME.greaterThan(timeStamp)).and(OPTIMALJSON.COINLABEL.eq(coin)).fetch();
            for (int i = 0; i < result.size(); i++) {
                ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
                if (isJson) {
                    print(resultOfCalculation.toJSON());
                } else {
                    print(resultOfCalculation.toString());
                }
            }
        }
        return printString;
    }



    private void print(String text) {
        printString += text + "\n";
    }
}
