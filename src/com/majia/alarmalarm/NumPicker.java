package com.majia.alarmalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

public class NumPicker extends DialogFragment implements NumberPicker.OnValueChangeListener{

	public NumberPicker np;
	Context context;
	int value = 1;
	ToggleButton earliestToggleButton;
	private MySettings mySetting;
	private boolean toggle_state;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    context = getActivity();
	    	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    LayoutInflater li = (LayoutInflater)     
	        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    // Inflate and set the layout for the dialog
	    View view = li.inflate(R.layout.fragment_number_picker, null);
	    earliestToggleButton = (ToggleButton) view.findViewById(R.id.toggleButton1);
	    SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
	   mySetting = new MySettings(context);
	   toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
	   earliestToggleButton.setOnClickListener(new View.OnClickListener(){
		   @Override
		    public void onClick(View v){
			   if(toggle_state){
				   toggle_state = false; 
			   }else{
				   toggle_state = true;
			   }
			   //mySetting.savePreferences("Toggle_State", toggle_state);
		    }
	   });
	   earliestToggleButton.setChecked(toggle_state);
	    
	    builder
	    // Set view:
	       .setView(view)
	    // Add action buttons
	       .setPositiveButton("Done", new DialogInterface.OnClickListener(){
	            @Override
	            public void onClick(DialogInterface dialog, int id) {
	            	mySetting.savePreferences("Toggle_State", toggle_state);
	            	
	            	EditDialogListener passToActivity = (EditDialogListener)getActivity();
	        		passToActivity.onFinishEditDialog(value + "");	        		
	        		
					dialog.dismiss();
	            }
	         })
	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	            }
	        });   

	   NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPickerInFragment);
	   np.setMaxValue(30);
	   np.setMinValue(1);

	   np.setFocusable(true);
	   np.setFocusableInTouchMode(true);
	   np.setOnValueChangedListener(this);
	   
	   return builder.create();
	}
	
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub
		value = newVal;
	}

}
