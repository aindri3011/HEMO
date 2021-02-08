package com.aindri.hemo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    MyViewHolder myViewHolder;
    ArrayList<Rowitem> arrayList = new ArrayList<>();
    Context context;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    TextView editnamefrag,editemailfrag,editphnfrag,cityeditfrag,bloodeditfrag,gendereditfrag,ageeditfrag;
    Button callfrag,messagefrag;
    ImageButton closebutton;


    public CustomAdapter( ArrayList<Rowitem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users,parent,false);
    myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {
    myViewHolder.Personname.setText(arrayList.get(position).getName());
    myViewHolder.Personblood.setText(arrayList.get(position).getBlood());
    myViewHolder.detailbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myViewHolder.key=arrayList.get(position).getKey();

            Showdetails(myViewHolder.key,position);
        }
    });


    }

    private void Showdetails(final String key,final int position){
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference(mAuth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInformationModel information=snapshot.getValue(UserInformationModel.class);
                editnamefrag.setText(information.name);
                editemailfrag.setText(information.email);
                editphnfrag.setText(information.phone);
                cityeditfrag.setText(information.city);
                bloodeditfrag.setText(information.bloodgroup);
                gendereditfrag.setText(information.gender);
                ageeditfrag.setText(information.agegroup);

                displayDialog(position);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void displayDialog(int position){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.fragment_details);
        editnamefrag=dialog.findViewById(R.id.editnamefrag);
        editemailfrag=dialog.findViewById(R.id.editemailfrag);
        editphnfrag=dialog.findViewById(R.id.editphnfrag);
        ageeditfrag=dialog.findViewById(R.id.ageeditfrag);
        cityeditfrag=dialog.findViewById(R.id.cityeditfrag);
        bloodeditfrag=dialog.findViewById(R.id.bloodeditfrag);
        gendereditfrag=dialog.findViewById(R.id.gendereditfrag);
        callfrag=dialog.findViewById(R.id.callfrag);
        messagefrag=dialog.findViewById(R.id.messagefrag);
        closebutton=dialog.findViewById(R.id.closebutton);
        dialog.setCancelable(false);
        dialog.show();
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        callfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callintent=new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:"+editphnfrag.getText().toString()));
                context.startActivity(callintent);


            }
        });

        messagefrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView personpic;
        TextView Personname, Personblood;
        Button detailbutton;
        String key;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personpic = (CircleImageView) itemView.findViewById(R.id.personpic);
            Personname=itemView.findViewById(R.id.Personname);
            Personblood=itemView.findViewById(R.id.Personblood);
            detailbutton=itemView.findViewById(R.id.detailbutton);
        }

    }
}
