package com.tiendas3b.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Correr desde Gradle -> :dao -> Tasks -> application -> run
 * No se va a ver en la consola (como antes) pero sí se van a crear las clases.
 * @author dfa
 */
public class MyDaoGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";//Esto es para regresar una carpeta
    private static final int DB_VERSION = 29;
    public static final String SPINNER_BASE = "SpinnerBase";
    private static final String STORE_BASE = "StoreBase";
    public static final String EQUALS_BASE = "EqualsBase";

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(DB_VERSION, "com.tiendas3b.almacen.db.dao");
        schema.enableKeepSectionsByDefault();
        System.out.println(OUT_DIR);
        addTables(schema);
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    private static void addTables(Schema schema) {
        Entity region = addRegion(schema);
        Entity user = addUser(schema);
        Entity configProvider = ExpressReceipt.addConfigProvider(schema);
        Entity provider = addProvider(schema, configProvider);
        Entity timetableReceipt = addTimetableReceipt(schema, region, provider);
        Entity store = addStore(schema, region);

        Entity providerEpackage = Epackage.addProviderEpackagae(schema);
        Entity epackage = Epackage.addPackage(schema, region, store, providerEpackage);
        Entity area = Organization.addArea(schema, region);
        Entity rack = Organization.addRack(schema, area);
        Entity rackLevel = Organization.addRackLevel(schema, rack);
        Entity position = Organization.addPosition(schema, rackLevel);
        Entity positionPackage = Organization.addPositionPackage(schema, position, epackage);
        Entity epackageTruck = Epackage.addEpackageTruck(schema);
        Entity epackageDownload= Epackage.addEpackageDownload(schema);
        Entity TruckPackage = Organization.addTruckPackage(schema, epackageTruck, epackage);
        Entity orderType = Receipt.addOrderType(schema, region);



        Entity level = addLevel(schema);
        Entity articleState = addArticleState(schema);
        Entity receiptSheet = addReceiptSheet(schema, region, provider);
        Entity dateType = addDateType(schema, region);
        Entity unit = addUnit(schema);
        Entity vfArticles = addVFArticles(schema, provider, unit, articleState);
        Entity storePickingStatus = Picking.addStorePickingStatus(schema, region, store);
        Entity order = Picking.addOrder(schema, region, store, storePickingStatus);
        Entity orderDetail = Picking.addOrderDetail(schema, order, vfArticles);
        Entity orderCapture = Picking.addOrderCapture(schema, region, store);
        Entity orderDetailCapture = Picking.addOrderDetailCapture(schema, orderCapture, vfArticles);
        Entity complaintCat = addComplaintCat(schema);
        Entity check = addCheck(schema);
        Entity buyDetail = addBuyDetail(schema, vfArticles);

        Entity expressReceipt = ExpressReceipt.addExpressReceipt(schema, region, buyDetail);
        Entity expressProvider = ExpressReceipt.addExpressProvider(schema, region, provider);
        Entity expressArticle = ExpressReceipt.addExpressArticles(schema, region, provider);

        Entity receiptSheetDetailCapture = addReceiptSheetDetailCapture(schema, buyDetail, check, complaintCat);
        Entity buy = addBuy(schema, region, provider, buyDetail);
        Entity receiptSheetCapture = addReceiptSheetCapture(schema, level, dateType, buy, receiptSheetDetailCapture);
        Entity receiptSheetDetail = addReceiptSheetDetail(schema, receiptSheet, timetableReceipt);

        Entity globalDecrease = addGlobalDecrease(schema, region);

        Entity activity = addActivity(schema);
        Entity truck = addTruck(schema);
        Entity driver = addDriver(schema);
        Entity roadmap = addRoadmap(schema, region, truck, driver, user);
        Entity roadmapDetail = addRoadmapDetail(schema, roadmap, region, truck, driver, activity, store);
        Entity route = addRoute(schema, store, region);
        Entity inputTransference = addInputTransference(schema, store, region);
        Entity inputTransferenceDetail = addInputTransferenceDetail(schema, inputTransference);
        Entity utilization = addUtilization(schema);
        Entity timetableInfo = addTimetableInfo(schema, region, utilization);
        Entity timetablePicking = addTimetablePicking(schema, region, store);
        Entity pendingDocument = addPendingDocument(schema, region);
        Entity pendingDocumentDetail = addPendingDocumentDetail(schema, pendingDocument);
        Entity articleTransference = addArticleTransference(schema);
        Entity articleResearch = addArticleResearch(schema, region, store);
        Entity complaint = addComplaint(schema, region, store, vfArticles);
        Entity scannerCount = addScannerCount(schema, region, vfArticles);
        Entity observaionsType = addObservaionsType(schema);
        Entity articleDecrease = addArticleDecrease(schema);

        Entity message = addMessage(schema);//local to send
//        Entity device = addDevice(schema, region);
//        Entity deviceTruck = addDeviceTruck(schema, device, truck);
        Entity receiptExpressLog = addReceiptExpressLog(schema);
        Entity barcode = addBarcode(schema);
        Entity decreaseMP = addDecreaseMP(schema);
        Entity fmuestras = addFMuestras(schema);
        Entity receiptComments = addReceiptComments(schema);

        Shipment.addShipment(schema, truck);

    }

    private static Entity addTimetablePicking(Schema schema, Entity region, Entity store) {
        Entity entity = schema.addEntity("TimetablePicking");
        entity.addIdProperty().primaryKey().autoincrement();
//        entity.setSuperclass(EQUALS_BASE);
        Property dayXD = entity.addIntProperty("day").notNull().getProperty();

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(dayXD);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addReceiptSheetDetailCapture(Schema schema, Entity buyDetail, Entity check, Entity complaint) {
        Entity entity = schema.addEntity("ReceiptSheetDetailCapture");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("billAmount");
        entity.addIntProperty("receivedAmount");
        entity.addIntProperty("paletizado");
        entity.addIntProperty("paletizadoCount");
        entity.addIntProperty("sample");
        entity.addIntProperty("packing");
//        entity.addIntProperty("check");
//        entity.addStringProperty("rejectionReason");
        entity.addStringProperty("expiryDate");
        entity.addStringProperty("lote");
        entity.addStringProperty("comments");
//        entity.addIntProperty("iclave");
//        entity.addIntProperty("msc");
//        entity.addStringProperty("barcode");
//        entity.addStringProperty("description");
//        entity.addStringProperty("unity");
//        entity.addIntProperty("active");
//        entity.addStringProperty("registeredName");
//        entity.addStringProperty("sustn");
//        entity.addStringProperty("mark");
//        entity.addStringProperty("espec");
//        entity.addIntProperty("type");

        Property idForPK = entity.addLongProperty("buyDetailId").notNull().getProperty();
        entity.addToOne(buyDetail, idForPK, "buyDetail");

        Property p = buyDetail.addLongProperty("receiptSheetCaptureId").getProperty();
        buyDetail.addToOne(entity, p, "receiptSheetCapture");

        Property checkForPK = entity.addLongProperty("checkId").notNull().getProperty();
        entity.addToOne(check, checkForPK, "check");

        Property complaintCatForPK = entity.addLongProperty("rejectionReasonId").notNull().getProperty();
        entity.addToOne(complaint, complaintCatForPK, "rejectionReason");

        return entity;
    }

    private static Entity addArticleState(Schema schema) {
        Entity entity = schema.addEntity("ArticleState");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        return entity;
    }

    private static Entity addCheck(Schema schema) {
        Entity entity = schema.addEntity("Check");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        return entity;
    }

    private static Entity addComplaintCat(Schema schema) {
        Entity entity = schema.addEntity("ComplaintCat");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("group");
        entity.addStringProperty("description");
        entity.addBooleanProperty("active");

        Property strForPK = entity.addStringProperty("idStr").notNull().getProperty();
        Property typeForPK = entity.addIntProperty("type").notNull().getProperty();

        Index indexUnique = new Index();
        indexUnique.addProperty(strForPK);
        indexUnique.addProperty(typeForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addDateType(Schema schema, Entity region) {
        Entity entity = schema.addEntity("DateType");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        entity.addBooleanProperty("active");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property keyForPK = entity.addIntProperty("key").notNull().getProperty();

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(keyForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addLevel(Schema schema) {
        Entity entity = schema.addEntity("Level");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        return entity;
    }

    private static Entity addPendingDocumentDetail(Schema schema, Entity pendingDocument) {
        Entity entity = schema.addEntity("PendingDocumentDetail");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("amount");
        entity.addIntProperty("stores");
        entity.addIntProperty("events");

        Property typeForPK = entity.addStringProperty("type").notNull().getProperty();
        Property pendingDocumentIdForPK = entity.addLongProperty("pendingDocumentId").notNull().getProperty();
        entity.addToOne(pendingDocument, pendingDocumentIdForPK, "pendingDocument");

        Index indexUnique = new Index();
        indexUnique.addProperty(pendingDocumentIdForPK);
        indexUnique.addProperty(typeForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addPendingDocument(Schema schema, Entity region) {
        Entity entity = schema.addEntity("PendingDocument");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("ta");
        entity.addIntProperty("taPieces");
        entity.addIntProperty("rt");
        entity.addIntProperty("rtPieces");
        entity.addIntProperty("taStore");
        entity.addIntProperty("rtStore");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property dateForPK = entity.addStringProperty("date").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addMessage(Schema schema) {//mensaje local para enviar
        Entity entity = schema.addEntity("Message");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addLongProperty("emissionDateMillis").notNull();
        entity.addDoubleProperty("lat").notNull();
        entity.addDoubleProperty("lng").notNull();
        entity.addFloatProperty("accuracy");
        entity.addStringProperty("provider");
        entity.addFloatProperty("speed");
        entity.addFloatProperty("bearing");

        return entity;
    }

//    private static Entity addDeviceTruck(Schema schema, Entity device, Entity truck) {
//        Entity entity = schema.addEntity("DeviceTruck");
//        entity.addIdProperty().primaryKey().autoincrement();
//        entity.setSuperclass(EQUALS_BASE);
//
//        Property deviceIdForPK = entity.addLongProperty("deviceId").notNull().getProperty();
//        entity.addToOne(device, deviceIdForPK, "device");
//
//        Property truckIdForPK = entity.addLongProperty("truckId").notNull().getProperty();
//        entity.addToOne(device, truckIdForPK, "truck");
//
//        Index indexUnique = new Index();
//        indexUnique.addProperty(truckIdForPK);
//        indexUnique.addProperty(deviceIdForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);
//
//        return entity;
//    }
//
//    private static Entity addDevice(Schema schema, Entity region) {
//        Entity entity = schema.addEntity("Device");
//        entity.addIdProperty().primaryKey().autoincrement();
//        entity.setSuperclass(EQUALS_BASE);
//        entity.addStringProperty("name").notNull();
//        entity.addStringProperty("brand");
//        entity.addStringProperty("model");
//        entity.addBooleanProperty("active").notNull();
//        entity.addStringProperty("msisdn").notNull();
//        entity.addStringProperty("imei").notNull().unique().getProperty();
//
//        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
//        entity.addToOne(region, regionIdForPK, "region");
////        Property imeiForPk = entity.addStringProperty("imei").notNull().unique().getProperty();
//
////        Index indexUnique = new Index();
////        indexUnique.addProperty(regionIdForPK);
////        indexUnique.addProperty(imeiForPk);
////        indexUnique.makeUnique();
////        entity.addIndex(indexUnique);
//
//        return entity;
//
//    }

    private static Entity addRoute(Schema schema, Entity store, Entity region) {
        Entity entity = schema.addEntity("Route");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addDoubleProperty("lat").notNull();
        entity.addDoubleProperty("lng").notNull();
        entity.addStringProperty("truck").notNull();
//        entity.addDateProperty("date");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property dateForPk = entity.addStringProperty("date").notNull().getProperty();
        Property travelForPk = entity.addIntProperty("travel").notNull().getProperty();
        Property activityForPk = entity.addStringProperty("activity").notNull().getProperty();

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(dateForPk);
        indexUnique.addProperty(travelForPk);
        indexUnique.addProperty(activityForPk);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addRoadmapDetail(Schema schema, Entity roadmap, Entity region, Entity truck, Entity driver, Entity activity, Entity store) {
        Entity entity = schema.addEntity("RoadmapDetail");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("endDate").notNull();
        entity.addIntProperty("scaffoldUpload");
        entity.addIntProperty("scaffoldDownload");
        entity.addIntProperty("initialKm").notNull();
        entity.addIntProperty("finalKm").notNull();
        entity.addStringProperty("initialTime").notNull();
        entity.addStringProperty("finalTime").notNull();
        entity.addFloatProperty("cash");
        entity.addFloatProperty("iave");
        entity.addFloatProperty("diesel");
        entity.addFloatProperty("trafficTicket");
        entity.addFloatProperty("otherCost");
        entity.addStringProperty("notes");
        entity.addBooleanProperty("sync").notNull();

//        Property userId = entity.addLongProperty("userId").notNull().getProperty();
        Property originIdForPk = entity.addLongProperty("originId").notNull().getProperty();
        entity.addToOne(store, originIdForPk, "origin");
        Property destinationIdForPk = entity.addLongProperty("destinationId").notNull().getProperty();
        entity.addToOne(store, destinationIdForPk, "destination");
        Property activityIdForPk = entity.addLongProperty("activiyId").notNull().getProperty();
        entity.addToOne(activity, activityIdForPk, "activity");
        Property roadmapIdForPK = entity.addLongProperty("roadmapId").notNull().getProperty();
        entity.addToOne(roadmap, roadmapIdForPK, "roadmap");
//        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
//        entity.addToOne(region, regionIdForPK, "region");
//        Property driverIdForPK = entity.addLongProperty("driverId").notNull().getProperty();
//        entity.addToOne(driver, driverIdForPK, "driver");
//        Property truckIdForPK = entity.addLongProperty("truckId").notNull().getProperty();
//        entity.addToOne(truck, truckIdForPK, "truck");
//        Property travelForPK = entity.addIntProperty("travel").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(roadmapIdForPK);
        indexUnique.addProperty(activityIdForPk);
        indexUnique.addProperty(originIdForPk);
        indexUnique.addProperty(destinationIdForPk);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

//        Property timetableInfoIdForutilization = entity.addLongProperty("timetableInfoId").notNull().getProperty();
        ToMany roadmapToDetail = roadmap.addToMany(entity, roadmapIdForPK);
        roadmapToDetail.setName("activities");

        return entity;
    }

    private static Entity addRoadmap(Schema schema, Entity region, Entity truck, Entity driver, Entity user) {
        Entity entity = schema.addEntity("Roadmap");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addBooleanProperty("sync").notNull();
        entity.addIntProperty("scaffolds");

        Property userId = entity.addLongProperty("userId").notNull().getProperty();
//        entity.addToOne(user, userId, "user");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");
        Property driverIdForPK = entity.addLongProperty("driverId").notNull().getProperty();
        entity.addToOne(driver, driverIdForPK, "driver");
        Property truckIdForPK = entity.addLongProperty("truckId").notNull().getProperty();
        entity.addToOne(truck, truckIdForPK, "truck");
        Property travelForPK = entity.addIntProperty("travel").notNull().getProperty();
        Property dateForPK = entity.addStringProperty("date").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(driverIdForPK);
        indexUnique.addProperty(truckIdForPK);
        indexUnique.addProperty(travelForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addDriver(Schema schema) {
        Entity entity = schema.addEntity("Driver");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        entity.addIntProperty("active");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        Property driverIdForPK = entity.addLongProperty("driverId").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(driverIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addTruck(Schema schema) {
        Entity entity = schema.addEntity("Truck");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        entity.addIntProperty("active");
        entity.addIntProperty("aliasId");

        entity.addIntProperty("statusId");
        entity.addStringProperty("statusDescription");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        Property truckIdForPK = entity.addLongProperty("truckId").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(truckIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addActivity(Schema schema) {
        Entity entity = schema.addEntity("Activity");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        return entity;
    }

    private static Entity addScannerCount(Schema schema, Entity region, Entity vfArticles) {
        Entity entity = schema.addEntity("ScannerCount");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("folio");
        entity.addIntProperty("amount");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property articleIdForPK = entity.addLongProperty("varticleId").notNull().getProperty();
        entity.addToOne(vfArticles, articleIdForPK, "varticle");

        Property iclaveForPK = entity.addLongProperty("iclave").notNull().getProperty();
        Property dateForPK = entity.addStringProperty("date").notNull().getProperty();
        Property seqForPK = entity.addLongProperty("seq").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.addProperty(iclaveForPK);
        indexUnique.addProperty(seqForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addComplaint(Schema schema, Entity region, Entity store, Entity vfArticles) {
        Entity entity = schema.addEntity("Complaint");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("quantityOrdered");
        entity.addIntProperty("quantitySm");
        entity.addIntProperty("quantityAccepted");
        entity.addIntProperty("conditionId");
        entity.addStringProperty("obs");
        entity.addStringProperty("action");
        entity.addStringProperty("responsibleArea");
        entity.addStringProperty("responsibleSubarea");
        entity.addStringProperty("storeObs");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property articleIdForPK = entity.addLongProperty("varticleId").notNull().getProperty();
        entity.addToOne(vfArticles, articleIdForPK, "varticle");

        Property dateForPK = entity.addStringProperty("date").notNull().getProperty();
        Property idForPK = entity.addLongProperty("iclave").notNull().getProperty();
        Property seqForPK = entity.addLongProperty("seq").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.addProperty(idForPK);
        indexUnique.addProperty(seqForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addArticleResearch(Schema schema, Entity region, Entity store) {
        Entity entity = schema.addEntity("ArticleResearch");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("avgPed");
        entity.addIntProperty("avgFzd");
        entity.addIntProperty("avgSurt");
        entity.addIntProperty("avgSo");
        entity.addIntProperty("avgInvest");
        entity.addIntProperty("avgCancel");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property dateForPK = entity.addStringProperty("date").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addUnit(Schema schema) {
        Entity entity = schema.addEntity("Unit");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("description");
        entity.addStringProperty("abbreviation");
        entity.addShortProperty("type");
//        entity.addContentProvider();
        return entity;
    }

    private static Entity addTimetableInfo(Schema schema, Entity region, Entity utilization) {
        Entity entity = schema.addEntity("TimetableInfo");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
//        entity.addStringProperty("dateTime");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property timetableInfoIdForutilization = utilization.addLongProperty("timetableInfoId").notNull().getProperty();
        ToMany timetableInfoIdToutilization = entity.addToMany(utilization, timetableInfoIdForutilization);
        timetableInfoIdToutilization.setName("utilizations");

        Property facturaForPK = entity.addStringProperty("dateTime").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(facturaForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addUtilization(Schema schema) {
        Entity entity = schema.addEntity("Utilization");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("numAnden");
        entity.addIntProperty("useTime");//min
        entity.addIntProperty("totalTime");//min
        entity.addFloatProperty("usePercentage");
        entity.addIntProperty("palletsSum");

        return entity;
    }

    private static Entity addVFArticles(Schema schema, Entity provider, Entity unit, Entity articleState) {
        Entity entity = schema.addEntity("VArticle");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("description");
        entity.addStringProperty("unity");
        entity.addIntProperty("packing");
//        entity.addIntProperty("amount");//agrgados para envio
//        entity.addLongProperty("type");
//        entity.addLongProperty("obs");
        entity.addLongProperty("ivaId");
        entity.addFloatProperty("cost");
        entity.addFloatProperty("sale");
        entity.addShortProperty("inosurte");
        entity.addShortProperty("expiryMax");
        entity.addBooleanProperty("purchasing");
        entity.addIntProperty("typeId");
        entity.addStringProperty("circuit");
        entity.addIntProperty("fatherIclave");

        Property activeId = entity.addLongProperty("activeId").notNull().getProperty();
        entity.addToOne(articleState, activeId, "active");

        Property unitId = entity.addLongProperty("unitId").notNull().getProperty();
        entity.addToOne(unit, unitId, "unit");

        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");

        Property idForPK = entity.addLongProperty("iclave").notNull().getProperty();
        Property barcodeForPK = entity.addStringProperty("barcode").getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(idForPK);
        indexUnique.addProperty(barcodeForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

//        entity.addContentProvider();
        return entity;
    }

    private static Entity addReceiptSheetDetail(Schema schema, Entity receiptSheet, Entity timetableReceipt) {
        Entity entity = schema.addEntity("ReceiptSheetDetail");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addLongProperty("iclave");
        entity.addStringProperty("expiration");
        entity.addStringProperty("fillRate");
        entity.addStringProperty("description");
//        entity.addIntProperty("numPallets");
        entity.addIntProperty("numBoxesOdc");
        entity.addIntProperty("numBoxesEm");

        Property receiptSheetIdForPK = entity.addLongProperty("receiptSheetId").notNull().getProperty();
        entity.addToOne(receiptSheet, receiptSheetIdForPK, "receiptSheet");

        Property timetableReceiptIdForPK = entity.addLongProperty("timetableReceiptId").notNull().getProperty();
        entity.addToOne(timetableReceipt, timetableReceiptIdForPK, "timetableReceipt");

        return entity;
    }

    private static Entity addInputTransferenceDetail(Schema schema, Entity inputTransference) {
        Entity entity = schema.addEntity("InputTransferenceDetail");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addLongProperty("iclave");
//        entity.addFloatProperty("cost");
//        entity.addStringProperty("barcode");
//        entity.addStringProperty("description");
//        entity.addStringProperty("unity");
//        entity.addIntProperty("packing");
        entity.addIntProperty("amountSmn");
//        entity.addIntProperty("amountInv");
//        entity.addIntProperty("amountMer");
//        entity.addIntProperty("amountReg");
//        entity.addLongProperty("type");
//        entity.addLongProperty("obs");

        Property inputTransferenceIdForPK = entity.addLongProperty("inputTransferenceId").notNull().getProperty();
        entity.addToOne(inputTransference, inputTransferenceIdForPK, "inputTransference");

//        Property articleIdForPK = entity.addLongProperty("articleId").notNull().getProperty();
//        entity.addToOne(vfArticles, articleIdForPK, "article");

        return entity;
    }

    private static Entity addInputTransference(Schema schema, Entity store, Entity region) {
        Entity entity = schema.addEntity("InputTransference");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addDateProperty("date");

        Property typeForPK = entity.addShortProperty("type").notNull().getProperty();
        Property folioForPK = entity.addIntProperty("folio").notNull().getProperty();

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(typeForPK);
        indexUnique.addProperty(folioForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        entity.addContentProvider();
        return entity;
    }

    private static Entity addObservaionsType(Schema schema) {
        Entity entity = schema.addEntity("ObservationType");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("description");
        entity.addIntProperty("active");
        entity.addIntProperty("type");
        //entity.addContentProvider();
        return entity;
    }

    private static Entity addStore(Schema schema, Entity region) {
        Entity entity = schema.addEntity("Store");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(STORE_BASE);
        entity.addStringProperty("name");
        entity.addStringProperty("street");
        entity.addStringProperty("township");
        entity.addStringProperty("state");
        entity.addStringProperty("address");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        //entity.addContentProvider();
        return entity;
    }

    private static Entity addArticleDecrease(Schema schema) {
        Entity entity = schema.addEntity("ArticleDecrease");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addLongProperty("iclave");

//        Property articleIdForPK = entity.addLongProperty("articleId").notNull().getProperty();
//        entity.addToOne(vfArticles, articleIdForPK, "article");

        //entity.addContentProvider();
        return entity;
    }

    private static Entity addArticleTransference(Schema schema) {
        Entity entity = schema.addEntity("ArticleTransference");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addLongProperty("iclave");

//        Property articleIdForPK = entity.addLongProperty("articleId").notNull().getProperty();
//        entity.addToOne(vfArticles, articleIdForPK, "article");

        //entity.addContentProvider();
        return entity;
    }

    private static Entity addGlobalDecrease(Schema schema, Entity region) {
        Entity entity = schema.addEntity("GlobalDecrease");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("numreg");
        entity.addIntProperty("numarts");
        entity.addIntProperty("amount");
        entity.addFloatProperty("cost");
        entity.addFloatProperty("sale");
        entity.addFloatProperty("amountAvg");
        entity.addFloatProperty("costAvg");
        entity.addFloatProperty("saleAvg");


        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property dateForPK = entity.addDateProperty("date").notNull().getProperty();

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addReceiptSheet(Schema schema, Entity region, Entity provider) {
        Entity entity = schema.addEntity("ReceiptSheet");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
//        entity.addDateProperty("receiptDate");
//        entity.addIntProperty("odc");
//        entity.addStringProperty("facturaRef");
        entity.addIntProperty("level");
        entity.addStringProperty("dateTime");
        entity.addStringProperty("arriveTime");
        entity.addStringProperty("deliveryTime");
        entity.addStringProperty("departureTime");
        entity.addBooleanProperty("arrive");
        entity.addIntProperty("numReceivers");
        //userId
        entity.addIntProperty("withoutDate");
        entity.addIntProperty("folio");
        //status
        entity.addIntProperty("paletizado");
        entity.addFloatProperty("amountFact");
        entity.addFloatProperty("amountEm");
        entity.addStringProperty("rfcProvider");
        //serie
        entity.addFloatProperty("iva");
        entity.addFloatProperty("ieps");
        entity.addIntProperty("platform");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");

        Property dateForPK = entity.addDateProperty("receiptDate").notNull().getProperty();
        Property odcForPK = entity.addIntProperty("odc").notNull().getProperty();
        Property facturaForPK = entity.addStringProperty("facturaRef").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(providerIdForPK);
        indexUnique.addProperty(facturaForPK);
        indexUnique.addProperty(dateForPK);
        indexUnique.addProperty(odcForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addReceiptSheetCapture(Schema schema, Entity level, Entity dateType, Entity buy, Entity receiptSheetDetailCapture) {
        Entity entity = schema.addEntity("ReceiptSheetCapture");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("dateTime");
        entity.addStringProperty("arriveTime");
        entity.addStringProperty("deliveryTime");
        entity.addStringProperty("departureTime");
        entity.addIntProperty("arrive");
        entity.addIntProperty("numReceivers");
        //userId//de web service
//        entity.addIntProperty("withoutDate");
//        entity.addIntProperty("folio");//ws
        //status
        entity.addIntProperty("paletizado");
        entity.addFloatProperty("amountFact");
        entity.addFloatProperty("amountEm");//subtotal
//        entity.addStringProperty("rfcProvider");
        //serie
        entity.addFloatProperty("iva");
        entity.addFloatProperty("ieps");
        entity.addStringProperty("deliveryman");
        entity.addStringProperty("receiver");
        entity.addIntProperty("platform");

//        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
//        entity.addToOne(region, regionIdForPK, "region");
//
//        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
//        entity.addToOne(provider, providerIdForPK, "provider");

        Property levelIdForPK = entity.addLongProperty("levelId").getProperty();
        entity.addToOne(level, levelIdForPK, "level");

        Property dateTypeIdForPK = entity.addLongProperty("dateTypeId").getProperty();
        entity.addToOne(dateType, dateTypeIdForPK, "dateType");

        Property buyIdForPK = entity.addLongProperty("buyId").notNull().getProperty();
        entity.addToOne(buy, buyIdForPK, "buy");

        Property dateForPK = entity.addStringProperty("receiptDate").notNull().getProperty();
        Property odcForPK = entity.addIntProperty("odc").notNull().getProperty();
        Property facturaForPK = entity.addStringProperty("facturaRef").getProperty();


//        Property idForDetail = receiptSheetDetailCapture.addLongProperty("receiptSheetId").notNull().getProperty();
//        receiptSheetDetailCapture.addToOne(entity, idForDetail, "receiptSheet");

//        ToMany buyToBuyDetail = entity.addToMany(receiptSheetDetailCapture, idForDetail);
//        buyToBuyDetail.setName("receiptSheetDetails"); // one-to-many (userDetails.getListOfActions)


        Index indexUnique = new Index();
//        indexUnique.addProperty(regionIdForPK);
//        indexUnique.addProperty(providerIdForPK);
//        indexUnique.addProperty(facturaForPK);
        indexUnique.addProperty(dateForPK);
//        indexUnique.addProperty(odcForPK);
        indexUnique.addProperty(buyIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addTimetableReceipt(Schema schema, Entity region, Entity provider) {
        Entity entity = schema.addEntity("TimetableReceipt");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("platform");
        entity.addStringProperty("dateTime");//fecha
        Property timeForPK = entity.addStringProperty("time").notNull().getProperty();//cita
        entity.addStringProperty("arriveTime");//llega
        entity.addStringProperty("deliveryTime");//entrega
        entity.addStringProperty("departureTime");//huye XD
        entity.addStringProperty("downloadTime");//tiempo de descarga
        entity.addIntProperty("level");
        entity.addBooleanProperty("arrive");
        Property folioForPk = entity.addIntProperty("folio").getProperty();

//        Property dayForPK = entity.addIntProperty("day").notNull().getProperty();


        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(providerIdForPK);
//        indexUnique.addProperty(dayForPK);
//        indexUnique.addProperty(timeForPK);
        indexUnique.addProperty(folioForPk);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    private static Entity addBuyDetail(Schema schema, Entity vfArticles) {
        Entity entity = schema.addEntity("BuyDetail");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("articleAmount");
        entity.addIntProperty("balance");
//        entity.addIntProperty("iclave");
//        entity.addIntProperty("msc");
//        entity.addStringProperty("barcode");
//        entity.addStringProperty("description");
//        entity.addStringProperty("unity");
//        entity.addIntProperty("active");
//        entity.addStringProperty("registeredName");
//        entity.addStringProperty("sustn");
//        entity.addStringProperty("mark");
//        entity.addStringProperty("espec");
//        entity.addIntProperty("type");

//        ToMany buyToBuyDetail = entity.addToMany(buyDetail, buyIdForBuyDetail);//para
//        buyToBuyDetail.setName("buyDetails"); // one-to-many (userDetails.getListOfActions)


        Property idForPK = entity.addLongProperty("articleId").notNull().getProperty();
        entity.addToOne(vfArticles, idForPK, "article");

        return entity;
    }

    private static Entity addBuy(Schema schema, Entity region, Entity provider, Entity buyDetail) {
        Entity entity = schema.addEntity("Buy");
        entity.addIdProperty().primaryKey().autoincrement().getProperty();
        entity.setSuperclass(EQUALS_BASE);
        entity.addDoubleProperty("total");
        entity.addDateProperty("programedDate");
//        entity.addIntProperty("day");
        entity.addIntProperty("platform");
        entity.addStringProperty("time");
        entity.addBooleanProperty("express");
        entity.addStringProperty("deliveryTime");
        entity.addIntProperty("checked").notNull();

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");
        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");
        Property folioForPK = entity.addIntProperty("folio").notNull().getProperty();
        Property movForPK = entity.addStringProperty("mov").notNull().getProperty();
        Property forcedForPK = entity.addIntProperty("forced").notNull().getProperty();

        Property buyIdForBuyDetail = buyDetail.addLongProperty("buyId").notNull().getProperty();
        buyDetail.addToOne(entity, buyIdForBuyDetail, "Buy");

        ToMany buyToBuyDetail = entity.addToMany(buyDetail, buyIdForBuyDetail);
        buyToBuyDetail.setName("buyDetails"); // one-to-many (userDetails.getListOfActions)

        Property pFpk = buyDetail.getProperties().get(3);//article

        Index indexDetUnique = new Index();
        indexDetUnique.addProperty(buyIdForBuyDetail);
        indexDetUnique.addProperty(pFpk);
        indexDetUnique.makeUnique();
        buyDetail.addIndex(indexDetUnique);


        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(providerIdForPK);
        indexUnique.addProperty(folioForPK);
        indexUnique.addProperty(movForPK);
        indexUnique.addProperty(forcedForPK);
//        indexUnique.addProperty(buyIdForBuyDetail);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);



        return entity;
    }

    private static Entity addRegion(Schema schema) {
        Entity entity = schema.addEntity("Region");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("name");
        entity.addStringProperty("address");
        entity.addIntProperty("platformNum");
        entity.addStringProperty("tels");
        //entity.addContentProvider();
        return entity;
    }

    private static Entity addProvider(Schema schema, Entity configProvider) {
        Entity entity = schema.addEntity("Provider");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("name");
        entity.addStringProperty("businessName");
        entity.addStringProperty("rfc");
        entity.addStringProperty("phone1");
        entity.addStringProperty("phone2");
        entity.addStringProperty("mail");
        entity.addStringProperty("atention");
        entity.addBooleanProperty("active");
        entity.addShortProperty("supply");
        entity.addShortProperty("term");
        entity.addStringProperty("address");
//        entity.addStringProperty("mmpConfig");
        //entity.addContentProvider();

        Property mmpConfigForPK = entity.addLongProperty("mmpConfigId").notNull().getProperty();
        entity.addToOne(configProvider, mmpConfigForPK, "mmpConfig");

        return entity;
    }

    private static Entity addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().primaryKey().autoincrement();
        user.setSuperclass(EQUALS_BASE);
        user.addStringProperty("name")/*.notNull()*/;
        user.addStringProperty("lastName")/*.notNull()*/;
        user.addStringProperty("lastName2")/*.notNull()*/;
        user.addStringProperty("password")/*.notNull()*/;
        user.addStringProperty("email")/*.notNull()*/;
        user.addStringProperty("groupId").notNull();
        user.addLongProperty("region")/*.notNull()*/;

        Property seqForUserPK = user.addLongProperty("seq").notNull().getProperty();
        Property loginForUserPK = user.addStringProperty("login").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(seqForUserPK);
        indexUnique.addProperty(loginForUserPK);
        indexUnique.makeUnique();
        user.addIndex(indexUnique);

        return user;
    }

//    private static void createUserPK(Entity entity) {
//        Property seqForUserPK = entity.addLongProperty("seq").notNull().getProperty();
//        Property loginForUserPK = entity.addStringProperty("login").notNull().getProperty();
//        Index indexUnique = new Index();
//        indexUnique.addProperty(seqForUserPK);
//        indexUnique.addProperty(loginForUserPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);
//    }

    private static Entity addAccess(Schema schema) {
        Entity entity = schema.addEntity("Access");
        entity.addIdProperty().primaryKey().autoincrement();
//        entity.addStringProperty("node").notNull();
//        entity.addStringProperty("action").notNull();
//        entity.addLongProperty("profileId").notNull();
        return entity;
    }

    private static Entity addProfile(Schema schema) {
        Entity entity = schema.addEntity("Profile");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("description").notNull();
        entity.addBooleanProperty("hidden");
        return entity;
    }

    private static Entity addPossibleOrigin(Schema schema) {
        Entity standardSolution = schema.addEntity("PossibleOrigin");
        standardSolution.addIdProperty().primaryKey().autoincrement();
        standardSolution.addStringProperty("name").notNull();
        standardSolution.addStringProperty("description");
        standardSolution.setSuperclass(EQUALS_BASE);
        return standardSolution;
    }

    private static Entity addStandardSolution(Schema schema) {
        Entity standardSolution = schema.addEntity("StandardSolution");
        standardSolution.addIdProperty().primaryKey().autoincrement();
        standardSolution.addStringProperty("name").notNull();
        standardSolution.addStringProperty("description").notNull();
        return standardSolution;
    }

    private static Entity addBranch(Schema schema) {
        Entity provider = schema.addEntity("Branch");
        provider.addIdProperty().primaryKey().autoincrement();
        provider.addStringProperty("number3b").notNull();
        provider.addStringProperty("name").notNull();
        provider.addStringProperty("street");
        provider.addStringProperty("number");
        provider.addStringProperty("neighborhood");
        provider.addStringProperty("township");
        provider.addStringProperty("city").notNull();
        provider.addStringProperty("postalCode");
        provider.addStringProperty("phone");
        provider.addStringProperty("cellphone");
//        provider.addLongProperty("branchTypeId").notNull();
        provider.addLongProperty("storehouseId");
        provider.addLongProperty("districtId");
        provider.addLongProperty("technicianId").notNull();
        provider.addIntProperty("status");
        provider.addLongProperty("attendantId");
        provider.addBooleanProperty("purchaseDelivery");
        provider.addStringProperty("code");
        provider.addStringProperty("emails");
        return provider;
    }

    private static Entity addBranchType(Schema schema) {
        Entity branchType = schema.addEntity("BranchType");
        branchType.addIdProperty().primaryKey().autoincrement();
        branchType.addStringProperty("name").notNull();
        branchType.addStringProperty("description").notNull();
        return branchType;
    }

    private static Entity addImpCause(Schema schema) {
        Entity typeSymptom = schema.addEntity("ImpCause");
        typeSymptom.addIdProperty().primaryKey().autoincrement();
        typeSymptom.addStringProperty("description").notNull();
        typeSymptom.addStringProperty("generalDescription").notNull();
        typeSymptom.addStringProperty("impSolution").notNull();
        typeSymptom.addStringProperty("code");
        typeSymptom.addShortProperty("status").notNull();
        return typeSymptom;
    }

    private static Entity addSymptomDiagnostic(Schema schema) {
        Entity typeSymptom = schema.addEntity("SymptomDiagnostic");
        typeSymptom.addIdProperty().primaryKey().autoincrement();
        typeSymptom.addBooleanProperty("sub").notNull();
        typeSymptom.addBooleanProperty("status").notNull();
        typeSymptom.addStringProperty("renew").notNull();
        return typeSymptom;
    }

    private static Entity addTypeSymptom(Schema schema) {
        Entity typeSymptom = schema.addEntity("TypeSymptom");
        typeSymptom.addIdProperty().primaryKey().autoincrement();
        typeSymptom.addStringProperty("renew").notNull();
        typeSymptom.addBooleanProperty("status").notNull();
        return typeSymptom;
    }

    private static Entity addDiagnostic(Schema schema) {
        Entity diagnostic = schema.addEntity("Diagnostic");
        diagnostic.addIdProperty().primaryKey().autoincrement();
        diagnostic.addStringProperty("description").notNull();
        diagnostic.addStringProperty("status").notNull();
        return diagnostic;
    }

    private static Entity addSymptom(Schema schema) {
        Entity symptom = schema.addEntity("Symptom");
        symptom.addIdProperty().primaryKey().autoincrement();
        symptom.addStringProperty("name").notNull();
        symptom.addStringProperty("sub");
        symptom.addStringProperty("description");
        symptom.addShortProperty("failure").notNull();
        symptom.addStringProperty("status").notNull();
        return symptom;
    }


    private static Entity addProjectStatus(Schema schema) {
        Entity projectStatus = schema.addEntity("ProjectStatus");
        projectStatus.addIdProperty().primaryKey().autoincrement();
        projectStatus.addStringProperty("name").notNull();
        projectStatus.addStringProperty("description").notNull();
        projectStatus.addBooleanProperty("status").notNull();
        projectStatus.setSuperclass(EQUALS_BASE);
        return projectStatus;
    }

    private static Entity addType(Schema schema) {
        Entity type = schema.addEntity("Type");
        type.addIdProperty().primaryKey().autoincrement();
        type.addStringProperty("name").notNull();//.unique();
        type.addStringProperty("description");
        type.addStringProperty("abbreviation");
        type.addBooleanProperty("status").notNull();
        type.setSuperclass(EQUALS_BASE);
        return type;
    }

    private static Entity addCategories(Schema schema) {
        Entity category = schema.addEntity("Category");
        category.addIdProperty().primaryKey().autoincrement();
        category.addStringProperty("name").notNull();//.unique();
        category.addStringProperty("description");
        category.setSuperclass(EQUALS_BASE);
        return category;
    }

    private static Entity addTicketStatus(Schema schema) {
        Entity ticketStatus = schema.addEntity("TicketStatus");
        ticketStatus.addIdProperty().primaryKey().autoincrement();
        ticketStatus.addStringProperty("name").notNull().unique();
        ticketStatus.addStringProperty("description");
        ticketStatus.addStringProperty("abbreviation");
        ticketStatus.addBooleanProperty("hidden");
        ticketStatus.setSuperclass(EQUALS_BASE);
        return ticketStatus;
    }

    private static Entity addTicket(Schema schema) {
        Entity ticket = schema.addEntity("Ticket");
        ticket.addIdProperty().primaryKey().autoincrement();
        ticket.addStringProperty("observations").notNull();
        ticket.addBooleanProperty("remoteSolution");
        ticket.addDateProperty("openingDate");
        ticket.addDateProperty("updateDate");
        ticket.addDateProperty("solutionDate");
        ticket.addDateProperty("closingDate");
//        ticket.addLongProperty("applicantId").notNull();
//        ticket.addIntProperty("branchId").notNull();
//        ticket.addIntProperty("symptomId").notNull();
//        ticket.addIntProperty("diagnosticId");
//        ticket.addIntProperty("possibleOriginId");
//        ticket.addIntProperty("standardSolutionId");
//        ticket.addIntProperty("statusId").notNull();
//        ticket.addIntProperty("technicianId");
//        ticket.addIntProperty("keyboarderId").notNull();
//        ticket.addIntProperty("type");
//        ticket.addIntProperty("waitingForUserId");
//        ticket.addIntProperty("waitingForProviderId");
//        ticket.addIntProperty("causeId");
        ticket.addIntProperty("solutionId");
        ticket.addStringProperty("file");
        ticket.addStringProperty("caption");
        ticket.addIntProperty("openDays");
//        ticket.addIntProperty("categoryId").notNull();
//        ticket.addIntProperty("projectStatusId");
        ticket.addStringProperty("lapsed");
        return ticket;
    }

    private static Entity addActions(Schema schema) {
        Entity action = schema.addEntity("Action");
        action.addIdProperty().primaryKey().autoincrement();
//        action.addStringProperty("ticketId").notNull();
        action.addDateProperty("date").notNull();
        action.addStringProperty("description").notNull();
//        action.addIntProperty("technicianId").notNull();
//        action.addIntProperty("providerId");
        return action;
    }

    private static Entity addReceiptExpressLog(Schema schema){
        Entity receiptExpressLog = schema.addEntity("ReceiptExpressLog");
//        receiptExpressLog.addIdProperty().primaryKey().autoincrement();
        receiptExpressLog.addStringProperty("idAlmacen");
        receiptExpressLog.addLongProperty("idProveedor");
        receiptExpressLog.addIntProperty("odc");
        receiptExpressLog.addStringProperty("date");
        receiptExpressLog.addStringProperty("time");
        receiptExpressLog.addIntProperty("idOrden");
        receiptExpressLog.addIntProperty("idProcess");
        receiptExpressLog.addStringProperty("description");
        receiptExpressLog.addStringProperty("user");
        return receiptExpressLog;

    }

    private static Entity addBarcode(Schema schema){
        Entity barcode = schema.addEntity("Barcode");
//        receiptExpressLog.addIdProperty().primaryKey().autoincrement();
        barcode.addStringProperty("icb");
        barcode.addIntProperty("iclave");
        barcode.addStringProperty("idesc");
        barcode.addIntProperty("uclave");
        return barcode;
    }

    private static Entity addDecreaseMP(Schema schema){
        Entity decreaseMP = schema.addEntity("DecreaseMP");
        decreaseMP.addLongProperty("iclave");
        decreaseMP.addStringProperty("description");
        decreaseMP.addIntProperty("amount");
        decreaseMP.addLongProperty("type");
        decreaseMP.addLongProperty("obs");
        decreaseMP.addLongProperty("obsLog");
        decreaseMP.addFloatProperty("cost");
        decreaseMP.addStringProperty("obsStr");
        decreaseMP.addIntProperty("odc");
        return decreaseMP;
    }

    private static Entity addFMuestras(Schema schema){
        Entity fMuestras = schema.addEntity("FMuestras");
        fMuestras.addStringProperty("tclave");
        fMuestras.addLongProperty("iclave");
        fMuestras.addLongProperty("pclave");
        fMuestras.addStringProperty("fecha");
        fMuestras.addIntProperty("odc");
        fMuestras.addStringProperty("lote");
        fMuestras.addStringProperty("fechaCaducidad");
        fMuestras.addIntProperty("cantidad");
        fMuestras.addIntProperty("userId");
        return fMuestras;
    }

    private static Entity addReceiptComments(Schema schema){
        Entity receiptComments = schema.addEntity("ReceiptComments");

        receiptComments.addLongProperty("tclave");
        receiptComments.addDateProperty("date");
        receiptComments.addLongProperty("pclave");
        receiptComments.addIntProperty("odc");
        receiptComments.addIntProperty("userId");
        receiptComments.addStringProperty("comments");
        return receiptComments;
    }

}
