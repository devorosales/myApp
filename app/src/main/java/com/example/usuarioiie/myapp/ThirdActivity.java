package com.example.usuarioiie.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextWeb;
    private ImageButton imgButtonPhone;
    private ImageButton imgButtonWeb;
    private ImageButton imgButtonCamera;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextWeb = (EditText) findViewById(R.id.editTextWeb);
        imgButtonPhone = (ImageButton) findViewById(R.id.imageButtonPhone);
        imgButtonWeb = (ImageButton) findViewById(R.id.imageButtonWeb);
        imgButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        imgButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhone.getText().toString();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    //Comprobar version actual de android que estamos corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        //Comprobar si ha aceptado, si nunca a aceptado o nunca se le ha preguntado
                        if(CheckPermission(Manifest.permission.CALL_PHONE)){
                            //Ha aceptado
                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)return;
                            startActivity(i);
                        }else{
                            //o no a aceptado o es la primera vez que se le pregunta
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                // No se le ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            }else{
                                //Ha denegado
                                Toast.makeText(ThirdActivity.this, "Please enabled the request permission", Toast.LENGTH_LONG).show();
                                Intent ver = new Intent();
                                ver.addCategory(Intent.CATEGORY_DEFAULT);
                                ver.setData(Uri.parse("package:"+getPackageName()));
                                ver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ver.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                ver.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(ver);
                            }
                        }

                    } else {
                        olderVersion(phoneNumber);
                    }
                }
                else{
                    Toast.makeText(ThirdActivity.this, "Insert a phone number", Toast.LENGTH_LONG).show();
                }
            }

            private void olderVersion(String phoneNumber) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ThirdActivity.this, "You decline the access", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Boton para la direccion WEB
        imgButtonWeb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String url = editTextWeb.getText().toString();
                if(url != null && !url.isEmpty()){
                    Intent intentWeb = new Intent();
                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("http://"+url));
                    startActivity(intentWeb);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // estamos en el caso del telefono
        switch (requestCode) {
            case PHONE_CALL_CODE:
                String permision = permissions[0];
                int resul = grantResults[0];

                if (permision.equals(Manifest.permission.CALL_PHONE)) {
                    //COMPROBAR SI HA SIDO ACEPTADO O DENEGADO LA OPCION DE PERMISO
                    if (resul == PackageManager.PERMISSION_GRANTED) {
                        //Concedio su permiso
                        String phoneNumber = editTextPhone.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)return;
                        startActivity(intentCall);
                    }
                    else {
                        //No concedio su permiso
                        Toast.makeText(ThirdActivity.this, "You decline the access", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
