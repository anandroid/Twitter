/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables;


import appengine.parser.mysqlmodels.Keys;
import appengine.parser.mysqlmodels.Parser;
import appengine.parser.mysqlmodels.tables.records.SwiggyeventsRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
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
public class Swiggyevents extends TableImpl<SwiggyeventsRecord> {

    private static final long serialVersionUID = 1605503993;

    /**
     * The reference instance of <code>parser.swiggyevents</code>
     */
    public static final Swiggyevents SWIGGYEVENTS = new Swiggyevents();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SwiggyeventsRecord> getRecordType() {
        return SwiggyeventsRecord.class;
    }

    /**
     * The column <code>parser.swiggyevents.id</code>.
     */
    public final TableField<SwiggyeventsRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>parser.swiggyevents.json</code>.
     */
    public final TableField<SwiggyeventsRecord, String> JSON = createField("json", org.jooq.impl.SQLDataType.VARCHAR.length(2000), this, "");

    /**
     * The column <code>parser.swiggyevents.device_id</code>.
     */
    public final TableField<SwiggyeventsRecord, String> DEVICE_ID = createField("device_id", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

    /**
     * Create a <code>parser.swiggyevents</code> table reference
     */
    public Swiggyevents() {
        this("swiggyevents", null);
    }

    /**
     * Create an aliased <code>parser.swiggyevents</code> table reference
     */
    public Swiggyevents(String alias) {
        this(alias, SWIGGYEVENTS);
    }

    private Swiggyevents(String alias, Table<SwiggyeventsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Swiggyevents(String alias, Table<SwiggyeventsRecord> aliased, Field<?>[] parameters) {
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
    public Identity<SwiggyeventsRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SWIGGYEVENTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SwiggyeventsRecord> getPrimaryKey() {
        return Keys.KEY_SWIGGYEVENTS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SwiggyeventsRecord>> getKeys() {
        return Arrays.<UniqueKey<SwiggyeventsRecord>>asList(Keys.KEY_SWIGGYEVENTS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Swiggyevents as(String alias) {
        return new Swiggyevents(alias, this);
    }

    /**
     * Rename this table
     */
    public Swiggyevents rename(String name) {
        return new Swiggyevents(name, null);
    }
}
