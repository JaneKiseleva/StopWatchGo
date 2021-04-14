package com.example.stopwatchgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;

    private int seconds = 0;
    private boolean isRunning = false;
    private boolean wasRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //конфигурация, поиск текста, условие при котором присваивается текущее состояние таймера InstanteState, запуск таймера
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            isRunning = savedInstanceState.getBoolean("isRunning");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }

/*
    @Override
    protected void onStop() {
        super.onStop();
        wasRunning = isRunning;
        isRunning = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = wasRunning;
    }
*/

    // потеря фокуса активности - два метода переопределить

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = wasRunning;
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = isRunning;
        isRunning = false;
    }


    //переопределить метод для сохранения текущего состояния, параметр Bundle, вынуть текущее состояние таймера
    @Override
    protected void onSaveInstanceState(Bundle outState) { //Метод для сохранения текущего состояния активности, в качестве параметра
        // принимает Bundle - он позволяет объединить разные типы данных в один объект.
        super.onSaveInstanceState(outState); //переопредилить метод? Обязательно должно быть обращение к родительскому классу
        outState.putInt("seconds", seconds);
        outState.putBoolean("isRunning", isRunning);
        outState.putBoolean("wasRunning", wasRunning);
    }

    public void onClickStartTimer(View view) {
        isRunning = true;
    }

    public void onClickPauseTimer(View view) {
        isRunning = false;
    }

    public void onClickResetTimer(View view) {
        isRunning = false;
        seconds = 0;
    }

    private void runTimer () {

        final Handler handler = new Handler(); //новый объект класса Handler. Дает возможность работы с другими потоками и планировать работу в будущем
        handler.post(new Runnable() { // КАК можно быстрее вызови данный код (но он вызовется один раз, поэтому используем дальше еще один метод post.Delayed c задержкой в секунду
            @Override
            public void run() {
                int hours = seconds / 3600;  //взяла количество секунд с начала работы таймера
                int minutes = (seconds % 3600) / 60; //получила количество часов, минут и секунд
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);  //передать данные в String формат
                textViewTimer.setText(time); // присвоила текстовому полю  макета

                if (isRunning) {
                    seconds++; //количество секунд увеличила на единицу
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}
