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


public class FollowUsers {

    public String follow(String pageName, String userCurrentName) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<Integer> followedTillRecord = dslContext.select(INSTAGRAMFOLLOWERSUPDATE.FOLLOWED_TILL).from(INSTAGRAMFOLLOWERSUPDATE)
                .where(INSTAGRAMFOLLOWERSUPDATE.FROM_USER_NAME.eq(userCurrentName).and(INSTAGRAMFOLLOWERSUPDATE.PAGENAME.
                        eq(pageName))).fetchOne();


        int followedTill = 0;

        if (followedTillRecord != null) {
            followedTill = followedTillRecord.value1();
        }


        makeRequest(pageName, userCurrentName, followedTill);

        return "";
    }

    private synchronized void makeRequest(String pageName, String userCurrentName, int followedTill) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record3<Integer, String, String>> followersResult = dslContext.select(
                INSTAGRAMFOLLOWERS.ID, INSTAGRAMFOLLOWERS.USER_ID, INSTAGRAMFOLLOWERS.USER_NAME).from(INSTAGRAMFOLLOWERS).
                where(INSTAGRAMFOLLOWERS.ID.greaterThan(followedTill).and(INSTAGRAMFOLLOWERS.PAGENAME.eq(pageName))).limit(5).fetch();


        List<UserNameAndID> followersList = new ArrayList<>();

        for (int i = 0; i < followersResult.size(); i++) {
            followersList.add(new UserNameAndID(followersResult.get(i).value1(), followersResult.get(i).value2(), followersResult.get(i).value3()));
        }


        for (int i = 0; i < followersList.size(); i++) {
            try {
                boolean isSuccessFul = fireRequest(followersList.get(i));
                if (isSuccessFul) {
                    updateInDB(pageName, userCurrentName, followersList.get(i));
                    Thread.sleep(12000);
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void updateInDB(String pageName, String currentUserName, UserNameAndID userNameAndID) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMFOLLOWERSUPDATE, INSTAGRAMFOLLOWERSUPDATE.PAGENAME, INSTAGRAMFOLLOWERSUPDATE.FROM_USER_NAME,
                INSTAGRAMFOLLOWERSUPDATE.FOLLOWED_TILL).values(pageName, currentUserName, userNameAndID.id).onDuplicateKeyUpdate()
                .set(INSTAGRAMFOLLOWERSUPDATE.FOLLOWED_TILL, userNameAndID.id).execute();
    }

    private boolean fireRequest(UserNameAndID userNameAndID) {


        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/web/friendships/" + userNameAndID.userID + "/follow/")
                    .post(body)
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("x-instagram-ajax", "eb2a729248ed")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("x-devtools-emulate-network-conditions-client-id", "49BC859311D65BE5D4F8F545036E450D")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36")
                    .addHeader("x-csrftoken", "9JB6NN9Ua18QAoydupZUznUB95wILs1f")
                    .addHeader("referer", "https://www.instagram.com/i_am_sugith_raj/")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", "datr=AteyWnkpwiv6zyRuIn1M73Ce; shbid=13765; csrftoken=9JB6NN9Ua18QAoydupZUznUB95wILs1f; ds_user_id=5704301242; mid=Wq1HFgAEAAHtzAOJLQNZTFj6v5wy; mcd=3; fbm_124024574287414=base_domain=.instagram.com; rur=PRN; sessionid=IGSCdee73c72433e3e5b1f885cb38d38ec78b703d2f23b40abadcb89aa340124eccc%3AD7o40FWA3aCcskz3umWcVNxfQp7UfBLA%3A%7B%22_auth_user_id%22%3A5704301242%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_platform%22%3A4%2C%22_token_ver%22%3A2%2C%22_token%22%3A%225704301242%3AhyhUAUZx9U3Zwowzqjp7aQV1p6ULfrZd%3A80b32fbdc17146155189786bed857b2c3a8f593d389c4eba4fbccfdd6102b636%22%2C%22last_refreshed%22%3A1527838936.9332792759%7D; fbsr_124024574287414=nabVGjJbGGHpNguWHM_NSZnPrJ9_OnSEAzFxbv0kegI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUJNYnk4OUYtazMwUnJrY0Q5cGxQaTdKSG1URXh3czJ3M0VzRVhUbnpYaDB1c001czdmYl9kVmc2eXNZd0ZRSlR2bUVhdFJhbFVPdDY0MXlQMGp1SlJ4MEVIa1hWT1otbkhEYjFwVWowRWtwbUtvUThpUG5zcGhXZzhrMUYwZVpmQUQtLTNqS0NJY3F1LVYzdEc2bkVfWGVUZXlhOGFwU2N4U0MwcndERm8wOXN2RUVZcUIzeVljVGhzMWQ5ODVJWm4xLXNMQURsZVVDeHF1dlZid1dBTlU4U3FwdTB1aGpDVzluRDlXNFB3YVY4ZGtHUUhTdld1cWFNVDdLQmxBbGtsR0Z4QldCLTd0QUFrOXJGcURWSTYzNThSdkRVajdsSWJTRVQ2YlFoTmtQSnE4bGFVUXZjQmxack9rS2pXQzlVZlFEUm02ajAtaUo0UUN4OW1ldzMyYyIsImlzc3VlZF9hdCI6MTUyNzg4MjQ3MSwidXNlcl9pZCI6IjEwMDAwNDAzMDAwNjE2MCJ9; urlgen=\"{\"time\": 1527873349}:1fOq3p:vVirgoQ--i0kPW2l8l70As9z63U\"")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "a8da0161-10a0-84a9-1cca-0a9dfb712fd4")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                return true;
            }


        } catch (Exception e) {
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
