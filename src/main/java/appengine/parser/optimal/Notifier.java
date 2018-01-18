package appengine.parser.optimal;

import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;

import java.sql.Timestamp;

import static appengine.parser.mysqlmodels.Tables.OPTIMALJSON;

/**
 * Created by anand.kurapati on 17/01/18.
 */
public class Notifier {


    public void fetchAndNotify(){

        long twoMinsBack = System.currentTimeMillis()-60*1000*2;

        Timestamp timestamp = new Timestamp(twoMinsBack);

    }


    private void insertResultInDB(ResultOfCalculation resultOfCalculation) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALJSON, OPTIMALJSON.COINLABEL, OPTIMALJSON.JSON)
                .values(resultOfCalculation.getCoin(), resultOfCalculation.toJSON()).execute();
    }
}
