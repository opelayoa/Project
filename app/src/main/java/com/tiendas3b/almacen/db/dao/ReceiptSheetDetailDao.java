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

import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECEIPT_SHEET_DETAIL".
*/
public class ReceiptSheetDetailDao extends AbstractDao<ReceiptSheetDetail, Long> {

    public static final String TABLENAME = "RECEIPT_SHEET_DETAIL";

    /**
     * Properties of entity ReceiptSheetDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Iclave = new Property(1, Long.class, "iclave", false, "ICLAVE");
        public final static Property Expiration = new Property(2, String.class, "expiration", false, "EXPIRATION");
        public final static Property FillRate = new Property(3, String.class, "fillRate", false, "FILL_RATE");
        public final static Property Description = new Property(4, String.class, "description", false, "DESCRIPTION");
        public final static Property NumBoxesOdc = new Property(5, Integer.class, "numBoxesOdc", false, "NUM_BOXES_ODC");
        public final static Property NumBoxesEm = new Property(6, Integer.class, "numBoxesEm", false, "NUM_BOXES_EM");
        public final static Property ReceiptSheetId = new Property(7, long.class, "receiptSheetId", false, "RECEIPT_SHEET_ID");
        public final static Property TimetableReceiptId = new Property(8, long.class, "timetableReceiptId", false, "TIMETABLE_RECEIPT_ID");
    };

    private DaoSession daoSession;


    public ReceiptSheetDetailDao(DaoConfig config) {
        super(config);
    }
    
    public ReceiptSheetDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECEIPT_SHEET_DETAIL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ICLAVE\" INTEGER," + // 1: iclave
                "\"EXPIRATION\" TEXT," + // 2: expiration
                "\"FILL_RATE\" TEXT," + // 3: fillRate
                "\"DESCRIPTION\" TEXT," + // 4: description
                "\"NUM_BOXES_ODC\" INTEGER," + // 5: numBoxesOdc
                "\"NUM_BOXES_EM\" INTEGER," + // 6: numBoxesEm
                "\"RECEIPT_SHEET_ID\" INTEGER NOT NULL ," + // 7: receiptSheetId
                "\"TIMETABLE_RECEIPT_ID\" INTEGER NOT NULL );"); // 8: timetableReceiptId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECEIPT_SHEET_DETAIL\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ReceiptSheetDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long iclave = entity.getIclave();
        if (iclave != null) {
            stmt.bindLong(2, iclave);
        }
 
        String expiration = entity.getExpiration();
        if (expiration != null) {
            stmt.bindString(3, expiration);
        }
 
        String fillRate = entity.getFillRate();
        if (fillRate != null) {
            stmt.bindString(4, fillRate);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(5, description);
        }
 
        Integer numBoxesOdc = entity.getNumBoxesOdc();
        if (numBoxesOdc != null) {
            stmt.bindLong(6, numBoxesOdc);
        }
 
        Integer numBoxesEm = entity.getNumBoxesEm();
        if (numBoxesEm != null) {
            stmt.bindLong(7, numBoxesEm);
        }
        stmt.bindLong(8, entity.getReceiptSheetId());
        stmt.bindLong(9, entity.getTimetableReceiptId());
    }

    @Override
    protected void attachEntity(ReceiptSheetDetail entity) {
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
    public ReceiptSheetDetail readEntity(Cursor cursor, int offset) {
        ReceiptSheetDetail entity = new ReceiptSheetDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // iclave
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // expiration
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // fillRate
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // description
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // numBoxesOdc
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // numBoxesEm
            cursor.getLong(offset + 7), // receiptSheetId
            cursor.getLong(offset + 8) // timetableReceiptId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ReceiptSheetDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIclave(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setExpiration(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFillRate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDescription(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNumBoxesOdc(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setNumBoxesEm(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setReceiptSheetId(cursor.getLong(offset + 7));
        entity.setTimetableReceiptId(cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ReceiptSheetDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ReceiptSheetDetail entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getReceiptSheetDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getTimetableReceiptDao().getAllColumns());
            builder.append(" FROM RECEIPT_SHEET_DETAIL T");
            builder.append(" LEFT JOIN RECEIPT_SHEET T0 ON T.\"RECEIPT_SHEET_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN TIMETABLE_RECEIPT T1 ON T.\"TIMETABLE_RECEIPT_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ReceiptSheetDetail loadCurrentDeep(Cursor cursor, boolean lock) {
        ReceiptSheetDetail entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        ReceiptSheet receiptSheet = loadCurrentOther(daoSession.getReceiptSheetDao(), cursor, offset);
         if(receiptSheet != null) {
            entity.setReceiptSheet(receiptSheet);
        }
        offset += daoSession.getReceiptSheetDao().getAllColumns().length;

        TimetableReceipt timetableReceipt = loadCurrentOther(daoSession.getTimetableReceiptDao(), cursor, offset);
         if(timetableReceipt != null) {
            entity.setTimetableReceipt(timetableReceipt);
        }

        return entity;    
    }

    public ReceiptSheetDetail loadDeep(Long key) {
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
    public List<ReceiptSheetDetail> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ReceiptSheetDetail> list = new ArrayList<ReceiptSheetDetail>(count);
        
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
    
    protected List<ReceiptSheetDetail> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ReceiptSheetDetail> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
