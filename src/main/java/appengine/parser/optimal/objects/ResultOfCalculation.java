package appengine.parser.optimal.objects;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by anand.kurapati on 14/01/18.
 */
public class ResultOfCalculation {

    private String coin;
    private CoinMarket lowestBuyCoin;
    private CoinMarket highestSellCoin;
    private List<CoinMarket> allOtherCoins;
    private Timestamp timestamp;
    private Double profit;

    public ResultOfCalculation(String coin, CoinMarket lowestBuyCoin, CoinMarket highesSellCoin, List<CoinMarket> allMarketsOfCoins) {
        this.coin = coin;
        this.lowestBuyCoin = lowestBuyCoin;
        this.highestSellCoin = highesSellCoin;
        allMarketsOfCoins.remove(highesSellCoin);
        allMarketsOfCoins.remove(lowestBuyCoin);
        this.allOtherCoins = allMarketsOfCoins;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimeStamp() {
        return this.timestamp;
    }

    public void setLowestBuyCoin(CoinMarket coinMarket) {
        this.lowestBuyCoin = coinMarket;
    }

    public void setHighestSellCoin(CoinMarket coinMarket) {
        this.highestSellCoin = coinMarket;
    }

    public CoinMarket getLowestBuyCoin() {
        return lowestBuyCoin;
    }

    public CoinMarket getHighestSellCoin() {
        return highestSellCoin;
    }

    public Double profitPercentage() {

        Double lowestBuy = lowestBuyCoin.getOurBuyPrice();

        Double higherSell = highestSellCoin.getOurSellPrice();

        Double profitPercentage = ((higherSell - lowestBuy) / lowestBuy) * 100;

        this.profit = profitPercentage;

        setProfitForOtherMarkets();

        return profitPercentage;
    }

    private Double profitPercentage(CoinMarket higherSellCoin) {

        Double lowestBuy = lowestBuyCoin.getOurBuyPrice();

        Double higherSell = higherSellCoin.getOurSellPrice();

        Double profitPercentage = ((higherSell - lowestBuy) / lowestBuy) * 100;

        return profitPercentage;
    }

    public List<CoinMarket> getAllOtherMarkets() {
        return allOtherCoins;
    }

    private void setProfitForOtherMarkets() {

        for (CoinMarket otherMarket : allOtherCoins) {
            otherMarket.profit = profitPercentage(otherMarket);
        }
    }


    @Override
    public String toString() {

        String result = "----------------------------------------------------------------------------------";

        result = lowestBuyCoin.getCoinType() + " " + coin + " - Profit " + percentageFormatter(profitPercentage(highestSellCoin)) + " Buy for " + priceFormatter(lowestBuyCoin.getOurBuyPrice()) + " at " +
                lowestBuyCoin.getMarket() + "   Sell for " + priceFormatter(highestSellCoin.getOurSellPrice()) + " at " + highestSellCoin.getMarket() + "\n";

        if (allOtherCoins.size() > 0) {

            result += "Other Markets" + "\n";
        }

        for (int i = 0; i < allOtherCoins.size(); i++) {
            CoinMarket coinMarket = allOtherCoins.get(i);
            result += " Sell at " + coinMarket.getMarket() + " for " + priceFormatter(coinMarket.getOurSellPrice()) + " " +
                    "Profit : " + profitPercentage(coinMarket) + "\n";

        }

        result += "----------------------------------------------------------------------------------------";
        return result;
    }


    public String toJSON() {
        return new Gson().toJson(this, ResultOfCalculation.class);
    }

    public String getCoin() {
        return coin;
    }

    public String getUnderScoreBTCCoin() {

        return coin.toLowerCase() + "_btc";
    }

    private String percentageFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        return df.format(value);
    }


    private String priceFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(value);
    }


}
