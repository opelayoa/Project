package com.tiendas3b.almacen.db.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.TimetableReceipt;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TIMETABLE_RECEIPT".
*/
public class TimetableReceiptDao extends AbstractDao<TimetableReceipt, Long> {

    public static final String TABLENAME = "TIMETABLE_RECEIPT";

    /**
     * Properties of entity TimetableReceipt.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Platform = new Property(1, Integer.class, "platform", false, "PLATFORM");
        public final static Property DateTime = new Property(2, String.class, "dateTime", false, "DATE_TIME");
        public final static Property Time = new Property(3, String.class, "time", false, "TIME");
        public final static Property ArriveTime = new Property(4, String.class, "arriveTime", false, "ARRIVE_TIME");
        public final static Property DeliveryTime = new Property(5, String.class, "deliveryTime", false, "DELIVERY_TIME");
        public final static Property DepartureTime = new Property(6, String.class, "departureTime", false, "DEPARTURE_TIME");
        public final static Property DownloadTime = new Property(7, String.class, "downloadTime", false, "DOWNLOAD_TIME");
        public final static Property Level = new Property(8, Integer.class, "level", false, "LEVEL");
        public final static Property Arrive = new Property(9, Boolean.class, "arrive", false, "ARRIVE");
        public final static Property Folio = new Property(10, Integer.class, "folio", false, "FOLIO");
        public final static Property RegionId = new Property(11, long.class, "regionId", false, "REGION_ID");
        public final static Property ProviderId = new Property(12, long.class, "providerId", false, "PROVIDER_ID");
    };

    private DaoSession daoSession;


    public TimetableReceiptDao(DaoConfig config) {
        super(config);
    }
    
    public TimetableReceiptDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TIMETABLE_RECEIPT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PLATFORM\" INTEGER," + // 1: platform
                "\"DATE_TIME\" TEXT," + // 2: dateTime
                "\"TIME\" TEXT NOT NULL ," + // 3: time
                "\"ARRIVE_TIME\" TEXT," + // 4: arriveTime
                "\"DELIVERY_TIME\" TEXT," + // 5: deliveryTime
                "\"DEPARTURE_TIME\" TEXT," + // 6: departureTime
                "\"DOWNLOAD_TIME\" TEXT," + // 7: downloadTime
                "\"LEVEL\" INTEGER," + // 8: level
                "\"ARRIVE\" INTEGER," + // 9: arrive
                "\"FOLIO\" INTEGER," + // 10: folio
                "\"REGION_ID\" INTEGER NOT NULL ," + // 11: regionId
                "\"PROVIDER_ID\" INTEGER NOT NULL );"); // 12: providerId
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_TIMETABLE_RECEIPT_REGION_ID_PROVIDER_ID_FOLIO ON TIMETABLE_RECEIPT" +
                " (\"REGION_ID\",\"PROVIDER_ID\",\"FOLIO\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TIMETABLE_RECEIPT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TimetableReceipt entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer platform = entity.getPlatform();
        if (platform != null) {
            stmt.bindLong(2, platform);
        }
 
        String dateTime = entity.getDateTime();
        if (dateTime != null) {
            stmt.bindString(3, dateTime);
        }
        stmt.bindString(4, entity.getTime());
 
        String arriveTime = entity.getArriveTime();
        if (arriveTime != null) {
            stmt.bindString(5, arriveTime);
        }
 
        String deliveryTime = entity.getDeliveryTime();
        if (deliveryTime != null) {
            stmt.bindString(6, deliveryTime);
        }
 
        String departureTime = entity.getDepartureTime();
        if (departureTime != null) {
            stmt.bindString(7, departureTime);
        }
 
        String downloadTime = entity.getDownloadTime();
        if (downloadTime != null) {
            stmt.bindString(8, downloadTime);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(9, level);
        }
 
        Boolean arrive = entity.getArrive();
        if (arrive != null) {
            stmt.bindLong(10, arrive ? 1L: 0L);
        }
 
        Integer folio = entity.getFolio();
        if (folio != null) {
            stmt.bindLong(11, folio);
        }
        stmt.bindLong(12, entity.getRegionId());
        stmt.bindLong(13, entity.getProviderId());
    }

    @Override
    protected void attachEntity(TimetableReceipt entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TimetableReceipt readEntity(Cursor cursor, int offset) {
        TimetableReceipt entity = new TimetableReceipt( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // platform
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dateTime
            cursor.getString(offset + 3), // time
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // arriveTime
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // deliveryTime
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // departureTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // downloadTime
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // level
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // arrive
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // folio
            cursor.getLong(offset + 11), // regionId
            cursor.getLong(offset + 12) // providerId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TimetableReceipt entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlatform(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setDateTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime(cursor.getString(offset + 3));
        entity.setArriveTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDeliveryTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDepartureTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDownloadTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLevel(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setArrive(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setFolio(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setRegionId(cursor.getLong(offset + 11));
        entity.setProviderId(cursor.getLong(offset + 12));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TimetableReceipt entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TimetableReceipt entity) {
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
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRegionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getProviderDao().getAllColumns());
            builder.append(" FROM TIMETABLE_RECEIPT T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN PROVIDER T1 ON T.\"PROVIDER_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected TimetableReceipt loadCurrentDeep(Cursor cursor, boolean lock) {
        TimetableReceipt entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Region region = loadCurrentOther(daoSession.getRegionDao(), cursor, offset);
         if(region != null) {
            entity.setRegion(region);
        }
        offset += daoSession.getRegionDao().getAllColumns().length;

        Provider provider = loadCurrentOther(daoSession.getProviderDao(), cursor, offset);
         if(provider != null) {
            entity.setProvider(provider);
        }

        return entity;    
    }

    public TimetableReceipt loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<TimetableReceipt> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<TimetableReceipt> list = new ArrayList<TimetableReceipt>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<TimetableReceipt> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<TimetableReceipt> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
