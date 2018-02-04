package appengine.parser.optimal.livecoinokex.utils.livecoin;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class FetchFromRequest {


    public final java.lang.String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    public final java.lang.String UNICODE_CODE = "UTF-8";

    // your API key
    public final String apiKey = "m7k83ECx7u439MrYdg37QvjUDUV5mAn2";
    // your secret key
    public final String secKey = "JtShQF8Pes5uMtjef5B2VCrqNzFE5M4G";

    public String makePostRequest(Map<String, String> postData, String url) {

        try {
            String queryString = buildQueryString(postData);
            String signature = createSignature(queryString, secKey);

            URL queryUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) queryUrl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Api-Key", apiKey);
            connection.setRequestProperty("Sign", signature);
            connection.getOutputStream().write(queryString.getBytes());

            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }
            return sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String getAllOrderBook() {
        String result = "";
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.livecoin.net/exchange/all/order_book")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "11170834-be7b-9cdf-196c-662e31343f89")
                    .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    private String buildQueryString(Map<String, String> args) {
        StringBuilder result = new StringBuilder();
        for (String hashKey : args.keySet()) {
            if (result.length() > 0) result.append('&');
            try {
                result.append(URLEncoder.encode(hashKey, "UTF-8"))
                        .append("=").append(URLEncoder.encode(args.get(hashKey), "UTF-8"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    private String createSignature(String paramData, String plainSecretKey) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(plainSecretKey.getBytes(UNICODE_CODE), HMAC_SHA256_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(paramData.getBytes(UNICODE_CODE));
            return byteArrayToHexString(hmacData).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String byteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}