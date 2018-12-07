package com.tiendas3b.almacen.http;

import com.tiendas3b.almacen.db.dao.Activity;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.ArticleDecrease;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.db.dao.ArticleState;
import com.tiendas3b.almacen.db.dao.ArticleTransference;
import com.tiendas3b.almacen.db.dao.Barcode;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.Check;
import com.tiendas3b.almacen.db.dao.Complaint;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.DateType;
import com.tiendas3b.almacen.db.dao.Driver;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.ExpressArticle;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.GlobalDecrease;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;
import com.tiendas3b.almacen.db.dao.Level;
import com.tiendas3b.almacen.db.dao.Message;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.OrderPicking;
import com.tiendas3b.almacen.db.dao.OrderType;
import com.tiendas3b.almacen.db.dao.PendingDocument;
import com.tiendas3b.almacen.db.dao.PendingDocumentDetail;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.ReceiptSheet;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.Route;
import com.tiendas3b.almacen.db.dao.ScannerCount;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.dao.TimetablePicking;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.dao.TruckPackage;
import com.tiendas3b.almacen.db.dao.Unit;
import com.tiendas3b.almacen.db.dao.User;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.dto.BuyDetailAndCaptureDTO;
import com.tiendas3b.almacen.dto.CaptureDecrease;
import com.tiendas3b.almacen.dto.CaptureDevice;
import com.tiendas3b.almacen.dto.CaptureDocCompras;
import com.tiendas3b.almacen.dto.CaptureInputTransference;
import com.tiendas3b.almacen.dto.CaptureReturnVendor;
import com.tiendas3b.almacen.dto.CaptureRoadmap;
import com.tiendas3b.almacen.dto.CaptureTransference;
import com.tiendas3b.almacen.dto.GeneralErrorDTO;
import com.tiendas3b.almacen.dto.GeneralTravelDTO;
import com.tiendas3b.almacen.dto.GlobalDecreaseDTO;
import com.tiendas3b.almacen.dto.MeErrorDTO;
import com.tiendas3b.almacen.dto.ReceiptSheetDetailCaptureDTO;
import com.tiendas3b.almacen.dto.RoadmapVO;
import com.tiendas3b.almacen.dto.SmDetailDTO;
import com.tiendas3b.almacen.dto.SmSummaryDTO;
import com.tiendas3b.almacen.dto.TimetableInfoDTO;
import com.tiendas3b.almacen.dto.TravelDetailDTO;
import com.tiendas3b.almacen.dto.epackage.EPackageLeaveDTO;
import com.tiendas3b.almacen.dto.epackage.PositionPackageDTO;
import com.tiendas3b.almacen.dto.epackage.TruckPackageDTO;
import com.tiendas3b.almacen.dto.picking.MvlTiempoPickingDTO;
import com.tiendas3b.almacen.dto.receipt.FMuestrasDTO;
import com.tiendas3b.almacen.dto.receipt.FhojaReciboDetTempDTO;
import com.tiendas3b.almacen.dto.receipt.FhojaReciboEncTempDTO;
import com.tiendas3b.almacen.dto.receipt.MmpReciboExpressAsnComentariosDTO;
import com.tiendas3b.almacen.dto.receipt.MmpReciboExpressLogDTO;
import com.tiendas3b.almacen.dto.receipt.ReceiptFolioDTO;
import com.tiendas3b.almacen.picking.orders.OrderPickingCaptureDTO;
import com.tiendas3b.almacen.receipt.express.HeaderExpressDTO;
import com.tiendas3b.almacen.receipt.report.ReceiptSheetPrintDTO;
import com.tiendas3b.almacen.receipt.sheet.capture.dto.CaptureReceiptSheet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dfa on 22/02/2016.
 */
public interface Tiendas3bClient {

    @POST("user/login")
    Call<User> login();

    @POST("sync/providers")
    Call<List<Provider>> getProviderCatalog();

    @POST("sync/regions")
    Call<List<Region>> getRegionCatalog();

    @POST("sync/barcodes")
    Call<List<Barcode>> getBarcodeCatalog();

    @POST("sync/units")
    Call<List<Unit>> getUnits();

    @FormUrlEncoded
    @POST("sync/timetable_receipt")
    Call<List<TimetableReceipt>> getTimetableReceiptCatalog(@Field("regionId") Long regionId);

    @FormUrlEncoded
    @POST("sync/articles_decrease")
    Call<List<ArticleDecrease>> getViewArticlesDecrease(@Field("regionId") long regionId);

    @FormUrlEncoded
    @POST("sync/articles_transference")
    Call<List<ArticleTransference>> getViewArticlesTransference(@Field("regionId") long regionId);

    @FormUrlEncoded
    @POST("sync/stores")
    Call<List<Store>> getStores(@Field("regionId") long regionId);

    @FormUrlEncoded
    @POST("sync/input_transferences")
    Call<List<InputTransference>> getInputTransference(@Field("regionId") long regionId);

    @POST("sync/observation_types")
    Call<List<ObservationType>> getObservationsType();

    @POST("sync/articles")
    Call<List<VArticle>> getViewArticles();

    @POST("sync/activities")
    Call<List<Activity>> getActivities();

    @POST("sync/trucks")
    Call<List<Truck>> getTrucks();

    @POST("sync/levels")
    Call<List<Level>> getLevels();

    @POST("sync/checks")
    Call<List<Check>> getChecks();

    @POST("sync/dateTypes")
    Call<List<DateType>> getDateTypes();

    @POST("sync/drivers")
    Call<List<Driver>> getDrivers();
    //TODO syn frecibo_tipo_citas

    @POST("sync/complaints")
    Call<List<ComplaintCat>> getComplaints();

    @POST("sync/article_states")
    Call<List<ArticleState>> getArticleStates();

    @FormUrlEncoded
    @POST("sync/timetable_picking")
    Call<List<TimetablePicking>> getTimetablePicking(@Field("regionId") long regionId);

    @POST("sync/areas")
    Call<List<Area>> getAreas();

    @POST("sync/providers_epackage")
    Call<List<ProviderEpackage>> getProviderEpackageCatalog();

    @POST("sync/order_types")
    Call<List<OrderType>> getOrderTypes();

    @POST("sync/providers_express")
    Call<List<ExpressProvider>> getExpressProviders();

    @POST("sync/mmp_config_provider")
    Call<List<ConfigProvider>> getConfigProviders();

    @FormUrlEncoded
    @POST("sync/packages")
    Call<List<EPackage>> getEpackages(@Field("regionId") long region);

    @POST("sync/epackage_trucks")
    Call<List<EpackageTruck>> getEpackageTrucks();


    @FormUrlEncoded
    @POST("receipt/timetable_receipt")
    Call<List<TimetableReceipt>> getTimetableReceiptCatalog(@Field("regionId") Long regionId, @Field("date") String date);

    @FormUrlEncoded
    @POST("receipt/providers")
    Call<List<Buy>> getBuys(@Field("regionId") long regionId, @Field("programedDate") String programedDate);//yyyy-MM-dd

    @FormUrlEncoded
    @POST("receipt/express_providers")
    Call<List<Buy>> getBuysExpress(@Field("regionId") long regionId, @Field("programedDate") String programedDate);//yyyy-MM-dd

    @FormUrlEncoded
    @POST("receipt/express_providers2")
    Call<List<Buy>> getBuysExpress2(@Field("regionId") long regionId, @Field("programedDate") String programedDate);//yyyy-MM-dd

    @FormUrlEncoded
    @POST("receipt/express_header")
    Call<HeaderExpressDTO> getHeaderExpress(@Field("odc") int odc, @Field("regionId")  long regionId, @Field("providerId")  long providerId);

    @FormUrlEncoded
    @POST("receipt/express_header2")
    Call<HeaderExpressDTO> getHeaderExpress2(@Field("odc") int odc, @Field("regionId")  long regionId, @Field("providerId")  long providerId);

    @FormUrlEncoded
    @POST("receipt/express_details")
    Call<List<ExpressReceipt>> getExpressDetails(@Field("regionId") long regionId, @Field("folio") int folio);//para amarrar, con folio ODC

    @FormUrlEncoded
    @POST("receipt/express_details2")
    Call<List<ExpressReceipt>> getExpressDetails2(@Field("regionId") long regionId, @Field("folio") int folio);//para amarrar, con folio ODC

    @FormUrlEncoded
    @POST("receipt/express_articles")
    Call<List<ExpressArticle>> getExpressArticles(@Field("regionId") long regionId, @Field("folio") int folio);

    @FormUrlEncoded
    @POST("receipt/providers_detail")
    Call<List<BuyDetailAndCaptureDTO>> getBuyDetails(@Field("folio") int folio, @Field("regionId") String regionId, @Field("providerId") long providerId);

    @FormUrlEncoded
    @POST("receipt/receipt_sheet")
    Call<List<ReceiptSheet>> getReceiptSheets(@Field("regionId") long regionId, @Field("date") String date);//yyyy-MM-dd

    @FormUrlEncoded
    @POST("receipt/timetable_info")
    Call<TimetableInfoDTO> getTimetableInfo(@Field("regionId") long regionId, @Field("date") String date);//yyyy-MM-dd

    @FormUrlEncoded
    @POST("receipt/receipt_sheet_detail")
    Call<List<ReceiptSheetDetail>> getReceiptSheetDetails(@Field("regionId") long regionId, @Field("date") String date, @Field("odc") String odc);//yyyy-MM-dd

    @FormUrlEncoded
    @POST("receipt/global_decrease")
    Call<List<GlobalDecrease>> getDecrease(@Field("regionId") long regionId);


    @FormUrlEncoded
    @POST("receipt/global_decrease2")
    Call<List<GlobalDecreaseDTO>> getDecrease2(@Field("regionId") long region);

    @POST("receipt/insert_decrease")
    Call<GeneralResponseDTO> insertDecrease(@Body CaptureDecrease captureDecrease);

    @POST("receipt/insert_fmuestras")
    Call<GeneralResponseDTO> insertFMuestras(@Body List<FMuestrasDTO> fMuestrasDTO);

    @POST("receipt/insert_receipt_sheet")
    Call<CaptureDocCompras> insertReceiptSheet(@Body CaptureDocCompras captureDocCompras);

    @POST("receipt/insert_receipt_sheet_capture")
    Call<ReceiptSheetCapture> insertReceiptSheet(@Body CaptureReceiptSheet r);

    @FormUrlEncoded
    @POST("receipt/article")
    Call<VArticle> getArticle(@Field("barcode") String barcode);

    @POST("receipt/insert_receipt_sheet_detail")
    Call<ReceiptSheetDetailCapture> insertReceiptSheetDetail(@Body ReceiptSheetDetailCaptureDTO receiptSheetDetailCaptureDTO);

    @FormUrlEncoded
    @POST("receipt/validate_receipt_sheet")
    Call<GeneralResponseDTO> validateReceiptSheet(@Field("folio") int folio, @Field("regionId") long regionId, @Field("providerId") long providerId, @Field("date") String dateStr);

    @FormUrlEncoded
    @POST("receipt/receipt_sheet_for_print")
    Call<ReceiptSheetPrintDTO> receiptSheetForPrint(@Field("folio") int folio, @Field("regionId") long regionId, @Field("providerId") long providerId, @Field("billRef") String billRef);

    @FormUrlEncoded
    @POST("receipt/finish_receipt_sheet")
    Call<GeneralResponseDTO> finishReceiptSheet(@Field("folio") int folio, @Field("regionId") long regionId, @Field("providerId") long providerId, @Field("date") String dateStr);

    @FormUrlEncoded
    @POST("receipt/update_status_in_process")
    Call<GeneralResponseDTO> updateRecptStatusInProc(@Field("folio") int folio, @Field("regionId") long regionId, @Field("providerId") long providerId, @Field("date") String dateStr);


    @POST("receipt/insert_physical_change")
    Call<GeneralResponseDTO> insertPhysicalChange(@Body CaptureDecrease captureDecrease);

    @FormUrlEncoded
    @POST("receipt/input_transference_det")
    Call<List<InputTransferenceDetail>> getInputTransferenceDetail(@Field("regionId") long regionId, @Field("storeId") long storeId, @Field("folio") int folio,
                                                                   @Field("date") String date);

    @POST("receipt/insert_input_transference")
    Call<GeneralResponseDTO> insertInput(@Body CaptureInputTransference captureInput);

    @FormUrlEncoded
    @POST("receipt/return_vendor_detail")
    Call<List<VArticle>> getReturnVendorDetail(@Field("vendorId") long vendorId);

    @POST("receipt/insert_return_vendor")
    Call<GeneralResponseDTO> insertReturnVendor(@Body CaptureReturnVendor captureDecrease);

    @FormUrlEncoded
    @POST("receipt/input_transferences")
    Call<List<InputTransference>> getInputTransference(@Field("regionId") long regionId, @Field("type") short type);

    @FormUrlEncoded
    @POST("receipt/receipt_sheet_capture")
    Call<ReceiptSheetCapture> getReceiptSheetCapture(@Field("regionId") long region, @Field("date") String dateStr, @Field("folio") int folio,
                                                     @Field("providerId") long providerId, @Field("buyId") long buyId);

    @POST("receipt/insert_rec_expr_log")
    Call<GeneralResponseDTO> insertReciboExpressLog(@Body MmpReciboExpressLogDTO mmpReciboExpressLogDTO);

    @POST("receipt/insert_comment")
    Call<GeneralResponseDTO> insertComment(@Body MmpReciboExpressAsnComentariosDTO comentariosDTO);

    @FormUrlEncoded
    @POST("receipt/hoja_recibo_enc_temp")
    Call<FhojaReciboEncTempDTO> getHojaReciboEncTemp(@Field("odc") int odc, @Field("regionId") String regionId,
                                                     @Field("providerId") long providerId);

    @FormUrlEncoded
    @POST("receipt/hoja_recibo_agr")
    Call<List<FhojaReciboDetTempDTO>>getHojaReciboAGR(@Field("odc") int odc, @Field("regionId") String regionId,
                                                         @Field("providerId") long providerId);

    @FormUrlEncoded
    @POST("receipt/dpfolio")
    Call<ReceiptFolioDTO> getDpFolio(@Field("odc") int odc);


    @FormUrlEncoded
    @POST("picking/order/store_status")
    Call<List<StorePickingStatus>> getStorePickingStatus(@Field("regionId") long regionId, @Field("orderTypeId") int orderTypeId);

    @FormUrlEncoded
    @POST("picking/order/stores_by_type")//old picking/order/stores
    Call<List<OrderPicking>> getOrders(@Field("regionId") long region, @Field("storeId") long storeId, @Field("orderTypeId") int orderTypeId);

    @FormUrlEncoded
    @POST("picking/order/only")
    Call<List<OrderPicking>> getOnlyOrders(@Field("regionId") long region, @Field("storeIds") String storeIds);

    @FormUrlEncoded
    @POST("picking/order/store")
    Call<List<OrderPicking>> getOrdersStore(@Field("regionId") long region, @Field("storeId") long storeId);

    @FormUrlEncoded
    @POST("picking/errors/me")
    Call<List<MeErrorDTO>> getMeErrors(@Field("regionId") long region);

    @FormUrlEncoded
    @POST("picking/errors/general")
    Call<List<GeneralErrorDTO>> getGeneralErrors(@Field("regionId") long region);

    @FormUrlEncoded
    @POST("picking/sm/summary")
    Call<SmSummaryDTO> getSmSummary(@Field("regionId")long region, @Field("date") String dateStr);

    @FormUrlEncoded
    @POST("picking/sm/detail")
    Call<List<SmDetailDTO>> getSmDetail(@Field("regionId")long region, @Field("date") String dateStr);


    @POST("picking/order/insert")
    Call<GeneralResponseDTO> insertPickingConfirmation(@Body OrderPickingCaptureDTO captureDTO);

    //metodo nuevo para insertar tiempo de pickeo
    @POST("picking/time/insert")
    Call<GeneralResponseDTO> insertTimePicking(@Body MvlTiempoPickingDTO mvlTiempoPickingDTO);

    //metodo nuevo para actualizar tiempo de pickeo
    @POST("picking/time/update")
    Call<GeneralResponseDTO> updateTimePicking(@Body MvlTiempoPickingDTO mvlTiempoPickingDTO);


    @POST("audit/insert_transference")
    default Call<GeneralResponseDTO> insertTransferenceOutput(@Body CaptureTransference captureDecrease) {
        return null;
    }

    @FormUrlEncoded
    @POST("audit/articles_research")
    Call<List<ArticleResearch>> getArticlesResearch(@Field("regionId") long region, @Field("date") String date);

    @FormUrlEncoded
    @POST("audit/physical_inventory_detail")
    Call<List<ScannerCount>> getPhysicalInventory(@Field("regionId") long region, @Field("date") String date);

    @FormUrlEncoded
    @POST("audit/complaints_detail")
    Call<List<Complaint>> getComplaints(@Field("regionId") long region, @Field("date") String date);





    @FormUrlEncoded
    @POST("shipment/travel/general")
    Call<List<GeneralTravelDTO>> getTravel(@Field("regionId")long region, @Field("date") String dateStr);

    @FormUrlEncoded
    @POST("shipment/travel/detail")
    Call<List<TravelDetailDTO>> getTravelDetail(@Field("regionId")long region, @Field("date") String dateStr, @Field("truckId") long truckId);

    @POST("shipment/insert_roadmap")
    Call<GeneralResponseDTO> insertRoadmap(@Body CaptureRoadmap captureRoadmap);

    @POST("shipment/insert_device")
    Call<GeneralResponseDTO> insertDevice(@Body CaptureDevice device);




    @FormUrlEncoded
    @POST("management/pending_documents")
    Call<List<PendingDocument>> getPendingDocuments(@Field("regionId")long region);

    @FormUrlEncoded
    @POST("management/pending_documents/detail")
    Call<List<PendingDocumentDetail>> getPendingDocsDetail(@Field("regionId")long region, @Field("date") String dateStr);

    @FormUrlEncoded
    @POST("management/pending_documents/transactions")
    Call<List<PendingDocumentDetail>> getPendingDocsTransactions(@Field("regionId")long region, @Field("date") String dateStr);







    @FormUrlEncoded
    @POST("epackage/packages_status")
    Call<List<EPackage>> getEpackages(@Field("regionId") long regionId, @Field("statusId") int statusId);


    @POST("epackage/action_leave")
    Call<GeneralResponseDTO> actionEpackage_leave(@Body List<EPackageLeaveDTO> ePackageLeaveDTO);

    @POST("epackage/action_sincronizate")
    Call<GeneralResponseDTO> actionEpackage_Sincronizate(@Body List<EPackageLeaveDTO> ePackageLeaveDTO);

    @FormUrlEncoded
    @POST("epackage/action")
    Call<GeneralResponseDTO> actionEpackage(@Field("regionId") long regionId, @Field("epackageId") long epackageId,
                                            @Field("originStatus") long originStatus, @Field("targetStatus") int targetStatus);

    @FormUrlEncoded
    @POST("epackage/action_cage_in")
    Call<GeneralResponseDTO> actionEpackageCageIn(@Field("regionId") long regionId, @Field("epackageId") long epackageId, @Field("originStatus") long originStatus,
                                                  @Field("targetStatus") int targetStatus, @Field("areaId") long areaId, @Field("rackId") long rackId,
                                                  @Field("levelId") long levelId, @Field("positionId") long positionId);

    @FormUrlEncoded
    @POST("epackage/find_BarCode_Ub")
    Call<PositionPackageDTO> findBarCodeUb(@Field("BarCodeUb") String BarCodeUb);

    @FormUrlEncoded
    @POST("epackage/action_cage_out")
    Call<GeneralResponseDTO> actionEpackageCageOut(@Field("regionId") long regionId, @Field("epackageId") long epackageId, @Field("originStatus") long originStatus,
                                                   @Field("targetStatus") int targetStatus, @Field("areaId") long areaId, @Field("rackId") long rackId,
                                                   @Field("levelId") long levelId, @Field("positionId") long positionId);

    @FormUrlEncoded
    @POST("epackage/providers")
    Call<List<EPackage>> getProvidersEpackage(@Field("regionId") long region, @Field("date") String date);


    //Trae el catalogo de proveedores de EPackage
    @POST("epackage/providersCat")
    Call<List<ProviderEpackage>> getProvidersEpackageCat();


    @FormUrlEncoded
    @POST("epackage/position_package")
    default Call<List<PositionPackageDTO>> getPositionPackage(@Field("regionId") long region) {
        return null;
    }

    @FormUrlEncoded
    @POST("epackage/action_truck_in")
    Call<GeneralResponseDTO> actionEpackageTruckIn(@Field("regionId") long regionId, @Field("epackageId") long epackageId,
                                            @Field("originStatus") long originStatus, @Field("targetStatus") int targetStatus, @Field("camClave") long camclave);


    @FormUrlEncoded
    @POST("epackage/truck_package")
    default Call<List<TruckPackageDTO>> getTruckPackage(@Field("regionId") long region) {
        return null;
    }



    @FormUrlEncoded
    @POST("location")
    Call<Message> getLocation(@Field("regionId")long region, @Field("date") String dateStr, @Field("truckId") long truckId);

    @FormUrlEncoded
    @POST("routes")
    Call<List<Route>> getRoutes(@Field("regionId")long region, @Field("date") String dateStr, @Field("truckId") long truckId);

    @GET("camiones/consulta")
    Call<List<RoadmapVO>> getRoadmaps(@Query("idalmacen") long region, @Query("fecha") String dateStr, @Query("idcamion") long truckId);


    @GET("ConsultaRutasCamion")
    Call<Object> getTravels(@Query("idalmacen") long regionId, @Query("fecha") String date, @Query("camion") long truckId);

}
