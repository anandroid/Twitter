package appengine.parser.twitter;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterFactory;

/**
 * Created by anand.kurapati on 27/06/17.
 */
public class Twitter {

    private static int DEFAULT_COUNT=20;

    private static twitter4j.Twitter mTwitterInstance;

    public static twitter4j.Twitter getInstance(){
        if(mTwitterInstance==null){
            mTwitterInstance = TwitterFactory.getSingleton();
        }
        return mTwitterInstance;
    }

    public static QueryResult getTweets(String query){
        return getTweets(query,DEFAULT_COUNT);
    }

    public static QueryResult getTweets(String search_string,int count){
        try {
            twitter4j.Twitter twitter = getInstance();
            Query query = new Query(search_string);
            query.count(count);
            query.setGeoCode(new GeoLocation(21.94, 76.99), 2000, Query.Unit.km);
            QueryResult queryResult = twitter.search(query);
            return queryResult;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
