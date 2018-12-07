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

import com.tiendas3b.almacen.db.dao.PendingDocument;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PENDING_DOCUMENT".
*/
public class PendingDocumentDao extends AbstractDao<PendingDocument, Long> {

    public static final String TABLENAME = "PENDING_DOCUMENT";

    /**
     * Properties of entity PendingDocument.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Ta = new Property(1, Integer.class, "ta", false, "TA");
        public final static Property TaPieces = new Property(2, Integer.class, "taPieces", false, "TA_PIECES");
        public final static Property Rt = new Property(3, Integer.class, "rt", false, "RT");
        public final static Property RtPieces = new Property(4, Integer.class, "rtPieces", false, "RT_PIECES");
        public final static Property TaStore = new Property(5, Integer.class, "taStore", false, "TA_STORE");
        public final static Property RtStore = new Property(6, Integer.class, "rtStore", false, "RT_STORE");
        public final static Property RegionId = new Property(7, long.class, "regionId", false, "REGION_ID");
        public final static Property Date = new Property(8, String.class, "date", false, "DATE");
    };

    private DaoSession daoSession;


    public PendingDocumentDao(DaoConfig config) {
        super(config);
    }
    
    public PendingDocumentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PENDING_DOCUMENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TA\" INTEGER," + // 1: ta
                "\"TA_PIECES\" INTEGER," + // 2: taPieces
                "\"RT\" INTEGER," + // 3: rt
                "\"RT_PIECES\" INTEGER," + // 4: rtPieces
                "\"TA_STORE\" INTEGER," + // 5: taStore
                "\"RT_STORE\" INTEGER," + // 6: rtStore
                "\"REGION_ID\" INTEGER NOT NULL ," + // 7: regionId
                "\"DATE\" TEXT NOT NULL );"); // 8: date
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_PENDING_DOCUMENT_REGION_ID_DATE ON PENDING_DOCUMENT" +
                " (\"REGION_ID\",\"DATE\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PENDING_DOCUMENT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PendingDocument entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer ta = entity.getTa();
        if (ta != null) {
            stmt.bindLong(2, ta);
        }
 
        Integer taPieces = entity.getTaPieces();
        if (taPieces != null) {
            stmt.bindLong(3, taPieces);
        }
 
        Integer rt = entity.getRt();
        if (rt != null) {
            stmt.bindLong(4, rt);
        }
 
        Integer rtPieces = entity.getRtPieces();
        if (rtPieces != null) {
            stmt.bindLong(5, rtPieces);
        }
 
        Integer taStore = entity.getTaStore();
        if (taStore != null) {
            stmt.bindLong(6, taStore);
        }
 
        Integer rtStore = entity.getRtStore();
        if (rtStore != null) {
            stmt.bindLong(7, rtStore);
        }
        stmt.bindLong(8, entity.getRegionId());
        stmt.bindString(9, entity.getDate());
    }

    @Override
    protected void attachEntity(PendingDocument entity) {
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
    public PendingDocument readEntity(Cursor cursor, int offset) {
        PendingDocument entity = new PendingDocument( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // ta
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // taPieces
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // rt
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // rtPieces
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // taStore
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // rtStore
            cursor.getLong(offset + 7), // regionId
            cursor.getString(offset + 8) // date
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PendingDocument entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTa(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setTaPieces(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setRt(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setRtPieces(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setTaStore(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setRtStore(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setRegionId(cursor.getLong(offset + 7));
        entity.setDate(cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PendingDocument entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PendingDocument entity) {
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
            builder.append(" FROM PENDING_DOCUMENT T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PendingDocument loadCurrentDeep(Cursor cursor, boolean lock) {
        PendingDocument entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Region region = loadCurrentOther(daoSession.getRegionDao(), cursor, offset);
         if(region != null) {
            entity.setRegion(region);
        }

        return entity;    
    }

    public PendingDocument loadDeep(Long key) {
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
    public List<PendingDocument> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PendingDocument> list = new ArrayList<PendingDocument>(count);
        
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
    
    protected List<PendingDocument> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PendingDocument> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
