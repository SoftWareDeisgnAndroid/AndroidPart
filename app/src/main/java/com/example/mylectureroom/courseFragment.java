package com.example.mylectureroom;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link courseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link courseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class courseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IP_ADDRESS = "155.230.52.54:9900";
    private static final String TAG="phptest";
    private static final String TAG_CLASSCODE = "CLASSCODE";
    private static final String TAG_GRADE = "GRADE";
    private static final String TAG_CLASSNAME ="CLASSNAME";
    private static final String TAG_CLASSBUILDING ="CLASSBUILDING";
    private static final String TAG_CLASSROOM ="CLASSROOM";
    private static final String TAG_CLASSTIME ="CLASSTIME";
    ArrayList<HashMap<String, String>> mArrayList;

    String mJsonString;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public courseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment courseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static courseFragment newInstance(String param1, String param2) {
        courseFragment fragment = new courseFragment();
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

    // Spinner는 조건 검색 각 항목칸, Adapter는 values-arrays의 항목들을 Spinner에 적용시키기 위해
    private ArrayAdapter buildingAdapter;
    private Spinner buildingSpinner;
    private ArrayAdapter dayAdapter;
    private Spinner daySpinner;
    private ArrayAdapter timeAdapter;
    private Spinner timeSpinner;
    // major 관련 추후 삭제
    private ArrayAdapter majorAdapter;
    private Spinner majorSpinner;

    private String courseUniversity = "";
    private String courseBuilding = "";
    private String courseDay= "";
    private String pushDay="";
    private String courseTime = "";

    @Override
    // 실행 되었을 때 적용되는 함수
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        final RadioGroup courseUniversityGroup = (RadioGroup) getView().findViewById(R.id.courseUniversityGroup);
        buildingSpinner = (Spinner) getView().findViewById(R.id.buildingSpinner); // 건물
        daySpinner = (Spinner) getView().findViewById(R.id.daySpinner); // 층
        timeSpinner = (Spinner) getView().findViewById(R.id.timeSpinner); // 시간
        majorSpinner = (Spinner) getView().findViewById(R.id.majorSpinner);

        // RadioGroup Button("학부")을 눌렀을 때 바로 적용됨
        courseUniversityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton courseButton = (RadioButton) getView().findViewById(i);
                courseUniversity = courseButton.getText().toString();

                buildingAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.building, android.R.layout.simple_spinner_dropdown_item);
                buildingSpinner.setAdapter(buildingAdapter);

                dayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.day, android.R.layout.simple_spinner_dropdown_item);
                daySpinner.setAdapter(dayAdapter);


                timeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.time, android.R.layout.simple_spinner_dropdown_item);
                timeSpinner.setAdapter(timeAdapter);
                majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item);
                majorSpinner.setAdapter(majorAdapter);
            }
        });

        Button searchButton = (Button)getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                courseBuilding = buildingSpinner.getSelectedItem().toString();
                courseDay = daySpinner.getSelectedItem().toString();
                courseTime = timeSpinner.getSelectedItem().toString();

                String temp = "";
                if(courseTime.equals("09시")) temp = "1A";
                else if(courseTime.equals("09시30분")) temp="1B";
                else if(courseTime.equals("10시")) temp="2A";
                else if(courseTime.equals("10시30분")) temp="2B";
                else if(courseTime.equals("11시")) temp="3A";
                else if(courseTime.equals("11시30분")) temp="3B";
                else if(courseTime.equals("12시")) temp="4A";
                else if(courseTime.equals("12시30분")) temp="4B";
                else if(courseTime.equals("13시")) temp="5A";
                else if(courseTime.equals("13시30분")) temp="5B";
                else if(courseTime.equals("14시")) temp="6A";
                else if(courseTime.equals("14시30분")) temp="6B";
                else if(courseTime.equals("15시")) temp="7A";
                else if(courseTime.equals("15시30분")) temp="7B";
                else if(courseTime.equals("16시")) temp="8A";
                else if(courseTime.equals("16시30분")) temp="8B";
                else if(courseTime.equals("17시")) temp="9A";
                else if(courseTime.equals("17시30분")) temp="9B";
                else if(courseTime.equals("18시분")) temp="10A";
                else if(courseTime.equals("18시30분")) temp="10B";
                else if(courseTime.equals("19시분")) temp="11A";
                else if(courseTime.equals("19시30분")) temp="11B";
                else if(courseTime.equals("20시")) temp="12A";
                else if(courseTime.equals("20시30분")) temp="12B";

                pushDay = courseDay.substring(0,1);
                pushDay +=temp

                // new BackgroundTask().execute("http://" + IP_ADDRESS + "/login.php", courseBuilding, courseDay, courseTime);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_course2, container, false);
        return layout;
    }

    public void onCheckedChange(RadioGroup radioGroup, int i) {

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /*
    class BackgroundTask extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

                mJsonString = result;

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String classcode = params[1];
            String day = params[2];
            String time = params[3];

            String serverURL = "http://서버IP/query.php";
            String postParameters = "country=" + searchKeyword1 + "&name=" + searchKeyword2;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String classcode = item.getString(TAG_CLASSCODE);
                String grade = item.getString(TAG_GRADE);
                String classname = item.getString(TAG_CLASSNAME);
                String classbuilding = item.getString(TAG_CLASSBUILDING);
                String classroom = item.getString(TAG_CLASSROOM);
                String classtime = item.getString(TAG_CLASSTIME);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_CLASSCODE, classcode);
                hashMap.put(TAG_GRADE, grade);
                hashMap.put(TAG_CLASSNAME, classname);
                hashMap.put(TAG_CLASSBUILDING, classbuilding);
                hashMap.put(TAG_CLASSROOM, classroom);
                hashMap.put(TAG_CLASSTIME, classtime);

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    nextActivity.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_CLASSCODE, TAG_GRADE, TAG_CLASSNAME, TAG_CLASSBUILDING, TAG_CLASSROOM, TAG_CLASSTIME},
                    new int[]{R.id.textView_list_classcode, R.id.textView_list_classname, R.id.textView_list_classname,
                            R.id.textView_list_classbuilding, R.id.textView_list_classroom, R.id.textView_list_classtime}
            );

            mListViewList.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
    */
}
