package com.tiendas3b.almacen.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dfa on 02/09/2016.
 */
public class FileUtil {

    private static final String FILE_DEFAULT = "app.txt";

    public static synchronized void writeFile(String line) {
        writeFile(line, FILE_DEFAULT);
    }

    public static void writeFile(String line, String filename) {
        writeFile(line, Environment.getExternalStorageDirectory(), filename);
    }

    public static void writeFile(String line, String directory, String filename) {
        writeFile(line, new File(directory), filename);
    }

    public static void writeFile(String line, File root, String filename) {
        File file = new File(root, filename);
        try {
            if (root.canWrite()) {
                FileWriter filewriter = new FileWriter(file, true);
                BufferedWriter out = new BufferedWriter(filewriter);
                out.write(line.concat("\n"));
                out.close();
                filewriter.close();
            }
        } catch (IOException e) {
            Log.e("TAG", "Could not write file " + e.getMessage());
        }
    }
}
