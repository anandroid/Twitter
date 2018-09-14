/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables.records;


import appengine.parser.mysqlmodels.tables.Fetcher;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FetcherRecord extends UpdatableRecordImpl<FetcherRecord> implements Record6<String, String, Double, Double, Double, Timestamp> {

    private static final long serialVersionUID = 399120976;

    /**
     * Setter for <code>parser.fetcher.coin</code>.
     */
    public void setCoin(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>parser.fetcher.coin</code>.
     */
    public String getCoin() {
        return (String) get(0);
    }

    /**
     * Setter for <code>parser.fetcher.market</code>.
     */
    public void setMarket(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>parser.fetcher.market</code>.
     */
    public String getMarket() {
        return (String) get(1);
    }

    /**
     * Setter for <code>parser.fetcher.buy_for</code>.
     */
    public void setBuyFor(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>parser.fetcher.buy_for</code>.
     */
    public Double getBuyFor() {
        return (Double) get(2);
    }

    /**
     * Setter for <code>parser.fetcher.sell_for</code>.
     */
    public void setSellFor(Double value) {
        set(3, value);
    }

    /**
     * Getter for <code>parser.fetcher.sell_for</code>.
     */
    public Double getSellFor() {
        return (Double) get(3);
    }

    /**
     * Setter for <code>parser.fetcher.volume</code>.
     */
    public void setVolume(Double value) {
        set(4, value);
    }

    /**
     * Getter for <code>parser.fetcher.volume</code>.
     */
    public Double getVolume() {
        return (Double) get(4);
    }

    /**
     * Setter for <code>parser.fetcher.time</code>.
     */
    public void setTime(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>parser.fetcher.time</code>.
     */
    public Timestamp getTime() {
        return (Timestamp) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<String, String, Double, Double, Double, Timestamp> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<String, String, Double, Double, Double, Timestamp> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Fetcher.FETCHER.COIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Fetcher.FETCHER.MARKET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field3() {
        return Fetcher.FETCHER.BUY_FOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field4() {
        return Fetcher.FETCHER.SELL_FOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field5() {
        return Fetcher.FETCHER.VOLUME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return Fetcher.FETCHER.TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getCoin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getMarket();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value3() {
        return getBuyFor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value4() {
        return getSellFor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value5() {
        return getVolume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord value1(String value) {
        setCoin(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord value2(String value) {
        setMarket(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord value3(Double value) {
        setBuyFor(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord value4(Double value) {
        setSellFor(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord value5(Double value) {
        setVolume(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord value6(Timestamp value) {
        setTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FetcherRecord values(String value1, String value2, Double value3, Double value4, Double value5, Timestamp value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FetcherRecord
     */
    public FetcherRecord() {
        super(Fetcher.FETCHER);
    }

    /**
     * Create a detached, initialised FetcherRecord
     */
    public FetcherRecord(String coin, String market, Double buyFor, Double sellFor, Double volume, Timestamp time) {
        super(Fetcher.FETCHER);

        set(0, coin);
        set(1, market);
        set(2, buyFor);
        set(3, sellFor);
        set(4, volume);
        set(5, time);
    }
}
