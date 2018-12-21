package appengine.parser.twitter;

import appengine.parser.mysqlmodels.Tables;
import appengine.parser.utils.DataBaseConnector;
import appengine.parser.utils.OtherUtils;
import org.jooq.DSLContext;
import org.jooq.Record1;
import twitter4j.PagableResponseList;
import twitter4j.User;

import static appengine.parser.mysqlmodels.Tables.TWITTERFOLLOWERS;

public class GetFollowers {


    private static twitter4j.Twitter mTwitterInstance;


    public void getFollowers() {

        if (mTwitterInstance == null) {
            mTwitterInstance = Twitter.getInstance();
        }

        String followerOf = "xdadevelopers";

        long cursor = getCursor(followerOf);
        PagableResponseList<User> usersList;

        try {
            do {
                System.out.println(mTwitterInstance.getScreenName());
                usersList = mTwitterInstance.getFollowersList(followerOf, cursor);
                for (User user : usersList) {
                    if (!user.isFollowRequestSent()) {
                        //mTwitterInstance.createFriendship(user.getId());
                        System.out.println("Follow request sent to " + user.getName());
                        insertFollowers(user, followerOf, cursor);
                    }
                }
                Thread.sleep(10000);
                cursor = usersList.getNextCursor();
            } while (usersList.hasNext());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public long getCursor(String followerOf) {
        try {
            DSLContext dslContext = DataBaseConnector.getDSLContext();
            Record1<String> cursorId = dslContext.select(Tables.TWITTERFOLLOWERS.CURSOR).from(Tables.TWITTERFOLLOWERS)
                    .where(TWITTERFOLLOWERS.FOLLOWER_OF.eq(followerOf))
                    .orderBy(TWITTERFOLLOWERS.ID.desc())
                    .limit(1)
                    .fetchOne();
            return Long.parseLong(cursorId.value1());
        } catch (Exception e) {
            return -1;
        }

    }

    public void insertFollowers(User user, String followerOf, long cursor) {
        try {
            String userJson = user.getName();
            System.out.println(userJson);
            DSLContext dslContext = DataBaseConnector.getDSLContext();
            dslContext.insertInto(TWITTERFOLLOWERS, TWITTERFOLLOWERS.USER_ID, TWITTERFOLLOWERS.USER_JSON,
                    TWITTERFOLLOWERS.FOLLOWER_OF, TWITTERFOLLOWERS.IS_FOLLOW_REQUEST_SENT, TWITTERFOLLOWERS.CURSOR)
                    .values(String.valueOf(user.getId()), userJson, followerOf, OtherUtils.boolToByte(user.isFollowRequestSent()),
                            String.valueOf(cursor)).
                    onDuplicateKeyUpdate()
                    .set(TWITTERFOLLOWERS.USER_JSON, userJson)
                    .set(TWITTERFOLLOWERS.CURSOR, String.valueOf(cursor))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
