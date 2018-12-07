package com.tiendas3b.almacen.util;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.tiendas3b.almacen.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dfa on 21/06/2016.
 */
public class PdfUtil {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String createPdf(View content, String name) {
        //TODO pedir permiso de almacenamiento
        PdfDocument document = new PdfDocument();

//        LinearLayout content = (LinearLayout) view.findViewById(R.id.ejemplo);

        int pageNumber = 1;
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
//                PrintAttributes.MediaSize.ISO_A4.getWidthMils(),
//                PrintAttributes.MediaSize.ISO_A4.getHeightMils(),
                content.getWidth(),
                content.getHeight(),
// 612,
// 792,
                pageNumber).create();

        PdfDocument.Page page = document.startPage(pageInfo);
        content.draw(page.getCanvas());
        document.finishPage(page);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault());
        String pdfName = name + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

        Context context = content.getContext();
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                .concat(context.getString(R.string.root_folder).concat(context.getString(R.string.decrease_folder))), pdfName);

        File parent = outputFile.getParentFile();
        if(!parent.exists() && !parent.mkdirs()){
            return null;
        } else {
            try {
                outputFile.createNewFile();
                OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(out);
                document.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("TAG", "PDF!");
        }


        return outputFile.getPath();
    }
}
