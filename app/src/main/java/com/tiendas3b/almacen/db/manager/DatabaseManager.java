package com.tiendas3b.almacen.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tiendas3b.almacen.db.dao.*;
import com.tiendas3b.almacen.db.dao.Activity;
import com.tiendas3b.almacen.db.dao.ActivityDao;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.AreaDao;
import com.tiendas3b.almacen.db.dao.ArticleDecreaseDao;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.db.dao.ArticleResearchDao;
import com.tiendas3b.almacen.db.dao.ArticleTransferenceDao;
import com.tiendas3b.almacen.db.dao.Barcode;
import com.tiendas3b.almacen.db.dao.BarcodeDao;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDao;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.BuyDetailDao;
import com.tiendas3b.almacen.db.dao.Complaint;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.ComplaintCatDao;
import com.tiendas3b.almacen.db.dao.ComplaintDao;
import com.tiendas3b.almacen.db.dao.DaoMaster;
import com.tiendas3b.almacen.db.dao.DaoSession;
import com.tiendas3b.almacen.db.dao.Driver;
import com.tiendas3b.almacen.db.dao.DriverDao;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EPackageDao;
import com.tiendas3b.almacen.db.dao.ExpressArticle;
import com.tiendas3b.almacen.db.dao.ExpressArticleDao;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.ExpressProviderDao;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.ExpressReceiptDao;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.FormDao;
import com.tiendas3b.almacen.db.dao.GlobalDecrease;
import com.tiendas3b.almacen.db.dao.GlobalDecreaseDao;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.InputTransferenceDao;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetailDao;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.ObservationTypeDao;
import com.tiendas3b.almacen.db.dao.OrderDetail;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.db.dao.OrderDetailCaptureDao;
import com.tiendas3b.almacen.db.dao.OrderDetailDao;
import com.tiendas3b.almacen.db.dao.OrderPicking;
import com.tiendas3b.almacen.db.dao.OrderPickingCapture;
import com.tiendas3b.almacen.db.dao.OrderPickingCaptureDao;
import com.tiendas3b.almacen.db.dao.OrderPickingDao;
import com.tiendas3b.almacen.db.dao.OrderType;
import com.tiendas3b.almacen.db.dao.OrderTypeDao;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionDao;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.PositionPackageDao;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.ProviderDao;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.ProviderEpackageDao;
import com.tiendas3b.almacen.db.dao.Rack;
import com.tiendas3b.almacen.db.dao.RackDao;
import com.tiendas3b.almacen.db.dao.RackLevel;
import com.tiendas3b.almacen.db.dao.RackLevelDao;
import com.tiendas3b.almacen.db.dao.ReceiptExpressLog;
import com.tiendas3b.almacen.db.dao.ReceiptExpressLogDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheet;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCaptureDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailDao;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.RoadmapDao;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;
import com.tiendas3b.almacen.db.dao.RoadmapDetailDao;
import com.tiendas3b.almacen.db.dao.ScannerCount;
import com.tiendas3b.almacen.db.dao.ScannerCountDao;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.ShipmentControlDao;
import com.tiendas3b.almacen.db.dao.SpinnerBase;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.StoreDao;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.dao.StorePickingStatusDao;
import com.tiendas3b.almacen.db.dao.TimetableInfo;
import com.tiendas3b.almacen.db.dao.TimetableInfoDao;
import com.tiendas3b.almacen.db.dao.TimetablePicking;
import com.tiendas3b.almacen.db.dao.TimetablePickingDao;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.TimetableReceiptDao;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDao;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.dao.TripDetailDao;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.dao.TruckDao;
import com.tiendas3b.almacen.db.dao.User;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.dao.VArticleDao;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.Join;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * @author dfa
 */
@SuppressWarnings("unchecked")
public class DatabaseManager implements IDatabaseManager, AsyncOperationListener {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static final String PASSWORD = "unPasswordMamalonXD";
    private static DatabaseManager instance;
    protected DaoMaster.DevOpenHelper mHelper;
    //    protected DaoMaster.OpenHelper mHelper;
    //    protected DaoMaster.EncryptedDevOpenHelper mHelper;
    protected SQLiteDatabase database;
    //    protected Database database;
    protected DaoMaster daoMaster;
    protected DaoSession daoSession;
    protected AsyncSession asyncSession;
    protected List<AsyncOperation> completedOperations;

    /**
     * Constructs a new DatabaseManager with the specified arguments.
     *
     * @param context The Android {@link Context}.
     */
    public DatabaseManager(final Context context) {
        mHelper = new DaoMaster.DevOpenHelper(context, "database", null);
//        mHelper = new DaoMaster.OpenHelper(context, "database", null) {
//            @Override
//            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//                Log.i(TAG, "onUpgrade");
//            }
//        };
//        mHelper = new DaoMaster.EncryptedDevOpenHelper(context, "database");
        completedOperations = new CopyOnWriteArrayList<>();
        instance = this;
    }

    /**
     * @param context The Android {@link Context}.
     * @return this.instance
     */
    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    private void assertWaitForCompletion1Sec() {
        asyncSession.waitForCompletion(1000);
        asyncSession.isCompleted();
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
//        database = mHelper.getReadableDatabase(PASSWORD);
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
//        database = mHelper.getWritableDatabase(PASSWORD);
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    @Override
    public void closeDbConnections() {
        closeDbConnectionsWithoutInstance();
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public void closeDbConnectionsWithoutInstance() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {//TODO comentar isOpen para db cifrada
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    @Override
    public synchronized void dropDatabase() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(User.class);    // clear all elements from a table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> long insert(T obj) {
        long id = -1;
        Class<?> clazz = obj.getClass();
        try {
            openWritableDb();
            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            id = dao.insert(obj);
            Log.d(TAG, "Insert " + clazz);
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
            daoSession.clear();
        }
        return id;
    }

    @Override
    public <T> void insertOrReplaceInTx(T[] array) {
        try {
//            if (array != null) {
            openWritableDb();
            Class<?> clazz = array.getClass().getComponentType();
            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            dao.insertOrReplaceInTx(array);
            daoSession.clear();
            Log.d(TAG, "Inserted or replaced " + clazz);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void insertOrReplaceInTx(List<T> list) {
        try {
//            if (array != null) {
            openWritableDb();
            Class<?> clazz = list.get(0).getClass();//.getComponentType();
            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            dao.insertOrReplaceInTx(list);
            daoSession.clear();
            Log.d(TAG, "Inserted or replaced " + clazz);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void update(T obj) {
        try {
            openWritableDb();
            Class<?> clazz = obj.getClass();
            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            dao.update(obj);
            daoSession.clear();
            Log.d(TAG, "Update " + clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param obj
     * @param <T>
     * @return id
     */
    @Override
    public <T> long insertOrUpdate(T obj) {
        long id = -1;
        Class<?> clazz = obj.getClass();
        try {
            openWritableDb();
            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            dao.update(obj);
            Log.d(TAG, "Update " + clazz);
        } catch (DaoException e) {
//            e.printStackTrace();
            id = daoSession.insert(obj);
            Log.d(TAG, "Insert " + clazz);
        } finally {
            daoSession.clear();
        }
        return id;
    }

    @Override
    public <T> long insertOrUpdate(T obj, WhereCondition whereCondition, WhereCondition... whereConditions) {
        long id = -1;
        Class<?> clazz = obj.getClass();
        try {
            openWritableDb();
            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            T o = dao.queryBuilder().where(whereCondition, whereConditions).unique();
            if (o == null) {
                id = dao.insert(obj);
                Log.d(TAG, "Insert " + clazz);
            } else {
                dao.update(o);
                Log.d(TAG, "Update " + clazz);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
            daoSession.clear();
        }
        return id;
    }

    @Override
    public long insertOrUpdateOrderPicking(OrderPicking obj) {
        long id = -1;
//        Class<?> clazz = obj.getClass();
        try {
            openWritableDb();
//            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            OrderPickingDao dao = daoSession.getOrderPickingDao();
            OrderPicking o = dao.queryBuilder().where(OrderPickingDao.Properties.RegionId.eq(obj.getRegionId()), OrderPickingDao.Properties.StoreId.eq(obj.getStoreId()),
                    OrderPickingDao.Properties.Paybill.eq(obj.getPaybill())).unique();
            if (o == null) {
                id = dao.insert(obj);
                Log.d(TAG, "Insert order");
            }
//            else {
//                dao.update(o);
//                Log.d(TAG, "Update order");
//            }
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
            daoSession.clear();
        }
        return id;
    }

    @Override
    public <T, S> void insertOrUpdateWithDetails(List<T> master, Class<S> detailClass) {
//        long id = -1;
//        Class<?> clazz = obj.getClass();
//        Class<?> detailClass = obj.getClass();
//        try {
//            openWritableDb();
//            AbstractDao<T, Long> dao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
//            dao.update(obj);
//            Log.d(TAG, "Update " + clazz);
//        } catch (DaoException e) {
//            e.printStackTrace();
//            id = daoSession.insert(obj);
//            Log.d(TAG, "Insert " + clazz);
//        } finally {
//            daoSession.clear();
//        }
    }

    @Override
    public void insertOrUpdateBuys(List<Buy> list) {
        try {
            openReadableDb();
            BuyDao dao = daoSession.getBuyDao();
            List<Buy> buysUpdated = new ArrayList<>();
            for (Buy b : list) {
                Buy buyLocal = dao.queryBuilder().where(BuyDao.Properties.RegionId.eq(b.getRegionId()),
                        BuyDao.Properties.ProviderId.eq(b.getProviderId()), BuyDao.Properties.Folio.eq(b.getFolio()),
                        BuyDao.Properties.Mov.eq(b.getMov()), BuyDao.Properties.Forced.eq(b.getForced())).unique();
                if (buyLocal == null) {
                    dao.insert(b);
                } else {
                    buyLocal.setChecked(b.getChecked());
                    buyLocal.setDeliveryTime(b.getDeliveryTime());
                    buyLocal.setPlatform(b.getPlatform());
                    buyLocal.setProgramedDate(b.getProgramedDate());
                    buyLocal.setTime(b.getTime());
                    buyLocal.setTotal(b.getTotal());
                    buysUpdated.add(buyLocal);
//                    dao.update(buyLocal);
                }
            }
            dao.updateInTx(buysUpdated);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrUpdateBuyDetails(List<BuyDetail> list) {
        try {
            openReadableDb();
            BuyDetailDao dao = daoSession.getBuyDetailDao();
            List<BuyDetail> buysUpdated = new ArrayList<>();
            for (BuyDetail b : list) {
                BuyDetail buyLocal = dao.queryBuilder().where(BuyDetailDao.Properties.ArticleId.eq(b.getArticleId()),
                        BuyDetailDao.Properties.BuyId.eq(b.getBuyId()), BuyDetailDao.Properties.ArticleAmount.eq(b.getArticleAmount())).unique();
                if (buyLocal == null) {
                    dao.insert(b);
                } else {
                    buyLocal.setBalance(b.getBalance());
                    buysUpdated.add(buyLocal);
                }
            }
            dao.updateInTx(buysUpdated);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Buy> listBuys(Date date, long regionId) {
        List<Buy> list = null;
        try {
            openReadableDb();
            BuyDao dao = daoSession.getBuyDao();
//            listAll = dao.queryDeep("where T.PROGRAMED_DATE = " + date.getTime() + " order by T1.BUSINESS_NAME");//deep para order by
//            listAll = dao.queryDeep("where T.PROGRAMED_DATE = " + date.getTime() + " order by T.PROGRAMED_DATE");
            list = dao.queryBuilder().where(BuyDao.Properties.ProgramedDate.eq(date), BuyDao.Properties.RegionId.eq(regionId)).orderAsc(BuyDao.Properties.Time, BuyDao.Properties.Platform).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Buy> listBuys(String date, long regionId) {
        try {
            return listBuys(DateUtil.getDate(date), regionId);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Buy> listBuysExpress(String date, long regionId, Integer... status) {
        List<Buy> list = null;
        try {
            openReadableDb();
            BuyDao dao = daoSession.getBuyDao();
            list = dao.queryBuilder().where(BuyDao.Properties.Mov.eq(Constants.CM), BuyDao.Properties.ProgramedDate.eq(DateUtil.getDate(date)),
                    BuyDao.Properties.RegionId.eq(regionId), BuyDao.Properties.Express.eq(true)
                    , BuyDao.Properties.Checked.in(status)
            )
                    .orderAsc(BuyDao.Properties.Time, BuyDao.Properties.Platform).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public boolean providerAutomatic(long providerId) {
        try {
            openReadableDb();
            ExpressProviderDao dao = daoSession.getExpressProviderDao();
            ExpressProvider p = dao.queryBuilder().where(ExpressProviderDao.Properties.Automatic.eq(true), ExpressProviderDao.Properties.ProviderId.eq(providerId)).unique();
            daoSession.clear();
            return p != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ExpressProvider findExpressProvider(Long providerId) {
        try {
            openReadableDb();
            ExpressProviderDao dao = daoSession.getExpressProviderDao();
            ExpressProvider p = dao.queryBuilder().where(ExpressProviderDao.Properties.ProviderId.eq(providerId)).unique();
            daoSession.clear();
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Buy> listBuys(String date, long regionId, Integer... status) {
        List<Buy> list = null;
        try {
            openReadableDb();
            BuyDao dao = daoSession.getBuyDao();
            list = dao.queryBuilder().where(BuyDao.Properties.Mov.eq(Constants.CM), BuyDao.Properties.ProgramedDate.eq(DateUtil.getDate(date)),
                    BuyDao.Properties.RegionId.eq(regionId)
                    , BuyDao.Properties.Checked.in(status)
            )
                    .orderAsc(BuyDao.Properties.Time, BuyDao.Properties.Platform).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public <T> T getById(Long id, Class<T> clazz) {
        Log.d(TAG, "search " + clazz.getSimpleName() + " id:" + id);
        T entity = null;
        try {
            openReadableDb();
            AbstractDao<T, Long> actionDao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            entity = actionDao.load(id);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<ReceiptSheet> listReceiptSheets(String date, long regionId) {
        try {
            return listReceiptSheets(DateUtil.getDate(date), regionId);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ReceiptSheet> listReceiptSheets(Date date, long regionId) {
        List<ReceiptSheet> list = null;
        try {
            openReadableDb();
            ReceiptSheetDao dao = daoSession.getReceiptSheetDao();
            list = dao.queryBuilder().where(ReceiptSheetDao.Properties.ReceiptDate.eq(date), ReceiptSheetDao.Properties.RegionId.eq(regionId)).orderAsc(ReceiptSheetDao.Properties.DateTime).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<GlobalDecrease> listGlobalDecrease(String date, long regionId) {
        Date date1 = null;
        try {
            date1 = DateUtil.getDate(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.DATE, -8);
            Date date2 = cal.getTime();
            return listGlobalDecrease(date2, date1, regionId);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<GlobalDecrease> listGlobalDecrease(Date date1, Date date2, long regionId) {
        List<GlobalDecrease> list = null;
        try {
            openReadableDb();
            GlobalDecreaseDao dao = daoSession.getGlobalDecreaseDao();
            list = dao.queryBuilder().where(GlobalDecreaseDao.Properties.Date.between(date1, date2), GlobalDecreaseDao.Properties.RegionId.eq(regionId)).orderAsc(GlobalDecreaseDao.Properties.Date).limit(8).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<VArticle> listViewArticlesDecrease() {
        List<VArticle> list = null;
        try {
            openReadableDb();
            VArticleDao dao = daoSession.getVArticleDao();
            list = dao.queryBuilder().where(new WhereCondition.StringCondition(VArticleDao.Properties.Iclave.columnName + " IN (SELECT " + ArticleDecreaseDao.Properties.Iclave.columnName + " FROM " + ArticleDecreaseDao.TABLENAME + ")"), VArticleDao.Properties.Inosurte.in(0, 2)).orderAsc(VArticleDao.Properties.Iclave).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<TimetableReceipt> listTimetable(int dayOfWeek, long regionId) {
        List<TimetableReceipt> list = null;
        try {
            openReadableDb();
            TimetableReceiptDao dao = daoSession.getTimetableReceiptDao();
            list = dao.queryBuilder().where(TimetableReceiptDao.Properties.RegionId.eq(regionId)).orderAsc(TimetableReceiptDao.Properties.Platform, TimetableReceiptDao.Properties.Time).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<TimetableReceipt> listTimetable(Date date, long regionId) {
        List<TimetableReceipt> list = null;
        try {
            openReadableDb();
            TimetableReceiptDao dao = daoSession.getTimetableReceiptDao();
            list = dao.queryBuilder().where(TimetableReceiptDao.Properties.DateTime.eq(new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(date)), TimetableReceiptDao.Properties.RegionId.eq(regionId)).orderAsc(TimetableReceiptDao.Properties.Platform, TimetableReceiptDao.Properties.Time).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public TimetableInfo getTimetableInfo(Long regionId, String currentDate) {
        TimetableInfo entity = null;
        try {
            openReadableDb();
            TimetableInfoDao dao = daoSession.getTimetableInfoDao();//K por Long
            entity = dao.queryBuilder().where(TimetableInfoDao.Properties.RegionId.eq(regionId), TimetableInfoDao.Properties.DateTime.eq(currentDate)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }


    @Override
    public OrderPickingCapture findOrderCapture(long regionId, long storeId, int paybill) {
        OrderPickingCapture entity = null;
        try {
            openReadableDb();
            OrderPickingCaptureDao dao = daoSession.getOrderPickingCaptureDao();//K por Long
            entity = dao.queryBuilder().where(OrderPickingCaptureDao.Properties.RegionId.eq(regionId), OrderPickingCaptureDao.Properties.StoreId.eq(storeId),
                    OrderPickingCaptureDao.Properties.Paybill.eq(paybill)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<OrderPickingCapture> findOrderCapture(long regionId, long storeId) {
        List<OrderPickingCapture> list = null;
        try {
            openReadableDb();
            OrderPickingCaptureDao dao = daoSession.getOrderPickingCaptureDao();
            list = dao.queryBuilder().where(OrderPickingCaptureDao.Properties.RegionId.eq(regionId), OrderPickingCaptureDao.Properties.StoreId.eq(storeId)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrderDetailCapture> findOrderDetails(long regionId, long storeId) {
        List<OrderDetailCapture> list = null;
        try {
            openReadableDb();
            OrderDetailCaptureDao dao = daoSession.getOrderDetailCaptureDao();
            QueryBuilder<OrderDetailCapture> query = dao.queryBuilder();
            query.join(OrderDetailCaptureDao.Properties.OrderCaptureId, OrderPickingCapture.class).where(OrderPickingCaptureDao.Properties.StoreId.eq(storeId));
//            query.join(OrderDetailDao.Properties.ArticleId, VArticle.class);
            list = query
//                    .where(OrderDetailDao.Properties.Existence.gt(0))
                    .orderRaw("J1.paybill ASC").list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<OrderDetailCapture> findOrderDetailsByFatherIclave(long regionId, long storeId, Integer fatherIclave) {
        List<OrderDetailCapture> list = null;
        try {
            openReadableDb();
            OrderDetailCaptureDao dao = daoSession.getOrderDetailCaptureDao();
            QueryBuilder<OrderDetailCapture> query = dao.queryBuilder();
            query.join(OrderDetailCaptureDao.Properties.OrderCaptureId, OrderPickingCapture.class).where(OrderPickingCaptureDao.Properties.StoreId.eq(storeId));
//            query.join(OrderDetailDao.Properties.ArticleId, VArticle.class);
            list = query
                    .where(
//                            OrderDetailDao.Properties.Existence.gt(0)
                            OrderDetailCaptureDao.Properties.FatherIclave.eq(fatherIclave)
                    )
//                    .orderRaw("J1.paybill ASC")
                    .list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public ExpressArticle findExpressArticle(long regionId, int odc, long iclave) {
        ExpressArticle obj = null;
        try {
            openReadableDb();
            ExpressArticleDao dao = daoSession.getExpressArticleDao();
            obj = dao.queryBuilder().where(ExpressArticleDao.Properties.RegionId.eq(regionId), ExpressArticleDao.Properties.Odc.eq(odc),
                    ExpressArticleDao.Properties.Iclave.eq(iclave)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public ExpressReceipt findExpressReceipt(long regionId, int odc, long iclave, short palletId) {
        ExpressReceipt entity = null;
        try {
            openReadableDb();
            ExpressReceiptDao dao = daoSession.getExpressReceiptDao();
            entity = dao.queryBuilder().where(ExpressReceiptDao.Properties.RegionId.eq(regionId),
                    ExpressReceiptDao.Properties.FolioOdc.eq(odc), ExpressReceiptDao.Properties.Iclave.eq(iclave), ExpressReceiptDao.Properties.ScaffoldNum.eq(palletId))
                    .unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<ExpressReceipt> listExpressReceiptByOdc(long regionId, int odc) {
        List<ExpressReceipt> list = null;
        try {
            openReadableDb();
            ExpressReceiptDao dao = daoSession.getExpressReceiptDao();
//            list = dao.queryBuilder().where(ExpressReceiptDao.Properties.RegionId.eq(regionId), ExpressReceiptDao.Properties.Barcode.like("%" + odc + "%")).list();
            list = dao.queryBuilder().where(ExpressReceiptDao.Properties.RegionId.eq(regionId), ExpressReceiptDao.Properties.FolioOdc.eq(odc)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ExpressArticle> listExpressArticles(long regionId, int odc) {
        List<ExpressArticle> list = null;
        try {
            openReadableDb();
            ExpressArticleDao dao = daoSession.getExpressArticleDao();
//            list = dao.queryBuilder().where(ExpressReceiptDao.Properties.RegionId.eq(regionId), ExpressReceiptDao.Properties.Barcode.like("%" + odc + "%")).list();
            list = dao.queryBuilder().where(ExpressArticleDao.Properties.RegionId.eq(regionId), ExpressArticleDao.Properties.Odc.eq(odc)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ExpressReceipt> listExpressReceiptByFolio(int odc, long regionId, boolean received) {
        List<ExpressReceipt> list = null;
        try {
            openReadableDb();
            ExpressReceiptDao dao = daoSession.getExpressReceiptDao();
//            list = dao.queryBuilder().where(ExpressReceiptDao.Properties.RegionId.eq(regionId), ExpressReceiptDao.Properties.Barcode.like("%" + odc + "%")).list();
            list = dao.queryBuilder().where(ExpressReceiptDao.Properties.RegionId.eq(regionId), ExpressReceiptDao.Properties.FolioOdc.eq(odc)
                    , ExpressReceiptDao.Properties.Received.eq(received)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ExpressReceipt findExpressReceipt(String barcode) {
        ExpressReceipt entity = null;
        try {
            openReadableDb();
            ExpressReceiptDao dao = daoSession.getExpressReceiptDao();
            entity = dao.queryBuilder().where(ExpressReceiptDao.Properties.Barcode.eq(barcode)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<ExpressReceipt> findExpressReceiptList(int odc, long iclave) {
        List<ExpressReceipt> entity = null;
        try {
            openReadableDb();
            ExpressReceiptDao dao = daoSession.getExpressReceiptDao();
            entity = dao.queryBuilder().where(ExpressReceiptDao.Properties.FolioOdc.eq(odc), ExpressReceiptDao.Properties.Iclave.eq(iclave)).
                    orderAsc(ExpressReceiptDao.Properties.Barcode).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public int getPiecesByUnit(int uclave, long iclave) {
//        int pieces = 0;
        List<VArticle> list = null;
        try {
            openReadableDb();
            VArticleDao dao = daoSession.getVArticleDao();
            list = dao.queryBuilder().where(VArticleDao.Properties.UnitId.eq(uclave), VArticleDao.Properties.Iclave.eq(iclave)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return list.get(0).getPacking();
        }
        return -1;
    }

    @Override
    public EPackage findEpackageBy(String barcode) {
        EPackage entity = null;
        try {
            openReadableDb();
            QueryBuilder<EPackage> qb = daoSession.getEPackageDao().queryBuilder();
            qb.where(EPackageDao.Properties.Barcode.like(barcode));
            List<EPackage> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "epackage size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public EPackage findEpackageById(long packageId) {
        EPackage entity = null;
        try {
            openReadableDb();
            QueryBuilder<EPackage> qb = daoSession.getEPackageDao().queryBuilder();
            qb.where(EPackageDao.Properties.Id.eq(packageId));
            List<EPackage> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "epackage size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public EPackage findEpackageByDate(String barcode, String selectedDate) {
        EPackage entity = null;
        try {
            openReadableDb();
            QueryBuilder<EPackage> qb = daoSession.getEPackageDao().queryBuilder();
            qb.where(EPackageDao.Properties.Barcode.like(barcode), EPackageDao.Properties.ReceiptDate.eq(selectedDate));
            List<EPackage> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "epackage size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public ProviderEpackage findProviderEpackageBy(long id) {
        ProviderEpackage entity = null;
        try {
            openReadableDb();
            QueryBuilder<ProviderEpackage> qb = daoSession.getProviderEpackageDao().queryBuilder();
            qb.where(ProviderEpackageDao.Properties.Id.eq(id));
            List<ProviderEpackage> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "ProviderEpackage size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public TruckPackage findTruckPackageByIdPackage(long id) {
        TruckPackage entity = null;
        try {
            openReadableDb();
            QueryBuilder<TruckPackage> qb = daoSession.getTruckPackageDao().queryBuilder();
            qb.where(TruckPackageDao.Properties.PackageId.eq(id), TruckPackageDao.Properties.Status.eq(1));
            List<TruckPackage> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "TruckPackage size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public EpackageTruck findEpackageTruckbyIdTruck(long id) {
        EpackageTruck entity = null;
        try {
            openReadableDb();
            QueryBuilder<EpackageTruck> qb = daoSession.getEpackageTruckDao().queryBuilder();
            qb.where(EpackageTruckDao.Properties.TruckId.eq(id), EpackageTruckDao.Properties.Active.eq(1));
            List<EpackageTruck> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "EpackageTruck size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public EpackageTruck findEpackageTruckbyId(long id) {
        EpackageTruck entity = null;
        try {
            openReadableDb();
            QueryBuilder<EpackageTruck> qb = daoSession.getEpackageTruckDao().queryBuilder();
            qb.where(EpackageTruckDao.Properties.Id.eq(id), EpackageTruckDao.Properties.Active.eq(1));
            List<EpackageTruck> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "EpackageTruck size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public EpackageTruck findEpackageTruckbyBarcode(String barcode) {
        EpackageTruck entity = null;
        try {
            openReadableDb();
            QueryBuilder<EpackageTruck> qb = daoSession.getEpackageTruckDao().queryBuilder();
            qb.where(EpackageTruckDao.Properties.Barcode.eq(barcode), EpackageTruckDao.Properties.Active.eq(1));
            List<EpackageTruck> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "EpackageTruck size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<ProviderEpackage> findProvidersBy(String date, long region) {
        List<ProviderEpackage> list = new ArrayList<>();
        List<EPackage> list2 = null;
        try {
            openReadableDb();
            ProviderEpackageDao dao = daoSession.getProviderEpackageDao();
            QueryBuilder<ProviderEpackage> query = dao.queryBuilder();
            query.join(EPackage.class, EPackageDao.Properties.ProviderId)
                    .where(EPackageDao.Properties.ReceiptDate.eq(date), EPackageDao.Properties.Status.eq(com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_PENDING),
                            EPackageDao.Properties.RegionId.eq(region));
            //TODO El estatus en 0 trae  los proveedores inactivos, si se van a poner los proveedores activos en la BD, hay q cambiar el estatus aqui
            list = query.where(ProviderEpackageDao.Properties.Status.eq(1)).orderAsc(ProviderEpackageDao.Properties.Name).list();
            daoSession.clear();


            //EPackageDao dao = daoSession.getEPackageDao();
            //list2 = dao.queryBuilder().where(ProviderEpackageDao.Properties.Status.eq(1)).list();


        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (list2 != null || list2.size() > 0) {
            System.out.println("*************lista2: " + list2.toString());
            for(int i = 0; i < list2.size(); i++){
                System.out.println("******** " + list2.get(i).toString());
                ProviderEpackage prov = new ProviderEpackage(list2.get(i).getProviderId(), "Provedor: "+list2.get(i).getProviderId(), list2.get(i).getStatus());
                list.add(prov);
            }

        }*/


        if (list != null) {
            List<ProviderEpackage> res = new ArrayList<>();
            for (ProviderEpackage p : list) {
                if (!res.contains(p)) {
                    res.add(p);
                }
            }
            //daoSession.clear();
            return res;
        }

        //daoSession.clear();
        return null;
    }

    @Override
    public PositionPackage getBy(Long ePackageId) {
        PositionPackage entity = null;
        try {
            openReadableDb();
            QueryBuilder<PositionPackage> qb = daoSession.getPositionPackageDao().queryBuilder();
            qb.where(PositionPackageDao.Properties.PackageId.eq(ePackageId));
            entity = qb.unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Position findPosition(long areaId, long rackId, long levelId, long positionId) {
        Position entity = null;
        try {
            openReadableDb();
            QueryBuilder<Position> qb = daoSession.getPositionDao().queryBuilder();
            Join rackLevel = qb.join(PositionDao.Properties.LevelId, RackLevel.class, RackLevelDao.Properties.Id).where(RackLevelDao.Properties.LevelId.eq(levelId));
            Join rack = qb.join(rackLevel, RackLevelDao.Properties.RackId, Rack.class, RackDao.Properties.Id).where(RackDao.Properties.RackId.eq(rackId));
            qb.join(rack, RackDao.Properties.AreaId, Area.class, AreaDao.Properties.Id).where(AreaDao.Properties.AreaId.eq(areaId));
            qb.where(PositionDao.Properties.PositionId.eq(positionId));
            entity = qb.unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<Area> listAreas(long regionId) {
        List<Area> list = null;
        try {
            openReadableDb();
            AreaDao dao = daoSession.getAreaDao();
            list = dao.queryBuilder().where(AreaDao.Properties.RegionId.eq(regionId)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public OrderDetail findOrderDetail(long orderDetailId) {
        OrderDetail orderDetail = null;
        try {
            openReadableDb();
            OrderDetailDao dao = daoSession.getOrderDetailDao();
            QueryBuilder<OrderDetail> query = dao.queryBuilder();
            orderDetail = query.where(OrderDetailDao.Properties.Id.eq(orderDetailId)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (orderDetail != null) {
            return orderDetail;
        }
        return null;
    }

    @Override
    public List<OrderType> findOrdersAvailable(long regionId) {
        List<OrderType> list = null;
        try {
            openReadableDb();
            OrderTypeDao dao = daoSession.getOrderTypeDao();
            QueryBuilder<OrderType> query = dao.queryBuilder();
            list = query.where(OrderTypeDao.Properties.RegionId.eq(regionId), OrderTypeDao.Properties.Active.eq(true), OrderTypeDao.Properties.MobileAvailable.eq(true)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public String findOrderTypeByType(long regionId, int type) {
        String list = null;
        try {
            openReadableDb();
            OrderTypeDao dao = daoSession.getOrderTypeDao();
            QueryBuilder<OrderType> query = dao.queryBuilder();
            list = query.where(OrderTypeDao.Properties.RegionId.eq(regionId)
                    , OrderTypeDao.Properties.Active.eq(true)
                    , OrderTypeDao.Properties.MobileAvailable.eq(true)
                    , OrderTypeDao.Properties.TypeId.eq(type)
            ).unique().getAlias();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return list;
        }
        return null;
    }

    @Override
    public <T> List<T> listAll(Class<T> clazz) {
        List<T> list = null;
        try {
            openReadableDb();
            AbstractDao<T, Long> actionDao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            list = actionDao.loadAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Store> listStores(long regionId) {
        List<Store> list = null;
        try {
            openReadableDb();
            StoreDao dao = daoSession.getStoreDao();
            list = dao.queryBuilder().where(StoreDao.Properties.RegionId.eq(regionId), StoreDao.Properties.Id.lt(1000L)).orderAsc(StoreDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Store> listStoresToPick(long region, int dayOfWeek) {
        List<Store> list = null;
        try {
            openReadableDb();
            StoreDao dao = daoSession.getStoreDao();
            QueryBuilder<Store> query = dao.queryBuilder();
            query.join(TimetablePicking.class, TimetablePickingDao.Properties.StoreId)
                    .where(TimetablePickingDao.Properties.Day.eq(dayOfWeek));
            //list = query.where(StoreDao.Properties.RegionId.eq(region)).orderAsc(StoreDao.Properties.Id).list();
            list = query.where(StoreDao.Properties.RegionId.eq(region)).orderAsc(StoreDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }


    @Override
    public List<Store> listStoresToPick(long region, int dayOfWeek, int type, int position) {
        List<Store> list = null;
        try {
            openReadableDb();
            StoreDao dao = daoSession.getStoreDao();
            QueryBuilder<Store> query = dao.queryBuilder();
            query.join(TimetablePicking.class, TimetablePickingDao.Properties.StoreId)
                    .where(TimetablePickingDao.Properties.Day.eq(dayOfWeek));
            list = query.where(StoreDao.Properties.RegionId.eq(region)).orderAsc(StoreDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<StorePickingStatus> listStoresStatusPick(long regionId, String date, int type, int status) {
        List<StorePickingStatus> list = null;
        try {
            openReadableDb();
            StorePickingStatusDao dao = daoSession.getStorePickingStatusDao();
            QueryBuilder<StorePickingStatus> query = dao.queryBuilder();
//            query.join(TimetablePicking.class, TimetablePickingDao.Properties.StoreId)
//                    .where(TimetablePickingDao.Properties.Day.eq(dayOfWeek));
            list = query.where(StorePickingStatusDao.Properties.RegionId.eq(regionId),
                    StorePickingStatusDao.Properties.PickDate.eq(date),
                    StorePickingStatusDao.Properties.OtroiSmn.eq(type),
                    StorePickingStatusDao.Properties.Status.eq(status)).orderAsc(StorePickingStatusDao.Properties.StoreId).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<VArticle> listViewArticlesTransference() {
        List<VArticle> list = null;
        try {
            openReadableDb();
            VArticleDao dao = daoSession.getVArticleDao();
            list = dao.queryBuilder().where(new WhereCondition.StringCondition(VArticleDao.Properties.Iclave.columnName + " IN (SELECT " + ArticleTransferenceDao.Properties.Iclave.columnName + " FROM " + ArticleTransferenceDao.TABLENAME + ")"), VArticleDao.Properties.Inosurte.in(0, 2)).orderAsc(VArticleDao.Properties.Iclave).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    /**
     * ver bot_relacion_razon_mov
     *
     * @param typeId
     */
    @Override
    public List<ObservationType> listObservationType(int typeId) {
        List<ObservationType> list = null;
        if (typeId == 0) {
            list = new ArrayList<>();

            ObservationType type1 = new ObservationType();
            type1.setActive(1);
            type1.setType(0);
            type1.setDescription("Caducidad");
            type1.setId(1L);
            list.add(type1);

            ObservationType type2 = new ObservationType();
            type2.setActive(1);
            type2.setType(0);
            type2.setDescription("Rotura x Manipoleo");
            type2.setId(2L);
            list.add(type2);

            ObservationType type3 = new ObservationType();
            type3.setActive(1);
            type3.setType(0);
            type3.setDescription("Rotura x Robo");
            type3.setId(3L);
            list.add(type3);

            ObservationType type4 = new ObservationType();
            type4.setActive(1);
            type4.setType(0);
            type4.setDescription("Mala Calidad Embalaje");
            type4.setId(4L);
            list.add(type4);

            ObservationType type5 = new ObservationType();
            type5.setActive(1);
            type5.setType(0);
            type5.setDescription("Mala Calidad Producto");
            type5.setId(1L);
            list.add(type5);

            return list;
        } else {
            try {
                openReadableDb();
                ObservationTypeDao dao = daoSession.getObservationTypeDao();
                list = dao.queryBuilder().where(ObservationTypeDao.Properties.Active.eq(1), ObservationTypeDao.Properties.Type.eq(typeId)).orderAsc(ObservationTypeDao.Properties.Description).list();
                daoSession.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list != null && !list.isEmpty()) {
                return new ArrayList<>(list);
            }
        }
        return null;
    }

    @Override
    public List<InputTransference> listInputTransference(short type, long regionId) {
        List<InputTransference> list = null;
        try {
            openReadableDb();
            InputTransferenceDao dao = daoSession.getInputTransferenceDao();
            QueryBuilder<InputTransference> db = dao.queryBuilder();
            if (type == -1) {
                db.where(InputTransferenceDao.Properties.Type.notIn(1, 2, 3, 5), InputTransferenceDao.Properties.RegionId.eq(regionId));
            } else {
                db.where(InputTransferenceDao.Properties.Type.eq(type), InputTransferenceDao.Properties.RegionId.eq(regionId));
            }
            list = db.orderAsc(InputTransferenceDao.Properties.StoreId).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<InputTransferenceDetail> listInputTransferenceDetail(Long inputTransferenceId) {
        List<InputTransferenceDetail> list = null;
        try {
            openReadableDb();
            InputTransferenceDetailDao dao = daoSession.getInputTransferenceDetailDao();
            QueryBuilder<InputTransferenceDetail> db = dao.queryBuilder();
            db.where(InputTransferenceDetailDao.Properties.InputTransferenceId.eq(inputTransferenceId));
            list = db.orderAsc(InputTransferenceDetailDao.Properties.Iclave).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public <T> void delete(Class<T> clazz, T... rows) {
        try {
            openReadableDb();
            AbstractDao<T, Long> actionDao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            actionDao.deleteInTx(rows);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ReceiptSheetDetail> listReceiptSheetDetails(Long timetableId) {
        List<ReceiptSheetDetail> list = null;
        try {
            openReadableDb();
            ReceiptSheetDetailDao dao = daoSession.getReceiptSheetDetailDao();
            QueryBuilder<ReceiptSheetDetail> db = dao.queryBuilder();
            db.where(ReceiptSheetDetailDao.Properties.TimetableReceiptId.eq(timetableId));
            list = db.orderAsc(ReceiptSheetDetailDao.Properties.Iclave).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<VArticle> listViewArticles(long vendorId) {
        List<VArticle> list = null;
        try {
            openReadableDb();
            VArticleDao dao = daoSession.getVArticleDao();
            list = dao.queryBuilder().where(VArticleDao.Properties.ProviderId.eq(vendorId), VArticleDao.Properties.Inosurte.in(0, 2), VArticleDao.Properties.ActiveId.notIn(0, 5))
                    .orderAsc(VArticleDao.Properties.Iclave).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Provider> listSupplyVendors() {
        List<Provider> list = null;
        try {
            openReadableDb();
            ProviderDao dao = daoSession.getProviderDao();
            list = dao.queryBuilder().where(ProviderDao.Properties.Active.eq(1), ProviderDao.Properties.Supply.eq(1)).orderAsc(ProviderDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Provider> listSupplyActiveVendors() {//TODO solo proveedores con articulos
        List<Provider> list = null;
        try {
            openReadableDb();
            ProviderDao dao = daoSession.getProviderDao();
//            VArticleDao articleDao = daoSession.getVArticleDao();
            list = dao.queryBuilder().where(ProviderDao.Properties.Active.eq(1), ProviderDao.Properties.Supply.eq(1)).orderAsc(ProviderDao.Properties.Id).list();
//            articleDao.queryBuilder().where(VArticleDao.Properties.)
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public VArticle findViewArticleByBarcode(String barcode) {
        VArticle entity = null;
        try {
            openReadableDb();
            QueryBuilder<VArticle> qb = daoSession.getVArticleDao().queryBuilder();
            qb.where(VArticleDao.Properties.Barcode.like(barcode));
            List<VArticle> list = qb.list();
            if (list == null || list.isEmpty()) {

            } else {
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<VArticle> listViewArticlesDecrease(Object... ds) {
        List<VArticle> list = null;
        try {
            openReadableDb();
            VArticleDao dao = daoSession.getVArticleDao();
            list = dao.queryBuilder().where(VArticleDao.Properties.Id.in(ds)).orderAsc(VArticleDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public void deleteByInputTransferenceId(long inputTransId) {
        try {
            openReadableDb();
            InputTransferenceDetailDao actionDao = daoSession.getInputTransferenceDetailDao();
            List<InputTransferenceDetail> l = actionDao.queryBuilder().where(InputTransferenceDetailDao.Properties.InputTransferenceId.eq(inputTransId)).list();
            actionDao.deleteInTx(l.toArray(new InputTransferenceDetail[l.size()]));
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrderBy(long storeId) {
        try {
            openWritableDb();
            OrderPickingDao dao = daoSession.getOrderPickingDao();
            OrderDetailDao daoDet = daoSession.getOrderDetailDao();
            List<OrderPicking> l = dao.queryBuilder().where(OrderPickingDao.Properties.StoreId.eq(storeId)).list();
            for (OrderPicking o : l) {
                daoDet.deleteInTx(o.getDetails());
            }
            dao.deleteInTx(l);

            OrderPickingCaptureDao daoC = daoSession.getOrderPickingCaptureDao();
            OrderDetailCaptureDao daoDetC = daoSession.getOrderDetailCaptureDao();
            List<OrderPickingCapture> lC = daoC.queryBuilder().where(OrderPickingCaptureDao.Properties.StoreId.eq(storeId)).list();
            for (OrderPickingCapture o : lC) {
                daoDetC.deleteInTx(o.getDetails());
            }
            daoC.deleteInTx(lC);
//            dao.deleteInTx(l.toArray(new OrderPicking[l.size()]));
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrderBy(Integer[] selectedPaybills) {
        try {
            openWritableDb();
            OrderPickingDao dao = daoSession.getOrderPickingDao();
            OrderDetailDao daoDet = daoSession.getOrderDetailDao();
            List<OrderPicking> l = dao.queryBuilder().where(OrderPickingDao.Properties.Paybill.notIn(selectedPaybills)).list();
            for (OrderPicking o : l) {
                daoDet.deleteInTx(o.getDetails());
            }
            dao.deleteInTx(l);

            OrderPickingCaptureDao daoC = daoSession.getOrderPickingCaptureDao();
            OrderDetailCaptureDao daoDetC = daoSession.getOrderDetailCaptureDao();
            List<OrderPickingCapture> lC = daoC.queryBuilder().where(OrderPickingCaptureDao.Properties.Paybill.notIn(selectedPaybills)).list();
            for (OrderPickingCapture o : lC) {
                daoDetC.deleteInTx(o.getDetails());
            }
            daoC.deleteInTx(lC);
//            dao.deleteInTx(l.toArray(new OrderPicking[l.size()]));
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllOrders() {
        try {
            openWritableDb();
            OrderPickingDao dao = daoSession.getOrderPickingDao();
            OrderDetailDao daoDet = daoSession.getOrderDetailDao();
            daoDet.deleteAll();
            dao.deleteAll();

            OrderPickingCaptureDao daoC = daoSession.getOrderPickingCaptureDao();
            OrderDetailCaptureDao daoDetC = daoSession.getOrderDetailCaptureDao();
            daoDetC.deleteAll();
            daoC.deleteAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VArticle findViewArticleByIclave(long iclave) {
//        Log.i(TAG, "iclave: " + iclave);
        VArticle entity = null;
        try {
            openReadableDb();
            QueryBuilder<VArticle> qb = daoSession.getVArticleDao().queryBuilder();
            qb.where(VArticleDao.Properties.Iclave.eq(iclave), VArticleDao.Properties.Inosurte.in(0, 2));
            List<VArticle> list = qb.list();
            if (list == null || list.isEmpty()) {
                Log.e(TAG, "iclave: " + iclave + " not found!");
            } else {
                entity = list.get(0);
                if (entity.getDescription() == null) {
                    if (list.size() > 1) {
                        entity = list.get(1);
                    }
                }
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<VArticle> findViewArticleByFatherIclave(int fatherIclave) {
        List<VArticle> list = null;
        try {
            openReadableDb();
            QueryBuilder<VArticle> qb = daoSession.getVArticleDao().queryBuilder();
            qb.where(VArticleDao.Properties.FatherIclave.eq(fatherIclave), VArticleDao.Properties.Inosurte.in(1, 2));
            list = qb.list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<ArticleResearch> listArticlesResearch(String date, long regionId) {
        List<ArticleResearch> list = null;
        try {
            openReadableDb();
            ArticleResearchDao dao = daoSession.getArticleResearchDao();
            list = dao.queryBuilder().where(ArticleResearchDao.Properties.Date.eq(date), ArticleResearchDao.Properties.RegionId.eq(regionId))
//                    .orderAsc(ArticleResearchDao.Properties.AvgInvest)
                    .list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<ScannerCount> listScannerCount(String dateStr, long regionId) {
        List<ScannerCount> list = null;
        try {
            openReadableDb();
            ScannerCountDao dao = daoSession.getScannerCountDao();
            list = dao.queryBuilder().where(ScannerCountDao.Properties.RegionId.eq(regionId), ScannerCountDao.Properties.Date.eq(dateStr)).orderAsc(ScannerCountDao.Properties.Iclave).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Complaint> listComplaint(String dateStr, long regionId) {
        List<Complaint> list = null;
        try {
            openReadableDb();
            ComplaintDao dao = daoSession.getComplaintDao();
            list = dao.queryBuilder().where(ComplaintDao.Properties.RegionId.eq(regionId), ComplaintDao.Properties.Date.eq(dateStr)).orderAsc(ComplaintDao.Properties.StoreId).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Truck> listActiveTrucks(long regionId) {
        List<Truck> list = null;
        try {
            openReadableDb();
            TruckDao dao = daoSession.getTruckDao();
            list = dao.queryBuilder().where(TruckDao.Properties.RegionId.eq(regionId), TruckDao.Properties.Active.eq(1)).orderAsc(TruckDao.Properties.Description).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Driver> listActiveDrivers(long regionId) {
        List<Driver> list = null;
        try {
            openReadableDb();
            DriverDao dao = daoSession.getDriverDao();
            list = dao.queryBuilder().where(DriverDao.Properties.RegionId.eq(regionId), DriverDao.Properties.Active.eq(1)).orderAsc(DriverDao.Properties.Description).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public Driver getDriverByDesc(String driverDesc) {
        Driver entity = null;
        try {
            openReadableDb();
            Log.w(TAG, "driverDesc:" + driverDesc);
            QueryBuilder<Driver> qb = daoSession.getDriverDao().queryBuilder();
            Log.w(TAG, "daoSession:" + daoSession);
            qb.where(DriverDao.Properties.Description.eq(driverDesc), DriverDao.Properties.Active.eq(1));
            entity = qb.unique();
            if (entity == null) {
                Log.e(TAG, "driverDesc: " + driverDesc + " not found!");
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<Roadmap> listRoadmap(long regionId, long truckId) {
        List<Roadmap> list = null;
        try {
            openReadableDb();
            RoadmapDao dao = daoSession.getRoadmapDao();
            list = dao.queryBuilder().where(RoadmapDao.Properties.RegionId.eq(regionId), RoadmapDao.Properties.Sync.eq(false), RoadmapDao.Properties.TruckId.eq(truckId),
                    RoadmapDao.Properties.Date.eq(DateUtil.getDateStr(new Date())))
                    .orderDesc(RoadmapDao.Properties.Date)
                    .orderAsc(RoadmapDao.Properties.Travel).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<RoadmapDetail> listRoadmapDetails(long roadmapId) {
        List<RoadmapDetail> list = null;
        try {
            openReadableDb();
            RoadmapDetailDao dao = daoSession.getRoadmapDetailDao();
            list = dao.queryBuilder().where(RoadmapDetailDao.Properties.RoadmapId.eq(roadmapId), RoadmapDetailDao.Properties.Sync.eq(false)).orderAsc(RoadmapDetailDao.Properties.InitialTime).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public <T> T getByDescription(String description, Class<T> clazz) {
        T entity = null;
        try {
            openReadableDb();
            AbstractDao<T, Long> actionDao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            Property[] properties = actionDao.getProperties();
            Property descProperty = null;
            for (Property p : properties) {
                if (p.name.equals(SpinnerBase.DESCRIPTION_ATT)) {
                    descProperty = p;
                    break;
                }
            }
            if (descProperty != null) {
                entity = actionDao.queryBuilder().where(descProperty.eq(description)).unique();
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<Activity> listActivities(Long... ids) {
        List<Activity> list = null;
        try {
            openReadableDb();
            ActivityDao dao = daoSession.getActivityDao();
            list = dao.queryBuilder().where(ActivityDao.Properties.Id.in(ids)).orderAsc(ActivityDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public Roadmap findRoadmap(long regionId, Long driverId, long truckId, int travel, String dateStr) {
        Roadmap entity = null;
        try {
            openReadableDb();
            QueryBuilder<Roadmap> qb = daoSession.getRoadmapDao().queryBuilder();
            qb.where(RoadmapDao.Properties.RegionId.eq(regionId), RoadmapDao.Properties.DriverId.eq(driverId), RoadmapDao.Properties.TruckId.eq(truckId),
                    RoadmapDao.Properties.Travel.eq(travel), RoadmapDao.Properties.Date.eq(dateStr));
            entity = qb.unique();
//            if (entity == null) {
//                Log.e(TAG, "roadmap not found!");
//            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<Store> listDiesel(long regionId) {
        List<Store> list = null;
        try {
            openReadableDb();
            StoreDao dao = daoSession.getStoreDao();
            list = dao.queryBuilder().where(StoreDao.Properties.RegionId.eq(regionId), StoreDao.Properties.Id.ge(Constants.DIESEL_ID)).orderAsc(StoreDao.Properties.Id).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<Roadmap> listRoadmapSync(boolean sync) {
        List<Roadmap> list = null;
        try {
            openReadableDb();
            RoadmapDao dao = daoSession.getRoadmapDao();
            list = dao.queryBuilder().where(RoadmapDao.Properties.Sync.eq(sync)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public void updateRoadmaps() {
        List<Roadmap> list = listRoadmapSync(false);
        for (Roadmap r : list) {
            r.setSync(true);
        }
        try {
            openReadableDb();
            RoadmapDao dao = daoSession.getRoadmapDao();
            dao.updateInTx(list);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void truncate(Class<T> clazz) {
        try {
            openReadableDb();
            AbstractDao<T, Long> actionDao = (AbstractDao<T, Long>) daoSession.getDao(clazz);//K por Long
            actionDao.deleteAll();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long findBuyByTimetableId(long timetableId, long regionId) {
        long list = -1L;
        try {
            openReadableDb();
            TimetableReceipt timetable = getById(timetableId, TimetableReceipt.class);
            BuyDao dao = daoSession.getBuyDao();
            Buy buy = dao.queryBuilder().where(BuyDao.Properties.ProgramedDate.eq(DateUtil.getDate(timetable.getDateTime())),
                    BuyDao.Properties.RegionId.eq(regionId), BuyDao.Properties.Folio.eq(timetable.getFolio())).unique();
            if (buy != null) {
                list = buy.getId();
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Buy findBuyByFolio(int odc, long regionId) {
        Buy list = null;
        try {
            openReadableDb();
            BuyDao dao = daoSession.getBuyDao();
            Buy buy = dao.queryBuilder().where(BuyDao.Properties.Folio.eq(odc), BuyDao.Properties.RegionId.eq(regionId)).unique();
            if (buy != null) {
                list = buy;
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public BuyDetail findBuyDetailByArticle(int iclave, long buyId) {
        BuyDetail obj = null;
        try {
            openReadableDb();
            BuyDetailDao dao = daoSession.getBuyDetailDao();
            QueryBuilder<BuyDetail> query = dao.queryBuilder();
            query.join(BuyDetailDao.Properties.ArticleId, VArticle.class).where(VArticleDao.Properties.Iclave.eq(iclave));
            BuyDetail detail = query.where(BuyDetailDao.Properties.BuyId.eq(buyId)).unique();
            if (detail != null) {
                obj = detail;
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public List<ObservationType> listActiveObservationType() {
        List<ObservationType> list = null;
        try {
            openReadableDb();
            ObservationTypeDao dao = daoSession.getObservationTypeDao();
            list = dao.queryBuilder().where(ObservationTypeDao.Properties.Active.eq(1))
                    .orderAsc(ObservationTypeDao.Properties.Description).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<ComplaintCat> listComplaintBy(int type) {
        List<ComplaintCat> list = null;
        try {
            openReadableDb();
            ComplaintCatDao dao = daoSession.getComplaintCatDao();
            list = dao.queryBuilder().where(ComplaintCatDao.Properties.Active.eq(true), ComplaintCatDao.Properties.Type.eq(type))
                    .orderAsc(ComplaintCatDao.Properties.Description).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public VArticle findViewArticleForPurchasing(Integer iclave, String barcode) {//si ya se usa Purchasing, se puede quitar barcode
        VArticle entity = null;
        try {
            openReadableDb();
            QueryBuilder<VArticle> qb = daoSession.getVArticleDao().queryBuilder();
            qb.where(VArticleDao.Properties.Barcode.like(barcode), VArticleDao.Properties.Iclave.eq(iclave), VArticleDao.Properties.Purchasing.eq(true));
            List<VArticle> list = qb.list();
            if (list == null || list.isEmpty()) {
//Log o algo?
            } else {
                Log.d(TAG, "Article for purchasing size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public long findViewArticleIdForPurchasing(Integer iclave, String barcode) {
        VArticle a = findViewArticleForPurchasing(iclave, barcode);
        return a == null ? -1 : a.getId();
    }

    @Override
    public long findViewArticleIdForPicking(int iclave) {
        VArticle entity;
        long id = -1;
        try {
            openReadableDb();
            QueryBuilder<VArticle> qb = daoSession.getVArticleDao().queryBuilder();
            qb.where(VArticleDao.Properties.Iclave.eq(iclave), VArticleDao.Properties.Inosurte.in(1, 2));
            List<VArticle> list = qb.list();
            if (list == null || list.isEmpty()) {
                Log.e(TAG, "Article for picking not found:" + iclave);
            } else {
                Log.d(TAG, "Article for picking size:" + list.size());
                entity = list.get(0);
                id = entity.getId();
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public ReceiptSheetCapture findReceiptSheetCaptureBy(long buyId) {
        ReceiptSheetCapture entity = null;
        try {
            openReadableDb();
            QueryBuilder<ReceiptSheetCapture> qb = daoSession.getReceiptSheetCaptureDao().queryBuilder();
            qb.where(ReceiptSheetCaptureDao.Properties.BuyId.eq(buyId));
            List<ReceiptSheetCapture> list = qb.list();
            if (list == null || list.isEmpty()) {
            } else {
                Log.d(TAG, "Header for buy size:" + list.size());
                entity = list.get(0);
            }
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public List<OrderDetail> listOrderDetails(long storeId) {
        List<OrderDetail> list = null;
        try {
            openReadableDb();
            OrderDetailDao dao = daoSession.getOrderDetailDao();
            QueryBuilder<OrderDetail> query = dao.queryBuilder();
            query.join(OrderDetailDao.Properties.OrderId, OrderPicking.class).where(OrderPickingDao.Properties.StoreId.eq(storeId));
            query.join(OrderDetailDao.Properties.ArticleId, VArticle.class);
            list = query
//                    .where(OrderDetailDao.Properties.Existence.gt(0))
                    .orderRaw("J2.circuit ASC").list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<OrderDetail> listOrderDetails(Integer[] selectedPaybills) {
        List<OrderDetail> list = null;
        try {
            openReadableDb();
            OrderDetailDao dao = daoSession.getOrderDetailDao();
            QueryBuilder<OrderDetail> query = dao.queryBuilder();
            query.join(OrderDetailDao.Properties.OrderId, OrderPicking.class).where(OrderPickingDao.Properties.Paybill.in(selectedPaybills));
            query.join(OrderDetailDao.Properties.ArticleId, VArticle.class);
            list = query
//                    .where(OrderDetailDao.Properties.Existence.gt(0))
                    .orderRaw("J2.circuit ASC").list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<OrderPicking> listOrders(long storeId) {
        List<OrderPicking> list = null;
        try {
            openReadableDb();
            OrderPickingDao dao = daoSession.getOrderPickingDao();
            QueryBuilder<OrderPicking> query = dao.queryBuilder();
            list = query
                    .where(OrderPickingDao.Properties.StoreId.eq(storeId))
                    .list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    @Override
    public List<EPackage> listEPackages(int status) {
        List<EPackage> list = null;
        try {
            openReadableDb();
            EPackageDao dao = daoSession.getEPackageDao();
            QueryBuilder<EPackage> query = dao.queryBuilder();
            list = query.where(EPackageDao.Properties.Status.eq(status)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Override
    public List<EPackage> listEPackages(long providerId, int status, String dateStr) {
        List<EPackage> list = null;
        try {
            openReadableDb();
            EPackageDao dao = daoSession.getEPackageDao();
            QueryBuilder<EPackage> query = dao.queryBuilder();
            list = query.where(EPackageDao.Properties.Status.eq(status), EPackageDao.Properties.ProviderId.eq(providerId), EPackageDao.Properties.ReceiptDate.eq(dateStr)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Override
    public List<EPackage> listEPackagesStore(long StoreId, int status, String dateStr) {
        List<EPackage> list = null;
        try {
            openReadableDb();
            EPackageDao dao = daoSession.getEPackageDao();
            QueryBuilder<EPackage> query = dao.queryBuilder();
            list = query.where(EPackageDao.Properties.Status.eq(status), EPackageDao.Properties.StoreId.eq(StoreId), EPackageDao.Properties.ReceiptDate.eq(dateStr)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Override
    public List<EPackage> listEPackages(int status, String dateStr) {
        List<EPackage> list = null;
        try {
            openReadableDb();
            EPackageDao dao = daoSession.getEPackageDao();
            QueryBuilder<EPackage> query = dao.queryBuilder();
            list = query.where(EPackageDao.Properties.Status.eq(status), EPackageDao.Properties.ReceiptDate.eq(dateStr)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Override
    public List<EPackage> listEPackages(int status, long storeId) {
        List<EPackage> list = null;
        try {
            openReadableDb();
            EPackageDao dao = daoSession.getEPackageDao();
            QueryBuilder<EPackage> query = dao.queryBuilder();
            list = query.where(EPackageDao.Properties.Status.eq(status), EPackageDao.Properties.StoreId.eq(storeId)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Override
    public List<EPackage> listEPackagesDownload(int status, long storeId) {
        List<EPackage> list = null;
        try {
            openReadableDb();
            EPackageDao dao = daoSession.getEPackageDao();
            QueryBuilder<EPackage> query = dao.queryBuilder();
            query.join(EPackageDao.Properties.Id, EpackageDownload.class, EpackageDownloadDao.Properties.PackageId).where(EpackageDownloadDao.Properties.Activo.eq(1));
            list = query.where(EPackageDao.Properties.Status.eq(status), EPackageDao.Properties.StoreId.eq(storeId)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    public List<EpackageDownload> listEPackagesDownload(int status) {
        List<EpackageDownload> list = null;
        try {
            openReadableDb();
            EpackageDownloadDao dao = daoSession.getEpackageDownloadDao();
            QueryBuilder<EpackageDownload> query = dao.queryBuilder();
            list = query.where(EpackageDownloadDao.Properties.Activo.eq(status)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ArrayList<>(list);
    }

    @Override
    public <T> boolean isEmpty(Class<T> clazz) {
        try {
            openReadableDb();
            AbstractDao<T, Long> actionDao = (AbstractDao<T, Long>) daoSession.getDao(clazz);
            long count = actionDao.count();
            daoSession.clear();
            if (count > 0L) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<ReceiptExpressLog> findReceiptExpressLog(String idAlmacen, Long idProveedor, Integer odc) {

        List<ReceiptExpressLog> receiptExpressLog = null;
        try {
            openReadableDb();
            ReceiptExpressLogDao dao = daoSession.getReceiptExpressLogDao();
            receiptExpressLog = dao.queryBuilder().where(ReceiptExpressLogDao.Properties.IdAlmacen.eq(idAlmacen), ReceiptExpressLogDao.Properties.Odc.eq(odc),
                    ReceiptExpressLogDao.Properties.IdProveedor.eq(idProveedor)).orderDesc(ReceiptExpressLogDao.Properties.IdOrden).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptExpressLog;

    }

    @Override
    public Barcode findIclaveBarcode(String barcodes) {

        Barcode barcode = null;

        try {
            openReadableDb();
            BarcodeDao barcodeDao = daoSession.getBarcodeDao();
            barcode = barcodeDao.queryBuilder().where(BarcodeDao.Properties.Icb.eq(barcodes)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return barcode;
    }

    @Override
    public ShipmentControl findShipmentControlByDate(Date date) {
        ShipmentControl shipmentControl = null;

        try {
            openReadableDb();
            ShipmentControlDao shipmentControlDao = daoSession.getShipmentControlDao();
            shipmentControl = shipmentControlDao.queryBuilder().where(ShipmentControlDao.Properties.Date.eq(date)).unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shipmentControl;
    }

    @Override
    public List<Trip> findTrips(Date date, long truckId, long regionId) {

        List<Trip> trips = null;
        try {
            openReadableDb();
            TripDao tripDao = daoSession.getTripDao();
            trips = tripDao.queryBuilder()
                    .where(TripDao.Properties.Date.eq(date)
                            , TripDao.Properties.TruckId.eq(truckId)
                            , TripDao.Properties.RegionId.eq(regionId))
                    .orderAsc(TripDao.Properties.Sequence)
                    .list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trips;
    }

    @Override
    public Trip findNextTrip(long truckId) {
        Trip trip = null;
        try {
            openReadableDb();
            TripDao tripDao = daoSession.getTripDao();
            trip = tripDao.queryBuilder()
                    .where(
                            TripDao.Properties.Status.notEq(ShipmentConstants.TRIP_STATUS_COMPLETED),
                            TripDao.Properties.TruckId.eq(truckId)
                    )
                    .orderAsc(TripDao.Properties.Sequence)
                    .limit(1)
                    .unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trip;
    }

    @Override
    public Trip findTripById(long tripId) {
        Trip trip = null;
        try {
            openReadableDb();
            TripDao tripDao = daoSession.getTripDao();
            trip = tripDao.queryBuilder()
                    .where(TripDao.Properties.Id.eq(tripId))
                    .unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trip;
    }

    @Override
    public List<TripDetail> listTripDetails(long tripId) {
        List<TripDetail> tripDetails = null;
        try {
            openReadableDb();
            TripDetailDao tripDetailDao = daoSession.getTripDetailDao();
            tripDetails = tripDetailDao.queryBuilder()
                    .where(TripDetailDao.Properties.TripId.eq(tripId))
                    .orderAsc(TripDetailDao.Properties.Sequence)
                    .list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripDetails;
    }

    @Override
    public Form findFormById(long formId) {
        Form form = null;
        try {
            openReadableDb();
            FormDao formDao = daoSession.getFormDao();
            form = formDao.queryBuilder()
                    .where(FormDao.Properties.Id.eq(formId))
                    .unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return form;

    }

    @Override
    public TripDetail findTripDetailById(long tripDetailId) {
        TripDetail tripDetail = null;
        try {
            openReadableDb();
            TripDetailDao tripDetailDao = daoSession.getTripDetailDao();
            tripDetail = tripDetailDao.queryBuilder()
                    .where(TripDetailDao.Properties.Id.eq(tripDetailId))
                    .unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripDetail;
    }

    @Override
    public TripDetail findNextTripDetail(long tripId, int... statuses) {
        TripDetail tripDetail = null;
        try {
            openReadableDb();
            TripDetailDao tripDetailDao = daoSession.getTripDetailDao();

            List<Integer> statusList = new ArrayList();
            for (int status : statuses) statusList.add(status);

            QueryBuilder<TripDetail> queryBuilder = tripDetailDao.queryBuilder()
                    .where(TripDetailDao.Properties.TripId.eq(tripId))
                    .where(TripDetailDao.Properties.Status.in(statusList));


            tripDetail = queryBuilder.orderDesc(TripDetailDao.Properties.Sequence)
                    .limit(1)
                    .unique();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tripDetail;
    }


    @Override
    public List<DecreaseMP> findDecreaseMP(Integer odc){

        List<DecreaseMP> decreaseMP = null;
        try {
            openReadableDb();
            DecreaseMPDao dao = daoSession.getDecreaseMPDao();
            decreaseMP = dao.queryBuilder().where(DecreaseMPDao.Properties.Odc.eq(odc)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decreaseMP;

    }

    @Override
    public List<FMuestras> findFMuestras(Integer odc){

        List<FMuestras> fMuestras = null;
        try {
            openReadableDb();
            FMuestrasDao dao = daoSession.getFMuestrasDao();
            fMuestras = dao.queryBuilder().where(FMuestrasDao.Properties.Odc.eq(odc)).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fMuestras;

    }

    @Override
    public List<EpackageTruck> listActiveEpackageTruck(long regionId) {
        List<EpackageTruck> list = null;
        try {
            openReadableDb();
            EpackageTruckDao dao = daoSession.getEpackageTruckDao();
            list = dao.queryBuilder().where(EpackageTruckDao.Properties.RegionId.eq(regionId), EpackageTruckDao.Properties.Active.eq(1)).orderAsc(EpackageTruckDao.Properties.Description).list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

    public EpackageTruck TruckByEmei(long regionId, String Imei, int status){
        EpackageTruck epackageTruck = null;

        try {
            openReadableDb();
            EpackageTruckDao epackageTruckDao = daoSession.getEpackageTruckDao();
            epackageTruck = epackageTruckDao.queryBuilder().where(EpackageTruckDao.Properties.RegionId.eq(regionId), EpackageTruckDao.Properties.Imei.eq(Imei), EpackageTruckDao.Properties.Active.eq(status)).unique();
            daoSession.clear();
        }catch (Exception e){
            e.printStackTrace();
        }

        return epackageTruck;
    }

    @Override
    public List<TruckPackage> listEpackageByTruck(long truckId) {
        List<TruckPackage> list = null;
        try {
            openReadableDb();
            TruckPackageDao dao = daoSession.getTruckPackageDao();
            list = dao.queryBuilder().where(TruckPackageDao.Properties.TruckId.eq(truckId), TruckPackageDao.Properties.Status.eq(1)).list();
            //list = dao.queryBuilder().list();
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            return new ArrayList<>(list);
        }
        return null;
    }

}
