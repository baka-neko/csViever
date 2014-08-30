package com.ColdStage.csViever;

/**
 * Created with IntelliJ IDEA.
 * User: Spek
 * Date: 30.08.14
 * Time: 4:45
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Адаптер хранит только текущую страницу и по одной соседней (справа и слева).
 * Этот адаптер менее производителен при при быстром многократном перелистывании
 * в обе стороны, чем FragmentPagerAdapter,
 * т.к. постоянно пересоздает страницы,
 * но при этом он требует минимум памяти.
 */
public class PageStatePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> fif;

    public PageStatePagerAdapter(FragmentManager fm) {
        super(fm);
        fif = PhotoListCombiner.getPhotoNames(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/"));   //// !
    }

    @Override
    public Fragment getItem(int position) {
        fif = PhotoListCombiner.getPhotoNames(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/"));   //// !
        int relativePosition;
        relativePosition = ((fif.size() + position) % fif.size());
        Bundle bundle = new Bundle();
        bundle.putInt(PageFragment.ARGUMENT_LENGTH, fif.size());
        bundle.putInt(PageFragment.ARGUMENT_POSITION, relativePosition);
        bundle.putInt(PageFragment.ARGUMENT_RESOLUTION, 1280);  //// !!!
        //  Вместо позиции pager'а вычисляем относительную позицию текущего изображения в списке.
        //  Таким образом, реализуем почти бесконечную, бесшовную и плавную прокрутку изображений.
        bundle.putString(PageFragment.ARGUMENT_PATH, fif.get(relativePosition));//fif.get(position));
        //  Создаем фрагмент страницы и передаем в него аргументы.
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(bundle);

        return pageFragment;
    }

    @Override
    public int getCount() {
        //  Устанешь листать.
        return Integer.MAX_VALUE;
    }
}   ////    TODO ПЕРЕПИСАТЬ
