package com.ap.zapper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ap.zapper.MainActivity;
import com.ap.zapper.R;
import com.ap.zapper.adapters.ItemAdapter;
import com.ap.zapper.controllers.ConnectivityController;
import com.ap.zapper.controllers.HttpClientController;
import com.ap.zapper.controllers.PeopleController;
import com.ap.zapper.listeners.PeopleTaskListener;
import com.ap.zapper.models.Person;
import com.ap.zapper.services.requests.PeopleRequest;
import com.ap.zapper.services.tasks.PeopleTask;
import com.ap.zapper.utililites.Constants;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MasterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MasterFragment extends Fragment implements PeopleTaskListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listViewPeople;

    private OnFragmentInteractionListener mListener;

    public static final String TAG = MasterFragment.class.getSimpleName();

    public MasterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MasterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MasterFragment newInstance(String param1, String param2) {
        MasterFragment fragment = new MasterFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_master, container, false);

        listViewPeople = (ListView)rootView.findViewById(R.id.list_view_people);

        try {
            readPeopleFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ItemAdapter itemAdapter = new ItemAdapter(getActivity(), PeopleController.getInstance().getPeople());
//
//        listViewPeople.setClickable(true);
//        listViewPeople.setScrollContainer(true);
//        listViewPeople.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        listViewPeople.setAdapter(itemAdapter);
//
//        listViewPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                final int position = i;
//                System.out.println("Item clicked " + i);
//
//                // update ui
//                getActivity().runOnUiThread(new Thread(new Runnable() {
//                    public void run() {
//
//                        ((MainActivity)getActivity()).updateDetailFragment(position);
//
//                    }
//                }));
//            }
//        });

        return rootView;
    }

    public void setAdapter() {
        ItemAdapter itemAdapter = new ItemAdapter(getActivity(), PeopleController.getInstance().getPeople());

        listViewPeople.setClickable(true);
        listViewPeople.setScrollContainer(true);
        listViewPeople.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listViewPeople.setAdapter(itemAdapter);

        listViewPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int position = i;
                System.out.println("Item clicked " + i);

                // update ui
                getActivity().runOnUiThread(new Thread(new Runnable() {
                    public void run() {

                        ((MainActivity)getActivity()).updateDetailFragment(position);

                    }
                }));
            }
        });
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

    @Override
    public void peopleTaskFinished() {
        try {
            readPeopleFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void readPeopleFile() throws IOException {


        final String cacheDir = "/Android/data/" + getActivity().getPackageName() + "/cache/";

        final String fileDir = Environment.getExternalStorageDirectory().getPath() + cacheDir;

        File file = new File(fileDir + File.separator + "people.txt");

        if(!file.exists())
            return;

        String str = FileUtils.readFileToString(file);

        PeopleController.getInstance().getPeople().clear();

        try {

            JSONArray peopleArray = new JSONArray(str);

            for(int i = 0; i < peopleArray.length(); i++) {

                Person person = new Person();

                JSONObject object = peopleArray.getJSONObject(i);

                if(!object.isNull("id")) {
                    person.setId(object.getInt("id"));
                }

                if(!object.isNull("firstName")) {
                    person.setFirstName(object.getString("firstName"));
                }

                if(!object.isNull("lastName")) {
                    person.setLastName(object.getString("lastName"));
                }

                PeopleController.getInstance().getPeople().add(person);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setAdapter();

    }
}
