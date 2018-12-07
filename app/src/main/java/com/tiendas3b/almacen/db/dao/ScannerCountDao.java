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

import com.tiendas3b.almacen.db.dao.ScannerCount;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SCANNER_COUNT".
*/
public class ScannerCountDao extends AbstractDao<ScannerCount, Long> {

    public static final String TABLENAME = "SCANNER_COUNT";

    /**
     * Properties of entity ScannerCount.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Folio = new Property(1, Integer.class, "folio", false, "FOLIO");
        public final static Property Amount = new Property(2, Integer.class, "amount", false, "AMOUNT");
        public final static Property RegionId = new Property(3, long.class, "regionId", false, "REGION_ID");
        public final static Property VarticleId = new Property(4, long.class, "varticleId", false, "VARTICLE_ID");
        public final static Property Iclave = new Property(5, long.class, "iclave", false, "ICLAVE");
        public final static Property Date = new Property(6, String.class, "date", false, "DATE");
        public final static Property Seq = new Property(7, long.class, "seq", false, "SEQ");
    };

    private DaoSession daoSession;


    public ScannerCountDao(DaoConfig config) {
        super(config);
    }
    
    public ScannerCountDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SCANNER_COUNT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FOLIO\" INTEGER," + // 1: folio
                "\"AMOUNT\" INTEGER," + // 2: amount
                "\"REGION_ID\" INTEGER NOT NULL ," + // 3: regionId
                "\"VARTICLE_ID\" INTEGER NOT NULL ," + // 4: varticleId
                "\"ICLAVE\" INTEGER NOT NULL ," + // 5: iclave
                "\"DATE\" TEXT NOT NULL ," + // 6: date
                "\"SEQ\" INTEGER NOT NULL );"); // 7: seq
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_SCANNER_COUNT_REGION_ID_DATE_ICLAVE_SEQ ON SCANNER_COUNT" +
                " (\"REGION_ID\",\"DATE\",\"ICLAVE\",\"SEQ\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SCANNER_COUNT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ScannerCount entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer folio = entity.getFolio();
        if (folio != null) {
            stmt.bindLong(2, folio);
        }
 
        Integer amount = entity.getAmount();
        if (amount != null) {
            stmt.bindLong(3, amount);
        }
        stmt.bindLong(4, entity.getRegionId());
        stmt.bindLong(5, entity.getVarticleId());
        stmt.bindLong(6, entity.getIclave());
        stmt.bindString(7, entity.getDate());
        stmt.bindLong(8, entity.getSeq());
    }

    @Override
    protected void attachEntity(ScannerCount entity) {
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
    public ScannerCount readEntity(Cursor cursor, int offset) {
        ScannerCount entity = new ScannerCount( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // folio
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // amount
            cursor.getLong(offset + 3), // regionId
            cursor.getLong(offset + 4), // varticleId
            cursor.getLong(offset + 5), // iclave
            cursor.getString(offset + 6), // date
            cursor.getLong(offset + 7) // seq
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ScannerCount entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFolio(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setAmount(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setRegionId(cursor.getLong(offset + 3));
        entity.setVarticleId(cursor.getLong(offset + 4));
        entity.setIclave(cursor.getLong(offset + 5));
        entity.setDate(cursor.getString(offset + 6));
        entity.setSeq(cursor.getLong(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ScannerCount entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ScannerCount entity) {
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
            SqlUtils.appendColumns(builder, "T1", daoSession.getVArticleDao().getAllColumns());
            builder.append(" FROM SCANNER_COUNT T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN VARTICLE T1 ON T.\"VARTICLE_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ScannerCount loadCurrentDeep(Cursor cursor, boolean lock) {
        ScannerCount entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Region region = loadCurrentOther(daoSession.getRegionDao(), cursor, offset);
         if(region != null) {
            entity.setRegion(region);
        }
        offset += daoSession.getRegionDao().getAllColumns().length;

        VArticle varticle = loadCurrentOther(daoSession.getVArticleDao(), cursor, offset);
         if(varticle != null) {
            entity.setVarticle(varticle);
        }

        return entity;    
    }

    public ScannerCount loadDeep(Long key) {
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
    public List<ScannerCount> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ScannerCount> list = new ArrayList<ScannerCount>(count);
        
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
    
    protected List<ScannerCount> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ScannerCount> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
