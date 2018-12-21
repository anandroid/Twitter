package appengine.parser.twitter;

import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.TWITTERFOLLOWERS;

public class FollowTwitterUsers {

    private static twitter4j.Twitter mTwitterInstance;

    public void makeLimitRequest() {

        if (mTwitterInstance == null) {
            mTwitterInstance = Twitter.getInstance();
        }

        String followerOf = "OneDevloperArmy";


        List<String> usersList = getNotFollowedUsers(followerOf);

        for (String userId : usersList) {
            try {
                mTwitterInstance.createFriendship(userId);
                System.out.println("follow request to : " + userId);
                updateRequestSent(userId);
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


    }

    public void updateRequestSent(String userId) {
        try {
            DSLContext dslContext = DataBaseConnector.getDSLContext();
            dslContext.update(TWITTERFOLLOWERS)
                    .set(TWITTERFOLLOWERS.IS_FOLLOW_REQUEST_SENT, (byte) 1)
                    .where(TWITTERFOLLOWERS.USER_ID.eq(userId))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> getNotFollowedUsers(String followerOf) {

        List<String> usersList = new ArrayList<>();

        try {

            DSLContext dslContext = DataBaseConnector.getDSLContext();
            Result<Record1<String>> userIdResults = dslContext.select(TWITTERFOLLOWERS.USER_ID).from(TWITTERFOLLOWERS).
                    where(TWITTERFOLLOWERS.IS_FOLLOW_REQUEST_SENT.eq((byte) 0))
                    .limit(20).fetch();

            for (Record1<String> userId : userIdResults) {
                usersList.add(userId.value1());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersList;
    }
}
