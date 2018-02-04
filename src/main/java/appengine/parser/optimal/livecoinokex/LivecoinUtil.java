package appengine.parser.optimal.livecoinokex;

import appengine.parser.optimal.livecoinokex.utils.SymbolUtil;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.livecoinokex.utils.livecoin.FetchFromRequest;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class LivecoinUtil {

    FetchFromRequest requestMaker;

    public LivecoinUtil() {
        requestMaker = new FetchFromRequest();
    }

    public ArrayList<TradeDepth> getOrderBookAll() {

        ArrayList<TradeDepth> tradeDepthsList = new ArrayList<>();

        if (requestMaker == null) {
            requestMaker = new FetchFromRequest();
        }

        String jsonString = requestMaker.getAllOrderBook();

        JSONObject jsonObject = new JSONObject(jsonString);

        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains("/BTC")) {
                JSONObject coinJsonObject = jsonObject.getJSONObject(key);

                TradeDepth tradeDepth = TradeDepth.fromJSONString(coinJsonObject.toString(), new SymbolUtil().getCoin(key));
                tradeDepthsList.add(tradeDepth);
            }
        }

        return tradeDepthsList;
    }

    public TradeDepth getOrderBook(String symbol) {

        TradeDepth tradeDepth = null;


        return tradeDepth;
    }


}
