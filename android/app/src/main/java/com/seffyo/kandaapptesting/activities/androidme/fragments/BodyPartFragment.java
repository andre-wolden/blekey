package com.seffyo.kandaapptesting.activities.androidme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.activities.androidme.data.AndroidImageAssets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renkar on 18.10.2017.
 */

public class BodyPartFragment extends Fragment {
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String LIST_INDEX = "list_index";

    private List<Integer> mListOfIds;
    private int mImageId;

    public BodyPartFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mListOfIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
            mImageId = savedInstanceState.getInt(LIST_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_body_view, container, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);
        if(mListOfIds != null) {
            imageView.setImageResource(mListOfIds.get(mImageId));

            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(mImageId < mListOfIds.size() - 1){
                        mImageId ++;
                    }
                    else {
                        mImageId = 0;
                    }
                    imageView.setImageResource(mListOfIds.get(mImageId));
                }
            });
        }
        return rootView;
    }

    public void SetImageId(int imageId){
        mImageId = imageId;
    }

    public void SetListOfIds(List<Integer> listOfIds){
        mListOfIds = listOfIds;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(IMAGE_ID_LIST, (ArrayList<Integer>) mListOfIds);
        outState.putInt(LIST_INDEX, mImageId);
    }
}
