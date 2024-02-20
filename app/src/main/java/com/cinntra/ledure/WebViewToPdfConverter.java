package com.cinntra.ledure;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import android.webkit.WebView;
import androidx.core.content.FileProvider;
import java.io.File;


public class WebViewToPdfConverter {

    private static final int REQUEST_CODE = 101;


    public static void createWebPrintJob(Activity activity, WebView webView, File directory, String fileName, final Callback callback) {

        //check the marshmallow permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                callback.failure();
                return;
            }
        }

        String jobName = activity.getString(R.string.app_name) + " Document";
        PrintAttributes attributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
        }
        PdfPrint pdfPrint = new PdfPrint(attributes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pdfPrint.print(webView.createPrintDocumentAdapter(jobName), directory, fileName, new PdfPrint.CallbackPrint() {
                @Override
                public void success(String path) {
                    callback.success(path);
                }

                @Override
                public void onFailure() {
                    callback.failure();
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pdfPrint.print(webView.createPrintDocumentAdapter(), directory, fileName, new PdfPrint.CallbackPrint() {
                    @Override
                    public void success(String path) {
                        callback.success(path);
                    }

                    @Override
                    public void onFailure() {
                        callback.failure();
                    }
                });
            }
        }
    }



    public static void openPdfFile(final Activity activity, String title, String message, final String path) {

        //check the marshmallow permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fileChooser(activity, path);
            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    public interface Callback
            {
        void success(String path);

        void failure();
    }



    private static void fileChooser(Activity activity, String path)
            {
        File file = new File(path);
        Intent target = new Intent("android.intent.action.VIEW");
        Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext()
                .getPackageName() + ".FileProvider", file);
        target.setDataAndType(uri, "application/pdf");
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = Intent.createChooser(target, "Open File");
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException var6) {
            var6.printStackTrace();
        }

    }

}
