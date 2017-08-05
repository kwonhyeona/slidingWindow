package sungshin.ac.kr.smartwindow.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by apple on 2017. 8. 5..
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "PageAdapter";

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new DustFragment().newInstance();
            case 1:
                return new TempuratureFragment().newInstance();
            case 2:
                return new HumidityFragment().newInstance();
            case 3:
                return new RainFragment().newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
