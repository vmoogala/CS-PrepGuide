package ca.dal.cs.web.cs_prepguide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.android.gms.games.snapshot.Snapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static ca.dal.cs.web.cs_prepguide.R.layout.spinner_dropdown_item;
import static ca.dal.cs.web.cs_prepguide.R.layout.spinner_item;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link filterFragment.filterFragmentInterface} interface
 * to handle interaction events.
 * Use the {@link filterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class filterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String job_Title="jobtitle";
    public static final String job_ID="jobid";
    public static final String v="";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String STREAM="STREAM";
    public static final String COMPANY="COMPANY";
    public static final String TYPE="TYPE";

//    Activity parent = null;

    private Button btnApplyFilter;
    private Spinner spinnerCompany, spinnerType, spinnerStream;
    private ListView listView_jobs;
    //private EditText editText_addJob;

    public DatabaseReference databaseJobs= FirebaseDatabase.getInstance().getReference("jobs");



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean flag=false;
    private filterFragmentInterface mListener;

    public  filterFragment() {
    }

//    @SuppressLint("ValidFragment")
//    public filterFragment(Activity parent) {
//        // Required empty public constructor
//        this.parent = parent;
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment filterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static filterFragment newInstance(String param1, String param2) {
        filterFragment fragment = new filterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private List<job> jobList;
    private List<job> jobList1;
    private List<job> jobListSetSpinner;

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


        final View view = inflater.inflate(R.layout.fragment_filter, container, false);
//        System.out.println(getView());

        System.out.println(getView());


        //listView_jobs= (ListView) parent.findViewById(R.id.list_job);
        jobList =new ArrayList<>();
        jobList1= new ArrayList<>();


        // Inflate the layout for this fragment
        btnApplyFilter =  view.findViewById(R.id.btnApplyFilter);
        spinnerStream= view.findViewById(R.id.spinner_stream);
        spinnerCompany= view.findViewById(R.id.spinner_company);
        spinnerType= view.findViewById(R.id.spinner_type);
        listView_jobs=(ListView)view.findViewById(R.id.ListView_jobs);
        //editText_addJob=(EditText)view.findViewById(R.id.edittext_addJob);
        btnApplyFilter =  view.findViewById(R.id.btnApplyFilter);
        spinnerStream= view.findViewById(R.id.spinner_stream);
        spinnerCompany= view.findViewById(R.id.spinner_company);
        spinnerType= view.findViewById(R.id.spinner_type);

        String typeInitial;
        String companyInitial;





        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt=null;
                //edt= editText_addJob.getText().toString();
                //if(edt.equalsIgnoreCase("")){
                    getJob();
                //}else {
                  //  addJob();
                //}

                //suggestJob();


            }
        });




        listView_jobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                job job1= jobList.get(i);
                mListener.onFilterClicked(job1.getJobId(),job1.getJobTitle());

            }
        });


        databaseJobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                jobList.clear();
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    job job1 = jobSnapshot.getValue(job.class);

                    jobList.add(job1);
                    //streamIntial = streamIntial+","+job1.jobStream;

                }

                jobList adapter = new jobList(getActivity(), jobList);
                listView_jobs.setAdapter(adapter);
                String[] sArry, cArry, tArry;

                sArry=setStreamSpinners();
                final List<String> streamList= new ArrayList<>(Arrays.asList(sArry));
                final ArrayAdapter<String> stringArrayAdapter;
                stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_dropdown_item, streamList);
                stringArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinnerStream.setAdapter(stringArrayAdapter);

                cArry=setCompanySpinners();
                final List<String> companyList= new ArrayList<>(Arrays.asList(cArry));
                final ArrayAdapter<String> stringArrayAdapterC;
                stringArrayAdapterC = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_dropdown_item, companyList);
                stringArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinnerCompany.setAdapter(stringArrayAdapterC);

                tArry=setTypeSpinners();
                final List<String> typeList= new ArrayList<>(Arrays.asList(tArry));
                final ArrayAdapter<String> stringArrayAdapterT;
                stringArrayAdapterT = new ArrayAdapter<String>(getActivity(),
                        R.layout.spinner_dropdown_item, typeList);
                stringArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinnerType.setAdapter(stringArrayAdapterT);





            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


    @Override
    public void onStart(){
        super.onStart();
    }

//    private void addJob(){
//        //String title_job= editText_addJob.getText().toString().trim();
//        String jobStream= spinnerStream.getSelectedItem().toString();
//        String company=spinnerCompany.getSelectedItem().toString();
//        String type=spinnerType.getSelectedItem().toString();
//
//       if(!TextUtils.isEmpty(title_job)){
//           String id= databaseJobs.push().getKey();
//            job jobInstance= new job(id,title_job,jobStream, company, type);
//
//           databaseJobs.child(id).setValue(jobInstance);
//            Toast.makeText(getApplicationContext(), "Job added",
//                    Toast.LENGTH_SHORT).show();
//
//        }else{
//            Toast.makeText(getApplicationContext(), "Enter a job",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }


    private void getJob() {

        String jobStream = spinnerStream.getSelectedItem().toString();
        String company = spinnerCompany.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();
        if(jobStream.equalsIgnoreCase("select stream")){
            jobStream="all";
        }
        if(company.equalsIgnoreCase("Select company")){
            company="all";
        }
        if(type.equalsIgnoreCase("Select position type")){
            type="all";
        }
        jobList1.clear();
       for(int i=0;i<jobList.size()-1;i++){
           job job1=jobList.get(i);
           //int l = job1.jobSkills.length;
           if((jobStream.equalsIgnoreCase("all")&& company.equalsIgnoreCase("all"))&& type.equalsIgnoreCase("all")){
               jobList1.add(job1);
           }else if((!jobStream.equalsIgnoreCase("all")&& company.equalsIgnoreCase("all"))&& type.equalsIgnoreCase("all")){
               if(job1.jobStream.equalsIgnoreCase(jobStream)){
                   jobList1.add(job1);
               }
           }else if((jobStream.equalsIgnoreCase("all")&& !company.equalsIgnoreCase("all"))&& type.equalsIgnoreCase("all")){
               if(job1.jobCompany.equalsIgnoreCase(company)){
                   jobList1.add(job1);
               }
           }else if((jobStream.equalsIgnoreCase("all")&& company.equalsIgnoreCase("all"))&& !type.equalsIgnoreCase("all")){
               if(job1.jobType.equalsIgnoreCase(type)){
                   jobList1.add(job1);
               }
           }else if((!jobStream.equalsIgnoreCase("all")&& !company.equalsIgnoreCase("all"))&& type.equalsIgnoreCase("all")){
               if(job1.jobStream.equalsIgnoreCase(jobStream)&& job1.jobCompany.equalsIgnoreCase(company)){
                   jobList1.add(job1);
               }
           }else if((jobStream.equalsIgnoreCase("all")&& !company.equalsIgnoreCase("all"))&& !type.equalsIgnoreCase("all")){
               if(job1.jobType.equalsIgnoreCase(type)&& job1.jobCompany.equalsIgnoreCase(company)){
                   jobList1.add(job1);
               }
           }else if((!jobStream.equalsIgnoreCase("all")&& company.equalsIgnoreCase("all"))&& !type.equalsIgnoreCase("all")){
               if(job1.jobStream.equalsIgnoreCase(jobStream)&& job1.jobType.equalsIgnoreCase(type)){
                   jobList1.add(job1);
               }
           }else if((!jobStream.equalsIgnoreCase("all")&& !company.equalsIgnoreCase("all"))&& !type.equalsIgnoreCase("all")){
               if((job1.jobStream.equalsIgnoreCase(jobStream)&& job1.jobCompany.equalsIgnoreCase(company))&& job1.jobType.equalsIgnoreCase(type)){
                   jobList1.add(job1);
               }
           }




       }
        jobList adapter = new jobList(getActivity(), jobList1);
        listView_jobs.setAdapter(adapter);



   }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof filterFragmentInterface) {
            mListener = (filterFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public String[] setStreamSpinners() {

        String streamIntial;
        streamIntial ="Select Stream,All";

        for (int i = 0; i < jobList.size() - 1; i++) {
            job job1 = jobList.get(i);
            if (!streamIntial.contains(job1.jobStream)) {
                streamIntial = streamIntial + "," + job1.jobStream;
            }
        }
        String[] streamArry = null;
        streamArry = streamIntial.split(",");
        return streamArry;
    }

    public String[] setCompanySpinners() {

        String companyIntial;
        companyIntial ="Select Company,All";

        for (int i = 0; i < jobList.size() - 1; i++) {
            job job1 = jobList.get(i);
            if (!companyIntial.contains(job1.jobCompany)) {
                companyIntial = companyIntial + "," + job1.jobCompany;
            }
        }
        String[] Arry = null;
        Arry = companyIntial.split(",");
        return Arry;
    }
    public String[] setTypeSpinners() {

        String typeIntial;
        typeIntial ="Select Position Type,All";

        for (int i = 0; i < jobList.size() - 1; i++) {
            job job1 = jobList.get(i);
            if (!typeIntial.contains(job1.jobType)) {
                typeIntial = typeIntial + "," + job1.jobType;
            }
        }
        String[] Arry = null;
        Arry = typeIntial.split(",");
        return Arry;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void suggestJob() {
        CSPrepGuideSingleTon userSingleTonInstance;
        userSingleTonInstance = CSPrepGuideSingleTon.getInstance(getContext());
        ArrayList<String> userSkill = userSingleTonInstance.getAppUser().getSkills();
        job job1;
        String userSkillStr;
        int[] matchSkills = new int[jobList.size()];
        for (int i = 0; i < jobList.size()-1; i++) {
            job1 = jobList.get(i);
            matchSkills[i] = 0;
            for (int j = 0; j < userSkill.size(); j++) {
                userSkillStr = userSkill.get(j);
                if (job1.jobSkills != null) {
                    if (job1.jobSkills.contains(userSkill.get(j))) {
                        matchSkills[i]++;
                    }
                }
            }
        }

        jobList1.clear();
        String orderedSuggetion = "0";
        String head,tail;
        char c;
        int index, incertIndex = 0; 
        int incertVal = 0;
        for (int i = 1; i < matchSkills.length; i++) {
            incertIndex=-1;
            for (int j = 0; j < orderedSuggetion.length(); j++) {
                index = Integer.parseInt(String.valueOf(orderedSuggetion.charAt(j)));
                incertVal=i;
                if (matchSkills[i] > matchSkills[index] || matchSkills[i]==matchSkills[index]) {
                    incertIndex = j;
                } else if (matchSkills[i] != 0) {
                    incertIndex = j+1;
                }

            }
            if(incertIndex!=-1) {
                orderedSuggetion = orderedSuggetion.substring(0, incertIndex) + incertVal + orderedSuggetion.substring(incertIndex, orderedSuggetion.length());
            }
        }

        for(int i=0;i<orderedSuggetion.length();i++){
            index=Integer.parseInt(String.valueOf(orderedSuggetion.charAt(i)));
            job job2=jobList.get(index);
            job2.setMatchSkillNo("No. of matching skills: "+matchSkills[index]);
            jobList1.add(job2);
        }

        jobList adapter = new jobList(getActivity(), jobList1);
        listView_jobs.setAdapter(adapter);
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
    public interface filterFragmentInterface {
        // TODO: Update argument type and name
        void onFilterClicked(String JobId, String JobTitle);
    }
}
