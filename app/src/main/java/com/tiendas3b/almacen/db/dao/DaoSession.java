package com.tiendas3b.almacen.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.User;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.Rack;
import com.tiendas3b.almacen.db.dao.RackLevel;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.EpackageDownload;
import com.tiendas3b.almacen.db.dao.TruckPackage;
import com.tiendas3b.almacen.db.dao.OrderType;
import com.tiendas3b.almacen.db.dao.Level;
import com.tiendas3b.almacen.db.dao.ArticleState;
import com.tiendas3b.almacen.db.dao.ReceiptSheet;
import com.tiendas3b.almacen.db.dao.DateType;
import com.tiendas3b.almacen.db.dao.Unit;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.dao.OrderPicking;
import com.tiendas3b.almacen.db.dao.OrderDetail;
import com.tiendas3b.almacen.db.dao.OrderPickingCapture;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.Check;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.ExpressArticle;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;
import com.tiendas3b.almacen.db.dao.GlobalDecrease;
import com.tiendas3b.almacen.db.dao.Activity;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.dao.Driver;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;
import com.tiendas3b.almacen.db.dao.Route;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;
import com.tiendas3b.almacen.db.dao.Utilization;
import com.tiendas3b.almacen.db.dao.TimetableInfo;
import com.tiendas3b.almacen.db.dao.TimetablePicking;
import com.tiendas3b.almacen.db.dao.PendingDocument;
import com.tiendas3b.almacen.db.dao.PendingDocumentDetail;
import com.tiendas3b.almacen.db.dao.ArticleTransference;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.db.dao.Complaint;
import com.tiendas3b.almacen.db.dao.ScannerCount;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.ArticleDecrease;
import com.tiendas3b.almacen.db.dao.Message;
import com.tiendas3b.almacen.db.dao.ReceiptExpressLog;
import com.tiendas3b.almacen.db.dao.Barcode;
import com.tiendas3b.almacen.db.dao.DecreaseMP;
import com.tiendas3b.almacen.db.dao.FMuestras;
import com.tiendas3b.almacen.db.dao.ReceiptComments;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.dao.Question;
import com.tiendas3b.almacen.db.dao.Answer;
import com.tiendas3b.almacen.db.dao.QuestionAnswer;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.FormQuestion;
import com.tiendas3b.almacen.db.dao.FormApplied;
import com.tiendas3b.almacen.db.dao.FormAppliedResult;
import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.dao.ShipmentControl;

import com.tiendas3b.almacen.db.dao.RegionDao;
import com.tiendas3b.almacen.db.dao.UserDao;
import com.tiendas3b.almacen.db.dao.ConfigProviderDao;
import com.tiendas3b.almacen.db.dao.ProviderDao;
import com.tiendas3b.almacen.db.dao.TimetableReceiptDao;
import com.tiendas3b.almacen.db.dao.StoreDao;
import com.tiendas3b.almacen.db.dao.ProviderEpackageDao;
import com.tiendas3b.almacen.db.dao.EPackageDao;
import com.tiendas3b.almacen.db.dao.AreaDao;
import com.tiendas3b.almacen.db.dao.RackDao;
import com.tiendas3b.almacen.db.dao.RackLevelDao;
import com.tiendas3b.almacen.db.dao.PositionDao;
import com.tiendas3b.almacen.db.dao.PositionPackageDao;
import com.tiendas3b.almacen.db.dao.EpackageTruckDao;
import com.tiendas3b.almacen.db.dao.EpackageDownloadDao;
import com.tiendas3b.almacen.db.dao.TruckPackageDao;
import com.tiendas3b.almacen.db.dao.OrderTypeDao;
import com.tiendas3b.almacen.db.dao.LevelDao;
import com.tiendas3b.almacen.db.dao.ArticleStateDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDao;
import com.tiendas3b.almacen.db.dao.DateTypeDao;
import com.tiendas3b.almacen.db.dao.UnitDao;
import com.tiendas3b.almacen.db.dao.VArticleDao;
import com.tiendas3b.almacen.db.dao.StorePickingStatusDao;
import com.tiendas3b.almacen.db.dao.OrderPickingDao;
import com.tiendas3b.almacen.db.dao.OrderDetailDao;
import com.tiendas3b.almacen.db.dao.OrderPickingCaptureDao;
import com.tiendas3b.almacen.db.dao.OrderDetailCaptureDao;
import com.tiendas3b.almacen.db.dao.ComplaintCatDao;
import com.tiendas3b.almacen.db.dao.CheckDao;
import com.tiendas3b.almacen.db.dao.BuyDetailDao;
import com.tiendas3b.almacen.db.dao.ExpressReceiptDao;
import com.tiendas3b.almacen.db.dao.ExpressProviderDao;
import com.tiendas3b.almacen.db.dao.ExpressArticleDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCaptureDao;
import com.tiendas3b.almacen.db.dao.BuyDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCaptureDao;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailDao;
import com.tiendas3b.almacen.db.dao.GlobalDecreaseDao;
import com.tiendas3b.almacen.db.dao.ActivityDao;
import com.tiendas3b.almacen.db.dao.TruckDao;
import com.tiendas3b.almacen.db.dao.DriverDao;
import com.tiendas3b.almacen.db.dao.RoadmapDao;
import com.tiendas3b.almacen.db.dao.RoadmapDetailDao;
import com.tiendas3b.almacen.db.dao.RouteDao;
import com.tiendas3b.almacen.db.dao.InputTransferenceDao;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetailDao;
import com.tiendas3b.almacen.db.dao.UtilizationDao;
import com.tiendas3b.almacen.db.dao.TimetableInfoDao;
import com.tiendas3b.almacen.db.dao.TimetablePickingDao;
import com.tiendas3b.almacen.db.dao.PendingDocumentDao;
import com.tiendas3b.almacen.db.dao.PendingDocumentDetailDao;
import com.tiendas3b.almacen.db.dao.ArticleTransferenceDao;
import com.tiendas3b.almacen.db.dao.ArticleResearchDao;
import com.tiendas3b.almacen.db.dao.ComplaintDao;
import com.tiendas3b.almacen.db.dao.ScannerCountDao;
import com.tiendas3b.almacen.db.dao.ObservationTypeDao;
import com.tiendas3b.almacen.db.dao.ArticleDecreaseDao;
import com.tiendas3b.almacen.db.dao.MessageDao;
import com.tiendas3b.almacen.db.dao.ReceiptExpressLogDao;
import com.tiendas3b.almacen.db.dao.BarcodeDao;
import com.tiendas3b.almacen.db.dao.DecreaseMPDao;
import com.tiendas3b.almacen.db.dao.FMuestrasDao;
import com.tiendas3b.almacen.db.dao.ReceiptCommentsDao;
import com.tiendas3b.almacen.db.dao.TripDao;
import com.tiendas3b.almacen.db.dao.TripDetailDao;
import com.tiendas3b.almacen.db.dao.QuestionDao;
import com.tiendas3b.almacen.db.dao.AnswerDao;
import com.tiendas3b.almacen.db.dao.QuestionAnswerDao;
import com.tiendas3b.almacen.db.dao.FormDao;
import com.tiendas3b.almacen.db.dao.FormQuestionDao;
import com.tiendas3b.almacen.db.dao.FormAppliedDao;
import com.tiendas3b.almacen.db.dao.FormAppliedResultDao;
import com.tiendas3b.almacen.db.dao.SheetTripDao;
import com.tiendas3b.almacen.db.dao.ShipmentControlDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig regionDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig configProviderDaoConfig;
    private final DaoConfig providerDaoConfig;
    private final DaoConfig timetableReceiptDaoConfig;
    private final DaoConfig storeDaoConfig;
    private final DaoConfig providerEpackageDaoConfig;
    private final DaoConfig ePackageDaoConfig;
    private final DaoConfig areaDaoConfig;
    private final DaoConfig rackDaoConfig;
    private final DaoConfig rackLevelDaoConfig;
    private final DaoConfig positionDaoConfig;
    private final DaoConfig positionPackageDaoConfig;
    private final DaoConfig epackageTruckDaoConfig;
    private final DaoConfig epackageDownloadDaoConfig;
    private final DaoConfig truckPackageDaoConfig;
    private final DaoConfig orderTypeDaoConfig;
    private final DaoConfig levelDaoConfig;
    private final DaoConfig articleStateDaoConfig;
    private final DaoConfig receiptSheetDaoConfig;
    private final DaoConfig dateTypeDaoConfig;
    private final DaoConfig unitDaoConfig;
    private final DaoConfig vArticleDaoConfig;
    private final DaoConfig storePickingStatusDaoConfig;
    private final DaoConfig orderPickingDaoConfig;
    private final DaoConfig orderDetailDaoConfig;
    private final DaoConfig orderPickingCaptureDaoConfig;
    private final DaoConfig orderDetailCaptureDaoConfig;
    private final DaoConfig complaintCatDaoConfig;
    private final DaoConfig checkDaoConfig;
    private final DaoConfig buyDetailDaoConfig;
    private final DaoConfig expressReceiptDaoConfig;
    private final DaoConfig expressProviderDaoConfig;
    private final DaoConfig expressArticleDaoConfig;
    private final DaoConfig receiptSheetDetailCaptureDaoConfig;
    private final DaoConfig buyDaoConfig;
    private final DaoConfig receiptSheetCaptureDaoConfig;
    private final DaoConfig receiptSheetDetailDaoConfig;
    private final DaoConfig globalDecreaseDaoConfig;
    private final DaoConfig activityDaoConfig;
    private final DaoConfig truckDaoConfig;
    private final DaoConfig driverDaoConfig;
    private final DaoConfig roadmapDaoConfig;
    private final DaoConfig roadmapDetailDaoConfig;
    private final DaoConfig routeDaoConfig;
    private final DaoConfig inputTransferenceDaoConfig;
    private final DaoConfig inputTransferenceDetailDaoConfig;
    private final DaoConfig utilizationDaoConfig;
    private final DaoConfig timetableInfoDaoConfig;
    private final DaoConfig timetablePickingDaoConfig;
    private final DaoConfig pendingDocumentDaoConfig;
    private final DaoConfig pendingDocumentDetailDaoConfig;
    private final DaoConfig articleTransferenceDaoConfig;
    private final DaoConfig articleResearchDaoConfig;
    private final DaoConfig complaintDaoConfig;
    private final DaoConfig scannerCountDaoConfig;
    private final DaoConfig observationTypeDaoConfig;
    private final DaoConfig articleDecreaseDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig receiptExpressLogDaoConfig;
    private final DaoConfig barcodeDaoConfig;
    private final DaoConfig decreaseMPDaoConfig;
    private final DaoConfig fMuestrasDaoConfig;
    private final DaoConfig receiptCommentsDaoConfig;
    private final DaoConfig tripDaoConfig;
    private final DaoConfig tripDetailDaoConfig;
    private final DaoConfig questionDaoConfig;
    private final DaoConfig answerDaoConfig;
    private final DaoConfig questionAnswerDaoConfig;
    private final DaoConfig formDaoConfig;
    private final DaoConfig formQuestionDaoConfig;
    private final DaoConfig formAppliedDaoConfig;
    private final DaoConfig formAppliedResultDaoConfig;
    private final DaoConfig sheetTripDaoConfig;
    private final DaoConfig shipmentControlDaoConfig;

    private final RegionDao regionDao;
    private final UserDao userDao;
    private final ConfigProviderDao configProviderDao;
    private final ProviderDao providerDao;
    private final TimetableReceiptDao timetableReceiptDao;
    private final StoreDao storeDao;
    private final ProviderEpackageDao providerEpackageDao;
    private final EPackageDao ePackageDao;
    private final AreaDao areaDao;
    private final RackDao rackDao;
    private final RackLevelDao rackLevelDao;
    private final PositionDao positionDao;
    private final PositionPackageDao positionPackageDao;
    private final EpackageTruckDao epackageTruckDao;
    private final EpackageDownloadDao epackageDownloadDao;
    private final TruckPackageDao truckPackageDao;
    private final OrderTypeDao orderTypeDao;
    private final LevelDao levelDao;
    private final ArticleStateDao articleStateDao;
    private final ReceiptSheetDao receiptSheetDao;
    private final DateTypeDao dateTypeDao;
    private final UnitDao unitDao;
    private final VArticleDao vArticleDao;
    private final StorePickingStatusDao storePickingStatusDao;
    private final OrderPickingDao orderPickingDao;
    private final OrderDetailDao orderDetailDao;
    private final OrderPickingCaptureDao orderPickingCaptureDao;
    private final OrderDetailCaptureDao orderDetailCaptureDao;
    private final ComplaintCatDao complaintCatDao;
    private final CheckDao checkDao;
    private final BuyDetailDao buyDetailDao;
    private final ExpressReceiptDao expressReceiptDao;
    private final ExpressProviderDao expressProviderDao;
    private final ExpressArticleDao expressArticleDao;
    private final ReceiptSheetDetailCaptureDao receiptSheetDetailCaptureDao;
    private final BuyDao buyDao;
    private final ReceiptSheetCaptureDao receiptSheetCaptureDao;
    private final ReceiptSheetDetailDao receiptSheetDetailDao;
    private final GlobalDecreaseDao globalDecreaseDao;
    private final ActivityDao activityDao;
    private final TruckDao truckDao;
    private final DriverDao driverDao;
    private final RoadmapDao roadmapDao;
    private final RoadmapDetailDao roadmapDetailDao;
    private final RouteDao routeDao;
    private final InputTransferenceDao inputTransferenceDao;
    private final InputTransferenceDetailDao inputTransferenceDetailDao;
    private final UtilizationDao utilizationDao;
    private final TimetableInfoDao timetableInfoDao;
    private final TimetablePickingDao timetablePickingDao;
    private final PendingDocumentDao pendingDocumentDao;
    private final PendingDocumentDetailDao pendingDocumentDetailDao;
    private final ArticleTransferenceDao articleTransferenceDao;
    private final ArticleResearchDao articleResearchDao;
    private final ComplaintDao complaintDao;
    private final ScannerCountDao scannerCountDao;
    private final ObservationTypeDao observationTypeDao;
    private final ArticleDecreaseDao articleDecreaseDao;
    private final MessageDao messageDao;
    private final ReceiptExpressLogDao receiptExpressLogDao;
    private final BarcodeDao barcodeDao;
    private final DecreaseMPDao decreaseMPDao;
    private final FMuestrasDao fMuestrasDao;
    private final ReceiptCommentsDao receiptCommentsDao;
    private final TripDao tripDao;
    private final TripDetailDao tripDetailDao;
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final QuestionAnswerDao questionAnswerDao;
    private final FormDao formDao;
    private final FormQuestionDao formQuestionDao;
    private final FormAppliedDao formAppliedDao;
    private final FormAppliedResultDao formAppliedResultDao;
    private final SheetTripDao sheetTripDao;
    private final ShipmentControlDao shipmentControlDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        regionDaoConfig = daoConfigMap.get(RegionDao.class).clone();
        regionDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        configProviderDaoConfig = daoConfigMap.get(ConfigProviderDao.class).clone();
        configProviderDaoConfig.initIdentityScope(type);

        providerDaoConfig = daoConfigMap.get(ProviderDao.class).clone();
        providerDaoConfig.initIdentityScope(type);

        timetableReceiptDaoConfig = daoConfigMap.get(TimetableReceiptDao.class).clone();
        timetableReceiptDaoConfig.initIdentityScope(type);

        storeDaoConfig = daoConfigMap.get(StoreDao.class).clone();
        storeDaoConfig.initIdentityScope(type);

        providerEpackageDaoConfig = daoConfigMap.get(ProviderEpackageDao.class).clone();
        providerEpackageDaoConfig.initIdentityScope(type);

        ePackageDaoConfig = daoConfigMap.get(EPackageDao.class).clone();
        ePackageDaoConfig.initIdentityScope(type);

        areaDaoConfig = daoConfigMap.get(AreaDao.class).clone();
        areaDaoConfig.initIdentityScope(type);

        rackDaoConfig = daoConfigMap.get(RackDao.class).clone();
        rackDaoConfig.initIdentityScope(type);

        rackLevelDaoConfig = daoConfigMap.get(RackLevelDao.class).clone();
        rackLevelDaoConfig.initIdentityScope(type);

        positionDaoConfig = daoConfigMap.get(PositionDao.class).clone();
        positionDaoConfig.initIdentityScope(type);

        positionPackageDaoConfig = daoConfigMap.get(PositionPackageDao.class).clone();
        positionPackageDaoConfig.initIdentityScope(type);

        epackageTruckDaoConfig = daoConfigMap.get(EpackageTruckDao.class).clone();
        epackageTruckDaoConfig.initIdentityScope(type);

        epackageDownloadDaoConfig = daoConfigMap.get(EpackageDownloadDao.class).clone();
        epackageDownloadDaoConfig.initIdentityScope(type);

        truckPackageDaoConfig = daoConfigMap.get(TruckPackageDao.class).clone();
        truckPackageDaoConfig.initIdentityScope(type);

        orderTypeDaoConfig = daoConfigMap.get(OrderTypeDao.class).clone();
        orderTypeDaoConfig.initIdentityScope(type);

        levelDaoConfig = daoConfigMap.get(LevelDao.class).clone();
        levelDaoConfig.initIdentityScope(type);

        articleStateDaoConfig = daoConfigMap.get(ArticleStateDao.class).clone();
        articleStateDaoConfig.initIdentityScope(type);

        receiptSheetDaoConfig = daoConfigMap.get(ReceiptSheetDao.class).clone();
        receiptSheetDaoConfig.initIdentityScope(type);

        dateTypeDaoConfig = daoConfigMap.get(DateTypeDao.class).clone();
        dateTypeDaoConfig.initIdentityScope(type);

        unitDaoConfig = daoConfigMap.get(UnitDao.class).clone();
        unitDaoConfig.initIdentityScope(type);

        vArticleDaoConfig = daoConfigMap.get(VArticleDao.class).clone();
        vArticleDaoConfig.initIdentityScope(type);

        storePickingStatusDaoConfig = daoConfigMap.get(StorePickingStatusDao.class).clone();
        storePickingStatusDaoConfig.initIdentityScope(type);

        orderPickingDaoConfig = daoConfigMap.get(OrderPickingDao.class).clone();
        orderPickingDaoConfig.initIdentityScope(type);

        orderDetailDaoConfig = daoConfigMap.get(OrderDetailDao.class).clone();
        orderDetailDaoConfig.initIdentityScope(type);

        orderPickingCaptureDaoConfig = daoConfigMap.get(OrderPickingCaptureDao.class).clone();
        orderPickingCaptureDaoConfig.initIdentityScope(type);

        orderDetailCaptureDaoConfig = daoConfigMap.get(OrderDetailCaptureDao.class).clone();
        orderDetailCaptureDaoConfig.initIdentityScope(type);

        complaintCatDaoConfig = daoConfigMap.get(ComplaintCatDao.class).clone();
        complaintCatDaoConfig.initIdentityScope(type);

        checkDaoConfig = daoConfigMap.get(CheckDao.class).clone();
        checkDaoConfig.initIdentityScope(type);

        buyDetailDaoConfig = daoConfigMap.get(BuyDetailDao.class).clone();
        buyDetailDaoConfig.initIdentityScope(type);

        expressReceiptDaoConfig = daoConfigMap.get(ExpressReceiptDao.class).clone();
        expressReceiptDaoConfig.initIdentityScope(type);

        expressProviderDaoConfig = daoConfigMap.get(ExpressProviderDao.class).clone();
        expressProviderDaoConfig.initIdentityScope(type);

        expressArticleDaoConfig = daoConfigMap.get(ExpressArticleDao.class).clone();
        expressArticleDaoConfig.initIdentityScope(type);

        receiptSheetDetailCaptureDaoConfig = daoConfigMap.get(ReceiptSheetDetailCaptureDao.class).clone();
        receiptSheetDetailCaptureDaoConfig.initIdentityScope(type);

        buyDaoConfig = daoConfigMap.get(BuyDao.class).clone();
        buyDaoConfig.initIdentityScope(type);

        receiptSheetCaptureDaoConfig = daoConfigMap.get(ReceiptSheetCaptureDao.class).clone();
        receiptSheetCaptureDaoConfig.initIdentityScope(type);

        receiptSheetDetailDaoConfig = daoConfigMap.get(ReceiptSheetDetailDao.class).clone();
        receiptSheetDetailDaoConfig.initIdentityScope(type);

        globalDecreaseDaoConfig = daoConfigMap.get(GlobalDecreaseDao.class).clone();
        globalDecreaseDaoConfig.initIdentityScope(type);

        activityDaoConfig = daoConfigMap.get(ActivityDao.class).clone();
        activityDaoConfig.initIdentityScope(type);

        truckDaoConfig = daoConfigMap.get(TruckDao.class).clone();
        truckDaoConfig.initIdentityScope(type);

        driverDaoConfig = daoConfigMap.get(DriverDao.class).clone();
        driverDaoConfig.initIdentityScope(type);

        roadmapDaoConfig = daoConfigMap.get(RoadmapDao.class).clone();
        roadmapDaoConfig.initIdentityScope(type);

        roadmapDetailDaoConfig = daoConfigMap.get(RoadmapDetailDao.class).clone();
        roadmapDetailDaoConfig.initIdentityScope(type);

        routeDaoConfig = daoConfigMap.get(RouteDao.class).clone();
        routeDaoConfig.initIdentityScope(type);

        inputTransferenceDaoConfig = daoConfigMap.get(InputTransferenceDao.class).clone();
        inputTransferenceDaoConfig.initIdentityScope(type);

        inputTransferenceDetailDaoConfig = daoConfigMap.get(InputTransferenceDetailDao.class).clone();
        inputTransferenceDetailDaoConfig.initIdentityScope(type);

        utilizationDaoConfig = daoConfigMap.get(UtilizationDao.class).clone();
        utilizationDaoConfig.initIdentityScope(type);

        timetableInfoDaoConfig = daoConfigMap.get(TimetableInfoDao.class).clone();
        timetableInfoDaoConfig.initIdentityScope(type);

        timetablePickingDaoConfig = daoConfigMap.get(TimetablePickingDao.class).clone();
        timetablePickingDaoConfig.initIdentityScope(type);

        pendingDocumentDaoConfig = daoConfigMap.get(PendingDocumentDao.class).clone();
        pendingDocumentDaoConfig.initIdentityScope(type);

        pendingDocumentDetailDaoConfig = daoConfigMap.get(PendingDocumentDetailDao.class).clone();
        pendingDocumentDetailDaoConfig.initIdentityScope(type);

        articleTransferenceDaoConfig = daoConfigMap.get(ArticleTransferenceDao.class).clone();
        articleTransferenceDaoConfig.initIdentityScope(type);

        articleResearchDaoConfig = daoConfigMap.get(ArticleResearchDao.class).clone();
        articleResearchDaoConfig.initIdentityScope(type);

        complaintDaoConfig = daoConfigMap.get(ComplaintDao.class).clone();
        complaintDaoConfig.initIdentityScope(type);

        scannerCountDaoConfig = daoConfigMap.get(ScannerCountDao.class).clone();
        scannerCountDaoConfig.initIdentityScope(type);

        observationTypeDaoConfig = daoConfigMap.get(ObservationTypeDao.class).clone();
        observationTypeDaoConfig.initIdentityScope(type);

        articleDecreaseDaoConfig = daoConfigMap.get(ArticleDecreaseDao.class).clone();
        articleDecreaseDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        receiptExpressLogDaoConfig = daoConfigMap.get(ReceiptExpressLogDao.class).clone();
        receiptExpressLogDaoConfig.initIdentityScope(type);

        barcodeDaoConfig = daoConfigMap.get(BarcodeDao.class).clone();
        barcodeDaoConfig.initIdentityScope(type);

        decreaseMPDaoConfig = daoConfigMap.get(DecreaseMPDao.class).clone();
        decreaseMPDaoConfig.initIdentityScope(type);

        fMuestrasDaoConfig = daoConfigMap.get(FMuestrasDao.class).clone();
        fMuestrasDaoConfig.initIdentityScope(type);

        receiptCommentsDaoConfig = daoConfigMap.get(ReceiptCommentsDao.class).clone();
        receiptCommentsDaoConfig.initIdentityScope(type);

        tripDaoConfig = daoConfigMap.get(TripDao.class).clone();
        tripDaoConfig.initIdentityScope(type);

        tripDetailDaoConfig = daoConfigMap.get(TripDetailDao.class).clone();
        tripDetailDaoConfig.initIdentityScope(type);

        questionDaoConfig = daoConfigMap.get(QuestionDao.class).clone();
        questionDaoConfig.initIdentityScope(type);

        answerDaoConfig = daoConfigMap.get(AnswerDao.class).clone();
        answerDaoConfig.initIdentityScope(type);

        questionAnswerDaoConfig = daoConfigMap.get(QuestionAnswerDao.class).clone();
        questionAnswerDaoConfig.initIdentityScope(type);

        formDaoConfig = daoConfigMap.get(FormDao.class).clone();
        formDaoConfig.initIdentityScope(type);

        formQuestionDaoConfig = daoConfigMap.get(FormQuestionDao.class).clone();
        formQuestionDaoConfig.initIdentityScope(type);

        formAppliedDaoConfig = daoConfigMap.get(FormAppliedDao.class).clone();
        formAppliedDaoConfig.initIdentityScope(type);

        formAppliedResultDaoConfig = daoConfigMap.get(FormAppliedResultDao.class).clone();
        formAppliedResultDaoConfig.initIdentityScope(type);

        sheetTripDaoConfig = daoConfigMap.get(SheetTripDao.class).clone();
        sheetTripDaoConfig.initIdentityScope(type);

        shipmentControlDaoConfig = daoConfigMap.get(ShipmentControlDao.class).clone();
        shipmentControlDaoConfig.initIdentityScope(type);

        regionDao = new RegionDao(regionDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        configProviderDao = new ConfigProviderDao(configProviderDaoConfig, this);
        providerDao = new ProviderDao(providerDaoConfig, this);
        timetableReceiptDao = new TimetableReceiptDao(timetableReceiptDaoConfig, this);
        storeDao = new StoreDao(storeDaoConfig, this);
        providerEpackageDao = new ProviderEpackageDao(providerEpackageDaoConfig, this);
        ePackageDao = new EPackageDao(ePackageDaoConfig, this);
        areaDao = new AreaDao(areaDaoConfig, this);
        rackDao = new RackDao(rackDaoConfig, this);
        rackLevelDao = new RackLevelDao(rackLevelDaoConfig, this);
        positionDao = new PositionDao(positionDaoConfig, this);
        positionPackageDao = new PositionPackageDao(positionPackageDaoConfig, this);
        epackageTruckDao = new EpackageTruckDao(epackageTruckDaoConfig, this);
        epackageDownloadDao = new EpackageDownloadDao(epackageDownloadDaoConfig, this);
        truckPackageDao = new TruckPackageDao(truckPackageDaoConfig, this);
        orderTypeDao = new OrderTypeDao(orderTypeDaoConfig, this);
        levelDao = new LevelDao(levelDaoConfig, this);
        articleStateDao = new ArticleStateDao(articleStateDaoConfig, this);
        receiptSheetDao = new ReceiptSheetDao(receiptSheetDaoConfig, this);
        dateTypeDao = new DateTypeDao(dateTypeDaoConfig, this);
        unitDao = new UnitDao(unitDaoConfig, this);
        vArticleDao = new VArticleDao(vArticleDaoConfig, this);
        storePickingStatusDao = new StorePickingStatusDao(storePickingStatusDaoConfig, this);
        orderPickingDao = new OrderPickingDao(orderPickingDaoConfig, this);
        orderDetailDao = new OrderDetailDao(orderDetailDaoConfig, this);
        orderPickingCaptureDao = new OrderPickingCaptureDao(orderPickingCaptureDaoConfig, this);
        orderDetailCaptureDao = new OrderDetailCaptureDao(orderDetailCaptureDaoConfig, this);
        complaintCatDao = new ComplaintCatDao(complaintCatDaoConfig, this);
        checkDao = new CheckDao(checkDaoConfig, this);
        buyDetailDao = new BuyDetailDao(buyDetailDaoConfig, this);
        expressReceiptDao = new ExpressReceiptDao(expressReceiptDaoConfig, this);
        expressProviderDao = new ExpressProviderDao(expressProviderDaoConfig, this);
        expressArticleDao = new ExpressArticleDao(expressArticleDaoConfig, this);
        receiptSheetDetailCaptureDao = new ReceiptSheetDetailCaptureDao(receiptSheetDetailCaptureDaoConfig, this);
        buyDao = new BuyDao(buyDaoConfig, this);
        receiptSheetCaptureDao = new ReceiptSheetCaptureDao(receiptSheetCaptureDaoConfig, this);
        receiptSheetDetailDao = new ReceiptSheetDetailDao(receiptSheetDetailDaoConfig, this);
        globalDecreaseDao = new GlobalDecreaseDao(globalDecreaseDaoConfig, this);
        activityDao = new ActivityDao(activityDaoConfig, this);
        truckDao = new TruckDao(truckDaoConfig, this);
        driverDao = new DriverDao(driverDaoConfig, this);
        roadmapDao = new RoadmapDao(roadmapDaoConfig, this);
        roadmapDetailDao = new RoadmapDetailDao(roadmapDetailDaoConfig, this);
        routeDao = new RouteDao(routeDaoConfig, this);
        inputTransferenceDao = new InputTransferenceDao(inputTransferenceDaoConfig, this);
        inputTransferenceDetailDao = new InputTransferenceDetailDao(inputTransferenceDetailDaoConfig, this);
        utilizationDao = new UtilizationDao(utilizationDaoConfig, this);
        timetableInfoDao = new TimetableInfoDao(timetableInfoDaoConfig, this);
        timetablePickingDao = new TimetablePickingDao(timetablePickingDaoConfig, this);
        pendingDocumentDao = new PendingDocumentDao(pendingDocumentDaoConfig, this);
        pendingDocumentDetailDao = new PendingDocumentDetailDao(pendingDocumentDetailDaoConfig, this);
        articleTransferenceDao = new ArticleTransferenceDao(articleTransferenceDaoConfig, this);
        articleResearchDao = new ArticleResearchDao(articleResearchDaoConfig, this);
        complaintDao = new ComplaintDao(complaintDaoConfig, this);
        scannerCountDao = new ScannerCountDao(scannerCountDaoConfig, this);
        observationTypeDao = new ObservationTypeDao(observationTypeDaoConfig, this);
        articleDecreaseDao = new ArticleDecreaseDao(articleDecreaseDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        receiptExpressLogDao = new ReceiptExpressLogDao(receiptExpressLogDaoConfig, this);
        barcodeDao = new BarcodeDao(barcodeDaoConfig, this);
        decreaseMPDao = new DecreaseMPDao(decreaseMPDaoConfig, this);
        fMuestrasDao = new FMuestrasDao(fMuestrasDaoConfig, this);
        receiptCommentsDao = new ReceiptCommentsDao(receiptCommentsDaoConfig, this);
        tripDao = new TripDao(tripDaoConfig, this);
        tripDetailDao = new TripDetailDao(tripDetailDaoConfig, this);
        questionDao = new QuestionDao(questionDaoConfig, this);
        answerDao = new AnswerDao(answerDaoConfig, this);
        questionAnswerDao = new QuestionAnswerDao(questionAnswerDaoConfig, this);
        formDao = new FormDao(formDaoConfig, this);
        formQuestionDao = new FormQuestionDao(formQuestionDaoConfig, this);
        formAppliedDao = new FormAppliedDao(formAppliedDaoConfig, this);
        formAppliedResultDao = new FormAppliedResultDao(formAppliedResultDaoConfig, this);
        sheetTripDao = new SheetTripDao(sheetTripDaoConfig, this);
        shipmentControlDao = new ShipmentControlDao(shipmentControlDaoConfig, this);

        registerDao(Region.class, regionDao);
        registerDao(User.class, userDao);
        registerDao(ConfigProvider.class, configProviderDao);
        registerDao(Provider.class, providerDao);
        registerDao(TimetableReceipt.class, timetableReceiptDao);
        registerDao(Store.class, storeDao);
        registerDao(ProviderEpackage.class, providerEpackageDao);
        registerDao(EPackage.class, ePackageDao);
        registerDao(Area.class, areaDao);
        registerDao(Rack.class, rackDao);
        registerDao(RackLevel.class, rackLevelDao);
        registerDao(Position.class, positionDao);
        registerDao(PositionPackage.class, positionPackageDao);
        registerDao(EpackageTruck.class, epackageTruckDao);
        registerDao(EpackageDownload.class, epackageDownloadDao);
        registerDao(TruckPackage.class, truckPackageDao);
        registerDao(OrderType.class, orderTypeDao);
        registerDao(Level.class, levelDao);
        registerDao(ArticleState.class, articleStateDao);
        registerDao(ReceiptSheet.class, receiptSheetDao);
        registerDao(DateType.class, dateTypeDao);
        registerDao(Unit.class, unitDao);
        registerDao(VArticle.class, vArticleDao);
        registerDao(StorePickingStatus.class, storePickingStatusDao);
        registerDao(OrderPicking.class, orderPickingDao);
        registerDao(OrderDetail.class, orderDetailDao);
        registerDao(OrderPickingCapture.class, orderPickingCaptureDao);
        registerDao(OrderDetailCapture.class, orderDetailCaptureDao);
        registerDao(ComplaintCat.class, complaintCatDao);
        registerDao(Check.class, checkDao);
        registerDao(BuyDetail.class, buyDetailDao);
        registerDao(ExpressReceipt.class, expressReceiptDao);
        registerDao(ExpressProvider.class, expressProviderDao);
        registerDao(ExpressArticle.class, expressArticleDao);
        registerDao(ReceiptSheetDetailCapture.class, receiptSheetDetailCaptureDao);
        registerDao(Buy.class, buyDao);
        registerDao(ReceiptSheetCapture.class, receiptSheetCaptureDao);
        registerDao(ReceiptSheetDetail.class, receiptSheetDetailDao);
        registerDao(GlobalDecrease.class, globalDecreaseDao);
        registerDao(Activity.class, activityDao);
        registerDao(Truck.class, truckDao);
        registerDao(Driver.class, driverDao);
        registerDao(Roadmap.class, roadmapDao);
        registerDao(RoadmapDetail.class, roadmapDetailDao);
        registerDao(Route.class, routeDao);
        registerDao(InputTransference.class, inputTransferenceDao);
        registerDao(InputTransferenceDetail.class, inputTransferenceDetailDao);
        registerDao(Utilization.class, utilizationDao);
        registerDao(TimetableInfo.class, timetableInfoDao);
        registerDao(TimetablePicking.class, timetablePickingDao);
        registerDao(PendingDocument.class, pendingDocumentDao);
        registerDao(PendingDocumentDetail.class, pendingDocumentDetailDao);
        registerDao(ArticleTransference.class, articleTransferenceDao);
        registerDao(ArticleResearch.class, articleResearchDao);
        registerDao(Complaint.class, complaintDao);
        registerDao(ScannerCount.class, scannerCountDao);
        registerDao(ObservationType.class, observationTypeDao);
        registerDao(ArticleDecrease.class, articleDecreaseDao);
        registerDao(Message.class, messageDao);
        registerDao(ReceiptExpressLog.class, receiptExpressLogDao);
        registerDao(Barcode.class, barcodeDao);
        registerDao(DecreaseMP.class, decreaseMPDao);
        registerDao(FMuestras.class, fMuestrasDao);
        registerDao(ReceiptComments.class, receiptCommentsDao);
        registerDao(Trip.class, tripDao);
        registerDao(TripDetail.class, tripDetailDao);
        registerDao(Question.class, questionDao);
        registerDao(Answer.class, answerDao);
        registerDao(QuestionAnswer.class, questionAnswerDao);
        registerDao(Form.class, formDao);
        registerDao(FormQuestion.class, formQuestionDao);
        registerDao(FormApplied.class, formAppliedDao);
        registerDao(FormAppliedResult.class, formAppliedResultDao);
        registerDao(SheetTrip.class, sheetTripDao);
        registerDao(ShipmentControl.class, shipmentControlDao);
    }
    
    public void clear() {
        regionDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
        configProviderDaoConfig.getIdentityScope().clear();
        providerDaoConfig.getIdentityScope().clear();
        timetableReceiptDaoConfig.getIdentityScope().clear();
        storeDaoConfig.getIdentityScope().clear();
        providerEpackageDaoConfig.getIdentityScope().clear();
        ePackageDaoConfig.getIdentityScope().clear();
        areaDaoConfig.getIdentityScope().clear();
        rackDaoConfig.getIdentityScope().clear();
        rackLevelDaoConfig.getIdentityScope().clear();
        positionDaoConfig.getIdentityScope().clear();
        positionPackageDaoConfig.getIdentityScope().clear();
        epackageTruckDaoConfig.getIdentityScope().clear();
        epackageDownloadDaoConfig.getIdentityScope().clear();
        truckPackageDaoConfig.getIdentityScope().clear();
        orderTypeDaoConfig.getIdentityScope().clear();
        levelDaoConfig.getIdentityScope().clear();
        articleStateDaoConfig.getIdentityScope().clear();
        receiptSheetDaoConfig.getIdentityScope().clear();
        dateTypeDaoConfig.getIdentityScope().clear();
        unitDaoConfig.getIdentityScope().clear();
        vArticleDaoConfig.getIdentityScope().clear();
        storePickingStatusDaoConfig.getIdentityScope().clear();
        orderPickingDaoConfig.getIdentityScope().clear();
        orderDetailDaoConfig.getIdentityScope().clear();
        orderPickingCaptureDaoConfig.getIdentityScope().clear();
        orderDetailCaptureDaoConfig.getIdentityScope().clear();
        complaintCatDaoConfig.getIdentityScope().clear();
        checkDaoConfig.getIdentityScope().clear();
        buyDetailDaoConfig.getIdentityScope().clear();
        expressReceiptDaoConfig.getIdentityScope().clear();
        expressProviderDaoConfig.getIdentityScope().clear();
        expressArticleDaoConfig.getIdentityScope().clear();
        receiptSheetDetailCaptureDaoConfig.getIdentityScope().clear();
        buyDaoConfig.getIdentityScope().clear();
        receiptSheetCaptureDaoConfig.getIdentityScope().clear();
        receiptSheetDetailDaoConfig.getIdentityScope().clear();
        globalDecreaseDaoConfig.getIdentityScope().clear();
        activityDaoConfig.getIdentityScope().clear();
        truckDaoConfig.getIdentityScope().clear();
        driverDaoConfig.getIdentityScope().clear();
        roadmapDaoConfig.getIdentityScope().clear();
        roadmapDetailDaoConfig.getIdentityScope().clear();
        routeDaoConfig.getIdentityScope().clear();
        inputTransferenceDaoConfig.getIdentityScope().clear();
        inputTransferenceDetailDaoConfig.getIdentityScope().clear();
        utilizationDaoConfig.getIdentityScope().clear();
        timetableInfoDaoConfig.getIdentityScope().clear();
        timetablePickingDaoConfig.getIdentityScope().clear();
        pendingDocumentDaoConfig.getIdentityScope().clear();
        pendingDocumentDetailDaoConfig.getIdentityScope().clear();
        articleTransferenceDaoConfig.getIdentityScope().clear();
        articleResearchDaoConfig.getIdentityScope().clear();
        complaintDaoConfig.getIdentityScope().clear();
        scannerCountDaoConfig.getIdentityScope().clear();
        observationTypeDaoConfig.getIdentityScope().clear();
        articleDecreaseDaoConfig.getIdentityScope().clear();
        messageDaoConfig.getIdentityScope().clear();
        receiptExpressLogDaoConfig.getIdentityScope().clear();
        barcodeDaoConfig.getIdentityScope().clear();
        decreaseMPDaoConfig.getIdentityScope().clear();
        fMuestrasDaoConfig.getIdentityScope().clear();
        receiptCommentsDaoConfig.getIdentityScope().clear();
        tripDaoConfig.getIdentityScope().clear();
        tripDetailDaoConfig.getIdentityScope().clear();
        questionDaoConfig.getIdentityScope().clear();
        answerDaoConfig.getIdentityScope().clear();
        questionAnswerDaoConfig.getIdentityScope().clear();
        formDaoConfig.getIdentityScope().clear();
        formQuestionDaoConfig.getIdentityScope().clear();
        formAppliedDaoConfig.getIdentityScope().clear();
        formAppliedResultDaoConfig.getIdentityScope().clear();
        sheetTripDaoConfig.getIdentityScope().clear();
        shipmentControlDaoConfig.getIdentityScope().clear();
    }

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ConfigProviderDao getConfigProviderDao() {
        return configProviderDao;
    }

    public ProviderDao getProviderDao() {
        return providerDao;
    }

    public TimetableReceiptDao getTimetableReceiptDao() {
        return timetableReceiptDao;
    }

    public StoreDao getStoreDao() {
        return storeDao;
    }

    public ProviderEpackageDao getProviderEpackageDao() {
        return providerEpackageDao;
    }

    public EPackageDao getEPackageDao() {
        return ePackageDao;
    }

    public AreaDao getAreaDao() {
        return areaDao;
    }

    public RackDao getRackDao() {
        return rackDao;
    }

    public RackLevelDao getRackLevelDao() {
        return rackLevelDao;
    }

    public PositionDao getPositionDao() {
        return positionDao;
    }

    public PositionPackageDao getPositionPackageDao() {
        return positionPackageDao;
    }

    public EpackageTruckDao getEpackageTruckDao() {
        return epackageTruckDao;
    }

    public EpackageDownloadDao getEpackageDownloadDao() {
        return epackageDownloadDao;
    }

    public TruckPackageDao getTruckPackageDao() {
        return truckPackageDao;
    }

    public OrderTypeDao getOrderTypeDao() {
        return orderTypeDao;
    }

    public LevelDao getLevelDao() {
        return levelDao;
    }

    public ArticleStateDao getArticleStateDao() {
        return articleStateDao;
    }

    public ReceiptSheetDao getReceiptSheetDao() {
        return receiptSheetDao;
    }

    public DateTypeDao getDateTypeDao() {
        return dateTypeDao;
    }

    public UnitDao getUnitDao() {
        return unitDao;
    }

    public VArticleDao getVArticleDao() {
        return vArticleDao;
    }

    public StorePickingStatusDao getStorePickingStatusDao() {
        return storePickingStatusDao;
    }

    public OrderPickingDao getOrderPickingDao() {
        return orderPickingDao;
    }

    public OrderDetailDao getOrderDetailDao() {
        return orderDetailDao;
    }

    public OrderPickingCaptureDao getOrderPickingCaptureDao() {
        return orderPickingCaptureDao;
    }

    public OrderDetailCaptureDao getOrderDetailCaptureDao() {
        return orderDetailCaptureDao;
    }

    public ComplaintCatDao getComplaintCatDao() {
        return complaintCatDao;
    }

    public CheckDao getCheckDao() {
        return checkDao;
    }

    public BuyDetailDao getBuyDetailDao() {
        return buyDetailDao;
    }

    public ExpressReceiptDao getExpressReceiptDao() {
        return expressReceiptDao;
    }

    public ExpressProviderDao getExpressProviderDao() {
        return expressProviderDao;
    }

    public ExpressArticleDao getExpressArticleDao() {
        return expressArticleDao;
    }

    public ReceiptSheetDetailCaptureDao getReceiptSheetDetailCaptureDao() {
        return receiptSheetDetailCaptureDao;
    }

    public BuyDao getBuyDao() {
        return buyDao;
    }

    public ReceiptSheetCaptureDao getReceiptSheetCaptureDao() {
        return receiptSheetCaptureDao;
    }

    public ReceiptSheetDetailDao getReceiptSheetDetailDao() {
        return receiptSheetDetailDao;
    }

    public GlobalDecreaseDao getGlobalDecreaseDao() {
        return globalDecreaseDao;
    }

    public ActivityDao getActivityDao() {
        return activityDao;
    }

    public TruckDao getTruckDao() {
        return truckDao;
    }

    public DriverDao getDriverDao() {
        return driverDao;
    }

    public RoadmapDao getRoadmapDao() {
        return roadmapDao;
    }

    public RoadmapDetailDao getRoadmapDetailDao() {
        return roadmapDetailDao;
    }

    public RouteDao getRouteDao() {
        return routeDao;
    }

    public InputTransferenceDao getInputTransferenceDao() {
        return inputTransferenceDao;
    }

    public InputTransferenceDetailDao getInputTransferenceDetailDao() {
        return inputTransferenceDetailDao;
    }

    public UtilizationDao getUtilizationDao() {
        return utilizationDao;
    }

    public TimetableInfoDao getTimetableInfoDao() {
        return timetableInfoDao;
    }

    public TimetablePickingDao getTimetablePickingDao() {
        return timetablePickingDao;
    }

    public PendingDocumentDao getPendingDocumentDao() {
        return pendingDocumentDao;
    }

    public PendingDocumentDetailDao getPendingDocumentDetailDao() {
        return pendingDocumentDetailDao;
    }

    public ArticleTransferenceDao getArticleTransferenceDao() {
        return articleTransferenceDao;
    }

    public ArticleResearchDao getArticleResearchDao() {
        return articleResearchDao;
    }

    public ComplaintDao getComplaintDao() {
        return complaintDao;
    }

    public ScannerCountDao getScannerCountDao() {
        return scannerCountDao;
    }

    public ObservationTypeDao getObservationTypeDao() {
        return observationTypeDao;
    }

    public ArticleDecreaseDao getArticleDecreaseDao() {
        return articleDecreaseDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public ReceiptExpressLogDao getReceiptExpressLogDao() {
        return receiptExpressLogDao;
    }

    public BarcodeDao getBarcodeDao() {
        return barcodeDao;
    }

    public DecreaseMPDao getDecreaseMPDao() {
        return decreaseMPDao;
    }

    public FMuestrasDao getFMuestrasDao() {
        return fMuestrasDao;
    }

    public ReceiptCommentsDao getReceiptCommentsDao() {
        return receiptCommentsDao;
    }

    public TripDao getTripDao() {
        return tripDao;
    }

    public TripDetailDao getTripDetailDao() {
        return tripDetailDao;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public AnswerDao getAnswerDao() {
        return answerDao;
    }

    public QuestionAnswerDao getQuestionAnswerDao() {
        return questionAnswerDao;
    }

    public FormDao getFormDao() {
        return formDao;
    }

    public FormQuestionDao getFormQuestionDao() {
        return formQuestionDao;
    }

    public FormAppliedDao getFormAppliedDao() {
        return formAppliedDao;
    }

    public FormAppliedResultDao getFormAppliedResultDao() {
        return formAppliedResultDao;
    }

    public SheetTripDao getSheetTripDao() {
        return sheetTripDao;
    }

    public ShipmentControlDao getShipmentControlDao() {
        return shipmentControlDao;
    }

}
