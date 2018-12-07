package com.tiendas3b.almacen.util;

/**
 * Created by dfa on 25/02/2016.
 */
public final class Constants {

//    public static final String URL_1000 = "http://201.161.111.20:8080/almacen/rest/";
    public static final String URL_1000 = "http://192.168.10.7:8080/almacen/rest/";
    public static final String URL_1001 = "http://192.168.20.7:8080/almacen/rest/";
    public static final String URL_1002 = "http://192.168.30.7:8080/almacen/rest/";
    public static final String URL_1003 = "http://192.168.40.7:8080/almacen/rest/";
    public static final String URL_1004 = "http://192.168.50.7:8080/almacen/rest/";
    public static final String URL_1005 = "http://192.168.60.7:8080/almacen/rest/";

    public static final String REGION_1000 = "1000";
    public static final String REGION_1001 = "1001";
    public static final String REGION_1002 = "1002";
    public static final String REGION_1003 = "1003";
    public static final String REGION_1004 = "1004";
    public static final String REGION_1005 = "1005";
    public static final String LOCAL = "1000";

//    public static final String URL_LOCAL = "http://192.200.1.114:8080/almacen/rest/";
    //URL para pruebas
    //public static final String URL_LOCAL = "http://192.168.1.179:8080/almacen/rest/";

    //public static final String URL_LOCAL = "http://192.200.2.104:8080/almacen/rest/";
    public static final String URL_LOCAL = "http://192.200.1.107:8080/almacen/rest/";
    public static final String URL_VRP = "http://201.161.90.103:80/WEBT3BRutas/";

    public static final String URL_SHIPMENT = "http://201.161.90.103:8080/WEBT3BRutas2018/";

//    public static final String DATE_FORMAT_DB = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SHORT = "dd-MM-yy";
//    public static final String DATE_FORMAT = "dd MMMM yyyy";
    public static final String DATETIME_FORMAT = "dd MMMM yyyy HH:mm";
    public static final String DATETIME_FORMAT_WS = "dd-MM-yyyy HH:mm";
    public static final String DATETIME_FORMAT_WS_TIME = "HH:mm";
    public static final String DD_MM = "dd-MM";

    public static final String EXTRA_BUY = "EXTRA_BUY";
    public static final String EXTRA_RECEIP_SHEET = "EXTRA_RECEIP_SHEET";
    public static final String EXTRA_PROVIDER_NAME = "EXTRA_PROVIDER_NAME";
    public static final String EXTRA_BUY_DETAIL_ID = "EXTRA_BUY_DETAIL_ID";
    public static final String EXTRA_EXPRESS_RECEIPT = "EXTRA_BUY_DETAIL_ID";

    public static final String FORMAT_INT = "%d";
    public static final String FORMAT_FLOAT = "%f";



    public static final String TICKETS_OPEN_TICKET = "tickets.ticketabierto";

    public static final String NODE_ADMIN = "admin";
    public static final String NODE_ADD = "add";

    public static final String FORMAT_MONEY = "#,##0.00";
    public static final String FORMAT_INT_THOUSANDS = "#,###";
    public static final int ME = 1;
    public static final int DP = 2;
    public static final int TA = 3;
    public static final int TE = 4;
    public static final int EM = 5;
    public static final int SCAFFOLDS_IN_TRUCK = 15;
    public static final long DIESEL_ID = 2002L;
    public static final long UPLOAD_ACTIVITY_ID = 1L;
    public static final long DIESEL_ACTIVITY_ID = 3L;
    public static final long RETURN_ACTIVITY_ID = 4L;
    public static final long ROUTE_ACTIVITY_ID = 5L;
    public static final long CHECK_ACTIVITY_ID = 6L;

    public static final int NOT_ARRIVE = 0;
    public static final int ARRIVE = 1;
    public static final int IN_PROCESS = 2;
    public static final int NOT_ARRIVE_TEMP = 2;
    public static final int NOT_ARRIVE_AUT = 4;
    public static final int NOT_ARRIVE_AUT_TEMP = 5;
    public static final int ARRIVE_HR_IMPR = 6;

    public static final long WS_SUCCESS = 1;
    public static final String CM = "CM";

    public static final String GROUP_GA = "GA";
    public static final String GROUP_REC = "REC";
    public static final String GROUP_AUDI = "AUDI";
    public static final String GROUP_PCK = "PCK";
    public static final String GROUP_EMB = "EMB";
    public static final String GROUP_FINA = "FINA";

    public static final int UNITY_PLL = 4;
    public static final int MMP_PAL = 1;
    public static final int MMP_BULK = 2;
    public static final int MMP_MIX = 3;
    public static final int MMP_NOT_IN = 0;

    public static final int PICKING_PENDING = 0;
//    public static final int PICKING_PENDING_AUTOMATIC = 3;
    public static final int PICKING_IN_PROCESS = 1;//3 confirmado en boa
    public static final int PICKING_COMPLETE = 2;


    public static final int RECEIPT_PENDING = 0;
    public static final int RECEIPT_PENDING_AUTOMATIC = 1;
    public static final int RECEIPT_IN_PROCESS = 2;//3 confirmado en boa
    public static final int RECEIPT_TK_IMP = 3;
    public static final int RECEIPT_HR_IMP = 4;

    //ESTATUS ID_PROCESO PARA LOG DE RECIBO EXPRESS
    public static final int IDLOG_SELECT_PROV = 1;
    public static final int IDLOG_START_REX = 2;
    public static final int IDLOG_SCANN_PROD = 3;
    public static final int IDLOG_CONFIRM_PROD = 4;
    public static final int IDLOG_CANCEL_REX = 5;
    public static final int IDLOG_PRINT_TICKET = 6;
    public static final int IDLOG_SAVE_REC_EXP = 7;


    //DESCRIPCIONES PARA LOG DE RECIBO EXPRESS
    public static final String LOG_SELECT_PROV = "SELECCIONA PROVEEDOR";
    public static final String LOG_START_REX = "INICIA DESCARGA";
    public static final String LOG_SCANN_PROD = "ESCANEA PRODUCTO";
    public static final String LOG_CONFIRM_PROD = "CONFIRMA PRODUCTO";
    public static final String LOG_CANCEL_REX = "SALE DE PROVEEDOR";
    public static final String LOG_PRINT_TICKET = "IMPRIME TICKET";
    public static final String LOG_SAVE_REC_EXP = "GUARDA RECIBO EXPRESS";



}
