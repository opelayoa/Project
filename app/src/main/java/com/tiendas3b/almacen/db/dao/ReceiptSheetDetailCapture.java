package com.tiendas3b.almacen.db.dao;

import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "RECEIPT_SHEET_DETAIL_CAPTURE".
 */
public class ReceiptSheetDetailCapture extends EqualsBase  {

    private Long id;
    private Integer billAmount;
    private Integer receivedAmount;
    private Integer paletizado;
    private Integer paletizadoCount;
    private Integer sample;
    private Integer packing;
    private String expiryDate;
    private String lote;
    private String comments;
    private long buyDetailId;
    private long checkId;
    private long rejectionReasonId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ReceiptSheetDetailCaptureDao myDao;

    private BuyDetail buyDetail;
    private Long buyDetail__resolvedKey;

    private Check check;
    private Long check__resolvedKey;

    private ComplaintCat rejectionReason;
    private Long rejectionReason__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ReceiptSheetDetailCapture() {
    }

    public ReceiptSheetDetailCapture(Long id) {
        this.id = id;
    }

    public ReceiptSheetDetailCapture(Long id, Integer billAmount, Integer receivedAmount, Integer paletizado, Integer paletizadoCount, Integer sample, Integer packing, String expiryDate, String lote, String comments, long buyDetailId, long checkId, long rejectionReasonId) {
        this.id = id;
        this.billAmount = billAmount;
        this.receivedAmount = receivedAmount;
        this.paletizado = paletizado;
        this.paletizadoCount = paletizadoCount;
        this.sample = sample;
        this.packing = packing;
        this.expiryDate = expiryDate;
        this.lote = lote;
        this.comments = comments;
        this.buyDetailId = buyDetailId;
        this.checkId = checkId;
        this.rejectionReasonId = rejectionReasonId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReceiptSheetDetailCaptureDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Integer billAmount) {
        this.billAmount = billAmount;
    }

    public Integer getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(Integer receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public Integer getPaletizado() {
        return paletizado;
    }

    public void setPaletizado(Integer paletizado) {
        this.paletizado = paletizado;
    }

    public Integer getPaletizadoCount() {
        return paletizadoCount;
    }

    public void setPaletizadoCount(Integer paletizadoCount) {
        this.paletizadoCount = paletizadoCount;
    }

    public Integer getSample() {
        return sample;
    }

    public void setSample(Integer sample) {
        this.sample = sample;
    }

    public Integer getPacking() {
        return packing;
    }

    public void setPacking(Integer packing) {
        this.packing = packing;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getBuyDetailId() {
        return buyDetailId;
    }

    public void setBuyDetailId(long buyDetailId) {
        this.buyDetailId = buyDetailId;
    }

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
    }

    public long getRejectionReasonId() {
        return rejectionReasonId;
    }

    public void setRejectionReasonId(long rejectionReasonId) {
        this.rejectionReasonId = rejectionReasonId;
    }

    /** To-one relationship, resolved on first access. */
    public BuyDetail getBuyDetail() {
        long __key = this.buyDetailId;
        if (buyDetail__resolvedKey == null || !buyDetail__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BuyDetailDao targetDao = daoSession.getBuyDetailDao();
            BuyDetail buyDetailNew = targetDao.load(__key);
            synchronized (this) {
                buyDetail = buyDetailNew;
            	buyDetail__resolvedKey = __key;
            }
        }
        return buyDetail;
    }

    public void setBuyDetail(BuyDetail buyDetail) {
        if (buyDetail == null) {
            throw new DaoException("To-one property 'buyDetailId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.buyDetail = buyDetail;
            buyDetailId = buyDetail.getId();
            buyDetail__resolvedKey = buyDetailId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Check getCheck() {
        long __key = this.checkId;
        if (check__resolvedKey == null || !check__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CheckDao targetDao = daoSession.getCheckDao();
            Check checkNew = targetDao.load(__key);
            synchronized (this) {
                check = checkNew;
            	check__resolvedKey = __key;
            }
        }
        return check;
    }

    public void setCheck(Check check) {
        if (check == null) {
            throw new DaoException("To-one property 'checkId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.check = check;
            checkId = check.getId();
            check__resolvedKey = checkId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public ComplaintCat getRejectionReason() {
        long __key = this.rejectionReasonId;
        if (rejectionReason__resolvedKey == null || !rejectionReason__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ComplaintCatDao targetDao = daoSession.getComplaintCatDao();
            ComplaintCat rejectionReasonNew = targetDao.load(__key);
            synchronized (this) {
                rejectionReason = rejectionReasonNew;
            	rejectionReason__resolvedKey = __key;
            }
        }
        return rejectionReason;
    }

    public void setRejectionReason(ComplaintCat rejectionReason) {
        if (rejectionReason == null) {
            throw new DaoException("To-one property 'rejectionReasonId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.rejectionReason = rejectionReason;
            rejectionReasonId = rejectionReason.getId();
            rejectionReason__resolvedKey = rejectionReasonId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
