/**
 * This class is generated by jOOQ
 */
package appengine.parser.mysqlmodels.tables.records;


import appengine.parser.mysqlmodels.tables.Ubereats;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
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
public class UbereatsRecord extends UpdatableRecordImpl<UbereatsRecord> implements Record8<String, String, String, String, String, Integer, String, Integer> {

    private static final long serialVersionUID = 1274469148;

    /**
     * Setter for <code>parser.ubereats.uuid</code>.
     */
    public void setUuid(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>parser.ubereats.uuid</code>.
     */
    public String getUuid() {
        return (String) get(0);
    }

    /**
     * Setter for <code>parser.ubereats.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>parser.ubereats.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>parser.ubereats.item_description</code>.
     */
    public void setItemDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>parser.ubereats.item_description</code>.
     */
    public String getItemDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>parser.ubereats.image_url</code>.
     */
    public void setImageUrl(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>parser.ubereats.image_url</code>.
     */
    public String getImageUrl() {
        return (String) get(3);
    }

    /**
     * Setter for <code>parser.ubereats.query</code>.
     */
    public void setQuery(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>parser.ubereats.query</code>.
     */
    public String getQuery() {
        return (String) get(4);
    }

    /**
     * Setter for <code>parser.ubereats.city_id</code>.
     */
    public void setCityId(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>parser.ubereats.city_id</code>.
     */
    public Integer getCityId() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>parser.ubereats.store_uuid</code>.
     */
    public void setStoreUuid(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>parser.ubereats.store_uuid</code>.
     */
    public String getStoreUuid() {
        return (String) get(6);
    }

    /**
     * Setter for <code>parser.ubereats.id</code>.
     */
    public void setId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>parser.ubereats.id</code>.
     */
    public Integer getId() {
        return (Integer) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<String, String, String, String, String, Integer, String, Integer> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<String, String, String, String, String, Integer, String, Integer> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Ubereats.UBEREATS.UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Ubereats.UBEREATS.TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Ubereats.UBEREATS.ITEM_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Ubereats.UBEREATS.IMAGE_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Ubereats.UBEREATS.QUERY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return Ubereats.UBEREATS.CITY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Ubereats.UBEREATS.STORE_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return Ubereats.UBEREATS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getItemDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getImageUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getQuery();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getCityId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getStoreUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value1(String value) {
        setUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value2(String value) {
        setTitle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value3(String value) {
        setItemDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value4(String value) {
        setImageUrl(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value5(String value) {
        setQuery(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value6(Integer value) {
        setCityId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value7(String value) {
        setStoreUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord value8(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UbereatsRecord values(String value1, String value2, String value3, String value4, String value5, Integer value6, String value7, Integer value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UbereatsRecord
     */
    public UbereatsRecord() {
        super(Ubereats.UBEREATS);
    }

    /**
     * Create a detached, initialised UbereatsRecord
     */
    public UbereatsRecord(String uuid, String title, String itemDescription, String imageUrl, String query, Integer cityId, String storeUuid, Integer id) {
        super(Ubereats.UBEREATS);

        set(0, uuid);
        set(1, title);
        set(2, itemDescription);
        set(3, imageUrl);
        set(4, query);
        set(5, cityId);
        set(6, storeUuid);
        set(7, id);
    }
}