package sungshin.ac.kr.smartwindow.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sungshin.ac.kr.smartwindow.R;

public class HumidityFragment extends android.support.v4.app.Fragment {
    private String humidity;
    private View v;
    private TextView tv_humidity;

    public HumidityFragment() {

    }

    public static HumidityFragment newInstance(String humidity) {
        HumidityFragment fragment = new HumidityFragment();
        Bundle args = new Bundle();
        args.putString("humidity", humidity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        humidity = getArguments().getString("humidity");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_humidity, container, false);

        tv_humidity = (TextView)v.findViewById(R.id.textview_humidity_humidityindex);
        tv_humidity.setText(humidity);
        return v;
    }
}