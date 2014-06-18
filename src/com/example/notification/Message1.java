package com.example.notification;

import com.example.notification.databaseAD.DatabaseAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Message1 extends Activity implements OnClickListener{
	private DatabaseAdapter dbAdapter;
	private long mRowId;
	TextView information,from, email;
	Button confirm,ignore,back;
	String body,title;
	Bundle b;
	public static final String EXTRAS_FOR_UNITTEST_KEY="EXTRAS_FOR_UNITTEST";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message1);
		initialize();
	    b=getIntent().getExtras();
		body= b.getString("GCM");
		if(body!=null){   // it is from gcm directly
			information.setText(body);
			from.setText(b.getString("FROM"));
			email.setText(from+"@bi.com");
		}else{  // it has already existed in mainpage
			title = b.getString(dbAdapter.KEY_TITLE);
			body = b.getString(dbAdapter.KEY_BODY);
			mRowId = b.getLong(dbAdapter.KEY_ROWID);
			information.setText(body);	
			from.setText(title);
			email.setText(from+"@bi.com");
		}
		
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();
		View v = findViewById(R.id.bodytext);
		v.getBackground().setAlpha(50);
	}
	
	public void initialize(){
		confirm = (Button)findViewById(R.id.confirmB);
		ignore = (Button)findViewById(R.id.ignoreB);
		back = (Button)findViewById(R.id.back);
		information = (TextView)findViewById(R.id.bodytext);
		from = (TextView)findViewById(R.id.fromwho);
		email =(TextView)findViewById(R.id.email);
		ignore.setOnClickListener(this);
		confirm.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.ignoreB:
			//testing
			int[] window= new int[2];
			findViewById(R.id.ignoreB).getLocationOnScreen(window);
			Log.i("POSITION X", String.valueOf(window[0]));
			Log.i("POSITION Y", String.valueOf(window[1]));
			
			dbAdapter.deleteNotif(mRowId);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("Ignore ");
			builder.setMessage("Task Removed");
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.show();
			break;
		case R.id.confirmB:
			//testing
			int[] window2= new int[2];
			findViewById(R.id.confirmB).getLocationOnScreen(window2);
			Log.i("POSITION X", String.valueOf(window2[0]));
			Log.i("POSITION Y", String.valueOf(window2[1]));
			
			dbAdapter.deleteNotif(mRowId);
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setIcon(android.R.drawable.ic_dialog_info);
			builder2.setTitle("Confirm");
			builder2.setMessage("Task Accepted");
			builder2.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			} );
			builder2.show();
			break; 
		case R.id.back:
			//testing
			int[] window3= new int[2];
			findViewById(R.id.back).getLocationOnScreen(window3);
			Log.i("POSITION X", String.valueOf(window3[0]));
			Log.i("POSITION Y", String.valueOf(window3[1]));
			
			Intent in = new Intent(Message1.this, MainActivity.class);
			if(b.getString(dbAdapter.KEY_TITLE)!=null){
			setResult(RESULT_OK, in);
		}else{
			startActivity(in);
		}
			finish();
		}
	}

}
