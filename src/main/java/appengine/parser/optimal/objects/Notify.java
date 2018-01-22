package appengine.parser.optimal.objects;

import java.sql.Timestamp;

/**
 * Created by anand.kurapati on 22/01/18.
 */
public class Notify {
    public String coinlabel;
    public Timestamp timestamp;
    public Market frommarket;
    public Market tomarket;
    public NotifyType notifyType;
    public Double profit;

    public Notify(String coinlabel, Timestamp timestamp, Double profit, String frommarket, String tomarket, NotifyType notifyType) {

        this.coinlabel = coinlabel;
        this.timestamp = timestamp;
        this.profit = profit;
        this.frommarket = Market.valueOf(frommarket);
        this.tomarket = Market.valueOf(tomarket);
        this.notifyType = notifyType;

    }

    public Notify(String coinlabel, Timestamp timestamp, Double profit, Market frommarket, Market tomarket, NotifyType notifyType) {

        this.coinlabel = coinlabel;
        this.timestamp = timestamp;
        this.profit = profit;
        this.frommarket = frommarket;
        this.tomarket = tomarket;
        this.notifyType = notifyType;
    }

    public void setNotifyType(NotifyType notifyType) {
        this.notifyType = notifyType;
    }

    @Override
    public String toString() {
        String message = "";
        message = coinlabel + " Profit :" + profit + " " + notifyType + "\n" +
                "Buy From : " + frommarket + " Sell At : " + tomarket + "\n\n";
        return message;

    }
}
