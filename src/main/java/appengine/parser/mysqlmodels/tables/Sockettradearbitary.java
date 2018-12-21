/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables;


import appengine.parser.mysqlmodels.Keys;
import appengine.parser.mysqlmodels.Parser;
import appengine.parser.mysqlmodels.enums.SockettradearbitaryTradetype;
import appengine.parser.mysqlmodels.tables.records.SockettradearbitaryRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class Sockettradearbitary extends TableImpl<SockettradearbitaryRecord> {

    private static final long serialVersionUID = 1744628621;

    /**
     * The reference instance of <code>parser.sockettradearbitary</code>
     */
    public static final Sockettradearbitary SOCKETTRADEARBITARY = new Sockettradearbitary();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SockettradearbitaryRecord> getRecordType() {
        return SockettradearbitaryRecord.class;
    }

    /**
     * The column <code>parser.sockettradearbitary.coinlabel</code>.
     */
    public final TableField<SockettradearbitaryRecord, String> COINLABEL = createField("coinlabel", org.jooq.impl.SQLDataType.VARCHAR.length(10), this, "");

    /**
     * The column <code>parser.sockettradearbitary.tradetype</code>.
     */
    public final TableField<SockettradearbitaryRecord, SockettradearbitaryTradetype> TRADETYPE = createField("tradetype", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(appengine.parser.mysqlmodels.enums.SockettradearbitaryTradetype.class), this, "");

    /**
     * The column <code>parser.sockettradearbitary.buyprice</code>.
     */
    public final TableField<SockettradearbitaryRecord, Double> BUYPRICE = createField("buyprice", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>parser.sockettradearbitary.sellprice</code>.
     */
    public final TableField<SockettradearbitaryRecord, Double> SELLPRICE = createField("sellprice", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>parser.sockettradearbitary.createdtime</code>.
     */
    public final TableField<SockettradearbitaryRecord, Timestamp> CREATEDTIME = createField("createdtime", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>parser.sockettradearbitary.market</code>.
     */
    public final TableField<SockettradearbitaryRecord, String> MARKET = createField("market", org.jooq.impl.SQLDataType.VARCHAR.length(20), this, "");

    /**
     * The column <code>parser.sockettradearbitary.amount</code>.
     */
    public final TableField<SockettradearbitaryRecord, Double> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>parser.sockettradearbitary.arbitarysellprice</code>.
     */
    public final TableField<SockettradearbitaryRecord, Double> ARBITARYSELLPRICE = createField("arbitarysellprice", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>parser.sockettradearbitary.iscompleted</code>.
     */
    public final TableField<SockettradearbitaryRecord, Byte> ISCOMPLETED = createField("iscompleted", org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>parser.sockettradearbitary.updatedarbitarysellprice</code>.
     */
    public final TableField<SockettradearbitaryRecord, Double> UPDATEDARBITARYSELLPRICE = createField("updatedarbitarysellprice", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>parser.sockettradearbitary.updatedtime</code>.
     */
    public final TableField<SockettradearbitaryRecord, Timestamp> UPDATEDTIME = createField("updatedtime", org.jooq.impl.SQLDataType.TIMESTAMP.defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>parser.sockettradearbitary.updatedsellprice</code>.
     */
    public final TableField<SockettradearbitaryRecord, Double> UPDATEDSELLPRICE = createField("updatedsellprice", org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>parser.sockettradearbitary.order_id</code>.
     */
    public final TableField<SockettradearbitaryRecord, String> ORDER_ID = createField("order_id", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * Create a <code>parser.sockettradearbitary</code> table reference
     */
    public Sockettradearbitary() {
        this("sockettradearbitary", null);
    }

    /**
     * Create an aliased <code>parser.sockettradearbitary</code> table reference
     */
    public Sockettradearbitary(String alias) {
        this(alias, SOCKETTRADEARBITARY);
    }

    private Sockettradearbitary(String alias, Table<SockettradearbitaryRecord> aliased) {
        this(alias, aliased, null);
    }

    private Sockettradearbitary(String alias, Table<SockettradearbitaryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Parser.PARSER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SockettradearbitaryRecord>> getKeys() {
        return Arrays.<UniqueKey<SockettradearbitaryRecord>>asList(Keys.KEY_SOCKETTRADEARBITARY_SOCKETTRADEARBITARY_COINLABEL_MARKET_TRADETYPE_CREATEDTIME_PK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sockettradearbitary as(String alias) {
        return new Sockettradearbitary(alias, this);
    }

    /**
     * Rename this table
     */
    public Sockettradearbitary rename(String name) {
        return new Sockettradearbitary(name, null);
    }
}