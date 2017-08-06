package sungshin.ac.kr.smartwindow.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sungshin.ac.kr.smartwindow.R;

public class RainFragment extends android.support.v4.app.Fragment {
    private String type;
    private String sinceOntime;
    private View v;
    private TextView tv_onTime;

    public RainFragment() {

    }

    public static RainFragment newInstance(String type, String sinceOntime) {
        RainFragment fragment = new RainFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("sinceOntime", sinceOntime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            type = getArguments().getString("type");
            sinceOntime = getArguments().getString("sinceOntime");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_rain, container, false);
        tv_onTime = (TextView)v.findViewById(R.id.textview_rain_rainfall);
        tv_onTime.setText(sinceOntime);
        return v;
    }
}