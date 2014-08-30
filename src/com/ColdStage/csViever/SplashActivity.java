package com.ColdStage.csViever;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: Spek
 * Date: 28.08.14
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
//  Splash Screen
public class SplashActivity extends Activity {
    //  Константа хранит время отображения splash screen (в мс).
    private final int SPLASH_DISPLAY_DURATION = 3000;   ////  Нужно ли записывать имя капсом?

    @Override   //  Проверка действительно ли метод помеченный данной аннотацией, переопределяет метод суперкласса.
    protected void onCreate(Bundle savedInstanceState) {
        //  Вызывает переоперделяемый метод из суперкласса.
        super.onCreate(savedInstanceState);
        //  Определяем какой layout рисовать на экране.
        setContentView(R.layout.splash);
    }

    @Override
    protected void onResume()   ////    Почему именно в onResume?
    {
        super.onResume();
        //  Отложенный запуск кода.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  Завершаем Activity, до перезапуска приложения не понадобится.
                SplashActivity.this.finish();
                Intent GoToApp = new Intent(SplashActivity.this, FirstActivity.class);
                //  запускаем первое окно приложения
                SplashActivity.this.startActivity(GoToApp);
            }
            //  Задержка выполенния на определённое в константе время.
        }, SPLASH_DISPLAY_DURATION);
    }
}   ////    OK