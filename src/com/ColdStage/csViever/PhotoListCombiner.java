package com.ColdStage.csViever;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Spek
 * Date: 29.08.14
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
//  Вспомогательный класс.
public abstract class PhotoListCombiner {   ////    Экземпляров создаавть не будем.
    //  Возвращает сортированный список изображеный в заданной директории.
    public static ArrayList<String> getPhotoNames(File photoFilePath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        // Фильтр файлов по расширению.
        FilenameFilter fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.lastIndexOf('.')>0)
                {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');

                    // get extension
                    String str = name.substring(lastIndex);

                    // match path name extension
                    if( (str.equals(".jpg")) || (str.equals(".jpeg")) || (str.equals(".gif")) || (str.equals(".png")) )
                    {
                        return true;
                    }
                }
                return false;
            }
        };

        File[] files = photoFilePath.listFiles(fileNameFilter);
        if (files.length == 0)
            return null;
        else {
            for(int i=0; i<files.length; i++)
                MyFiles.add(files[i].getName());
        }
        //  Сортируем по имени.
        Collections.sort(MyFiles);
        return MyFiles;
    }
    //  Загружает уменьшенное изображение в ImageView.
    public static ImageView setPic(ImageView mImageView, int maxResolution, String mCurrentPhotoPath) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 1;
        if (maxResolution != 0){
            scaleFactor = Math.max(photoW, photoH)/maxResolution;
        }
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //  Set bitmap to ImageView
        mImageView.setImageBitmap(bitmap);
        return mImageView;  ////    TODO Подумать, нет ли смысла возвращать bitmap вместо ImageView.
    }
}   ////    OK