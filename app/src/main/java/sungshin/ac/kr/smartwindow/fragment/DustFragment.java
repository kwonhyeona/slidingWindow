package sungshin.ac.kr.smartwindow.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sungshin.ac.kr.smartwindow.R;

public class DustFragment extends android.support.v4.app.Fragment {
    private String dust_grade;
    private String dust_value;
    private View v;
    private TextView tv_dust_grade, tv_dust_value;

    public DustFragment() {

    }

    public static DustFragment newInstance(String dust_grade, String dust_value) {
        DustFragment fragment = new DustFragment();
        Bundle args = new Bundle();
        args.putString("dust_grade", dust_grade);
        args.putString("dust_value", dust_value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dust_grade = getArguments().getString("dust_grade");
        dust_value = getArguments().getString("dust_value");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_dust, container, false);

        tv_dust_grade = (TextView)v.findViewById(R.id.textview_dust_grade);
        tv_dust_value = (TextView)v.findViewById(R.id.textview_dust_value);


        tv_dust_grade.setText(dust_grade);
        tv_dust_value.setText(dust_value);

        return v;
    }
}
