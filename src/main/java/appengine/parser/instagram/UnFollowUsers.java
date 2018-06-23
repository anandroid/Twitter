package appengine.parser.instagram;

import appengine.parser.utils.DataBaseConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERS;
import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERSUPDATE;
import static appengine.parser.mysqlmodels.Tables.INSTAGRAMUNFOLLOWERSUPDATE;

public class UnFollowUsers {

    public String follow(String pageName, String userCurrentName) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<Integer> unFollowedTillRecord = dslContext.select(INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL).from(INSTAGRAMUNFOLLOWERSUPDATE)
                .where(INSTAGRAMUNFOLLOWERSUPDATE.FROM_USER_NAME.eq(userCurrentName).and(INSTAGRAMUNFOLLOWERSUPDATE.PAGENAME.
                        eq(pageName))).fetchOne();


        int unFollowedTill = 0;

        if (unFollowedTillRecord != null) {
            unFollowedTill = unFollowedTillRecord.value1();
        }


        makeRequest(pageName, userCurrentName, unFollowedTill);

        return "";
    }

    private synchronized void makeRequest(String pageName, String userCurrentName, int unFollowedTill) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record3<Integer, String, String>> followersResult = dslContext.select(
                INSTAGRAMFOLLOWERS.ID, INSTAGRAMFOLLOWERS.USER_ID, INSTAGRAMFOLLOWERS.USER_NAME).from(INSTAGRAMFOLLOWERS).
                where(INSTAGRAMFOLLOWERS.ID.greaterThan(unFollowedTill).and(INSTAGRAMFOLLOWERS.PAGENAME.eq(pageName))).limit(5).fetch();


        List<UserNameAndID> unFollowersList = new ArrayList<>();

        for (int i = 0; i < followersResult.size(); i++) {
            unFollowersList.add(new UserNameAndID(followersResult.get(i).value1(), followersResult.get(i).value2(), followersResult.get(i).value3()));
        }


        for (int i = 0; i < unFollowersList.size(); i++) {
            try {
                boolean isSuccessFul = fireRequest(unFollowersList.get(i));
                if (isSuccessFul) {
                    updateInDB(pageName, userCurrentName, unFollowersList.get(i));
                    Thread.sleep(12000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void updateInDB(String pageName, String currentUserName, UserNameAndID userNameAndID) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMUNFOLLOWERSUPDATE, INSTAGRAMUNFOLLOWERSUPDATE.PAGENAME, INSTAGRAMUNFOLLOWERSUPDATE.FROM_USER_NAME,
                INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL).values(pageName, currentUserName, userNameAndID.id).onDuplicateKeyUpdate()
                .set(INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL, userNameAndID.id).execute();
    }



    private boolean fireRequest(UserNameAndID userNameAndID){

        try {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/web/friendships/"+userNameAndID.userID+"/unfollow/")
                    .post(body)
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("accept-encoding", "gzip, deflate, br")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36")
                    .addHeader("cookie", "datr=AteyWnkpwiv6zyRuIn1M73Ce; shbid=13765; csrftoken=9JB6NN9Ua18QAoydupZUznUB95wILs1f; ds_user_id=5704301242; mid=Wq1HFgAEAAHtzAOJLQNZTFj6v5wy; mcd=3; fbm_124024574287414=base_domain=.instagram.com; sessionid=IGSC04e4466d3d0f10a57c3bde783bde57d5e3da8296b0fe1a76f90473cc2d2e7843%3A7jtioPF2q370WYo4sM6FoQ1KVoiAofYH%3A%7B%22_auth_user_id%22%3A5704301242%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_platform%22%3A4%2C%22_token_ver%22%3A2%2C%22_token%22%3A%225704301242%3AhyhUAUZx9U3Zwowzqjp7aQV1p6ULfrZd%3A80b32fbdc17146155189786bed857b2c3a8f593d389c4eba4fbccfdd6102b636%22%2C%22last_refreshed%22%3A1528733278.6057548523%7D; fbsr_124024574287414=tJZekNECD0nfgpadC6zGzWttzyepS6PZ4U628fc1mZ8.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUJjV2JybUx3RVRaa0l1blVmX2RBbVZ0SnhwM2FIRFFBZGhxLWx4S256ZkdJcElFQWhfVmNlTDhiYzRXdC1PZlBQSE5kcndNTlExaWIwcGw2RTg3RE9QbVVzaWlkTGhraHAwUHNESExGbmljT2Y0ekJQcTlSZndLUGtDcGstNTRvU1RHR0RtTUktSkM4RHhVWFBkd05Lb0pFRGM4TEhhY2hCWS1DQWdqQmVNT21ETkl2MkFpLWVqMm5uOW9KYXlfNVdQbmhkSmtVckFiSnY3OTFYQk5UTU5RbV85WVRZa0tjS3VxZkhsdE9MWTBYQ1VBQU84THpodmNOeV9EXzQ0ZnBlSkQ1U2t1SGFMS3Q0Y2oxaFZ6cnBhX181c0pQY0V3b3VhNDV0Q19ob1dWa2QyeHZwS2lOOEVjTzNucDhUMmlTbksyanVCQUZwYVl3U3gwSm82cS1faiIsImlzc3VlZF9hdCI6MTUyODc0MDI0MSwidXNlcl9pZCI6IjEwMDAwNDAzMDAwNjE2MCJ9; shbts=1528740256.0252903; rur=FTW; urlgen=\"{\"time\": 1528733278}:1fSRAu:Q1pa6uS8i2jYqepGGRJlmuDlQTo\"")
                    .addHeader("x-csrftoken", "9JB6NN9Ua18QAoydupZUznUB95wILs1f")
                    .addHeader("x-instagram-ajax", "6aea51e23819")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://www.instagram.com/eloisa_j.e/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "df0854e2-6098-b6ba-fe74-4b926f861d6a")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                return true;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    class UserNameAndID {
        int id;
        String userName;
        String userID;

        UserNameAndID(int id, String userID, String userName) {
            this.id = id;
            this.userID = userID;
            this.userName = userName;
        }

    }
}
