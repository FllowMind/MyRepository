package com.example.administrator.kok_music_player.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.fieldsutils.ArtistInfoFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicInfoFields;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicsetListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicsetListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicsetListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static  ArrayList<Map<String,String>> infos;
    private ListView listView;


    public MusicsetListFragment() {

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicsetListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicsetListFragment newInstance(ArrayList<Map<String,String>> info, String param2) {
        infos = info;
        MusicsetListFragment fragment = new MusicsetListFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_musicset_list, container, false);
        listView = (ListView) view.findViewById(R.id.musiclist);
        listView.setAdapter(new MyAdapter());
        return view ;
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
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

            ViewHolder holder = null;
            TextView textView = null;
            if (convertView == null) {

                textView = new TextView(getActivity());
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.musicinfo_item, null);
                holder = new ViewHolder();
                holder.music_title = (TextView) convertView.findViewById(R.id.music_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String musictitle = infos.get(position).get(MusicInfoFields.MUSIC_TITLE);
            String artist = infos.get(position).get(ArtistInfoFields.ARTIST_NAME);
            holder.music_title.setText(artist + " - " + musictitle);

            return convertView;
        }

        class ViewHolder {
            TextView music_title;
            TextView music_info;
        }
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
}
