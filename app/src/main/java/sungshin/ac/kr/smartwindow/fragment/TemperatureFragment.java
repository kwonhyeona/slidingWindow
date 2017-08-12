package sungshin.ac.kr.smartwindow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import sungshin.ac.kr.smartwindow.R;


public class TemperatureFragment extends android.support.v4.app.Fragment {
    private String temp, tmax, tmin;
    private View v;
    private TextView tv_temp;
    private TextView tv_tmax;
    private TextView tv_tmin;

    public TemperatureFragment() {

    }

    public static TemperatureFragment newInstance(String temp, String tmax, String tmin) {
        TemperatureFragment fragment = new TemperatureFragment();
        Bundle args = new Bundle();
        args.putString("temp", temp);
        args.putString("tmax", tmax);
        args.putString("tmin", tmin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temp = getArguments().getString("temp");
        tmax = getArguments().getString("tmax");
        tmin = getArguments().getString("tmin");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_temperature, container, false);

        tv_temp = (TextView) v.findViewById(R.id.textview_temperature_dustindex);
        tv_tmax = (TextView) v.findViewById(R.id.textview_temperature_maximum);
        tv_tmin = (TextView) v.findViewById(R.id.textview_temperature_minimum);
        tv_temp.setText(temp);
        tv_tmax.setText(tmax);
        tv_tmin.setText(tmin);
        return v;
    }
}
