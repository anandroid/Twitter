package appengine.parser.optimal.livecoinokex;

import appengine.parser.optimal.livecoinokex.utils.SymbolUtil;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.livecoinokex.utils.okex.StockRestApi;
import org.apache.http.HttpException;

import java.io.IOException;

public class OkexUtil {

    String url_prex = "https://www.okex.com/";
    public static final String apiKey = "66e9ee07-3ac7-4c2f-932f-8324356b2c44";
    public static final String secretKey = "03C4524D6BD1C6041C0EC1E2A0E62111";

    StockRestApi stockRestApi;

    public OkexUtil() {
        stockRestApi = new StockRestApi(url_prex, apiKey, secretKey);
    }

    public TradeDepth getOrderBook(String symbol) {

        TradeDepth tradeDepth = null;

        if (stockRestApi == null) {
            stockRestApi = new StockRestApi(url_prex, apiKey, secretKey);
        }

        try {
            tradeDepth = TradeDepth.fromJSONString(stockRestApi.depth(symbol), new SymbolUtil().getCoin(symbol),true);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tradeDepth;
    }


}
