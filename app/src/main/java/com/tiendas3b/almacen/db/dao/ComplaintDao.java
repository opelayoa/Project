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

import com.tiendas3b.almacen.db.dao.Complaint;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPLAINT".
*/
public class ComplaintDao extends AbstractDao<Complaint, Long> {

    public static final String TABLENAME = "COMPLAINT";

    /**
     * Properties of entity Complaint.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property QuantityOrdered = new Property(1, Integer.class, "quantityOrdered", false, "QUANTITY_ORDERED");
        public final static Property QuantitySm = new Property(2, Integer.class, "quantitySm", false, "QUANTITY_SM");
        public final static Property QuantityAccepted = new Property(3, Integer.class, "quantityAccepted", false, "QUANTITY_ACCEPTED");
        public final static Property ConditionId = new Property(4, Integer.class, "conditionId", false, "CONDITION_ID");
        public final static Property Obs = new Property(5, String.class, "obs", false, "OBS");
        public final static Property Action = new Property(6, String.class, "action", false, "ACTION");
        public final static Property ResponsibleArea = new Property(7, String.class, "responsibleArea", false, "RESPONSIBLE_AREA");
        public final static Property ResponsibleSubarea = new Property(8, String.class, "responsibleSubarea", false, "RESPONSIBLE_SUBAREA");
        public final static Property StoreObs = new Property(9, String.class, "storeObs", false, "STORE_OBS");
        public final static Property RegionId = new Property(10, long.class, "regionId", false, "REGION_ID");
        public final static Property StoreId = new Property(11, long.class, "storeId", false, "STORE_ID");
        public final static Property VarticleId = new Property(12, long.class, "varticleId", false, "VARTICLE_ID");
        public final static Property Date = new Property(13, String.class, "date", false, "DATE");
        public final static Property Iclave = new Property(14, long.class, "iclave", false, "ICLAVE");
        public final static Property Seq = new Property(15, long.class, "seq", false, "SEQ");
    };

    private DaoSession daoSession;


    public ComplaintDao(DaoConfig config) {
        super(config);
    }
    
    public ComplaintDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPLAINT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"QUANTITY_ORDERED\" INTEGER," + // 1: quantityOrdered
                "\"QUANTITY_SM\" INTEGER," + // 2: quantitySm
                "\"QUANTITY_ACCEPTED\" INTEGER," + // 3: quantityAccepted
                "\"CONDITION_ID\" INTEGER," + // 4: conditionId
                "\"OBS\" TEXT," + // 5: obs
                "\"ACTION\" TEXT," + // 6: action
                "\"RESPONSIBLE_AREA\" TEXT," + // 7: responsibleArea
                "\"RESPONSIBLE_SUBAREA\" TEXT," + // 8: responsibleSubarea
                "\"STORE_OBS\" TEXT," + // 9: storeObs
                "\"REGION_ID\" INTEGER NOT NULL ," + // 10: regionId
                "\"STORE_ID\" INTEGER NOT NULL ," + // 11: storeId
                "\"VARTICLE_ID\" INTEGER NOT NULL ," + // 12: varticleId
                "\"DATE\" TEXT NOT NULL ," + // 13: date
                "\"ICLAVE\" INTEGER NOT NULL ," + // 14: iclave
                "\"SEQ\" INTEGER NOT NULL );"); // 15: seq
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_COMPLAINT_REGION_ID_STORE_ID_DATE_ICLAVE_SEQ ON COMPLAINT" +
                " (\"REGION_ID\",\"STORE_ID\",\"DATE\",\"ICLAVE\",\"SEQ\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPLAINT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Complaint entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer quantityOrdered = entity.getQuantityOrdered();
        if (quantityOrdered != null) {
            stmt.bindLong(2, quantityOrdered);
        }
 
        Integer quantitySm = entity.getQuantitySm();
        if (quantitySm != null) {
            stmt.bindLong(3, quantitySm);
        }
 
        Integer quantityAccepted = entity.getQuantityAccepted();
        if (quantityAccepted != null) {
            stmt.bindLong(4, quantityAccepted);
        }
 
        Integer conditionId = entity.getConditionId();
        if (conditionId != null) {
            stmt.bindLong(5, conditionId);
        }
 
        String obs = entity.getObs();
        if (obs != null) {
            stmt.bindString(6, obs);
        }
 
        String action = entity.getAction();
        if (action != null) {
            stmt.bindString(7, action);
        }
 
        String responsibleArea = entity.getResponsibleArea();
        if (responsibleArea != null) {
            stmt.bindString(8, responsibleArea);
        }
 
        String responsibleSubarea = entity.getResponsibleSubarea();
        if (responsibleSubarea != null) {
            stmt.bindString(9, responsibleSubarea);
        }
 
        String storeObs = entity.getStoreObs();
        if (storeObs != null) {
            stmt.bindString(10, storeObs);
        }
        stmt.bindLong(11, entity.getRegionId());
        stmt.bindLong(12, entity.getStoreId());
        stmt.bindLong(13, entity.getVarticleId());
        stmt.bindString(14, entity.getDate());
        stmt.bindLong(15, entity.getIclave());
        stmt.bindLong(16, entity.getSeq());
    }

    @Override
    protected void attachEntity(Complaint entity) {
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
    public Complaint readEntity(Cursor cursor, int offset) {
        Complaint entity = new Complaint( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // quantityOrdered
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // quantitySm
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // quantityAccepted
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // conditionId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // obs
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // action
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // responsibleArea
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // responsibleSubarea
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // storeObs
            cursor.getLong(offset + 10), // regionId
            cursor.getLong(offset + 11), // storeId
            cursor.getLong(offset + 12), // varticleId
            cursor.getString(offset + 13), // date
            cursor.getLong(offset + 14), // iclave
            cursor.getLong(offset + 15) // seq
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Complaint entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setQuantityOrdered(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setQuantitySm(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setQuantityAccepted(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setConditionId(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setObs(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAction(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setResponsibleArea(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setResponsibleSubarea(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStoreObs(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setRegionId(cursor.getLong(offset + 10));
        entity.setStoreId(cursor.getLong(offset + 11));
        entity.setVarticleId(cursor.getLong(offset + 12));
        entity.setDate(cursor.getString(offset + 13));
        entity.setIclave(cursor.getLong(offset + 14));
        entity.setSeq(cursor.getLong(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Complaint entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Complaint entity) {
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
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getVArticleDao().getAllColumns());
            builder.append(" FROM COMPLAINT T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN STORE T1 ON T.\"STORE_ID\"=T1.\"_id\"");
            builder.append(" LEFT JOIN VARTICLE T2 ON T.\"VARTICLE_ID\"=T2.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Complaint loadCurrentDeep(Cursor cursor, boolean lock) {
        Complaint entity = loadCurrent(cursor, 0, lock);
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
        offset += daoSession.getStoreDao().getAllColumns().length;

        VArticle varticle = loadCurrentOther(daoSession.getVArticleDao(), cursor, offset);
         if(varticle != null) {
            entity.setVarticle(varticle);
        }

        return entity;    
    }

    public Complaint loadDeep(Long key) {
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
    public List<Complaint> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Complaint> list = new ArrayList<Complaint>(count);
        
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
    
    protected List<Complaint> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Complaint> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
