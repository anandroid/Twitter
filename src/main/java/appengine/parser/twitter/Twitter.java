package appengine.parser.twitter;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by anand.kurapati on 27/06/17.
 */
public class Twitter {

    private static int DEFAULT_COUNT = 20;

    private static twitter4j.Twitter mTwitterInstance;

    public static twitter4j.Twitter getInstance() {
        if (mTwitterInstance == null) {

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("roAblILFxR4M6zEwM6kW7Q69c")
                    .setOAuthConsumerSecret("GiZimcBu2KGxD72V67aST31REeHwenKFP6Thg6V1b34IncKFJD")
                    .setOAuthAccessToken("1042608734-I7rtUyQAqTXeH5LIw3heB0ygdg3zzq84AKR9A2Q")
                    .setOAuthAccessTokenSecret("KAABt6whlc019jV6c772JIDPuGCl7IIWcyjWNLsXQixB7");

            TwitterFactory tf = new TwitterFactory(cb.build());
            mTwitterInstance = tf.getInstance();
        }
        return mTwitterInstance;
    }

    public static QueryResult getTweets(String query) {
        return getTweets(query, DEFAULT_COUNT);
    }

    public static QueryResult getTweets(String search_string, int count) {
        try {
            twitter4j.Twitter twitter = getInstance();
            Query query = new Query(search_string);
            query.count(count);
            query.setGeoCode(new GeoLocation(21.94, 76.99), 2000, Query.Unit.km);
            QueryResult queryResult = twitter.search(query);
            return queryResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
