package com.example.administrator.kok_music_player.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.customview.titlepopubview.ActionItem;
import com.example.administrator.kok_music_player.customview.titlepopubview.MyMenu;
import com.example.administrator.kok_music_player.services.musicsearchservice.MusicSearchServiceManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView title_tv, path_tv, num_tv;
    private Button scan_button;
    private Recevier recevier;

    private static final String ACTION_UPDATEPATH = "UPDATEPATH";
    private static final String PATH = "PATH";
    private static final String ACTION_UPDATENUM = "UPDATENUM";
    private static final String NUM = "NUM";
    private static final String ACTION_SCAN_OVER = "SCANOVER";
    private boolean isScanning = false;

    private MusicSearchServiceManager mssManager;
    //定义标题栏弹窗按钮
    private MyMenu myMenu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        path_tv = (TextView) view.findViewById(R.id.show_scan_path);
        num_tv = (TextView) view.findViewById(R.id.show_scan_num);
        scan_button = (Button) view.findViewById(R.id.scanmusic_btn);
        scan_button.setOnClickListener(this);

        mssManager = new MusicSearchServiceManager(getActivity());
        recevier = new Recevier();

        return view;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanmusic_btn:
                if (isScanning == false) {
                    startScan();
                } else {
                    stopScan();
                }

                break;
        }
    }

    private void startScan() {
        mssManager.startSearch();
        isScanning = true;
        scan_button.setText("停止扫描");
    }

    private void stopScan() {
        mssManager.stopSearch();
        isScanning = false;
        scan_button.setText("开始扫描");
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATENUM);
        filter.addAction(ACTION_UPDATEPATH);
        filter.addAction(ACTION_SCAN_OVER);
        getActivity().registerReceiver(recevier, filter);
    }

    class Recevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_UPDATEPATH)) {
                String path = intent.getStringExtra(PATH);
                path_tv.setText(path);
            } else if (intent.getAction().equals(ACTION_UPDATENUM)) {
                int num = intent.getIntExtra(NUM, 0);
                num_tv.setText("已扫描到" + num + "首歌曲");
            } else if (intent.getAction().equals(ACTION_SCAN_OVER)) {
                isScanning = false;
                path_tv.setText("");
                num_tv.setText("");
                scan_button.setText("重新扫描");
            }
        }
    }

    @Override
    public void onDestroy() {
        mssManager.unbind();// 关闭文件搜索服务
        super.onDestroy();
    }
}
