package com.ColdStage.csViever;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 29.08.14
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
//  Страница pager'а
public class PageFragment extends Fragment {

    public static final String ARGUMENT_POSITION = "arg_position";
    public static final String ARGUMENT_LENGTH = "arg_length";
    public static final String ARGUMENT_PATH = "arg_path";
    public static final String ARGUMENT_RESOLUTION = "arg_resolution";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment, container, false);
        //  Получаем аргументы из адаптера.
        Bundle bundle = getArguments();
        if (bundle != null){
            int position = bundle.getInt(ARGUMENT_POSITION);
            int length = bundle.getInt(ARGUMENT_LENGTH);
            int resolution = bundle.getInt(ARGUMENT_RESOLUTION);
            String path = bundle.getString(ARGUMENT_PATH);

            File camera_dir = new File(Environment.getExternalStorageDirectory() + getString(R.string.camera_folder));
            TextView tvPosition = (TextView) view.findViewById(R.id.textView_position);
            ImageView pImageView = (ImageView) view.findViewById(R.id.imageView_bigpic2);
            ////    Выполняться не должен по идее этот if.
            if (position >= length){
                position = 0;
            }   else if (position < 0){
                position = length - 1;
            }
            //  Инициализируем счетчик изображений.
            CharSequence sequence = (position+1)+"/"+length;
            tvPosition.setText(sequence);

            File photo_name = new File(camera_dir, path);
            //Uri uri = Uri.parse(photo_name.toString());
            //  Загружаем изображение в ImageView.
            PhotoListCombiner.setPic(pImageView, resolution, photo_name.getPath());
        }

        return view;
    }
}   ////    Отредактировать.
