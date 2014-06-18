package com.example.notification;

import java.io.IOException;

import com.example.notification.databaseAD.DatabaseAdapter;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

// Monkey & Monkey Runner.
// Monkey is program that runs on emulator or device and generates pseudo-random streams of user events such as clicks, 
//touches, or gestures, as well as a number of system-level events. RANDOM REPEATABLE MANNER --- adb shell directly on the device
//MonekyRunner: I can write a Python program that installs an Android application or test package, runs it, 
//sends keystrokes to it, takes screenshots of its user interface, and stores screenshots on the workstation.
// --- control device and emulator from a workstation by sending specific commands and events from an API
public class MainActivity extends ListActivity{
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	int num;
	private DatabaseAdapter dbAdaper;
	private Cursor mCursor;
	TextView count;
	GoogleCloudMessaging gcm;
	public static final String EXTRAS_FOR_UNITTEST = "unittest";
	Button refresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		dbAdaper = new DatabaseAdapter(MainActivity.this);
		dbAdaper.open();
		regGcmInBackground();
		count = (TextView)findViewById(R.id.showcount);
		refresh=(Button)findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//testing
				int[] window2= new int[2];
				findViewById(R.id.refresh).getLocationOnScreen(window2);
				Log.i("POSITION X", String.valueOf(window2[0]));
				Log.i("POSITION Y", String.valueOf(window2[1]));
				renderListView();
			}});
		renderListView();
	}
	
	
	private void renderListView(){
		mCursor = dbAdaper.showAllNotif();
		startManagingCursor(mCursor);
		String[] from = {DatabaseAdapter.KEY_BODY, DatabaseAdapter.KEY_CREATED};
		int[] to = {R.id.leftpart,R.id.rightpart};
		SimpleCursorAdapter lists = new SimpleCursorAdapter(this,R.layout.list_row,mCursor,from,to);
		setListAdapter(lists);
		num = mCursor.getCount();
		if(num==1){
			count.setText(num+ " Notification");
		}
		else if(num>0){
		count.setText(num+" Notifications");
		}else{
			count.setText(" ");
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int[] window2= new int[2];
		v.getLocationOnScreen(window2);
		Log.i("POSITION X", String.valueOf(window2[0]));
		Log.i("POSITION Y", String.valueOf(window2[1]));
		Cursor c = mCursor;
		c.moveToPosition(position);
		Intent intent = new Intent(MainActivity.this,Message1.class);
		intent.putExtra("EXTRAS_FOR_UNITTEST", EXTRAS_FOR_UNITTEST);
		intent.putExtra(dbAdaper.KEY_ROWID, id);
		intent.putExtra(dbAdaper.KEY_TITLE,
				c.getString(c.getColumnIndexOrThrow(dbAdaper.KEY_TITLE)));
		//should use this. cursor has been moved to the target position. id is automatically increasing. By the String, we can find the column index
		//next, by the index, we can find the information.
		intent.putExtra(dbAdaper.KEY_BODY,
				c.getString(c.getColumnIndexOrThrow(dbAdaper.KEY_BODY)));
		
		startActivityForResult(intent,ACTIVITY_EDIT); // 1 IS THE REQUEST CODE
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		renderListView();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int)event.getX();
		int y = (int)event.getY();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			Log.i("POSITION(list_item)",x+" , "+y );
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		}
		
		return true;
	}
	
	protected void regGcmInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					String regid = gcm
							.register(getString(R.string.gcm_sender_id));
					msg = "Device registered, registration ID=" + regid;
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

			}
		}.execute(null, null, null);
	}
			

		
}
