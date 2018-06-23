package appengine.parser.instagram;


import appengine.parser.utils.DataBaseConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.json.JSONArray;
import org.json.JSONObject;

import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERS;
import static appengine.parser.mysqlmodels.Tables.INSTAGRAMLASTFETCH;

public class FetchLikers {

    public String fetch() {

        String pagename = "dog.lovers-likers";
        String usercurrentname = "anand4joy";

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<String> lastcursorRecord = dslContext.select(INSTAGRAMLASTFETCH.LASTCURSOR).from(INSTAGRAMLASTFETCH)
                .where(INSTAGRAMLASTFETCH.PAGENAME.eq(pagename)).fetchOne();

        String lastCursor;
        if (lastcursorRecord != null) {
            lastCursor = lastcursorRecord.value1();
        } else {
            lastCursor = "AQAhmrUNWb1fp1Jt0K9cqh41I5DWMB82S_DVxio8VirSeGR6t0DcIk24jc84z95lvJhNCppl3bj70c7Ht00_YCJR1j61XvzIvqfDj6jAWAvSZw";
        }

        makeRequest(pagename, usercurrentname, lastCursor);

        return "";
    }

    private void processResponse(String pageName, String currentUserName, String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject edgeFollowedByJSONObject = jsonObject.getJSONObject("data").getJSONObject("shortcode_media").getJSONObject("edge_liked_by");;

            String endCursor = edgeFollowedByJSONObject.getJSONObject("page_info").getString("end_cursor");

            JSONArray jsonArray = edgeFollowedByJSONObject.getJSONArray("edges");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject userObject = jsonArray.getJSONObject(i).getJSONObject("node");
                String user_id = userObject.getString("id");
                String user_name = userObject.getString("username");
                boolean is_verified = userObject.getBoolean("is_verified");
                boolean followed_by_viewer = userObject.getBoolean("followed_by_viewer");
                boolean requested_by_viewer = userObject.getBoolean("requested_by_viewer");

                insertUser(pageName, user_id, user_name, is_verified, followed_by_viewer, requested_by_viewer, currentUserName);
            }

            updateCursor(pageName, endCursor);

            makeRequest(pageName, currentUserName, endCursor);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private byte boolToByte(boolean vIn) {
        byte vOut = (byte) (vIn ? 1 : 0);
        return vOut;
    }

    private void insertUser(String pageName, String user_id, String user_name, boolean is_verified,
                            boolean followed_by_viewer, boolean requested_by_viewer, String currentUserName) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMFOLLOWERS, INSTAGRAMFOLLOWERS.PAGENAME, INSTAGRAMFOLLOWERS.USER_ID,
                INSTAGRAMFOLLOWERS.USER_NAME, INSTAGRAMFOLLOWERS.IS_VERIFIED, INSTAGRAMFOLLOWERS.FOLLOWED_BY_VIEWER,
                INSTAGRAMFOLLOWERS.REQUESTED_BY_VIEWER, INSTAGRAMFOLLOWERS.FROM_USER_NAME)
                .values(pageName, user_id, user_name, boolToByte(is_verified), boolToByte(followed_by_viewer), boolToByte(requested_by_viewer),
                        currentUserName)
                .execute();

    }

    private void updateCursor(String pageName, String lastCursor) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMLASTFETCH, INSTAGRAMLASTFETCH.PAGENAME, INSTAGRAMLASTFETCH.LASTCURSOR)
                .values(pageName, lastCursor).onDuplicateKeyUpdate()
                .set(INSTAGRAMLASTFETCH.LASTCURSOR, lastCursor)
                .execute();
    }

    private void makeRequest(String pageName, String currentUserName, String lastCursor) {

        try {

            OkHttpClient client = new OkHttpClient();



            Request request = new Request.Builder()
                    .url("https://www.instagram.com/graphql/query/?query_hash=1cb6ec562846122743b61e492c85999f&variables=%7B%22shortcode%22%3A%22BgxPCyMF3pe%22%2C%22first%22%3A12%2C%22after%22%3A%22"+lastCursor+"%22%7D")
                    .get()
                    .addHeader("accept", "*/*")
                    .addHeader("x-devtools-emulate-network-conditions-client-id", "CB12C3C074B580996BE6784CBDF00D5C")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-instagram-gis", "ac447b98a5dbbed7bdcd1dfeb81c9394")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36")
                    .addHeader("referer", "https://www.instagram.com/dogs.lovers/followers/")
                    //.addHeader("accept-encoding", "gzip, deflate, br")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", "datr=AteyWnkpwiv6zyRuIn1M73Ce; shbid=13765; csrftoken=9JB6NN9Ua18QAoydupZUznUB95wILs1f; ds_user_id=5704301242; mid=Wq1HFgAEAAHtzAOJLQNZTFj6v5wy; mcd=3; fbm_124024574287414=base_domain=.instagram.com; sessionid=IGSC2956867d896383c9517f7f1ff721edc1d55e54b820e80e2591307a4df4b071a6%3ARP7PjUhKkVOEs4lLwLbNVEFQWvLNJcRB%3A%7B%22_auth_user_id%22%3A5704301242%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_platform%22%3A4%2C%22_token_ver%22%3A2%2C%22_token%22%3A%225704301242%3AhyhUAUZx9U3Zwowzqjp7aQV1p6ULfrZd%3A80b32fbdc17146155189786bed857b2c3a8f593d389c4eba4fbccfdd6102b636%22%2C%22last_refreshed%22%3A1527752402.2774214745%7D; rur=PRN; shbts=1527794302.0350387; fbsr_124024574287414=HW-4sS71LHip4euRAaCaqCBfqykFNOUHmQ0g6mYCF5c.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUFfRVVVQ3gycGFVenBscXprTm1ydmdiVjBNOUtSQVRLbTBUekZha0J2ZWNvYk1oN0JSQWt3UXppalZfSnpvQ3hMMng0Qi0zODB6VXIxMFZOYXJHcEo5RXVGUDBYTFl5dnZnaTNIbWpqZzdXeUJUT3ZpU0l4X0l2NWM5ME4ySEdPMElZWjVuLTNCTzdRS0tZbnBIVXoyRnNZRE5wejJxVWtwUHBWTUxNQlpCVUtVT3lPeGhydWxfck9GOFp1clB2NlVBZDdsRXEtWkZoaEhMU2VIRWtwQk1KSTVqdG1ETlNTX1dWQ3RsTTRrRkpPVFVtYjB1dUdoR09qVnJsc2lXeEtwM3dQbkpxQ1VCcWNtQ3dFOTBYWV9CejQzZnVNRmZlTUtGNkNMTC15cTBEcmZCcWhub0tIdmc0bTJ2SENfVmY2YnBGeE1JTmdwRzZVZEgzQUJZQVVuMCIsImlzc3VlZF9hdCI6MTUyNzc5Mzk3MCwidXNlcl9pZCI6IjEwMDAwNDAzMDAwNjE2MCJ9; urlgen=\"{\"time\": 1527752402\054 \"123.201.249.115\": 18207}:1fOT0i:a-pIcVVis8ZdVxXZytGM0fTf52o\"; \"=; datr=AteyWnkpwiv6zyRuIn1M73Ce; shbid=13765; csrftoken=9JB6NN9Ua18QAoydupZUznUB95wILs1f; ds_user_id=5704301242; mid=Wq1HFgAEAAHtzAOJLQNZTFj6v5wy; mcd=3; fbm_124024574287414=base_domain=.instagram.com; sessionid=IGSC2956867d896383c9517f7f1ff721edc1d55e54b820e80e2591307a4df4b071a6%3ARP7PjUhKkVOEs4lLwLbNVEFQWvLNJcRB%3A%7B%22_auth_user_id%22%3A5704301242%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_platform%22%3A4%2C%22_token_ver%22%3A2%2C%22_token%22%3A%225704301242%3AhyhUAUZx9U3Zwowzqjp7aQV1p6ULfrZd%3A80b32fbdc17146155189786bed857b2c3a8f593d389c4eba4fbccfdd6102b636%22%2C%22last_refreshed%22%3A1527752402.2774214745%7D; rur=PRN; shbts=1527800533.983759; urlgen=\"{\"time\": 1527752402\054 \"123.201.249.115\": 18207\054 \"175.100.147.13\": 18207}:1fOUi5:9DR9KwqMayXYe9txYt_iiHqnBew\"; fbsr_124024574287414=0LXbCwuiJzwIqHT5YNG3SefwRaRaCa7Gx279MvGSFdI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUI5OHhwZkotUEluR2hBNkUxM3h6UEMtVllmdHRldzQ5dWhzb0Q5cUZBck5meThlVS1KOE9rTHRuMkxyU0FGQzJKVWUxZlljMDdmNW1pVW1HWkRyR2gwRGVQaHVqQk93RElyWnZBTzdxZm1QZnE1ODJ5TXB6czEwbk5temFaZVBsUEhRMm1rNXc0QVU5SkVSNTF5WWxNSWMteGtJNHg3ZE5LWnZhS1NrZkZ1OURhY203bHVxb1ZBX0ZPRDYwcnFUSWVuU0RKdW9NeFBwTFVTeUxUQjJvMFVrWXNIbUJVOGFjMnMzanRhN0hMUTlIT0xLZjUtTjFnMFhmbmtuNHJEMzlzbDBmdDR4Q0dfMGdDNVVMTDZNXzIwdFFKd25DVnhHQ3ZnXzZWWE1RNE5KUlQ1d1ZnOTNhVHU1SXFmdi1tNFctSjRWY09MR0g3cGJhbHBaUHRfSmtSayIsImlzc3VlZF9hdCI6MTUyNzgwMDcwOCwidXNlcl9pZCI6IjEwMDAwNDAzMDAwNjE2MCJ9")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "6be20f72-d30c-674e-9d4a-27d10e5b2cad")
                    .build();

            Response response = client.newCall(request).execute();
            processResponse(pageName, currentUserName, response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
