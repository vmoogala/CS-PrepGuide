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
import java.util.List;

import static android.content.ContentValues.TAG;
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
    public int l=0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String STREAM="STREAM";
    public static final String COMPANY="COMPANY";
    public static final String TYPE="TYPE";

//    Activity parent = null;

    private Button btnApplyFilter;
    private Spinner spinnerCompany, spinnerType, spinnerStream;
    private ListView listView_jobs;
    private EditText editText_addJob;

    public DatabaseReference databaseJobs= FirebaseDatabase.getInstance().getReference("jobs");
//    ListView jobList;

//    //to store skills in arraylist variable
//    ArrayList<String> jobList = new ArrayList<String>();
//
//    //setting up the listview
//    ArrayAdapter<String> jobListAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        final String jobsList[]= { "google", "Apple", "Android"};

        //listView_jobs= (ListView) parent.findViewById(R.id.list_job);
        jobList =new ArrayList<>();
        jobList1= new ArrayList<>();

        // Inflate the layout for this fragment
        btnApplyFilter =  view.findViewById(R.id.btnApplyFilter);
        spinnerStream= view.findViewById(R.id.spinner_stream);
        spinnerCompany= view.findViewById(R.id.spinner_company);
        spinnerType= view.findViewById(R.id.spinner_type);
        listView_jobs=(ListView)view.findViewById(R.id.ListView_jobs);
        editText_addJob=(EditText)view.findViewById(R.id.edittext_addJob);
        btnApplyFilter =  view.findViewById(R.id.btnApplyFilter);
        spinnerStream= view.findViewById(R.id.spinner_stream);
        spinnerCompany= view.findViewById(R.id.spinner_company);
        spinnerType= view.findViewById(R.id.spinner_type);
//        jobList = parent.findViewById(R.id.jobList);


        ArrayAdapter<CharSequence> streamAdapter= ArrayAdapter.createFromResource(getActivity(),
                R.array.stream_array, R.layout.spinner_dropdown_item);
        spinnerStream.setAdapter(streamAdapter);

        ArrayAdapter<CharSequence> companyAdapter= ArrayAdapter.createFromResource(getActivity(),
                R.array.company_array, R.layout.spinner_dropdown_item);
        spinnerCompany.setAdapter(companyAdapter);

        ArrayAdapter<CharSequence> typeAdapter= ArrayAdapter.createFromResource(getActivity(),
                R.array.type_array, R.layout.spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        //ArrayAdapter<CharSequence> jobAdapter= ArrayAdapter.createFromResource(getActivity(),
        //    R.array.jobList, android.R.layout.simple_expandable_list_item_1);
        //listView_jobs.setAdapter(jobAdapter);

//        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onFilterClicked("Hello","World", "from Filter Fragment");
//            }
//        });

//        ArrayAdapter<CharSequence> jobListAdapter= ArrayAdapter.createFromResource(getActivity(),
//                R.array.jobList, R.layout.activity_filter);
//        jobList.setAdapter(jobListAdapter);



        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt=null;
                edt= editText_addJob.getText().toString();
                if(edt.equalsIgnoreCase("")){
                    getJob();
                }else {
                    addJob();
                }


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
                    l++;
                }

                jobList adapter = new jobList(getActivity(), jobList);
                listView_jobs.setAdapter(adapter);

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

    private void addJob(){
        String title_job= editText_addJob.getText().toString().trim();
        String jobStream= spinnerStream.getSelectedItem().toString();
        String company=spinnerCompany.getSelectedItem().toString();
        String type=spinnerType.getSelectedItem().toString();

        if(!TextUtils.isEmpty(title_job)){
            String id= databaseJobs.push().getKey();
             job jobInstance= new job(id,title_job,jobStream, company, type);

             databaseJobs.child(id).setValue(jobInstance);
            Toast.makeText(getApplicationContext(), "Job added",
                    Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "Enter a job",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getJob() {
        Query Qs = null;
        Query Qc = null;
        Query Qt = null;
        String jobStream = spinnerStream.getSelectedItem().toString();
        String company = spinnerCompany.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();
        if(jobStream.equalsIgnoreCase("select stream")){
            jobStream="all";
        }
        if(company.equalsIgnoreCase("company")){
            company="all";
        }
        if(type.equalsIgnoreCase("position type")){
            type="all";
        }
        jobList1.clear();
       for(int i=0;i<jobList.size()-1;i++){
           job job1=jobList.get(i);
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

//        if (jobStream.equalsIgnoreCase("all") || jobStream.equalsIgnoreCase("stream")) {
//            if (company.equalsIgnoreCase("all") || company.equalsIgnoreCase("company")) {
//                if (type.equalsIgnoreCase("all") || type.equalsIgnoreCase("position type")) {
//                    //display all
//                    Q = databaseJobs;
//                } else {
//                    Q = databaseJobs.orderByChild("jobType").equalTo(type);
//                }
//            } else if (type.equalsIgnoreCase("all") || type == null) {
//                Q = databaseJobs.orderByChild("jobCompany").equalTo(company);
//            } else {
//                //Q when stream=all company=value and type=value
//                Snapshot snapshot;
//
//            }
//        } else if (company.equalsIgnoreCase("all") || company == null) {
//            if (type.equalsIgnoreCase("all") || jobStream.equalsIgnoreCase("position type")) {
//                Q = databaseJobs.orderByChild("jobStream").equalTo(jobStream);
//            }
//
//
//        }
//
//        if (jobStream.equalsIgnoreCase("all") || jobStream.equalsIgnoreCase("stream")) {
//            Q = databaseJobs.orderByChild("jobType").startAt(type).endAt(type + "\uf0ff");
//        }

//        if(!jobStream.equalsIgnoreCase("All")){
//            Qt = databaseJobs.orderByChild("jobStream").equalTo(jobStream);
//        }else if(!company.equalsIgnoreCase("all")){
//            Qt = databaseJobs.orderByChild("jobCompany").equalTo(company);
//        }else if((!type.equalsIgnoreCase("all")&& !company.equalsIgnoreCase("all"))&& !jobStream.equalsIgnoreCase("all")){
//
//        }else{
//            Qt = databaseJobs.orderByChild("jobType").equalTo(type);
//        }




//        Qt.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                jobList.clear();
//                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
//                    job job1 = jobSnapshot.getValue(job.class);
//                    if(!type.equalsIgnoreCase("all")&& !company.equalsIgnoreCase("all")){
//                        if(job1.jobType==type && job1.jobCompany==company) {
//                            jobList.add(job1);
//                        }
//                   }
//
//                }
//                jobList adapter = new jobList(getActivity(), jobList);
//                listView_jobs.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //getting Post failed log a message
//                Log.w(TAG, "loadJob:onCanclled", databaseError.toException());
//            }
//        });
//
//
//

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
    public interface filterFragmentInterface {
        // TODO: Update argument type and name
        void onFilterClicked(String JobId, String JobTitle);
    }
}
