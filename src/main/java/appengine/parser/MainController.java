package appengine.parser;

/**
 * Created by anand.kurapati on 09/12/17.
 */

import appengine.parser.facebook.AcceptFriendRequests;
import appengine.parser.facebook.AutoPost;
import appengine.parser.facebook.DetailedPost;
import appengine.parser.facebook.Facebook;
import appengine.parser.objects.AccessToken;
import appengine.parser.objects.twitter4j.Tweet;
import appengine.parser.optimal.*;
import appengine.parser.optimal.exchangeutils.BinanceUtil;
import appengine.parser.optimal.livecoinokex.OkexLivecoinApi;
import appengine.parser.optimal.livecoinokex.TransferApi;
import appengine.parser.optimal.livecoinokex.utils.Transfer;
import appengine.parser.optimal.livecoinokex.utils.livecoin.LivecoinUtil;
import appengine.parser.optimal.utils.OrderBookCalculator;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        return "hello world! change";
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

    @GetMapping("/autopost")
    public String autoPost() {
        BaseRepository baseRepository = new DefaultRepository();
        AutoPost autoPost = new AutoPost(baseRepository);
        autoPost.loginAndPost();
        return "{\"success\"}";
    }

    @GetMapping("/candp")
    public String candp() {
        return new CandP().calculateCandP();
    }

    @GetMapping("/candy")
    public String candy() {
        return new CandP().calculateCryptoYoBit();
    }

    @GetMapping("/optimal/twoway/{investment}/exclude/{excludes}")
    public String optimal(@PathVariable String investment, @PathVariable String[] excludes) {
        return new Optimal().getData(investment, excludes);
    }

    @GetMapping("/optimal/twoway/{investment}")
    public String optimal(@PathVariable String investment) {
        return new Optimal().getData(investment);
    }

    @GetMapping("/optimal/twoway")
    public String optimal() {
        return new Optimal().getData();
    }

    @GetMapping("/optimal/oneway/{symbol}/{percentage}")
    public String optimal(@PathVariable String symbol, @PathVariable String percentage) {
        return new Optimal().getUSDToINDData(symbol, percentage);
    }

    @GetMapping("/optimal/oneway/{investment}/{symbol}/{numberofcoins}/{value}")
    public String optimal(@PathVariable String investment, @PathVariable String symbol, @PathVariable String numberOfCoins, @PathVariable String value) {
        return new Optimal().getUSDToINDData(investment, symbol, numberOfCoins, value);

    }

    @GetMapping("/optimal/oneway/{investment}/{symbol}/{percentage}")
    public String optimal(@PathVariable String investment, @PathVariable String symbol, @PathVariable String percentage) {
        return new Optimal().getUSDToINDData(investment, symbol, percentage);
    }

    @GetMapping("/coincalculator/fetch/okex")
    public String prefetcherOkex() {
        return new Fetcher().fetchOkex();
        // return "ok";
    }

    @GetMapping("/coincalculator/fetch/cobinhood")
    public String prefetcherCobinHood() {
        return new Fetcher().fetchCobinHood();
        // return "ok";
    }

    @GetMapping("/coincalculator/binance")
    public String coinCalculatorBinance() {
        return new CoinCalculator().fetchBinance();
    }

    @GetMapping("/coincalculator/bitz")
    public String coinCalculatorBitz() {
        return new CoinCalculator().fetchBitZ();
    }

    @GetMapping("/coincalculator/cryptopia")
    public String coinCalculatorCryptopia() {
        return new CoinCalculator().fetchCryptopia();
    }

    @GetMapping("/coincalculator/hitbtc")
    public String coinCalculatorHitBTC() {
        return new CoinCalculator().fetchHitBTC();
    }

    @GetMapping("/coincalculator/liqui")
    public String coinCalculatorLiqui() {
        return new CoinCalculator().fetchLiqui();
    }

    @GetMapping("/coincalculator/livecoin")
    public String coinCalculatorLiveCoin() {
        return new CoinCalculator().printLiveCoin();
    }

    @GetMapping("/coincalculator/poloneix")
    public String coinCalculatorPoloneix() {
        return new CoinCalculator().fetchPoloneix();
    }

    @GetMapping("/coincalculator/cobinhood")
    public String coinCalculatorCobinhood() {
        return new CoinCalculator().fetchCobinHood();
    }

    @GetMapping("/coincalculator/okex")
    public String coinCalculatorOkex() {
        return new CoinCalculator().fetchOkex();
    }

    @GetMapping("/coincalculator/all")
    public String coinAll() {
        return new CoinCalculator().fetchAll();
    }

    @GetMapping("/coincalculator/all/{includes}")
    public String coinAll(@PathVariable String[] includes) {
        return new CoinCalculator().fetchAll(includes);
    }

    @GetMapping("/coincalculator/all/{includes}/profit/{profit}")
    public String coinAll(@PathVariable String[] includes, @PathVariable String profit) {
        CoinCalculator coinCalculator = new CoinCalculator();
        coinCalculator.setMinimumProfitPercentage(Double.valueOf(profit));
        return coinCalculator.fetchAll(includes);
    }

    @GetMapping("/coincalculator/all/{includes}/profit/{profit}/from/{from}")
    public String coinAll(@PathVariable String[] includes, @PathVariable String profit, @PathVariable String from) {
        CoinCalculator coinCalculator = new CoinCalculator();
        coinCalculator.setMinimumProfitPercentage(Double.valueOf(profit));
        coinCalculator.setBuyFromMarket(from);
        return coinCalculator.fetchAll(includes);
    }

    @GetMapping("/coincalculator/dataanalyser/json/{labels}")
    public String coinDataAnalyzerJson(@PathVariable String[] labels) {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        return dataAnalyzer.coinAnalyzer(labels, true);

    }

    @GetMapping("/coincalculator/dataanalyser/{labels}")
    public String coinDataAnalyzer(@PathVariable String[] labels) {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        return dataAnalyzer.coinAnalyzer(labels, false);

    }

    @GetMapping("/coincalculator/dataanalyser/json/from/{time}")
    public String coinDataAnalyzerJson(@PathVariable String time) {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        Timestamp timestamp = getTimeStampFromString(time);
        if (timestamp == null) {
            return "Invalid timestamp . Enter in yyyy-MM-dd hh:mm:ss format";
        }

        return dataAnalyzer.getDataFromTime(timestamp, true);
        //return "hey response";
        //return dataAnalyzer.getDataFromTime(timestamp, true);
    }

    @GetMapping("/coincalculator/dataanalyser/from/{time}")
    public String coinDataAnalyzer(@PathVariable String time) {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        Timestamp timestamp = getTimeStampFromString(time);
        if (timestamp == null) {
            return "Invalid timestamp . Enter in yyyy-MM-dd hh:mm:ss format";
        }

        return dataAnalyzer.getDataFromTime(timestamp, false);
    }

    @GetMapping("/coincalculator/dataanalyser/from/{time}/coins/{coins}")
    public String coinDataAnalyzer(@PathVariable String time, @PathVariable String[] coins) {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        Timestamp timestamp = getTimeStampFromString(time);
        if (timestamp == null) {
            return "Invalid timestamp . Enter in yyyy-MM-dd hh:mm:ss format";
        }

        return dataAnalyzer.getDataFromTimeAndCoin(timestamp, coins, false);
    }

    @GetMapping("/coincalculator/dataanalyser/json/from/{time}/coins/{coins}")
    public String coinDataAnalyzerJson(@PathVariable String time, @PathVariable String[] coins) {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        Timestamp timestamp = getTimeStampFromString(time);
        if (timestamp == null) {
            return "Invalid timestamp . Enter in yyyy-MM-dd hh:mm:ss format";
        }

        return dataAnalyzer.getDataFromTimeAndCoin(timestamp, coins, true);
    }

    @GetMapping("/coincalculator/optimalupdatedtime")
    public String getOptimalUpdateTime() {
        return new DataAnalyzer().getLastUpdatedTime();
    }

    @GetMapping("/coincalculator/view/all")
    public String coincalculatorView() {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        return dataAnalyzer.getDataFromLastUpdate(false);
    }

    @GetMapping("/coincalculator/view/json/all")
    public String coincalculatorViewjson() {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        return dataAnalyzer.getDataFromLastUpdate(true);
    }

    @GetMapping("/coincalculator/json/marketscompare/{firstmarket}/{secondmarket}")
    public String coincalculatorMarketsCompare(@PathVariable String firstmarket, @PathVariable String secondmarket) {
        MarketsCompare marketsCompare = new MarketsCompare();
        return marketsCompare.getLastData(firstmarket, secondmarket, true);
    }

    @GetMapping("/coincalculator/json/marketscompare/{firstmarket}/{secondmarket}/{coin}")
    public String coincalculatorMarketsCoinCompare(@PathVariable String firstmarket, @PathVariable String secondmarket,
                                                   @PathVariable String coin) {
        MarketsCompare marketsCompare = new MarketsCompare();
        return marketsCompare.getCoinNotifyData(firstmarket, secondmarket, coin, true);
    }

    @GetMapping("/coincalculator/json/okexbinance")
    public String coincalculatorViewOkexBinance() {
        OkexBinanceApi okexBinanceApi = new OkexBinanceApi();
        return okexBinanceApi.getLastData(true);
    }

    @GetMapping("/coincalculator/json/okexbinance/coin/{coin}")
    public String coincalculatorViewOkexBinanceCoin(@PathVariable String coin) {
        OkexBinanceApi okexBinanceApi = new OkexBinanceApi();
        return okexBinanceApi.getCoinNotifyData(coin, true);
    }


    @GetMapping("/coincalculator/notifier")
    public String notifierAll() {
        Notifier notifier = new Notifier();
        return notifier.fetch();
    }

    @GetMapping("/coincalculator/notifier/okexbinance")
    public String notifierOkexBinance() {
        Notifier notifier = new Notifier();
        return notifier.fetchOkexBinance();
    }

    @GetMapping("/coincalculator/automate/okexlivecoin")
    public String automateOkexLiveCoin() {
        OkexLivecoinApi okexLivecoinApi = new OkexLivecoinApi();
        return okexLivecoinApi.automate();
    }

    @GetMapping("/coincalculator/automate/okexlivecoin/getorders")
    public String getOrdersOkexLiveCoin() {
        TransferApi transferApi = new TransferApi();
        transferApi.getPurchasedOrders();
        return "";
    }


    @GetMapping("/coincalculator/automate/livecoin/getaddress/{coin}")
    public String getCoinAddress(@PathVariable String coin) {
        LivecoinUtil livecoinUtil = new LivecoinUtil();
        return livecoinUtil.getAddress(coin.toUpperCase()).toString();
    }


    @GetMapping("/coincalculator/automate/okexlivecoin/transfer")
    public String makeTransfer() {
        TransferApi transferApi = new TransferApi();
        transferApi.transferOrder();
        return "";

    }

    @GetMapping("/coincalculator/orderbook/binance")
    public String orderBook() {
        BinanceUtil binanceUtil = new BinanceUtil();
        binanceUtil.getOrderBook();
        return "";
    }

    @GetMapping("/coincalculator/orderbook/{coin}/buyfrom/{buymarket}/sellat/{sellmarket}")
    public String calculateOrderBook(@PathVariable String coin, @PathVariable String buymarket, @PathVariable String sellmarket) {

        OrderBookCalculator orderBookCalculator = new OrderBookCalculator(coin, buymarket, sellmarket);
        Transfer transfer = orderBookCalculator.calculate();
        return transfer.toJSON();
    }


    private Timestamp getTimeStampFromString(String time) {
        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date parsedTimeStamp = dateFormat.parse(time);

            timestamp = new Timestamp(parsedTimeStamp.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }


}