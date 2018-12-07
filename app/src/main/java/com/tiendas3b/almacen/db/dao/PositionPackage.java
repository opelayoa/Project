package com.tiendas3b.almacen.db.dao;

import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "POSITION_PACKAGE".
 */
public class PositionPackage extends EqualsBase  {

    private Long id;
    private Boolean status;
    private long positionId;
    private long packageId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PositionPackageDao myDao;

    private Position position;
    private Long position__resolvedKey;

    private EPackage epackage;
    private Long epackage__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public PositionPackage() {
    }

    public PositionPackage(Long id) {
        this.id = id;
    }

    public PositionPackage(Long id, Boolean status, long positionId, long packageId) {
        this.id = id;
        this.status = status;
        this.positionId = positionId;
        this.packageId = packageId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPositionPackageDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    /** To-one relationship, resolved on first access. */
    public Position getPosition() {
        long __key = this.positionId;
        if (position__resolvedKey == null || !position__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PositionDao targetDao = daoSession.getPositionDao();
            Position positionNew = targetDao.load(__key);
            synchronized (this) {
                position = positionNew;
            	position__resolvedKey = __key;
            }
        }
        return position;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new DaoException("To-one property 'positionId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.position = position;
            positionId = position.getId();
            position__resolvedKey = positionId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public EPackage getEpackage() {
        long __key = this.packageId;
        if (epackage__resolvedKey == null || !epackage__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EPackageDao targetDao = daoSession.getEPackageDao();
            EPackage epackageNew = targetDao.load(__key);
            synchronized (this) {
                epackage = epackageNew;
            	epackage__resolvedKey = __key;
            }
        }
        return epackage;
    }

    public void setEpackage(EPackage epackage) {
        if (epackage == null) {
            throw new DaoException("To-one property 'packageId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.epackage = epackage;
            packageId = epackage.getId();
            epackage__resolvedKey = packageId;
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