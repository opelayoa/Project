package com.tiendas3b.almacen.shipment.http;


import com.tiendas3b.almacen.db.dao.SheetTrip;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.shipment.dto.QuestionnairesDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ShipmentService {

    @GET("CamionesPorAlmacen2")
    Call<List<Truck>> getTruckByRegion(@Query("paIdAlmacen") long paIdAlmacen);

    @GET("ViajesPorAlmacen")
    Call<List<Trip>> getTripsByRegion(@Query("paIdAlmacen") long paIdAlmacen, @Query("paFecha") String paFecha, @Query("paIdTipoRuta") long paIdTipoRuta);

    @GET("ViajesPorAlmacenDetalle")
    Call<List<TripDetail>> getTripsDetailByTruck(@Query("paIdAlmacen") long paIdAlmacen, @Query("paFecha") String paFecha, @Query("paIdTipoRuta") long paIdTipoRuta);

    @GET("ChecklistPorFormulario2")
    Call<QuestionnairesDto> getCheckList(@Query("paIdForm") long paIdForm);

    @POST("CargaInfoViajes")
    Call<Void> uploadInfoTrips(@Body List<SheetTrip> sheetTrips);

}
