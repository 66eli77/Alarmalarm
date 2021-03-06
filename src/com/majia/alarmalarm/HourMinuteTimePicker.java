package com.majia.alarmalarm;

import java.util.Calendar;

import android.app.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

public class HourMinuteTimePicker extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	String timeString;
	public int hour = 0;
	public int minutes = 0;
	private boolean flag;
	private SharedPreferences sharedPreferences;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour1 = c.get(Calendar.HOUR_OF_DAY);
		int minute1 = c.get(Calendar.MINUTE);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int early_hour = sharedPreferences.getInt("early_hour", hour1);
		int early_min = sharedPreferences.getInt("early_min", minute1);
		int must_hour = sharedPreferences.getInt("must_hour", hour1);
		int must_min = sharedPreferences.getInt("must_min", minute1);
		String myTage = getTag();
		if(myTage == "timePicker_early"){
			hour1 = early_hour;
			minute1 = early_min;
		}else if(myTage == "timePicker_must"){
			hour1 = must_hour;
			minute1 = must_min;
		}

		// Create a new instance of TimePickerDialog and return it
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour1, minute1,false);
		tpd.setTitle("set time");
		tpd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which)
		    {
		        if (which == DialogInterface.BUTTON_NEGATIVE)
		        {
		        	flag = false;
		        	dialog.dismiss();
		        }
		    }
		});
		tpd.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which)
		    {
		        if (which == DialogInterface.BUTTON_POSITIVE)
		        {
		        	flag = true;
		        	dialog.dismiss();
		        }
		    }
		});
		return tpd;
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		hour = hourOfDay;
		minutes = minute;
		if(flag){
			if(hourOfDay > 12){
				if(minute == 0){
					timeString = (hourOfDay - 12) + ":" + "00" + " PM ";
				}else{
					timeString = (hourOfDay - 12) + ":" + minute + " PM ";
				}
				EditDialogListener passToActivity = (EditDialogListener)getActivity();
				passToActivity.onFinishEditDialog(timeString);
				this.dismiss();
			}else{
				if(minute == 0){
					timeString = hourOfDay + ":" + "00" + " AM ";
				}else{
					timeString = hourOfDay + ":" + minute + " AM ";
				}
				EditDialogListener passToActivity = (EditDialogListener)getActivity();
				passToActivity.onFinishEditDialog(timeString);
				this.dismiss();
			}
		}
	}

}
