package com.seffyo.kandaapptesting.activities.androidme;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.activities.androidme.data.AndroidImageAssets;
import com.seffyo.kandaapptesting.activities.androidme.fragments.BodyPartFragment;

public class AndroidMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me);

        if(savedInstanceState == null) {
            BodyPartFragment headFragment = new BodyPartFragment();
            headFragment.SetListOfIds(AndroidImageAssets.getHeads());
            headFragment.SetImageId(1);
            BodyPartFragment bodyFragment = new BodyPartFragment();
            bodyFragment.SetListOfIds(AndroidImageAssets.getBodies());
            bodyFragment.SetImageId(1);
            BodyPartFragment legFragment = new BodyPartFragment();
            legFragment.SetListOfIds(AndroidImageAssets.getLegs());
            legFragment.SetImageId(1);

            // Get the correct index to access in the array of head images from the intent
            // Set the default value to 0
            int headIndex = getIntent().getIntExtra("headIndex", 0);
            headFragment.SetImageId(headIndex);

            int bodyIndex = getIntent().getIntExtra("bodyIndex", 0);
            bodyFragment.SetImageId(bodyIndex);

            int legIndex = getIntent().getIntExtra("legIndex", 0);
            bodyFragment.SetImageId(legIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.head_container, headFragment)
                    .add(R.id.body_container, bodyFragment)
                    .add(R.id.leg_container, legFragment)
                    .commit();
        }
    }
}
