package com.example.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button buttonIniciar, buttonFinalizar;
    private int valorI;
    // A thread que vai receber os códigos é a thread que criou o handler
    private Handler handler = new Handler();
    private boolean pararExecucao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonIniciar = findViewById(R.id.buttonIniciar);
        buttonFinalizar = findViewById(R.id.buttonfinalizar);
    }

    public void pararThread(View view) {
        pararExecucao = true;
    }

    public void iniciarThread(View view) {
//        for (int i = 0; i <= 15; i++) {
//            Log.d("Thread", "contador:  " + i);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        MyThread myThread = new MyThread();
//        myThread.start();
        pararExecucao = false;
        MyRunnable myRunnable = new MyRunnable();
        new Thread(myRunnable).start();
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i <= 15; i++) {
                if (pararExecucao)
                    return;
                valorI = i;
                Log.d("Thread", "contador:  " + i);


//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        buttonIniciar.setText("contador:  " + valorI);
//                    }
//                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        buttonIniciar.setText("contador:  " + valorI);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
//            for (int i = 0; i <= 15; i++) {
//                Log.d("Thread", "contador:  " + i);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
