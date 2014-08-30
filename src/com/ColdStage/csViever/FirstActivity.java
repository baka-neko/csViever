package com.ColdStage.csViever;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

// ОКНО 1
public class FirstActivity extends Activity {
    private ArrayList<String> filesInFolder;
    private ListView lv;

     /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //  Находим по идентификатору ListView на layout.main ...
        lv = (ListView)findViewById(R.id.listView); //// Надо бы попонятнее идентификаторы назначать сразу.

        //  Для ListView определяем поведеним onItemClick запуск полноэкранного просмотра выбранного изображения.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent GoToFullscreen = new Intent(FirstActivity.this, SecondActivity.class);
                //  Передаем параметры в новое Activity
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("filesInFolder", filesInFolder);
                //  Порядковый номер изображения, именно его будем показывать при переходе
                bundle.putInt("position", position);
                GoToFullscreen.putExtras(bundle);
                FirstActivity.this.startActivity(GoToFullscreen, bundle);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //  Поместил инициализацию в onStart, ибо в эту Activity ещё можно вернуться,
        //  а список файлов при этом может стать неактуальным.

        //  Получаем список имен изображений в директории камеры
        filesInFolder = PhotoListCombiner.getPhotoNames(getAlbumDir());
        //  ... и записываем в него имена изображений.
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, filesInFolder));
    }

    //  Обрабатываем нажатие кнопки Back на устройстве.
    @Override
    public void onBackPressed() {
        //  Создаем и показываем AlertDialog с заголовком "Выйти из программы?" и двумя кнопками "ДА" и "НЕТ".
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
        builder.setTitle(getString(R.string.close_alert_title))
                //.setCancelable(false) ////  Закомментим строку - оставим возможность закрыть диалог повторным нажатием Back, хоть в тз это поведение не описано.
                .setPositiveButton(R.string.close_alert_EXIT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Закрываем приложение при получении подтверждения.
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.close_alert_CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert_exit = builder.create();
        alert_exit.show();
    }

    //  Возвращает путь до директории с фотографиями
    private File getAlbumDir(){
        return (new File (Environment.getExternalStorageDirectory() +
                //  TODO Придумать как поулчать полный путь до директории с фотографиями камеры, без строковых констант.
                getString(R.string.camera_folder)));
    }
}   ////    OK
