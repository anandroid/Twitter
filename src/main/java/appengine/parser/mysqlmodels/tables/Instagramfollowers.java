/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables;


import appengine.parser.mysqlmodels.Keys;
import appengine.parser.mysqlmodels._6txkrsiwk3;
import appengine.parser.mysqlmodels.tables.records.InstagramfollowersRecord;

import java.sql.Timestamp;
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
public class Instagramfollowers extends TableImpl<InstagramfollowersRecord> {

    private static final long serialVersionUID = 1080448019;

    /**
     * The reference instance of <code>6txKRsiwk3.instagramfollowers</code>
     */
    public static final Instagramfollowers INSTAGRAMFOLLOWERS = new Instagramfollowers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InstagramfollowersRecord> getRecordType() {
        return InstagramfollowersRecord.class;
    }

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.pagename</code>.
     */
    public final TableField<InstagramfollowersRecord, String> PAGENAME = createField("pagename", org.jooq.impl.SQLDataType.VARCHAR.length(30), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.user_id</code>.
     */
    public final TableField<InstagramfollowersRecord, String> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.VARCHAR.length(30), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.user_name</code>.
     */
    public final TableField<InstagramfollowersRecord, String> USER_NAME = createField("user_name", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.is_verified</code>.
     */
    public final TableField<InstagramfollowersRecord, Byte> IS_VERIFIED = createField("is_verified", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.followed_by_viewer</code>.
     */
    public final TableField<InstagramfollowersRecord, Byte> FOLLOWED_BY_VIEWER = createField("followed_by_viewer", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.requested_by_viewer</code>.
     */
    public final TableField<InstagramfollowersRecord, Byte> REQUESTED_BY_VIEWER = createField("requested_by_viewer", org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.from_user_name</code>.
     */
    public final TableField<InstagramfollowersRecord, String> FROM_USER_NAME = createField("from_user_name", org.jooq.impl.SQLDataType.VARCHAR.length(30), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.id</code>.
     */
    public final TableField<InstagramfollowersRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.requested_on</code>.
     */
    public final TableField<InstagramfollowersRecord, Timestamp> REQUESTED_ON = createField("requested_on", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.is_private</code>.
     */
    public final TableField<InstagramfollowersRecord, Byte> IS_PRIVATE = createField("is_private", org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.follower_count</code>.
     */
    public final TableField<InstagramfollowersRecord, Integer> FOLLOWER_COUNT = createField("follower_count", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.following_count</code>.
     */
    public final TableField<InstagramfollowersRecord, Integer> FOLLOWING_COUNT = createField("following_count", org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.media_count</code>.
     */
    public final TableField<InstagramfollowersRecord, Integer> MEDIA_COUNT = createField("media_count", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.city_name</code>.
     */
    public final TableField<InstagramfollowersRecord, String> CITY_NAME = createField("city_name", org.jooq.impl.SQLDataType.VARCHAR.length(50), this, "");

    /**
     * The column <code>6txKRsiwk3.instagramfollowers.is_updated</code>.
     */
    public final TableField<InstagramfollowersRecord, Byte> IS_UPDATED = createField("is_updated", org.jooq.impl.SQLDataType.TINYINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * Create a <code>6txKRsiwk3.instagramfollowers</code> table reference
     */
    public Instagramfollowers() {
        this("instagramfollowers", null);
    }

    /**
     * Create an aliased <code>6txKRsiwk3.instagramfollowers</code> table reference
     */
    public Instagramfollowers(String alias) {
        this(alias, INSTAGRAMFOLLOWERS);
    }

    private Instagramfollowers(String alias, Table<InstagramfollowersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Instagramfollowers(String alias, Table<InstagramfollowersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return _6txkrsiwk3._6TXKRSIWK3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<InstagramfollowersRecord, Integer> getIdentity() {
        return Keys.IDENTITY_INSTAGRAMFOLLOWERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<InstagramfollowersRecord> getPrimaryKey() {
        return Keys.KEY_INSTAGRAMFOLLOWERS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<InstagramfollowersRecord>> getKeys() {
        return Arrays.<UniqueKey<InstagramfollowersRecord>>asList(Keys.KEY_INSTAGRAMFOLLOWERS_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instagramfollowers as(String alias) {
        return new Instagramfollowers(alias, this);
    }

    /**
     * Rename this table
     */
    public Instagramfollowers rename(String name) {
        return new Instagramfollowers(name, null);
    }
}
