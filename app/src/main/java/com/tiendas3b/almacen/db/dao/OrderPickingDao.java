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

import com.tiendas3b.almacen.db.dao.OrderPicking;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORDER_PICKING".
*/
public class OrderPickingDao extends AbstractDao<OrderPicking, Long> {

    public static final String TABLENAME = "ORDER_PICKING";

    /**
     * Properties of entity OrderPicking.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Status = new Property(1, Integer.class, "status", false, "STATUS");
        public final static Property Type = new Property(2, Integer.class, "type", false, "TYPE");
        public final static Property Date = new Property(3, String.class, "date", false, "DATE");
        public final static Property RegionId = new Property(4, long.class, "regionId", false, "REGION_ID");
        public final static Property StoreId = new Property(5, long.class, "storeId", false, "STORE_ID");
        public final static Property Paybill = new Property(6, int.class, "paybill", false, "PAYBILL");
    };

    private DaoSession daoSession;


    public OrderPickingDao(DaoConfig config) {
        super(config);
    }
    
    public OrderPickingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORDER_PICKING\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"STATUS\" INTEGER," + // 1: status
                "\"TYPE\" INTEGER," + // 2: type
                "\"DATE\" TEXT," + // 3: date
                "\"REGION_ID\" INTEGER NOT NULL ," + // 4: regionId
                "\"STORE_ID\" INTEGER NOT NULL ," + // 5: storeId
                "\"PAYBILL\" INTEGER NOT NULL );"); // 6: paybill
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_ORDER_PICKING_REGION_ID_STORE_ID_PAYBILL ON ORDER_PICKING" +
                " (\"REGION_ID\",\"STORE_ID\",\"PAYBILL\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORDER_PICKING\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrderPicking entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(2, status);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(3, type);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(4, date);
        }
        stmt.bindLong(5, entity.getRegionId());
        stmt.bindLong(6, entity.getStoreId());
        stmt.bindLong(7, entity.getPaybill());
    }

    @Override
    protected void attachEntity(OrderPicking entity) {
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
    public OrderPicking readEntity(Cursor cursor, int offset) {
        OrderPicking entity = new OrderPicking( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // status
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // date
            cursor.getLong(offset + 4), // regionId
            cursor.getLong(offset + 5), // storeId
            cursor.getInt(offset + 6) // paybill
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrderPicking entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStatus(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setType(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRegionId(cursor.getLong(offset + 4));
        entity.setStoreId(cursor.getLong(offset + 5));
        entity.setPaybill(cursor.getInt(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(OrderPicking entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(OrderPicking entity) {
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
            SqlUtils.appendColumns(builder, "T1", daoSession.getStoreDao().getAllColumns());
            builder.append(" FROM ORDER_PICKING T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN STORE T1 ON T.\"STORE_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected OrderPicking loadCurrentDeep(Cursor cursor, boolean lock) {
        OrderPicking entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Region region = loadCurrentOther(daoSession.getRegionDao(), cursor, offset);
         if(region != null) {
            entity.setRegion(region);
        }
        offset += daoSession.getRegionDao().getAllColumns().length;

        Store store = loadCurrentOther(daoSession.getStoreDao(), cursor, offset);
         if(store != null) {
            entity.setStore(store);
        }

        return entity;    
    }

    public OrderPicking loadDeep(Long key) {
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
    public List<OrderPicking> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<OrderPicking> list = new ArrayList<OrderPicking>(count);
        
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
    
    protected List<OrderPicking> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<OrderPicking> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
