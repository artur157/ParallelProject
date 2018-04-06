package com.example.pp;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class pp extends AppCompatActivity {
    public int quanOfThreads = 1;
    public int N;
    public double[] vec1, vec2, sum, res;
    public static double[][] matr1, matr2, matr3;
    public double num;
    public long time1, time2;
    //public int progress = 0;
    public int meth = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pp);
    }

    class ProgressTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... inParam) {
            int k = inParam[0];       // номер потока
            int begin = k * (N / quanOfThreads);
            int end = (k + 1) * (N / quanOfThreads);
            if (k + 1 == quanOfThreads){  // если поток последний, то захватываем всё что осталось
                end = N;
            }
            for (int j = begin; j < end; j++) {
                vec1[j] *= Math.sin(Math.sin(num));
            }
            return(null);
        }

        @Override
        protected void onProgressUpdate(Integer... items) {
            //super.onProgressUpdate(items);
        }

        @Override
        protected void onPostExecute(Void unused) {
            //super.onPostExecute(unused);
        }
    }

    class ProgressTask2 extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... inParam) {
            int k = inParam[0];       // номер потока
            int begin = k * (N / quanOfThreads);
            int end = (k + 1) * (N / quanOfThreads);
            if (k + 1 == quanOfThreads){  // если поток последний, то захватываем всё что осталось
                end = N;
            }
            for (int j = begin; j < end; j++) {
                double tmp = Math.sin(Math.sin(vec1[j] * vec2[j]));
                sum[k] += tmp;
            }
            return(null);
        }

        @Override
        protected void onProgressUpdate(Integer... items) {
            //super.onProgressUpdate(items);
        }

        @Override
        protected void onPostExecute(Void unused) {
            //super.onPostExecute(unused);
        }
    }

    class ProgressTask3 extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... inParam) {
            int k = inParam[0];       // номер потока
            int begin = k * (N / quanOfThreads);
            int end = (k + 1) * (N / quanOfThreads);
            if (k + 1 == quanOfThreads){  // если поток последний, то захватываем всё что осталось
                end = N;
            }
            for (int j = begin; j < end; j++) {
                vec1[j] *= Math.sin(Math.sin(num));
            }
            return(null);
        }

        @Override
        protected void onProgressUpdate(Integer... items) {
            //super.onProgressUpdate(items);
        }

        @Override
        protected void onPostExecute(Void unused) {
            //super.onPostExecute(unused);
        }
    }

    class ProgressTask4 extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... inParam) {
            int k = inParam[0];       // номер потока
            int begin = k * (N * N / quanOfThreads);
            int end = (k + 1) * (N * N / quanOfThreads);
            if (k + 1 == quanOfThreads){  // если поток последний, то захватываем всё что осталось
                end = N * N;
            }
            for (int j = begin; j < end; j++) {
                int rowNum = j / N, colNum = j % N;
                for (int m = 0; m < N; ++m) {
                    matr3[rowNum][colNum] += matr1[rowNum][m] * matr2[m][colNum];
                }
            }
            return(null);
        }

        @Override
        protected void onProgressUpdate(Integer... items) {
            //super.onProgressUpdate(items);
        }

        @Override
        protected void onPostExecute(Void unused) {
            //super.onPostExecute(unused);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String st = bundle.getString("Str");
            TextView infoTextView = (TextView) findViewById(R.id.textView3);
            infoTextView.setText(st);
        }
    };

    public void onButton1Click(View view)
    {
        N = 20000000;
        vec1 = new double[N];

        // присвоили числу и вектору значения
        num = Math.random() * 20 - 10;
        for (int i = 0; i < N; i++){
            vec1[i] = Math.random() * 20 - 10;
        }

        if (meth == 1) {
            Thread[] threads = new Thread[quanOfThreads];
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i] = new MyThread(i) {
                    public void run() {
                        int k = this.number;       // номер потока
                        int begin = k * (N / quanOfThreads);
                        int end = (k + 1) * (N / quanOfThreads);
                        if (k + 1 == quanOfThreads) {  // если поток последний, то захватываем всё что осталось
                            end = N;
                        }
                        for (int j = begin; j < end; j++) {
                            vec1[j] *= Math.sin(Math.sin(num));
                        }
                        /// начинаем операцию по пересылке сообщения в Handler
                        Bundle bundle = new Bundle();
                        bundle.putString("Str", "Сообщение из потока " + k);
                        Message msg = new Message();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        ///
                    }
                };
            }

            long time1 = System.currentTimeMillis();

            // запустили потоки
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i].start();
            }

            // ожидаем завершения потоков
            for (int i = 0; i < quanOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }

            long time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2 - time1) / 1000f));
        }
        else{
            time1 = System.currentTimeMillis();

            if(quanOfThreads == 2){
                ProgressTask progressTask = new ProgressTask();
                progressTask.execute(1);
            }

            // сам считает
            for (int j = 0; j < N / quanOfThreads; j++) {
                vec1[j] *= Math.sin(Math.sin(num));
            }

            time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2-time1) / 1000f));
            SystemClock.sleep(1000);
        }
        vec1 = null;
    }

    public void onButton2Click(View view)
    {
        N = 20000000;
        vec1 = new double[N];
        vec2 = new double[N];
        sum = new double[quanOfThreads];
        num = 0;

        // присвоили числу и вектору значения
        for (int i = 0; i < N; i++){
            vec1[i] = Math.random() * 20 - 10;
            vec2[i] = Math.random() * 20 - 10;
        }

        // обнуляем вектор сумм
        for (int i = 0; i < quanOfThreads; i++){
            sum[i] = 0;
        }

        if (meth == 1) {
            Thread[] threads = new Thread[quanOfThreads];
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i] = new MyThread(i) {
                    public void run() {
                        int k = this.number;       // номер потока
                        int begin = k * (N / quanOfThreads);
                        int end = begin + N / quanOfThreads;
                        if (k + 1 == quanOfThreads) {  // если поток последний, то захватываем всё что осталось
                            end = N;
                        }
                        for (int j = begin; j < end; j++) {
                            double tmp = Math.sin(Math.sin(vec1[j] * vec2[j]));
                            sum[k] += tmp;
                        }
                        /// начинаем операцию по пересылке сообщения в Handler
                        Bundle bundle = new Bundle();
                        bundle.putString("Str", "Сообщение из потока " + k);
                        Message msg = new Message();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        ///
                    }
                };
            }

            long time1 = System.currentTimeMillis();

            // запустили потоки
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i].start();
            }

            // ожидаем завершения потоков
            for (int i = 0; i < quanOfThreads; i++) {
                try {
                    threads[i].join();
                    num += sum[i];
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }

            long time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2 - time1) / 1000f));
        }
        else{
            time1 = System.currentTimeMillis();

            if(quanOfThreads == 2){
                ProgressTask2 progressTask = new ProgressTask2();
                progressTask.execute(1);
            }

            // сам считает
            for (int j = 0; j < N / quanOfThreads; j++) {
                double tmp = Math.sin(Math.sin(vec1[j] * vec2[j]));
                sum[0] += tmp;
            }

            time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2-time1) / 1000f));
            SystemClock.sleep(1000);
        }
        vec1 = null;
        vec2 = null;
    }

    public void onButton3Click(View view)
    {
        N = 5000;
        matr1 = new double[N][N];
        vec1 = new double[N];
        res = new double[N];

        // присвоили числу и вектору значения
        for (int i = 0; i < N; i++){
            vec1[i] = Math.random() * 20 - 10;
            for (int j = 0; j < N; j++){
                matr1[i][j] = Math.random() * 20 - 10;
            }
            res[i] = 0;
        }

        if (meth == 1) {
            Thread[] threads = new Thread[quanOfThreads];
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i] = new MyThread(i) {
                    public void run() {
                        int k = this.number;       // номер потока
                        int begin = k * (N * N / quanOfThreads);
                        int end = begin + N * N / quanOfThreads;
                        if (k + 1 == quanOfThreads) {  // если поток последний, то захватываем всё что осталось
                            end = N * N;
                        }
                        for (int j = begin; j < end; j++) {
                            int rowNum = j / N, colNum = j % N;
                            res[rowNum] += Math.sin(Math.sin(matr1[rowNum][colNum] * vec1[colNum]));
                        }
                        /// начинаем операцию по пересылке сообщения в Handler
                        Bundle bundle = new Bundle();
                        bundle.putString("Str", "Сообщение из потока " + k);
                        Message msg = new Message();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        ///
                    }
                };
            }

            long time1 = System.currentTimeMillis();

            // запустили потоки
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i].start();
            }

            // ожидаем завершения потоков
            for (int i = 0; i < quanOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }

            long time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2 - time1) / 1000f));
        }
        else{
            time1 = System.currentTimeMillis();

            if(quanOfThreads == 2){
                ProgressTask3 progressTask = new ProgressTask3();
                progressTask.execute(1);
            }

            // сам считает
            for (int j = 0; j < N*N / quanOfThreads; j++) {
                int rowNum = j / N, colNum = j % N;
                res[rowNum] += Math.sin(Math.sin(matr1[rowNum][colNum] * vec1[colNum]));
            }

            time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2-time1) / 1000f));
            SystemClock.sleep(1000);
        }
        matr1 = null;
        vec1 = null;
        res = null;
    }

    public void onButton4Click(View view)
    {
        N = 500;
        matr1 = new double[N][N];
        matr2 = new double[N][N];
        matr3 = new double[N][N];

        // присвоили числу и вектору значения
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                matr1[i][j] = (int)(Math.random() * 20 - 10);
                matr2[i][j] = (int)(Math.random() * 20 - 10);
                matr3[i][j] = 0;
            }
        }

        if (meth == 1) {
            Thread[] threads = new Thread[quanOfThreads];
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i] = new MyThread(i) {
                    public void run() {
                        int k = this.number;       // номер потока
                        int begin = k * (N * N / quanOfThreads);
                        int end = begin + N * N / quanOfThreads;
                        if (k + 1 == quanOfThreads) {  // если поток последний, то захватываем всё что осталось
                            end = N * N;
                        }
                        for (int j = begin; j < end; j++) {
                            int rowNum = j / N, colNum = j % N;
                            for (int m = 0; m < N; ++m) {
                                matr3[rowNum][colNum] += matr1[rowNum][m] * matr2[m][colNum];
                            }
                        }
                        /// начинаем операцию по пересылке сообщения в Handler
                        Bundle bundle = new Bundle();
                        bundle.putString("Str", "Сообщение из потока " + k);
                        Message msg = new Message();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        ///
                    }
                };
            }

            long time1 = System.currentTimeMillis();

            // запустили потоки
            for (int i = 0; i < quanOfThreads; i++) {
                threads[i].start();
            }

            // ожидаем завершения потоков
            for (int i = 0; i < quanOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }

            long time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2 - time1) / 1000f));
        }
        else{
            time1 = System.currentTimeMillis();

            if(quanOfThreads == 2){
                ProgressTask4 progressTask = new ProgressTask4();
                progressTask.execute(1);
            }

            // сам считает
            for (int j = 0; j < N*N / quanOfThreads; j++) {
                int rowNum = j / N, colNum = j % N;
                for (int m = 0; m < N; ++m) {
                    matr3[rowNum][colNum] += matr1[rowNum][m] * matr2[m][colNum];
                }
            }

            time2 = System.currentTimeMillis();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(String.format("Time: %f", (time2-time1) / 1000f));
            SystemClock.sleep(1000);
        }
        matr1 = null;
        matr2 = null;
        matr3 = null;
        // System.gc();
    }

    public void onRb1Click(View view) {
        quanOfThreads = 1;
    }

    public void onRb2Click(View view) {
        quanOfThreads = 2;
    }

    public void onRb11Click(View view) {
        meth = 1;
    }

    public void onRb22Click(View view) {
        meth = 2;
    }
}
