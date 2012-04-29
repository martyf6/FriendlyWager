package com.friendlywagerapp;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewWager extends Activity {
/** Called when the activity is first created. */
	
	private static String TAG = "NewWager";
	
	private final int DATE_DIALOG_ID = 0;
	private final int TIME_DIALOG_ID = 1;
	
	private TextView dateDisplay;
	private TextView timeDisplay;
	private int year, month, day, hour, minute;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.new_wager);
	    // capture our View elements
        dateDisplay = (TextView) findViewById(R.id.newWagerDateText);
        dateDisplay.setText("no date selected");
        timeDisplay = (TextView) findViewById(R.id.newWagerTimeText);
        timeDisplay.setText("no time selected");
        
        // add a click listener to the button
        dateDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        timeDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
	}
	
	private static String convertInt(int num){
		return (num < 10) ? "0" + num : "" + num;
	}
	
	// the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	dateDisplay.setText(convertInt(year) + "-" + convertInt(monthOfYear) + "-" + convertInt(dayOfMonth));
                }
            };
            
    // the callback received when the user "sets" the date in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
    			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    				timeDisplay.setText(convertInt(hourOfDay) + ":" + convertInt(minute) + ":00");	
				}
            };
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this,mDateSetListener, year, month, day);
	    case TIME_DIALOG_ID:
	    	return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);
	    }
	    return null;
	}
	
	public void onDateSelectClicked(View v){
		showDialog(DATE_DIALOG_ID);
	}
	
	public void onTimeSelectClicked(View v){
		showDialog(TIME_DIALOG_ID);
	}
	
	public void onFinishClicked(View v) {
		final EditText eventNameField = (EditText) findViewById(R.id.newWagerNameText);
	    String eventName = eventNameField.getText().toString();
	    final EditText locationField = (EditText) findViewById(R.id.newWagerLocationText);
	    String location = locationField.getText().toString();
	    final TextView dateField = (TextView) findViewById(R.id.newWagerDateText);
	    String date = dateField.getText().toString();
	    final TextView timeField = (TextView) findViewById(R.id.newWagerTimeText);
	    String time = timeField.getText().toString();
	    try {
			JSONObject result = FriendlyWagerServer.createWager(eventName, location, date + " " + time);
			String success = result.getString("success");
			if (success.equals("false")){
				String error = result.getString("error");
				Toast.makeText(NewWager.this, error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(NewWager.this, "successful", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(NewWager.this, "Error in http connection " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	    finish();
	}
    
    public void onCancelClicked(View v) {
    	finish();
    }
}
