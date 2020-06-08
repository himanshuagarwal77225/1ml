package com.medical.product.Ui;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.medical.product.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Family_Document extends Fragment {


    public Fragment_Family_Document() {
        // Required empty public constructor
    }

    View rootrow;
    LinearLayout btnPrescription, btnLab, btnXray, btnOther,btntrement,btnpre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootrow = inflater.inflate(R.layout.fragment_family_document, container, false);

        btnPrescription = (LinearLayout) rootrow.findViewById(R.id.btnPrescription);
        btnLab = (LinearLayout) rootrow.findViewById(R.id.btnLab);
        btnXray = (LinearLayout) rootrow.findViewById(R.id.btnXray);
        btnOther = (LinearLayout) rootrow.findViewById(R.id.btnOther);
        btntrement = (LinearLayout) rootrow.findViewById(R.id.btntrement);
        btnpre = (LinearLayout) rootrow.findViewById(R.id.btnPr);


        btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Family_Mamber_Activity) getActivity()).getMyString();
                if (!(str.equals("") || str.equals(null))) {
                    Intent i = new Intent(getActivity(), UploadFamilyDocument.class);
                    i.putExtra("documenttype", "3".toString());
                    i.putExtra("mamberid", str);
                    getActivity().startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Please create mamber", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btntrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Family_Mamber_Activity) getActivity()).getMyString();
                if (!(str.equals("") || str.equals(null))) {
                    Intent i = new Intent(getActivity(), UploadFamilyDocument.class);
                    i.putExtra("documenttype", "4".toString());
                    i.putExtra("mamberid", str);
                    getActivity().startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Please create mamber", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Family_Mamber_Activity) getActivity()).getMyString();
                if (!(str.equals("") || str.equals(null))) {
                    Intent i = new Intent(getActivity(), UploadFamilyDocument.class);
                    i.putExtra("documenttype", "5".toString());
                    i.putExtra("mamberid", str);
                    getActivity().startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "Please create mamber", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Family_Mamber_Activity) getActivity()).getMyString();

                Intent i = new Intent(getActivity(), UploadFamilyDocument.class);
                i.putExtra("documenttype", "0".toString());
                i.putExtra("mamberid", str);
                getActivity().startActivity(i);

            }
        });


        btnXray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Family_Mamber_Activity) getActivity()).getMyString();

                Intent i = new Intent(getActivity(), UploadFamilyDocument.class);
                i.putExtra("documenttype", "1".toString());
                i.putExtra("mamberid", str);
                getActivity().startActivity(i);

            }
        });


        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((Family_Mamber_Activity) getActivity()).getMyString();

                Intent i = new Intent(getActivity(), UploadFamilyDocument.class);
                i.putExtra("documenttype", "2".toString());
                i.putExtra("mamberid", str);
                getActivity().startActivity(i);

            }
        });


        return rootrow;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
