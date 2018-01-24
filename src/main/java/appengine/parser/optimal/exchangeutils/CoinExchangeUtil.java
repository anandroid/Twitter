package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anand.kurapati on 15/01/18.
 */
public class CoinExchangeUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    Map<String, String> codesLabelMap = new HashMap<>();

    @Override
    public void fetch() {

        getMarketsIds();


        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinexchange.io/api/v1/getmarketsummaries")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "377f33d5-503e-37f9-c229-b3694224b279")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < resultArray.length(); i++) {

                try {

                    JSONObject resultObject = resultArray.getJSONObject(i);

                    String marketId = resultObject.getString("MarketID");
                    String coin = codesLabelMap.get(marketId);
                    String volume = resultObject.getString("Volume");
                    String ourSellPrice = resultObject.getString("BidPrice");
                    String ourBuyPrice = resultObject.getString("AskPrice");
                    CoinMarket coinMarket = toCoinMarket(coin, ourSellPrice, ourBuyPrice, volume);
                    coinMarketList.add(coinMarket);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {

        }


    }

    private void getMarketsIds() {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinexchange.io/api/v1/getmarkets")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8eb84f4d-7ddb-4ce5-cc78-4022ee2eb303")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject resultObject = resultArray.getJSONObject(i);
                if (resultObject.getString("BaseCurrencyCode").equalsIgnoreCase("BTC")) {

                    String marketId = resultObject.getString("MarketID");
                    String marketAssetCode = resultObject.getString("MarketAssetCode");
                    codesLabelMap.put(marketId, marketAssetCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public List<CoinMarket> getCoinList() {
        if (coinMarketList == null || coinMarketList.size() == 0) {
            fetch();
        }
        return coinMarketList;
    }

    @Override
    public CoinMarket toCoinMarket(Object... rawCoinMarket) {
        String coin = (String) rawCoinMarket[0];
        String ourSellPrice = (String) rawCoinMarket[1];
        String ourBuyPrice = (String) rawCoinMarket[2];
        String volume = (String) rawCoinMarket[3];
        CoinMarket coinMarket = new CoinMarket(Market.COINEXCHANGE, coin, ourBuyPrice, ourSellPrice, volume);
        return coinMarket;
    }
}
