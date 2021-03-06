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

import com.tiendas3b.almacen.db.dao.InputTransference;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "INPUT_TRANSFERENCE".
*/
public class InputTransferenceDao extends AbstractDao<InputTransference, Long> {

    public static final String TABLENAME = "INPUT_TRANSFERENCE";

    /**
     * Properties of entity InputTransference.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Date = new Property(1, java.util.Date.class, "date", false, "DATE");
        public final static Property Type = new Property(2, short.class, "type", false, "TYPE");
        public final static Property Folio = new Property(3, int.class, "folio", false, "FOLIO");
        public final static Property RegionId = new Property(4, long.class, "regionId", false, "REGION_ID");
        public final static Property StoreId = new Property(5, long.class, "storeId", false, "STORE_ID");
    };

    private DaoSession daoSession;


    public InputTransferenceDao(DaoConfig config) {
        super(config);
    }
    
    public InputTransferenceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"INPUT_TRANSFERENCE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DATE\" INTEGER," + // 1: date
                "\"TYPE\" INTEGER NOT NULL ," + // 2: type
                "\"FOLIO\" INTEGER NOT NULL ," + // 3: folio
                "\"REGION_ID\" INTEGER NOT NULL ," + // 4: regionId
                "\"STORE_ID\" INTEGER NOT NULL );"); // 5: storeId
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_INPUT_TRANSFERENCE_REGION_ID_TYPE_FOLIO_STORE_ID ON INPUT_TRANSFERENCE" +
                " (\"REGION_ID\",\"TYPE\",\"FOLIO\",\"STORE_ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"INPUT_TRANSFERENCE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, InputTransference entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(2, date.getTime());
        }
        stmt.bindLong(3, entity.getType());
        stmt.bindLong(4, entity.getFolio());
        stmt.bindLong(5, entity.getRegionId());
        stmt.bindLong(6, entity.getStoreId());
    }

    @Override
    protected void attachEntity(InputTransference entity) {
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
    public InputTransference readEntity(Cursor cursor, int offset) {
        InputTransference entity = new InputTransference( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // date
            cursor.getShort(offset + 2), // type
            cursor.getInt(offset + 3), // folio
            cursor.getLong(offset + 4), // regionId
            cursor.getLong(offset + 5) // storeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, InputTransference entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setType(cursor.getShort(offset + 2));
        entity.setFolio(cursor.getInt(offset + 3));
        entity.setRegionId(cursor.getLong(offset + 4));
        entity.setStoreId(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(InputTransference entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(InputTransference entity) {
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
            builder.append(" FROM INPUT_TRANSFERENCE T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN STORE T1 ON T.\"STORE_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected InputTransference loadCurrentDeep(Cursor cursor, boolean lock) {
        InputTransference entity = loadCurrent(cursor, 0, lock);
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

    public InputTransference loadDeep(Long key) {
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
    public List<InputTransference> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<InputTransference> list = new ArrayList<InputTransference>(count);
        
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
    
    protected List<InputTransference> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<InputTransference> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
