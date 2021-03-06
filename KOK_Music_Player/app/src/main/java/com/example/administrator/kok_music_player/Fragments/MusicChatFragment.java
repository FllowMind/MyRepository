package com.example.administrator.kok_music_player.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.kok_music_player.R;
import com.example.administrator.kok_music_player.Utils.GsonUtil;
import com.example.administrator.kok_music_player.Utils.MessageUtil;
import com.example.administrator.kok_music_player.Utils.fieldsutils.AlbumInfoFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.ArtistInfoFields;
import com.example.administrator.kok_music_player.Utils.fieldsutils.MusicInfoFields;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicChatFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ArrayList<Map<String, String>> musicinfos;
    private ProgressDialog progressDialog;


    public MusicChatFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicChatFragment newInstance(String param1, String param2) {
        MusicChatFragment fragment = new MusicChatFragment();
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
        getMusicInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_chat, container, false);
        listView = (ListView) view.findViewById(R.id.music_chart_list);
        return view;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageUtil.FETCH_DATA_SUCCESS:
                    listView.setAdapter(new MyAdapter());
                    progressDialog.dismiss();
                    break;
                case MessageUtil.FETCH_DATA_FAILD:
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    private void getMusicInfo() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage("正在获取数据...");
        MyConnection connection = new MyConnection();
        connection.execute();
    }

    @Override
    public void onClick(View v) {

    }

    private class MyConnection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Log.i("test", "connnection1");
                URL r = new URL(getString(R.string.server_url) + "/musicinfoservlet");
                HttpURLConnection connection = (HttpURLConnection) r.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(3000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();
                String param = "name=123";
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();
//            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");


                byte[] data = new byte[1024];
                InputStream inputStream = connection.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                Map<String, String> userdata = ( Map<String, String>)objectInputStream.readObject();
                String string = (String) objectInputStream.readObject();
                musicinfos = GsonUtil.toObject(string, ArrayList.class);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                int   i=-1;
//                while((i=inputStream.read())!=-1){
//                    baos.write(i);
//                }
                if (musicinfos.size() > 0) {
                    Message message = handler.obtainMessage();
                    message.what = MessageUtil.FETCH_DATA_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    Message message = handler.obtainMessage();
                    message.what = MessageUtil.FETCH_DATA_FAILD;
                    handler.sendMessage(message);
                }

                Log.i("test", musicinfos.get(0).get("music_title"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
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


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return musicinfos.size();
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

            String musictitle = musicinfos.get(position).get(MusicInfoFields.MUSIC_TITLE);
            String artist = musicinfos.get(position).get(ArtistInfoFields.ARTIST_NAME);
            holder.music_title.setText(artist + " - " + musictitle);

            return convertView;
        }

        class ViewHolder {
            TextView music_title;
            TextView music_info;
        }
    }
}
