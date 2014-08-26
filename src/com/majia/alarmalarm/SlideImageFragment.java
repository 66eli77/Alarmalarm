package com.majia.alarmalarm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class SlideImageFragment extends Fragment{
	//private MySettings mySetting;
	/**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
	
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static SlideImageFragment create(int pageNumber) {
    	SlideImageFragment fragment = new SlideImageFragment();
    	Bundle args = new Bundle();
    	args.putInt(ARG_PAGE, pageNumber);
    	fragment.setArguments(args);
    	return fragment;
    }
    
    public SlideImageFragment(){
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
       // mySetting = new MySettings(getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

    	ImageView img = new ImageView(getActivity());
    	
    	switch(mPageNumber){
    	case 0: img.setImageResource(R.drawable.background2);
    			break;	
    	case 1: img.setImageResource(R.drawable.background1);
    			break;
    	case 2: img.setImageResource(R.drawable.background7);
				break;
    	case 3: img.setImageResource(R.drawable.background3);
				break;
    	case 4: img.setImageResource(R.drawable.background4);
				break;
    	case 5: img.setImageResource(R.drawable.background5);
				break;
    	case 6: img.setImageResource(R.drawable.background6);
				break;
    	case 7: img.setImageResource(R.drawable.background0);
				break;	
    	}
    //	Toast.makeText(getActivity(), "page " + mPageNumber, Toast.LENGTH_SHORT).show();
    	//mySetting.savePreferences("PageNumber", mPageNumber);
    	return img;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
