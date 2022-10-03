package com.example.huellafinal;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.os.Handler;
import android.widget.Toast;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;

public class LectorTarjeta extends AppCompatActivity implements BiometricCallback {


    int REQUEST_ENABLE_BT;
    float Transparencia = 0.75F;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    int auth=0;
    BluetoothSocket btSocket = null;

    SoundPool sp;
    ImageView CuadroVerde;
    Button BTarjeta;
    TextView tvbt;

    //==============ON CREATE==============//
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lector_tarjeta);



        //INICIALIZA VISUALES
        CuadroVerde = (ImageView) findViewById(R.id.ivVerdeAprobado);
        BTarjeta = (Button) findViewById(R.id.BTarjeta);
        tvbt = (TextView) findViewById(R.id.tvbt);


        tvbt.setText("ESPERANDO...");

        //INSTANCIA EL BLUETOOTH
        System.out.println("btAdapter.getBondedDevices()");
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices()); //Encuentra las direcciones mac de los dispositivos

        BluetoothDevice hc05 = btAdapter.getRemoteDevice("98:D3:31:F5:B1:8E");
        System.out.println("hc05.getName()");
        System.out.println(hc05.getName());


        //SOCKET PARA LA COMUNICACION
        //BluetoothSocket btSocket = null;

        int cont = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                //Conexion a la placa "Servidor"
                btSocket.connect();
                System.out.println("btSocket.isConnected()");
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }

            cont++;
        }while (!btSocket.isConnected() && cont <3);





    }

    //==============METODOS==============//

    public void Tarjeta(View view) {

        byte b = 0;
        int B=0;
        try { //Recibir datos

            InputStream inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available()); //INSTANCIA COSAS

            b = (byte) inputStream.read();
            System.out.println("(char) b"); //IMPRIME
            System.out.println((char) b); //IMPRIME
            B=b;
            System.out.println("(INT 1 b"); //IMPRIME
            System.out.println(B); //IMPRIME

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("(INT 2 b"); //IMPRIME
        System.out.println(B); //IMPRIME
        if (B == 49) {
            huella();
        }

    }



    public void huella(){

        System.out.println("todo correcto");

        new BiometricManager.BiometricBuilder(LectorTarjeta.this)
                .setTitle("Add a title")
                .setSubtitle("Add a subtitle")
                .setDescription("Add a description")
                .setNegativeButtonText("Add a cancel button")
                .build()
                .authenticate(LectorTarjeta.this);
    }

    //------------------------------------
    public void BotonAprobado(){

        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein); //CARGA EL TIPO DE ANIMACION
        //PROCESO DE ANIMACION
        animFadeIn.reset();
        CuadroVerde.clearAnimation();
        CuadroVerde.startAnimation(animFadeIn);
        //AL TERMINAR LA ANIMACION, SE HACE VISIBLE EL CUADRO VERDE
        CuadroVerde.setAlpha(Transparencia);
        CuadroVerde.setVisibility(View.VISIBLE);

    }
    public void BotonReprobado(){

        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein); //CARGA EL TIPO DE ANIMACION
        //PROCESO DE ANIMACION
        animFadeIn.reset();
        CuadroVerde.clearAnimation();
        CuadroVerde.startAnimation(animFadeIn);
        //AL TERMINAR LA ANIMACION, SE HACE VISIBLE EL CUADRO VERDE
        CuadroVerde.setAlpha(Transparencia);
        CuadroVerde.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSdkVersionNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {

    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {

    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {

    }

    @Override
    public void onAuthenticationFailed() {

    }

    @Override
    public void onAuthenticationCancelled() {

    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(this,"aaaaaa",Toast.LENGTH_SHORT).show();
        System.out.println("TOAST");

        try { //Enviar datos
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(48);
            System.out.println("xd");
        } catch (IOException e) {
            e.printStackTrace();
        }

      /*  //CERRAR CONEXION
        try {
            btSocket.close();
            System.out.println(btSocket.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }



    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

    }
}