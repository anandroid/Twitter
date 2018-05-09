package appengine.parser.optimal.coinsstatus;

import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.CoinsStatusUtil;
import appengine.parser.optimal.objects.Market;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LiquiCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.liqui.io/api/3/info")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject pairsObject = jsonObject.getJSONObject("pairs");

            Set<String> keys = pairsObject.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.contains("_btc")) {
                    JSONObject coinJSONObject = pairsObject.getJSONObject(key);


                    String name = "";
                    String label = (key.split("_btc")[0]).toUpperCase();
                    boolean isWalletActive = true;
                    boolean isListingActive = true;


                    if (coinJSONObject.getInt("hidden") == 1) {
                        isListingActive = false;
                    }

                    CoinStatus coinStatus = toCoinStatus(name, label, isWalletActive, isListingActive);
                    coinInfoList.add(coinStatus);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<CoinStatus> getCoinsStatusList() {
        if (coinInfoList == null || coinInfoList.size() == 0) {
            fetch();
        }
            return coinInfoList;
    }

    @Override
    public CoinStatus toCoinStatus(Object... rawCoinInfo) {
        String coinName = (String) rawCoinInfo[0];
        String label = (String) rawCoinInfo[1];
        boolean isWalletActive = (boolean) rawCoinInfo[2];
        boolean isListingActive = (boolean) rawCoinInfo[3];

        CoinStatus coinInfo = new CoinStatus(Market.LIQUI, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
