package com.usermindarchive.h.googlezxing;

import android.Manifest;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
@BindView(R.id.textView)
    TextView data;

    @BindView(R.id.button)
    Button button;

    ZXingScannerView zXingScannerView;
    String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        permissions= new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(MainActivity.this,permissions,6);
        if(getIntent().getStringExtra("data")!=null)
        data.setText(getIntent().getStringExtra("data"));
        else
            data.setText("this wont work");

    }

    @OnClick(R.id.button)
    public void button(View view){
        zXingScannerView=new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    @OnClick(R.id.textView)
    public void data(View view){
        Intent search=new Intent(Intent.ACTION_WEB_SEARCH);
        search.putExtra(SearchManager.QUERY,data.getText());
        Intent chose= Intent.createChooser(search,"select the brower to search");
        startActivity(chose);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(zXingScannerView!=null)
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
    }

    @Override
    public void handleResult(Result result) {
        Log.e("handler", result.getText());
        Log.e("handler", result.getBarcodeFormat().toString());

        Intent scan= new Intent(this,MainActivity.class);
        scan.putExtra("data",result.getText());
        startActivity(scan);


//        zXingScannerView.stopCamera();
//        update(result.getText());
//        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
//        setContentView(R.layout.activity_main);
//        zXingScannerView.resumeCameraPreview(this);

    }

    public void update(String str){
        data.setText(str);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 6 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //the main function u need

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(MainActivity.this, "THIS IS shouldS", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder dia = new AlertDialog.Builder(MainActivity.this);
                    dia.setMessage("Enable the required Permission for the Application\n Go to Setting and Enable the Permissions");
                    dia.setTitle("PERMISSIONS NEEDED");
                    dia.setPositiveButton("PERMISSION", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    dia.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "PERMISSIONS ARE NOT ENABLED", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = dia.create();
                    dialog.show();

                }
            }
        }


    }
}
