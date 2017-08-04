package com.example.kupal.testapp5;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Front_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Front_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Front_frag extends Fragment {

    //<---------------------------------Private Data members--------------------------------------------------->

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnButtonClickedListener bListener;
    private OnTask1ClickedListener t1Listener;
    private OnTask2ClickedListener t2Listener;
    Button aboutMe;
    Button task1;
    Button task2;



    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Front_frag() {
        // Required empty public constructor
    }

    public static Front_frag newInstance(String param1, String param2) {
        Front_frag fragment = new Front_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //<--------------------- When a new instance is created this is the main starting point--------------------------->

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try{
            bListener = (OnButtonClickedListener) getContext();
            t1Listener= (OnTask1ClickedListener) getContext();
            t2Listener= (OnTask2ClickedListener) getContext();
        }catch(ClassCastException ex){
            throw new ClassCastException("forgot to implement interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_front_frag, container, false);
        aboutMe = (Button) view.findViewById(R.id.aboutme);
        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bListener.onButtonClicked(savedInstanceState);
            }
        });

        task1 = (Button) view.findViewById(R.id.task1);
        task1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                t1Listener.onTask1ButtonClicked(savedInstanceState);
            }
        });

        task2 = (Button) view.findViewById(R.id.task2);
        task2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                t2Listener.onTask2ButtonClicked(savedInstanceState);
            }
        });

        return view;
    }

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

    //<-----------------------------Interface that allows interaction with activity------------------------------>

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnButtonClickedListener{
        public void onButtonClicked(Bundle savedInstanceState);
    }

    public interface OnTask1ClickedListener{
        public void onTask1ButtonClicked(Bundle savedInstanceState);
    }

    public interface OnTask2ClickedListener{
        public void onTask2ButtonClicked(Bundle savedInstanceState);
    }
}

//<------------------------------------------------END--------------------------------------------------->
