package sungshin.ac.kr.smartwindow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sungshin.ac.kr.smartwindow.R;


public class TempuratureFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "TempuratureFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: 필요한 매개변수 있으면 이름 바꾸기
    private String mParam1;
    private String mParam2;

    public TempuratureFragment() {

    }

    public static TempuratureFragment newInstance() {
        TempuratureFragment fragment = new TempuratureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tempurature, container, false);
    }
}
