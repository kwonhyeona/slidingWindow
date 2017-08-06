package sungshin.ac.kr.smartwindow.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import sungshin.ac.kr.smartwindow.weather.Dust;
import sungshin.ac.kr.smartwindow.weather.Weather;

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
                return new DustFragment().newInstance(Dust.getInstance().getGrade(), Dust.getInstance().getValue());
            case 1:
                return new TemperatureFragment().newInstance(Weather.getInstance().getTemperature());
            case 2:
                return new HumidityFragment().newInstance(Weather.getInstance().getHumidity());
            case 3:
                return new RainFragment().newInstance(Weather.getInstance().getPrecipitation_type(), Weather.getInstance().getPrecipitation_sinceOntime());
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
