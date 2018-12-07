package com.tiendas3b.almacen.util.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by danflo on 27/11/2017 madafaka.
 */

public class TscSdk {

    public static final CharSequence NAME = "BTSPP";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "TscSdk";
    private BluetoothSocket btSocket = null;
    private OutputStream OutStream = null;
//    private InputStream InStream = null;

    public boolean openPort(String macAddress) {
        BluetoothDevice device;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.isEnabled()) {
//                this.IsConnected = true;
            device = mBluetoothAdapter.getRemoteDevice(macAddress);

            try {
                this.btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException var8) {
                return false;
            }

            mBluetoothAdapter.cancelDiscovery();

            try {
                this.btSocket.connect();
                this.OutStream = this.btSocket.getOutputStream();
//                this.InStream = this.btSocket.getInputStream();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
//                this.detect_bluetooth();
//                this.IsConnected = false;
            return false;
        }
    }

    public String setup(int width, int height, int speed, int density, int sensor, int sensor_distance, int sensor_offset) {
        Log.e(TAG, "height:" + height);
        String message = "";
        String size = "SIZE " + width + " mm" + ", " + height + " mm";
        String speed_value = "SPEED " + speed;
        String density_value = "DENSITY " + density;
        String sensor_value = "";
        if(sensor == 0) {
            sensor_value = "GAP " + sensor_distance + " mm" + ", " + sensor_offset + " mm";
        } else if(sensor == 1) {
            sensor_value = "BLINE " + sensor_distance + " mm" + ", " + sensor_offset + " mm";
        }

        message = size + "\r\n" + speed_value + "\r\n" + density_value + "\r\n" + sensor_value + "\r\nDIRECTION 0,0\r\n";
        return sendCommand(message);
//        byte[] msgBuffer = message.getBytes();
//        if(this.OutStream != null && this.InStream != null) {
//            try {
//                this.OutStream.write(msgBuffer);
//                return "1";
//            } catch (IOException var15) {
//                return "-1";
//            }
//        } else {
//            return "-1";
//        }
    }

    public String cls() {
        String message = "CLS\r\n";
        return sendCommand(message);
//        byte[] msgBuffer = message.getBytes();
//        if(this.OutStream != null && this.InStream != null) {
//            try {
//                this.OutStream.write(msgBuffer);
//                return "1";
//            } catch (IOException var4) {
//                return "-1";
//            }
//        } else {
//            return "-1";
//        }
    }

    public String sendCommand(String message) {
        byte[] msgBuffer = message.getBytes();
        try {
            this.OutStream.write(msgBuffer);
            return "1";
        } catch (IOException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public void print() {
        print(1);
    }
    public String print(int quantity) {
        return print(quantity, 1);
    }

    public String print(int quantity, int copy) {
        String message = "PRINT " + quantity +"\r\n";
        return sendCommand(message);
//        byte[] msgBuffer = message.getBytes();
//        if(this.OutStream != null && this.InStream != null) {
//            try {
//                this.OutStream.write(msgBuffer);
//                return "1";
//            } catch (IOException var6) {
//                return "-1";
//            }
//        } else {
//            return "-1";
//        }
    }

    public void closePort() {
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException var4) {
            var4.printStackTrace();
        }

        if(this.btSocket.isConnected()) {
            try {
//                this.IsConnected = false;
                this.btSocket.close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "success");
        } else {
            Log.e(TAG, "else");
        }
    }

    public void textAndPrint(String text) {
        cls();
        String message = "TEXT 10,40,\"3\" ,0 ,1 ,1 ,\"" + text + "\"\r\n";
        sendCommand(message);
        print();
    }


    public void text(int y, String text) {
        String message = "TEXT 10," + y + ",\"3\" ,0 ,1 ,1 ,\"" + text + "\"\r\n";
        Log.e(TAG, message);
        sendCommand(message);
    }
}
