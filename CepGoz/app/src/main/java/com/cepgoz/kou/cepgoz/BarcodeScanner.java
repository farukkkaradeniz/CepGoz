package com.cepgoz.kou.cepgoz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;import java.lang.Exception;import java.lang.Override;import java.lang.Runnable;import java.lang.String;import java.lang.System;
import java.util.Arrays;

public class BarcodeScanner extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private Button scanButton;
    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(BarcodeScanner.this,
                    Manifest.permission.CAMERA)){
            }else{
                ActivityCompat.requestPermissions(BarcodeScanner.this,
                        new String[]{Manifest.permission.CAMERA},1);
            }
        }
        initControls();
    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(BarcodeScanner.this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (Button) findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();
                    fonk(scanResult);
//                    showAlertDialog(scanResult);

                    barcodeScanned = true;

                    break;
                }
            }
        }
    };

    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }
    private void fonk(String barcode){
        WebServiceBarcode wbs = new WebServiceBarcode();
        String _ItemName = wbs.getItemNameByBarcodeNumber(barcode);
        String _data = wbs.getItemPriceByItemName(_ItemName);
        String[] _fiyatlar =duzenleme(_data);
        if(_fiyatlar.length>0){
        String aradeger=_fiyatlar[0];
        int[] _newFiyatlar = new int[_fiyatlar.length];
        for (int i =0;i<_fiyatlar.length;i++){
            _newFiyatlar[i]=Integer.parseInt(_fiyatlar[i]);
        }
        Arrays.sort(_newFiyatlar);
        String returnValue =wbs.getItemMarketNameByPriceAndByItemName(_ItemName, String.valueOf(_newFiyatlar[0]));
        Toast.makeText(getApplicationContext(), returnValue.trim(), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Taranan ürün veritabanında bulunamadı",Toast.LENGTH_SHORT).show();
        }

    }
    private String[] duzenleme(String marketler){
        String[] markets = new String[]{};
        markets = marketler.split("string=");
        for (int i=0;i<markets.length-1;i++ ){
            markets[i]=markets[i+1];
        }
        String[] donus = new String[markets.length-1];
        for (int i=0;i<markets.length-1;i++){
            markets[i] = markets[i].substring(0,markets[i].indexOf(" "));
            donus[i]=markets[i];
        }
        for (int i=0;i<markets.length-1;i++){
            if(markets[i].contains(";")) {
                markets[i] = markets[i].substring(0, markets[i].indexOf(";"));
                donus[i] = markets[i];
            }
        }
        return  donus;
    }

}
