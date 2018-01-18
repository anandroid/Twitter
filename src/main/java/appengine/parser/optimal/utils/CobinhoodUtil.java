package appengine.parser.optimal.utils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 09/01/18.
 */
public class CobinhoodUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();


    @Override
    public void fetch() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.coinhills.com/api/internal/market_read.php?pri_code=&sec_code=&src_code=cobinhood&order=sec_type-" +
                            "desc%2Csec_code-asc%2Cvolume_btc-desc&page=0&row=250&_=" + System.currentTimeMillis())
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "40f5e258-5554-e542-f356-4a89a1c8c455")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray coinsJSONArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < coinsJSONArray.length(); i++) {

                try {

                    JSONObject coinObject = coinsJSONArray.getJSONObject(i);
                    if (!coinObject.getString("sec_code").equalsIgnoreCase("BTC")) {
                        continue;
                    }
                    CoinMarket coinMarket = toCoinMarket(coinObject.getString("pri_code"),
                            coinObject.getString("price"), coinObject.getString("volume"));
                    coinMarketList.add(coinMarket);
                } catch (Exception e) {
                    e.printStackTrace();
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
        String coinname = (String) rawCoinMarket[0];
        String last = (String) rawCoinMarket[1];
        String volume = (String) rawCoinMarket[2];

        CoinMarket coinMarket = new CoinMarket(Market.COBINHOOD, coinname, last, last, volume);
        return coinMarket;
    }


}
