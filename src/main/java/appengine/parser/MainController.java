package appengine.parser;

/**
 * Created by anand.kurapati on 09/12/17.
 */

import appengine.parser.facebook.AcceptFriendRequests;
import appengine.parser.facebook.DetailedPost;
import appengine.parser.facebook.Facebook;
import appengine.parser.objects.AccessToken;
import appengine.parser.objects.twitter4j.Tweet;
import appengine.parser.repository.BaseRepository;
import appengine.parser.repository.DefaultRepository;
import appengine.parser.repository.PagesAggregatorRepository;
import appengine.parser.twitter.Twitter;
import appengine.parser.utils.StringUtils;
import appengine.parser.wordpress.WordPress;
import com.restfb.types.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.QueryResult;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class MainController {

    private static final Logger logger = Logger.getLogger(MainController.class.getName());


    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/getfromfbtofb")
    public String getFromFBToFB() {

        BaseRepository baseRepository = new DefaultRepository();
        Facebook facebook = new Facebook(baseRepository);

        for (String pageId : baseRepository.getThirdPartyPages()) {
            ArrayList<DetailedPost> fbPosts = facebook.getPhotoPostsOfPage(pageId);
            for (AccessToken accessToken : baseRepository.getAccessTokensOfSameCategory(pageId)) {
                for (DetailedPost fbPost : fbPosts) {
                    System.out.println(fbPost.toString());
                    //Log.print("FbPost -  Picture " + fbPost.getFirstPictureLink() + "  Permalink " + fbPost.getPermalinkUrl());
                    facebook.publishImage(fbPost, accessToken);
                }
            }
        }

        return "{\"success\"}";
    }

    @GetMapping("/promoteownpageonprofiles")
    public String promoteOwnPageonProfiles() {

        BaseRepository baseRepository = new DefaultRepository();
        Facebook facebook = new Facebook(baseRepository);

        for (String pageId : baseRepository.getOwnPages()) {
            ArrayList<Post> fbPosts = facebook.getPermaLinksOfPage(pageId);
            for (Post fbPost : fbPosts) {
                System.out.println(fbPost.toString());
                System.out.println("FbPost -    Permalink " + fbPost.getPermalinkUrl());
                facebook.shareFromPage(fbPost);
            }
        }
        return "{\"success\"}";

    }

    @GetMapping("/acceptfriendrequests")
    public String acceptFriendRequests() {
        BaseRepository baseRepository = new DefaultRepository();
        AcceptFriendRequests acceptFriendRequests = new AcceptFriendRequests(baseRepository);
        acceptFriendRequests.acceptFriendRequestsParallelly();
        return "{\"success\"}";
    }

    @GetMapping("/getfromtwittertowpfb/{search}")
    public String getFromTwitterToWPFB(@PathVariable String search) {

        BaseRepository baseRepository = new DefaultRepository();
        Facebook facebook = new Facebook(baseRepository);

        try {
            QueryResult queryResult = Twitter.getTweets(search);
            ArrayList<Tweet> publishedTweets = new ArrayList<Tweet>();
            for (Status status : queryResult.getTweets()) {
                Tweet tweet = new Tweet(status);
                if (tweet.isWithImage() && !tweet.similarTweetExists(publishedTweets)) {
                    publishedTweets.add(tweet);
                    String wp_post_link = WordPress.publish(tweet.getFormattedTweet(), tweet.getOEmbedUrl(Twitter.getInstance()), tweet.getImageUrl());
                    if (StringUtils.isNonEmpty(wp_post_link))
                        facebook.publish(tweet.getFormattedTweet(), wp_post_link, tweet.getImageUrl());
                }
            }
            if (publishedTweets.size() == 0) {
                queryResult.getSinceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"success\"}";
    }

    @GetMapping("/publishaggregators")
    public String publishaggregators() {
        BaseRepository baseRepository = new PagesAggregatorRepository();
        Facebook facebook = new Facebook(baseRepository);
        try {
            for (String pageId : baseRepository.getThirdPartyPages()) {
                ArrayList<Post> fbPosts = facebook.getPermaLinksOfPage(pageId, 1000L * 60L * 60L * 24L * 7L);
                for (Post fbPost : fbPosts) {
                    System.out.println(fbPost.toString());
                    System.out.println("FbPost -    Permalink " + fbPost.getPermalinkUrl());
                    facebook.shareFromPage(fbPost);
                }
            }
        } catch (Throwable throwable) {
            logger.log(Level.SEVERE, throwable.toString() + " " + throwable.getMessage());
        }

        return "{\"success\"}";
    }


}