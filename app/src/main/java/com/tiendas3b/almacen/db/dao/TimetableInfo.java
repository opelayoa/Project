package com.tiendas3b.almacen.db.dao;

import java.util.List;
import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "TIMETABLE_INFO".
 */
public class TimetableInfo extends EqualsBase  {

    private Long id;
    private long regionId;
    /** Not-null value. */
    private String dateTime;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TimetableInfoDao myDao;

    private Region region;
    private Long region__resolvedKey;

    private List<Utilization> utilizations;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public TimetableInfo() {
    }

    public TimetableInfo(Long id) {
        this.id = id;
    }

    public TimetableInfo(Long id, long regionId, String dateTime) {
        this.id = id;
        this.regionId = regionId;
        this.dateTime = dateTime;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTimetableInfoDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    /** Not-null value. */
    public String getDateTime() {
        return dateTime;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /** To-one relationship, resolved on first access. */
    public Region getRegion() {
        long __key = this.regionId;
        if (region__resolvedKey == null || !region__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RegionDao targetDao = daoSession.getRegionDao();
            Region regionNew = targetDao.load(__key);
            synchronized (this) {
                region = regionNew;
            	region__resolvedKey = __key;
            }
        }
        return region;
    }

    public void setRegion(Region region) {
        if (region == null) {
            throw new DaoException("To-one property 'regionId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.region = region;
            regionId = region.getId();
            region__resolvedKey = regionId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Utilization> getUtilizations() {
        if (utilizations == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UtilizationDao targetDao = daoSession.getUtilizationDao();
            List<Utilization> utilizationsNew = targetDao._queryTimetableInfo_Utilizations(id);
            synchronized (this) {
                if(utilizations == null) {
                    utilizations = utilizationsNew;
                }
            }
        }
        return utilizations;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetUtilizations() {
        utilizations = null;
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
