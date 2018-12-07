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

import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECEIPT_SHEET_DETAIL_CAPTURE".
*/
public class ReceiptSheetDetailCaptureDao extends AbstractDao<ReceiptSheetDetailCapture, Long> {

    public static final String TABLENAME = "RECEIPT_SHEET_DETAIL_CAPTURE";

    /**
     * Properties of entity ReceiptSheetDetailCapture.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BillAmount = new Property(1, Integer.class, "billAmount", false, "BILL_AMOUNT");
        public final static Property ReceivedAmount = new Property(2, Integer.class, "receivedAmount", false, "RECEIVED_AMOUNT");
        public final static Property Paletizado = new Property(3, Integer.class, "paletizado", false, "PALETIZADO");
        public final static Property PaletizadoCount = new Property(4, Integer.class, "paletizadoCount", false, "PALETIZADO_COUNT");
        public final static Property Sample = new Property(5, Integer.class, "sample", false, "SAMPLE");
        public final static Property Packing = new Property(6, Integer.class, "packing", false, "PACKING");
        public final static Property ExpiryDate = new Property(7, String.class, "expiryDate", false, "EXPIRY_DATE");
        public final static Property Lote = new Property(8, String.class, "lote", false, "LOTE");
        public final static Property Comments = new Property(9, String.class, "comments", false, "COMMENTS");
        public final static Property BuyDetailId = new Property(10, long.class, "buyDetailId", false, "BUY_DETAIL_ID");
        public final static Property CheckId = new Property(11, long.class, "checkId", false, "CHECK_ID");
        public final static Property RejectionReasonId = new Property(12, long.class, "rejectionReasonId", false, "REJECTION_REASON_ID");
    };

    private DaoSession daoSession;


    public ReceiptSheetDetailCaptureDao(DaoConfig config) {
        super(config);
    }
    
    public ReceiptSheetDetailCaptureDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECEIPT_SHEET_DETAIL_CAPTURE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BILL_AMOUNT\" INTEGER," + // 1: billAmount
                "\"RECEIVED_AMOUNT\" INTEGER," + // 2: receivedAmount
                "\"PALETIZADO\" INTEGER," + // 3: paletizado
                "\"PALETIZADO_COUNT\" INTEGER," + // 4: paletizadoCount
                "\"SAMPLE\" INTEGER," + // 5: sample
                "\"PACKING\" INTEGER," + // 6: packing
                "\"EXPIRY_DATE\" TEXT," + // 7: expiryDate
                "\"LOTE\" TEXT," + // 8: lote
                "\"COMMENTS\" TEXT," + // 9: comments
                "\"BUY_DETAIL_ID\" INTEGER NOT NULL ," + // 10: buyDetailId
                "\"CHECK_ID\" INTEGER NOT NULL ," + // 11: checkId
                "\"REJECTION_REASON_ID\" INTEGER NOT NULL );"); // 12: rejectionReasonId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECEIPT_SHEET_DETAIL_CAPTURE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ReceiptSheetDetailCapture entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer billAmount = entity.getBillAmount();
        if (billAmount != null) {
            stmt.bindLong(2, billAmount);
        }
 
        Integer receivedAmount = entity.getReceivedAmount();
        if (receivedAmount != null) {
            stmt.bindLong(3, receivedAmount);
        }
 
        Integer paletizado = entity.getPaletizado();
        if (paletizado != null) {
            stmt.bindLong(4, paletizado);
        }
 
        Integer paletizadoCount = entity.getPaletizadoCount();
        if (paletizadoCount != null) {
            stmt.bindLong(5, paletizadoCount);
        }
 
        Integer sample = entity.getSample();
        if (sample != null) {
            stmt.bindLong(6, sample);
        }
 
        Integer packing = entity.getPacking();
        if (packing != null) {
            stmt.bindLong(7, packing);
        }
 
        String expiryDate = entity.getExpiryDate();
        if (expiryDate != null) {
            stmt.bindString(8, expiryDate);
        }
 
        String lote = entity.getLote();
        if (lote != null) {
            stmt.bindString(9, lote);
        }
 
        String comments = entity.getComments();
        if (comments != null) {
            stmt.bindString(10, comments);
        }
        stmt.bindLong(11, entity.getBuyDetailId());
        stmt.bindLong(12, entity.getCheckId());
        stmt.bindLong(13, entity.getRejectionReasonId());
    }

    @Override
    protected void attachEntity(ReceiptSheetDetailCapture entity) {
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
    public ReceiptSheetDetailCapture readEntity(Cursor cursor, int offset) {
        ReceiptSheetDetailCapture entity = new ReceiptSheetDetailCapture( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // billAmount
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // receivedAmount
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // paletizado
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // paletizadoCount
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // sample
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // packing
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // expiryDate
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // lote
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // comments
            cursor.getLong(offset + 10), // buyDetailId
            cursor.getLong(offset + 11), // checkId
            cursor.getLong(offset + 12) // rejectionReasonId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ReceiptSheetDetailCapture entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBillAmount(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setReceivedAmount(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setPaletizado(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setPaletizadoCount(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setSample(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setPacking(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setExpiryDate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLote(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setComments(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setBuyDetailId(cursor.getLong(offset + 10));
        entity.setCheckId(cursor.getLong(offset + 11));
        entity.setRejectionReasonId(cursor.getLong(offset + 12));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ReceiptSheetDetailCapture entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ReceiptSheetDetailCapture entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getBuyDetailDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getCheckDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getComplaintCatDao().getAllColumns());
            builder.append(" FROM RECEIPT_SHEET_DETAIL_CAPTURE T");
            builder.append(" LEFT JOIN BUY_DETAIL T0 ON T.\"BUY_DETAIL_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN CHECK T1 ON T.\"CHECK_ID\"=T1.\"_id\"");
            builder.append(" LEFT JOIN COMPLAINT_CAT T2 ON T.\"REJECTION_REASON_ID\"=T2.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ReceiptSheetDetailCapture loadCurrentDeep(Cursor cursor, boolean lock) {
        ReceiptSheetDetailCapture entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        BuyDetail buyDetail = loadCurrentOther(daoSession.getBuyDetailDao(), cursor, offset);
         if(buyDetail != null) {
            entity.setBuyDetail(buyDetail);
        }
        offset += daoSession.getBuyDetailDao().getAllColumns().length;

        Check check = loadCurrentOther(daoSession.getCheckDao(), cursor, offset);
         if(check != null) {
            entity.setCheck(check);
        }
        offset += daoSession.getCheckDao().getAllColumns().length;

        ComplaintCat rejectionReason = loadCurrentOther(daoSession.getComplaintCatDao(), cursor, offset);
         if(rejectionReason != null) {
            entity.setRejectionReason(rejectionReason);
        }

        return entity;    
    }

    public ReceiptSheetDetailCapture loadDeep(Long key) {
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
    public List<ReceiptSheetDetailCapture> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ReceiptSheetDetailCapture> list = new ArrayList<ReceiptSheetDetailCapture>(count);
        
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
    
    protected List<ReceiptSheetDetailCapture> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ReceiptSheetDetailCapture> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}