package com.tiendas3b.almacen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.SheetTrip;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SHEET_TRIP".
*/
public class SheetTripDao extends AbstractDao<SheetTrip, Long> {

    public static final String TABLENAME = "SHEET_TRIP";

    /**
     * Properties of entity SheetTrip.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Date = new Property(1, java.util.Date.class, "date", false, "DATE");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property TripId = new Property(3, long.class, "tripId", false, "TRIP_ID");
        public final static Property TruckId = new Property(4, long.class, "truckId", false, "TRUCK_ID");
        public final static Property StoreId = new Property(5, Long.class, "storeId", false, "STORE_ID");
        public final static Property Latitude = new Property(6, Double.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(7, Double.class, "longitude", false, "LONGITUDE");
        public final static Property ActivityId = new Property(8, Long.class, "activityId", false, "ACTIVITY_ID");
        public final static Property UserId = new Property(9, long.class, "userId", false, "USER_ID");
        public final static Property RegionId = new Property(10, long.class, "regionId", false, "REGION_ID");
    };


    public SheetTripDao(DaoConfig config) {
        super(config);
    }
    
    public SheetTripDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHEET_TRIP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DATE\" INTEGER NOT NULL ," + // 1: date
                "\"DESCRIPTION\" TEXT NOT NULL ," + // 2: description
                "\"TRIP_ID\" INTEGER NOT NULL ," + // 3: tripId
                "\"TRUCK_ID\" INTEGER NOT NULL ," + // 4: truckId
                "\"STORE_ID\" INTEGER," + // 5: storeId
                "\"LATITUDE\" REAL," + // 6: latitude
                "\"LONGITUDE\" REAL," + // 7: longitude
                "\"ACTIVITY_ID\" INTEGER," + // 8: activityId
                "\"USER_ID\" INTEGER NOT NULL ," + // 9: userId
                "\"REGION_ID\" INTEGER NOT NULL );"); // 10: regionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHEET_TRIP\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SheetTrip entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDate().getTime());
        stmt.bindString(3, entity.getDescription());
        stmt.bindLong(4, entity.getTripId());
        stmt.bindLong(5, entity.getTruckId());
 
        Long storeId = entity.getStoreId();
        if (storeId != null) {
            stmt.bindLong(6, storeId);
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(7, latitude);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(8, longitude);
        }
 
        Long activityId = entity.getActivityId();
        if (activityId != null) {
            stmt.bindLong(9, activityId);
        }
        stmt.bindLong(10, entity.getUserId());
        stmt.bindLong(11, entity.getRegionId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SheetTrip readEntity(Cursor cursor, int offset) {
        SheetTrip entity = new SheetTrip( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            new java.util.Date(cursor.getLong(offset + 1)), // date
            cursor.getString(offset + 2), // description
            cursor.getLong(offset + 3), // tripId
            cursor.getLong(offset + 4), // truckId
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // storeId
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // latitude
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // longitude
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // activityId
            cursor.getLong(offset + 9), // userId
            cursor.getLong(offset + 10) // regionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SheetTrip entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(new java.util.Date(cursor.getLong(offset + 1)));
        entity.setDescription(cursor.getString(offset + 2));
        entity.setTripId(cursor.getLong(offset + 3));
        entity.setTruckId(cursor.getLong(offset + 4));
        entity.setStoreId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setLatitude(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setLongitude(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setActivityId(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setUserId(cursor.getLong(offset + 9));
        entity.setRegionId(cursor.getLong(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SheetTrip entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SheetTrip entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}