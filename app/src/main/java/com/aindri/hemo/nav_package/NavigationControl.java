package com.aindri.hemo.nav_package;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.SidePropagation;

import com.aindri.hemo.EditProfileActivity;
import com.aindri.hemo.FeedbackActivity;
import com.aindri.hemo.HomeActivity;
import com.aindri.hemo.MyProfileActivity;
import com.aindri.hemo.R;
import com.aindri.hemo.SearchDonorActivity;

import java.util.ArrayList;

public class NavigationControl {
    Context con;
    Activity a;

    public NavigationControl(Context con, Activity a) {
        this.con = con;
        this.a = a;
    }
    public void openActivity(String event){
        if(event.equalsIgnoreCase("Home")){
            if(!(con instanceof HomeActivity)){
                Intent intent=new Intent(con,HomeActivity.class);
                con.startActivity(intent);
            }

        }

       if(event.equalsIgnoreCase("My Profile")){
            if(!(con instanceof MyProfileActivity)){
                Intent intent=new Intent(con,MyProfileActivity.class);
                con.startActivity(intent);
                if(!(con instanceof MyProfileActivity))
                    a.finish();

            }
        }
       if(event.equalsIgnoreCase("Edit Profile")){
            if(!(con instanceof EditProfileActivity )){
                Intent intent=new Intent(con,EditProfileActivity.class);
                con.startActivity(intent);
                if(!(con instanceof EditProfileActivity))
                    a.finish();

            }
        }

        if(event.equalsIgnoreCase("Search Donors")){
            if(!(con instanceof SearchDonorActivity)){
                Intent intent=new Intent(con,SearchDonorActivity.class);
                con.startActivity(intent);
                if(!(con instanceof  MyProfileActivity))
                    a.finish();
            }
        }

        if(event.equalsIgnoreCase("Feedback")){
            if(!(con instanceof FeedbackActivity)){
                Intent intent=new Intent(con,FeedbackActivity.class);
                con.startActivity(intent);
                if(!(con instanceof EditProfileActivity))
                    a.finish();
            }
        }
    }
       public static ArrayList<Nav_Model>prepareSideMenu(Activity act){
     ArrayList<Nav_Model> al_menu_item;
     Nav_Model side_menu=new Nav_Model();
     al_menu_item=new ArrayList<>();

     side_menu=new Nav_Model();
       side_menu.setActivity_name("Home");
       side_menu.setIcon(R.drawable.home);
       al_menu_item.add(side_menu);

       side_menu=new Nav_Model();
       side_menu.setActivity_name("My Profile");
       side_menu.setIcon(R.drawable.profile);
       al_menu_item.add(side_menu);

       side_menu=new Nav_Model();
       side_menu.setActivity_name("Edit Profile");
       side_menu.setIcon(R.drawable.edit);
       al_menu_item.add(side_menu);

       side_menu=new Nav_Model();
       side_menu.setActivity_name("Search Donors");
       side_menu.setIcon(R.drawable.search);
       al_menu_item.add(side_menu);

       side_menu=new Nav_Model();
       side_menu.setActivity_name("Feedback");
       side_menu.setIcon(R.drawable.feedback);
       al_menu_item.add(side_menu);

       return al_menu_item;


    }



}
