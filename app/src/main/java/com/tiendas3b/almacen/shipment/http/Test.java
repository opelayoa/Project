package com.tiendas3b.almacen.shipment.http;

import com.tiendas3b.almacen.db.dao.Truck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Test {

    public static void main(String args[]) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://201.161.90.103:8080/WEBT3BRutas2018/")
                .build();
        ShipmentService shipmentService = retrofit.create(ShipmentService.class);
        shipmentService.getTruckByRegion(1000).enqueue(new Callback<List<Truck>>() {
            @Override
            public void onResponse(Call<List<Truck>> call, Response<List<Truck>> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<List<Truck>> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
            }
        });
    }
}
