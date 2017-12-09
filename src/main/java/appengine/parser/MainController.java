package appengine.parser;

/**
 * Created by anand.kurapati on 09/12/17.
 */


import appengine.parser.facebook.AcceptFriendRequests;
import appengine.parser.facebook.DetailedPost;
import appengine.parser.facebook.Facebook;
import appengine.parser.utils.Constants;
import appengine.parser.utils.Log;
import com.restfb.types.Post;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@SpringBootApplication
@RestController
public class MainController {
    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/getfromfbtofb")
    public String getFromFBToFB() {

        ArrayList<DetailedPost> fbPosts = Facebook.getPostsOfPage(Constants.page_ids[1]);

        int count = 0;
        for (DetailedPost fbPost : fbPosts) {
            System.out.println(fbPost.toString());
            Log.print("FbPost -  Picture " + fbPost.getFirstPictureLink() + "  Permalink " + fbPost.getPermalinkUrl());
            Facebook.publishImageForPages(fbPost);
            count++;
            if (count > 10) {
                break;
            }
        }
        return "{\"success\"}";
    }

    @GetMapping("/promoteownpageonprofiles")
    public String promoteOwnPageonProfiles() {
        ArrayList<Post> fbPosts = Facebook.getPermaLinksOfPage(Constants.to_be_promoted_page);
        for (Post fbPost : fbPosts) {
            System.out.println(fbPost.toString());
            System.out.println("FbPost -    Permalink " + fbPost.getPermalinkUrl());
            Facebook.shareFromPage(fbPost);
        }
        return "{\"success\"}";
    }

    @GetMapping("/acceptfriendrequests")
    public String acceptFriendRequests() {
        AcceptFriendRequests.acceptFriendRequestsParallelly();
        return "{\"success\"}";
    }



}