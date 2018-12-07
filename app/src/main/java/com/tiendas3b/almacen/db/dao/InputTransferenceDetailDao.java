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

import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "INPUT_TRANSFERENCE_DETAIL".
*/
public class InputTransferenceDetailDao extends AbstractDao<InputTransferenceDetail, Long> {

    public static final String TABLENAME = "INPUT_TRANSFERENCE_DETAIL";

    /**
     * Properties of entity InputTransferenceDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Iclave = new Property(1, Long.class, "iclave", false, "ICLAVE");
        public final static Property AmountSmn = new Property(2, Integer.class, "amountSmn", false, "AMOUNT_SMN");
        public final static Property InputTransferenceId = new Property(3, long.class, "inputTransferenceId", false, "INPUT_TRANSFERENCE_ID");
    };

    private DaoSession daoSession;


    public InputTransferenceDetailDao(DaoConfig config) {
        super(config);
    }
    
    public InputTransferenceDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"INPUT_TRANSFERENCE_DETAIL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ICLAVE\" INTEGER," + // 1: iclave
                "\"AMOUNT_SMN\" INTEGER," + // 2: amountSmn
                "\"INPUT_TRANSFERENCE_ID\" INTEGER NOT NULL );"); // 3: inputTransferenceId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"INPUT_TRANSFERENCE_DETAIL\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, InputTransferenceDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long iclave = entity.getIclave();
        if (iclave != null) {
            stmt.bindLong(2, iclave);
        }
 
        Integer amountSmn = entity.getAmountSmn();
        if (amountSmn != null) {
            stmt.bindLong(3, amountSmn);
        }
        stmt.bindLong(4, entity.getInputTransferenceId());
    }

    @Override
    protected void attachEntity(InputTransferenceDetail entity) {
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
    public InputTransferenceDetail readEntity(Cursor cursor, int offset) {
        InputTransferenceDetail entity = new InputTransferenceDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // iclave
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // amountSmn
            cursor.getLong(offset + 3) // inputTransferenceId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, InputTransferenceDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIclave(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setAmountSmn(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setInputTransferenceId(cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(InputTransferenceDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(InputTransferenceDetail entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getInputTransferenceDao().getAllColumns());
            builder.append(" FROM INPUT_TRANSFERENCE_DETAIL T");
            builder.append(" LEFT JOIN INPUT_TRANSFERENCE T0 ON T.\"INPUT_TRANSFERENCE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected InputTransferenceDetail loadCurrentDeep(Cursor cursor, boolean lock) {
        InputTransferenceDetail entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        InputTransference inputTransference = loadCurrentOther(daoSession.getInputTransferenceDao(), cursor, offset);
         if(inputTransference != null) {
            entity.setInputTransference(inputTransference);
        }

        return entity;    
    }

    public InputTransferenceDetail loadDeep(Long key) {
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
    public List<InputTransferenceDetail> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<InputTransferenceDetail> list = new ArrayList<InputTransferenceDetail>(count);
        
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
    
    protected List<InputTransferenceDetail> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<InputTransferenceDetail> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
