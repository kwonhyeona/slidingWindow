package sungshin.ac.kr.smartwindow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sungshin.ac.kr.smartwindow.R;


public class TemperatureFragment extends android.support.v4.app.Fragment {
    private String temp;
    private View v;
    private TextView tv_temp;

    public TemperatureFragment() {

    }

    public static TemperatureFragment newInstance(String temp) {
        TemperatureFragment fragment = new TemperatureFragment();
        Bundle args = new Bundle();
        args.putString("temp", temp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            temp = getArguments().getString("temp");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tempurature, container, false);

        tv_temp = (TextView)v.findViewById(R.id.textview_temperature_dustindex);
        tv_temp.setText(temp);
        return v;
    }
}
