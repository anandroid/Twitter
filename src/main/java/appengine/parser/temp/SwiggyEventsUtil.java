package appengine.parser.temp;

import appengine.parser.mysqlmodels.tables.Swiggyevents;
import appengine.parser.utils.DataBaseConnector;
import com.google.gson.Gson;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwiggyEventsUtil {

    public void addAll(SwiggyEventList swiggyEventList) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();

        if (swiggyEventList.events == null || swiggyEventList.events.size() == 0) {
            return;
        }
        for (SwiggyEvent swiggyEvent : swiggyEventList.events) {
            dslContext.insertInto(Swiggyevents.SWIGGYEVENTS,
                    Swiggyevents.SWIGGYEVENTS.JSON).values(
                    swiggyEvent.toJSON())
                    .execute();
        }
    }

    public void add(SwiggyEvent swiggyEvent) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();

        dslContext.insertInto(Swiggyevents.SWIGGYEVENTS,
                Swiggyevents.SWIGGYEVENTS.JSON).values(
                swiggyEvent.toJSON())
                .execute();
    }

    public void deleteAll() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.deleteFrom(Swiggyevents.SWIGGYEVENTS).execute();
    }


    public JSONObject getEvents(int limit) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<Integer, String>> result = dslContext.select(Swiggyevents.SWIGGYEVENTS.ID,
                Swiggyevents.SWIGGYEVENTS.JSON).from(Swiggyevents.SWIGGYEVENTS).limit(limit).fetch();

        SwiggyEventList swiggyEventList = new SwiggyEventList();
        swiggyEventList.events = new ArrayList<>();

        for (Record2<Integer, String> record2 : result) {
            swiggyEventList.events.add(new Gson().fromJson(record2.value2(), SwiggyEvent.class));
        }


        return new JSONObject(swiggyEventList.toJSON());
}
}
