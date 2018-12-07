package com.tiendas3b.almacen.db.manager;

import com.tiendas3b.almacen.db.dao.Activity;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.db.dao.Barcode;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.Complaint;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.DecreaseMP;
import com.tiendas3b.almacen.db.dao.Driver;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageDownload;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.ExpressArticle;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.FMuestras;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.GlobalDecrease;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.OrderDetail;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.db.dao.OrderPicking;
import com.tiendas3b.almacen.db.dao.OrderPickingCapture;
import com.tiendas3b.almacen.db.dao.OrderType;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.ReceiptExpressLog;
import com.tiendas3b.almacen.db.dao.ReceiptSheet;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;
import com.tiendas3b.almacen.db.dao.ScannerCount;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.dao.TimetableInfo;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.dao.TruckPackage;
import com.tiendas3b.almacen.db.dao.VArticle;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.WhereCondition;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IDatabaseManager {

    /**
     * Closing available connections
     */
    void closeDbConnections();

    void closeDbConnectionsWithoutInstance();
    /**
     * Delete all tables and content from our database
     */
    void dropDatabase();

    <T> long insert(T o);

    <T> void insertOrReplaceInTx(T... array);

    <T> void insertOrReplaceInTx(List<T> list);

    <T> void update(T obj);

    <T> long insertOrUpdate(T obj);

    void insertOrUpdateBuys(List<Buy> list);

    void insertOrUpdateBuyDetails(List<BuyDetail> items);

    List<Buy> listBuys(Date date, long regionId);

    List<Buy> listBuys(String date, long regionId);

    List<Buy> listBuys(String date, long region, Integer... status);

    @SuppressWarnings("unchecked")
    <T> T getById(Long id, Class<T> clazz);

    List<ReceiptSheet> listReceiptSheets(String date, long regionId);

    List<ReceiptSheet> listReceiptSheets(Date date, long regionId);

    List<GlobalDecrease> listGlobalDecrease(String date, long regionId);

    List<GlobalDecrease> listGlobalDecrease(Date date1, Date date2, long regionId);

    List<VArticle> listViewArticlesDecrease();

    List<TimetableReceipt> listTimetable(int dayOfWeek, long warehouse);

    List<TimetableReceipt> listTimetable(Date date, long warehouse);

    TimetableInfo getTimetableInfo(Long regionId, String currentDate);

    <T> List<T> listAll(Class<T> clazz);

    List<Store> listStores(long region);

    List<Store> listStoresToPick(long region, int dayOfWeek);

    List<VArticle> listViewArticlesTransference();

    List<ObservationType> listObservationType(int typeId);

    List<InputTransference> listInputTransference(short type, long regionId);

    List<InputTransferenceDetail> listInputTransferenceDetail(Long inputTransferenceId);

    <T> void delete(Class<T> clazz, T... rows);

    List<ReceiptSheetDetail> listReceiptSheetDetails(Long timetableId);

    List<VArticle> listViewArticles(long vendorId);

    List<Provider> listSupplyVendors();

    VArticle findViewArticleByBarcode(String barcode);

    List<VArticle> listViewArticlesDecrease(Object... ds);

    void deleteByInputTransferenceId(long inputTransId);

    void deleteOrderBy(long storeId);

    void deleteOrderBy(Integer[] selectedPaybills);

    void deleteAllOrders();

    VArticle findViewArticleByIclave(long iclave);

    List<VArticle> findViewArticleByFatherIclave(int fatherIclave);

    List<ArticleResearch> listArticlesResearch(String date, long regionId);

    List<ScannerCount> listScannerCount(String dateStr, long region);

    List<Complaint> listComplaint(String dateStr, long region);

    List<Truck> listActiveTrucks(long regionId);

    List<Driver> listActiveDrivers(long region);

    Driver getDriverByDesc(String driverDesc);

    List<Roadmap> listRoadmap(long region, long truckId);

    List<RoadmapDetail> listRoadmapDetails(long roadmapId);

    <T> T getByDescription(String description, Class<T> clazz);

    List<Activity> listActivities(Long... ids);

    Roadmap findRoadmap(long regionId, Long driverId, long truckId, int travel, String dateStr);

    List<Store> listDiesel(long regionId);

    List<Roadmap> listRoadmapSync(boolean sync);

    void updateRoadmaps();

    <T> void truncate(Class<T> clazz);

    long findBuyByTimetableId(long timetableId, long regionId);

    Buy findBuyByFolio(int odc, long regionId);

    BuyDetail findBuyDetailByArticle(int iclave, long buyId);

    List<ObservationType> listActiveObservationType();

    List<ComplaintCat> listComplaintBy(int type);

    VArticle findViewArticleForPurchasing(Integer iclave, String barcode);

    long findViewArticleIdForPurchasing(Integer iclave, String barcode);

    long findViewArticleIdForPicking(int iclave);

    ReceiptSheetCapture findReceiptSheetCaptureBy(long buyId);

    List<OrderDetail> listOrderDetails(long storeId);

    List<OrderDetail> listOrderDetails(Integer[] selectedPaybills);

    List<OrderPicking> listOrders(long storeId);

    List<EPackage> listEPackages(int status);

    List<EPackage> listEPackages(long providerId, int status, String dateStr);

    List<EPackage> listEPackagesStore(long StoreId, int status, String dateStr);

    List<EPackage> listEPackages(int status, String dateStr);

    List<EPackage> listEPackages(int status, long storeId);

    List<EPackage> listEPackagesDownload(int status, long storeId);

    List<EpackageDownload> listEPackagesDownload(int status);

    List<EpackageTruck> listActiveEpackageTruck(long regionId);

    List<TruckPackage> listEpackageByTruck(long truckId);

    EpackageTruck TruckByEmei(long regionId, String Imei, int status);

    <T> boolean isEmpty(Class<T> clazz);

    List<Provider> listSupplyActiveVendors();


    <T> long insertOrUpdate(T obj, WhereCondition whereCondition, WhereCondition... whereConditions);

    long insertOrUpdateOrderPicking(OrderPicking obj);

    <T, S> void insertOrUpdateWithDetails(List<T> master, Class<S> detail);

    OrderPickingCapture findOrderCapture(long regionId, long storeId, int paybill);

    List<OrderPickingCapture> findOrderCapture(long regionId, long storeId);

    List<OrderDetailCapture> findOrderDetails(long regionId, long storeId);

    //    @Deprecated
    List<ExpressReceipt> listExpressReceiptByFolio(int odc, long regionId, boolean received);

    ExpressReceipt findExpressReceipt(String barcode);

    List<ExpressReceipt> findExpressReceiptList(int odc, long iclave);

    int getPiecesByUnit(int uclave, long iclave);

    EPackage findEpackageBy(String barcode);

    EPackage findEpackageById(long packageId);

    EPackage findEpackageByDate(String barcode, String selectedDate);

    ProviderEpackage findProviderEpackageBy(long id);

    TruckPackage findTruckPackageByIdPackage(long id);

    EpackageTruck findEpackageTruckbyIdTruck(long id);

    EpackageTruck findEpackageTruckbyId(long id);

    EpackageTruck findEpackageTruckbyBarcode(String barcode);

    List<ProviderEpackage> findProvidersBy(String date, long region);

    PositionPackage getBy(Long ePackageId);

    Position findPosition(long areaId, long rackId, long levelId, long positionId);

    List<Area> listAreas(long regionId);

    OrderDetail findOrderDetail(long orderDetailId);

    List<OrderType> findOrdersAvailable(long regionId);

    String findOrderTypeByType(long region, int type);

    List<Buy> listBuysExpress(String dateStr, long region, Integer... status);

    boolean providerAutomatic(long providerId);

    ExpressProvider findExpressProvider(Long id);

    List<OrderDetailCapture> findOrderDetailsByFatherIclave(long regionId, long storeId, Integer fatherIclave);

    ExpressArticle findExpressArticle(long regionId, int odc, long iclave);

    ExpressReceipt findExpressReceipt(long regionId, int odc, long iclave, short palletId);

    List<ExpressReceipt> listExpressReceiptByOdc(long regionId, int odc);

    List<ExpressArticle> listExpressArticles(long regionId, int odc);

    List<Store> listStoresToPick(long region, int day, int type, int position);

    List<StorePickingStatus> listStoresStatusPick(long regionId, String date, int type, int status);

    List<ReceiptExpressLog> findReceiptExpressLog(String idAlmacen, Long idProveedor, Integer odc);

    Barcode findIclaveBarcode(String barcodes);

    List<DecreaseMP> findDecreaseMP(Integer odc);

    List<FMuestras> findFMuestras(Integer odc);
    ShipmentControl findShipmentControlByDate(Date date);

    List<Trip> findTrips(Date date, long truckId, long regionId);

    Trip findNextTrip(long truckId);

    Trip findTripById(long tripId);

    List<TripDetail> listTripDetails(long tripId);

    Form findFormById(long formId);

    TripDetail findTripDetailById(long tripDetailId);

    TripDetail findNextTripDetail(long tripId, int... statuses);
}
