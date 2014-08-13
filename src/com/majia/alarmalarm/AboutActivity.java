package com.majia.alarmalarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class AboutActivity extends Activity implements View.OnTouchListener{
	private TextView back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    //opening transition animations
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
	    setContentView(R.layout.activity_about);
	    
	    back = (TextView) findViewById(R.id.back);
	    back.setText("<");
	    
	    back.setOnTouchListener(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	    //closing transition animations
	    overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.equals(back)){
			finish();
		}
		return true;
	}
}
