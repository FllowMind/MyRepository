package com.example.administrator.kok_music_player.Fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.kok_music_player.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String rootpath = Environment.getExternalStorageDirectory().getPath();

    private OnFragmentInteractionListener mListener;
    private ListView file_listview;
    private TextView directory_view;
    private LruCache<String,Bitmap> lruCaches;//用来缓存图片
    private MyAdapter myAdapter;
    private File root ;
    private List<String> directories;
    private List<String> names ;


    public FileListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileListFragment newInstance(String param1, String param2) {
        FileListFragment fragment = new FileListFragment();
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
        lruCaches = new LruCache<>(2);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.directory_icon);
        lruCaches.put("logo", bitmap);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        file_listview = (ListView) view.findViewById(R.id.file_list);
        directory_view = (TextView) view.findViewById(R.id.current_dir);

        //回退dir
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.file_dir);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direcotryBack();

            }
        });

        directory_view.setText(rootpath);
        showFileDir(rootpath);
        return view;
    }

    private void showFileDir(String path) {
        directories = new ArrayList<>();
        names = new ArrayList<>();
        File file = new File(path);

        if (!file.getName().equals("storage")&&file.isDirectory()) {

            File[] files = file.listFiles();
            for (File f : files) {

                if (f.isDirectory()) {
                    names.add(f.getName());
                    directories.add(f.getAbsolutePath());
                }

            }
            if (names.size() >0) {
                myAdapter = new MyAdapter();
                file_listview.setAdapter(myAdapter);
                directory_view.setText(rootpath);
            }else{
                file = file.getParentFile();
                files = file.listFiles();
                for (File f : files) {

                    if (f.isDirectory()) {
                        names.add(f.getName());
                        directories.add(f.getAbsolutePath());
                    }

                }
            }

        }

    }

    public boolean direcotryBack(){
        File file = new File(rootpath).getParentFile();
        if (!file.getName().equals("storage")&&file.isDirectory()) {
            rootpath = file.getPath();
            showFileDir(rootpath);
            return true;
        }
        return false;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame,new ScanFragment());
        transaction.commit();

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
        lruCaches.evictAll();//清空缓存
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MyAdapter extends BaseAdapter {




        @Override
        public int getCount() {
            return directories.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.file_list_item, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                holder.derectory_view = (TextView) convertView.findViewById(R.id.derectory_view);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.file_choose);
                convertView.setTag(holder);
            }else {
                holder =(Holder) convertView.getTag();
            }
            holder.imageView.setImageBitmap(lruCaches.get("logo"));
            holder.derectory_view.setText(names.get(pos));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("listviewssss", "change");
                    boolean hasDirectory = false;
                    rootpath = directories.get(pos);
                            showFileDir(rootpath);
                }
            });
            return convertView;
        }

        class Holder{
            public ImageView imageView;
           public  TextView derectory_view;
           public  CheckBox checkBox;
        }
    }

}
