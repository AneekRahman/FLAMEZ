package com.flamez.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class UserProfileFragment extends Fragment {

    //---------------------------------------------------------------------------------------------------

    // Declare all elements
    View tiltedView;
    ImageView profileDpIV, profileCoverIV;

    //---------------------------------------------------------------------------------------------------

    public UserProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //---------------------------------------------------------------------------------------------------

        // Set all elements
        tiltedView = (View) rootview.findViewById(R.id.uprofile_tilted_view);
        profileDpIV = (ImageView) rootview.findViewById(R.id.uprofile_dp);
        profileCoverIV = (ImageView) rootview.findViewById(R.id.uprofile_cover);

        //---------------------------------------------------------------------------------------------------

        // give tilted effect on cover
        tiltedView.setRotation(-4f);
        tiltedView.setScaleX(2f);

        // Glide Load cover
        try {
            Glide.with(getContext())
                    .load(new URL("http://www.bigapplesoftball.com/wp-content/uploads/sites/107/2017/11/WKNwZaExiYJKkX9N9dLm3EhJ.jpeg"))
                    .apply(RequestOptions.centerCropTransform())
                    .into(profileCoverIV);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Glide Load dp
        try {
            Glide.with(getContext())
                    .load(new URL("https://images.pexels.com/photos/324658/pexels-photo-324658.jpeg?auto=compress&cs=tinysrgb&h=350"))
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileDpIV);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //---------------------------------------------------------------------------------------------------

        // RETURNED ROOT
        return rootview;

    }

    //---------------------------------------------------------------------------------------------------


    //---------------------------------------------------------------------------------------------------

}
