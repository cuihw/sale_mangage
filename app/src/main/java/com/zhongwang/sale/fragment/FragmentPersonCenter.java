package com.zhongwang.sale.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongwang.sale.R;
import com.zhongwang.sale.utils.HwLog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentPersonCenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPersonCenter extends FragmentBase {
    private static final String TAG = "FragmentMonthStatistic";

    public static FragmentPersonCenter newInstance() {
        FragmentPersonCenter fragment = new FragmentPersonCenter();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HwLog.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        bindButterKnife(view);
        return view;
    }
}
