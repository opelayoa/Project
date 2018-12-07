package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.Answer;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.FormQuestion;
import com.tiendas3b.almacen.db.dao.Question;
import com.tiendas3b.almacen.db.dao.QuestionAnswer;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.dao.Trip;
import com.tiendas3b.almacen.db.dao.TripDetail;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.dto.FormDto;
import com.tiendas3b.almacen.shipment.dto.QuestionnairesDto;
import com.tiendas3b.almacen.shipment.dto.RelationshipDto;
import com.tiendas3b.almacen.shipment.http.ShipmentService;
import com.tiendas3b.almacen.shipment.views.ShipmentConfigView;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentConfigPresenterImpl implements ShipmentConfigPresenter {

    private final ShipmentConfigView view;
    private ShipmentService shipmentService;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public ShipmentConfigPresenterImpl(ShipmentConfigView view, Context context) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
        this.shipmentService = mContext.getShipmentHttpService();
    }

    @Override
    public void syncShipment() {
        this.view.startSyncProgress();
        syncTrucks();
    }

    private void syncTrucks() {


        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        if (shipmentControl != null && shipmentControl.getStatus() != 0) {
            ShipmentConfigPresenterImpl.this.view.stopSyncProgress();
            view.showError("El día de hoy ya no es posible realizar sincronización, debido a que ya tienes viajes en curso.");
            return;
        }

        shipmentService.getTruckByRegion(this.regionId)
                .enqueue(new Callback<List<Truck>>() {
                    @Override
                    public void onResponse(Call<List<Truck>> call, Response<List<Truck>> response) {
                        List<Truck> trucks = response.body();
                        System.out.println(trucks);
                        databaseManager.truncate(Truck.class);
                        databaseManager.insertOrReplaceInTx(trucks);
                        syncTrips();
                    }

                    @Override
                    public void onFailure(Call<List<Truck>> call, Throwable t) {
                        ShipmentConfigPresenterImpl.this.view.stopSyncProgress();
                        ShipmentConfigPresenterImpl.this.view.showError("Existió un error al realizar la sincronización, vuelva a intentarlo más tarde.");
                    }
                });
    }

    private void syncTrips() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        shipmentService.getTripsByRegion(regionId, dateFormat.format(new Date()), 1)
                .enqueue(new Callback<List<Trip>>() {
                    @Override
                    public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                        List<Trip> trips = response.body();
                        System.out.println(trips);
                        databaseManager.truncate(Trip.class);
                        databaseManager.insertOrReplaceInTx(trips);
                        syncTripDetails();
                    }

                    @Override
                    public void onFailure(Call<List<Trip>> call, Throwable t) {
                        ShipmentConfigPresenterImpl.this.view.stopSyncProgress();
                        ShipmentConfigPresenterImpl.this.view.showError("Existió un error al realizar la sincronización, vuelva a intentarlo más tarde.");
                    }
                });
    }

    private void syncTripDetails() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        shipmentService.getTripsDetailByTruck(regionId, dateFormat.format(new Date()), 1)
                .enqueue(new Callback<List<TripDetail>>() {
                    @Override
                    public void onResponse(Call<List<TripDetail>> call, Response<List<TripDetail>> response) {
                        List<TripDetail> tripDetails = response.body();
                        databaseManager.truncate(TripDetail.class);
                        databaseManager.insertOrReplaceInTx(tripDetails);
                        syncQuestionnaires();
                    }

                    @Override
                    public void onFailure(Call<List<TripDetail>> call, Throwable t) {
                        ShipmentConfigPresenterImpl.this.view.stopSyncProgress();
                        ShipmentConfigPresenterImpl.this.view.showError("Existió un error al realizar la sincronización, vuelva a intentarlo más tarde.");
                    }
                });
    }

    private void syncQuestionnaires() {
        shipmentService.getCheckList(1)
                .enqueue(new Callback<QuestionnairesDto>() {
                    @Override
                    public void onResponse(Call<QuestionnairesDto> call, Response<QuestionnairesDto> response) {
                        QuestionnairesDto questionnairesDto = response.body();

                        databaseManager.truncate(Form.class);
                        databaseManager.truncate(Question.class);
                        databaseManager.truncate(Answer.class);
                        databaseManager.truncate(QuestionAnswer.class);
                        databaseManager.truncate(FormQuestion.class);

                        List<Question> questions = questionnairesDto.getQuestions();
                        databaseManager.truncate(Question.class);
                        databaseManager.insertOrReplaceInTx(questions);
                        List<Answer> answers = questionnairesDto.getAnswers();
                        databaseManager.truncate(Answer.class);
                        databaseManager.insertOrReplaceInTx(questions);


                        List<FormDto> formDtos = questionnairesDto.getForm();

                        for (FormDto formDto : formDtos) {
                            Form form = new Form();
                            form.setId(formDto.getId());
                            form.setDescription(formDto.getDescription());

                            databaseManager.insert(form);
                            for (RelationshipDto relationshipDto : formDto.getRelationships()) {
                                Long questionId = relationshipDto.getQuestionId();
                                Long formId = form.getId();
                                FormQuestion formQuestion = new FormQuestion(formId, questionId);
                                databaseManager.insert(formQuestion);
                                for (Long answerId : relationshipDto.getAnswers()) {
                                    QuestionAnswer questionAnswer = new QuestionAnswer(questionId, answerId);
                                    databaseManager.insert(questionAnswer);
                                }
                            }
                        }
                        databaseManager.truncate(ShipmentControl.class);

                        ShipmentControl shipmentControl = new ShipmentControl();
                        shipmentControl.setDate(LocalDate.now().toDate());
                        shipmentControl.setStatus(0);
                        databaseManager.insert(shipmentControl);

                        view.stopSyncProgress();
                        view.showMessage("Sincronización realizada satisfactoriamente.");

                    }

                    @Override
                    public void onFailure(Call<QuestionnairesDto> call, Throwable t) {
                        ShipmentConfigPresenterImpl.this.view.stopSyncProgress();
                        ShipmentConfigPresenterImpl.this.view.showError("Existió un error al realizar la sincronización, vuelva a intentarlo más tarde.");
                    }
                });
    }

    @Override
    public void getTrucks() {
        List<Truck> trucks = databaseManager.listAll(Truck.class);
        this.view.showSelectTruck(trucks);
    }

    @Override
    public void saveCurrentTruck(long truckId) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        if (shipmentControl == null) {
            view.showError("No se ha realizado sincronización el día de hoy. Realice \"Sincronización\" e intente nuevamente.");
        } else {
            if (shipmentControl.getStatus() != 0) {
                ShipmentConfigPresenterImpl.this.view.stopSyncProgress();
                view.showError("No es posible seleccionar camión, debido a que ya tienes viajes en curso.");
                return;
            }
            if (truckId == 0) {
                view.showError("Debes seleccionar un camión para poder registrarlo.");
                return;
            }
            shipmentControl.setTruckId(truckId);
            databaseManager.update(shipmentControl);
            view.showMessage("Camíon seleccionado correctamente.");
        }
    }
}
