package com.ColdStage.csViever;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;
import java.io.File;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 29.08.14
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
//  ОКНО 2.
public class SecondActivity extends FragmentActivity {

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private ArrayList<String> filesInFolder;
    private int vp_position;
    private ImageView pImageView;
    private final int CAMERA_RESULT = 0;
    private Button btn_left, btn_right;
    private ImageButton btn_camera;
    private TextView tvPosition;
    public int maxScreenResolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_img);

        //  Получаем размер наибольшей стороны экрана.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        maxScreenResolution = Math.max(metrics.widthPixels, metrics.heightPixels);

        //  Получаем параметры из ОКНА 1.
        Bundle b = getIntent().getExtras();
        filesInFolder = b.getStringArrayList("filesInFolder");
        vp_position = b.getInt("position");
        //  640K ought to be enough for anybody
        vp_position = filesInFolder.size() * (640000 / filesInFolder.size()) + vp_position;//filesInFolder.size() * (Integer.MAX_VALUE / 2 / filesInFolder.size()) + vp_position;    Похоже, оказалось слишком много, приложение зависало.
        //  Назначаем обработчик страниц с фотографиями
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PageStatePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        //  Смещаем позицию начала просмотра чуть вперёд, чтобы было куда пролистывать влево.
        pager.setCurrentItem(vp_position,false);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //  Обновляем переменную, иначе попадем на начальную старницу после запуска камеры.
                vp_position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //  Скрываем интерфейс при перелистывании
                hideGUI (!(state == ViewPager.SCROLL_STATE_IDLE));
            }
        });

        // Находим кнопки и вешаем на них обработчики - перелистывание и съемка.
        btn_left = (Button) findViewById(R.id.button_left);
        btn_right = (Button) findViewById(R.id.button_right);
        btn_camera = (ImageButton) findViewById(R.id.button_camera);
        //  Переход на предыдущее изображение.
        btn_left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPhoto(-1); ////  Unused with ViewPager
                if (vp_position-- < 0){
                    vp_position = filesInFolder.size();
                }
                pager.setCurrentItem(vp_position);
            }
        });
        //  Переход на Следующее изображение.
        btn_right.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPhoto(1); ////  Unused with ViewPager
                vp_position++;
                pager.setCurrentItem(vp_position);
            }
        });
        // Съемка системной камерой.    ////   TODO Если камеры или системного приложения нет - косяк.
        btn_camera.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        //mediaUpdate();    ////    Unused with ViewPager
        //  Обновляем список изображений
        filesInFolder = PhotoListCombiner.getPhotoNames(getAlbumDir());
        //  Повторно инициализируем pager, чтобы избавиться от сохраненных в памяти страниц,
        //  т.к. они могут стать неактуальными после запуска камеры.
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(vp_position);
    }
    //  Скрываем кнопки при перелистывании, т.к. иначе они тоже "перелистываются".
    ////    Возможно, если поместить кнопки не поверх pager'а, проблему можно было бы решить, но часть экрана будет потеряна для фотографий.
    private void hideGUI(boolean hide){
        int isVisible;
        if (hide) {
            isVisible = View.INVISIBLE;
        }   else {
            isVisible = View.VISIBLE;
        }
        btn_left.setEnabled(!hide);
        btn_left.setVisibility(isVisible);

        btn_right.setEnabled(!hide);
        btn_right.setVisibility(isVisible);

        btn_camera.setEnabled(!hide);
        btn_camera.setVisibility(isVisible);
    }

    ////  Unused with ViewPager
    private void mediaUpdate(){
        filesInFolder = PhotoListCombiner.getPhotoNames(getAlbumDir());
        // Если фоток стало меньше, чем текущая позиция (например, удалили несколько неудачных)
        if (vp_position >= filesInFolder.size()){
            //Выставляем позицию на конец альбома
            vp_position = filesInFolder.size() - 1;
        }
        setPhoto();
    }

    private File getAlbumDir(){
        return (new File (Environment.getExternalStorageDirectory() +
                getString(R.string.camera_folder)));
    }
    ////  Unused with ViewPager
    private void setPhoto(){
        //  Циклический просмотр.
        if (vp_position >= filesInFolder.size()){
            vp_position = 0;
        }   else if (vp_position < 0){
            vp_position = filesInFolder.size() - 1;
        }
        //  Обновляем счетчик фотографий.
        CharSequence sequence = (vp_position +1)+"/"+filesInFolder.size();
        tvPosition.setText(sequence);
        File camera_dir = new File(Environment.getExternalStorageDirectory() + getString(R.string.camera_folder));
        File photo_name = new File(camera_dir, filesInFolder.get(vp_position));
        Uri uri = Uri.parse(photo_name.toString());
        //  Обновляем фотографию
        pImageView.setImageURI(uri);
    }
    ////  Unused with ViewPager
    private void setPhoto(int delta){
        vp_position += delta;
        setPhoto();
    }
}