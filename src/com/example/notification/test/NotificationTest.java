
package com.example.notification.test;


import java.util.Random;

import com.example.notification.MainActivity;
import com.example.notification.Message1;
import com.example.notification.R;
import com.example.notification.gcm.GcmIntentService;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class NotificationTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private MainActivity mActivity;
	String originalText = "The status is off"+"   "+"   Detail: app2.nj";
	Random r;
	public NotificationTest() { // cannot put any parameter here.
		super("com.example.notification",MainActivity.class);
		// TODO Auto-generated constructor stub
	}


	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		mActivity = getActivity();	
		r = new Random();
	}
	/*	@MediumTest
	public void testPreconditions(){
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, nCenter);
		assertNotNull("Notification Activity is null", mActivity);
		assertNotNull("Text in Main Page is null",nCenter.getText());
	}
*/
	@MediumTest
	public void testClickRefreshGetListview(){
		final View decorView = mActivity.getWindow().getDecorView();
		final ListView list  =mActivity.getListView();
		final Button refresh  =(Button)mActivity.findViewById(R.id.refresh);
		ViewAsserts.assertOnScreen(decorView, refresh); //I need to click refresh, and then get the listview
		TouchUtils.clickView(this, refresh);
		View v = list.getChildAt(0);
		assertNotNull("List item 1 is null", v);		
}
	@MediumTest
	public void testOpenNotificationFromMainPage(){
		final View decorView = mActivity.getWindow().getDecorView();
		final Button refresh  =(Button)mActivity.findViewById(R.id.refresh);
		ViewAsserts.assertOnScreen(decorView, refresh);
		TouchUtils.clickView(this, refresh);
		
		final ListView list  =mActivity.getListView();
		int randomChild = r.nextInt(list.getChildCount());
		// ActivityMonitor only monitors Activity. 
		ActivityMonitor message1Monitor = getInstrumentation().addMonitor(Message1.class.getName(), null, false);
		View list_item1 = list.getChildAt(randomChild);
		TouchUtils.clickView(this, list_item1);
		Message1 message1 = (Message1)message1Monitor.waitForActivityWithTimeout(5000);
		
		assertNotNull("Message1 Activity is null", message1);
		assertEquals("Monitor for Message1 Activity has not been called", 1, message1Monitor.getHits());
		assertEquals("Activity is of wrong type", Message1.class, message1.getClass());
		getInstrumentation().removeMonitor(message1Monitor);
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	public void testMessge1BodyTextIsCorect(){
		final Button refresh  =(Button)mActivity.findViewById(R.id.refresh);
		TouchUtils.clickView(this, refresh);
		
		final ListView list = mActivity.getListView();
		int randomChild = r.nextInt(list.getChildCount());
		ActivityMonitor message1Monitor = getInstrumentation().addMonitor(Message1.class.getName(), null, false);
		// this should be before the action of clicking button
		View list_item1 = list.getChildAt(randomChild);
		TouchUtils.clickView(this, list_item1);
		Message1 message1 = (Message1)message1Monitor.waitForActivityWithTimeout(5000);
		assertNotNull("Message1 is null", message1);
		TextView bodyText = (TextView)message1.findViewById(R.id.bodytext);
		String textInMessage1 = bodyText.getText().toString();
		assertEquals("Text is not equal to original text",  textInMessage1, originalText);
		getInstrumentation().removeMonitor(message1Monitor);
		this.sendKeys(KeyEvent.KEYCODE_BACK);  /// without CLICK BACK, the next one cannot be carried out. 
	}
	
	public void testButtonConfirmInMessage1(){
		final Button refresh  =(Button)mActivity.findViewById(R.id.refresh);
		TouchUtils.clickView(this, refresh);
		
		final ListView list = mActivity.getListView();
		int randomChild = r.nextInt(list.getChildCount());
		ActivityMonitor message1Monitor = getInstrumentation().addMonitor(Message1.class.getName(), null, false);
		// this should be before the action of clicking button
		View list_item1 = list.getChildAt(randomChild);
		TouchUtils.clickView(this, list_item1);
		Message1 message1 = (Message1)message1Monitor.waitForActivityWithTimeout(5000);
		assertNotNull("Message1 is null", message1);
		getInstrumentation().removeMonitor(message1Monitor);
		final Button confirm = (Button)message1.findViewById(R.id.confirmB);
		// create another monitor to monitor the process of going back to MainActivity
		ActivityMonitor mMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
		TouchUtils.clickView(this, confirm);
		// press twice!  Or it will pause (pressed OK button, does not release)
		this.sendKeys(KeyEvent.KEYCODE_ENTER);
		this.sendKeys(KeyEvent.KEYCODE_ENTER);
		MainActivity mActivity =(MainActivity)mMonitor.waitForActivity();
		assertNotNull("MainActivity is null",mActivity);
		getInstrumentation().removeMonitor(mMonitor);
	}
	public void testButtonIgnoreInMessage1(){
		final Button refresh  =(Button)mActivity.findViewById(R.id.refresh);
		TouchUtils.clickView(this, refresh);
		
		final ListView list = mActivity.getListView();
		int randomChild = r.nextInt(list.getChildCount());
		ActivityMonitor message1Monitor = getInstrumentation().addMonitor(Message1.class.getName(), null, false);
		// this should be before the action of clicking button
		View list_item1 = list.getChildAt(randomChild);
		TouchUtils.clickView(this, list_item1);
		Message1 message1 = (Message1)message1Monitor.waitForActivityWithTimeout(5000);
		assertNotNull("Message1 is null", message1);
		getInstrumentation().removeMonitor(message1Monitor);
		final Button ignore = (Button)message1.findViewById(R.id.ignoreB);
		// create another monitor to monitor the process of going back to MainActivity
		ActivityMonitor mMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
		TouchUtils.clickView(this, ignore);
		// press twice!  Or it will pause (pressed OK button, does not release)
		this.sendKeys(KeyEvent.KEYCODE_ENTER);
		this.sendKeys(KeyEvent.KEYCODE_ENTER);
		MainActivity mActivity =(MainActivity)mMonitor.waitForActivity();
		assertNotNull("MainActivity is null",mActivity);
		getInstrumentation().removeMonitor(mMonitor);
	}
	
	public void testButtonReturnInMessage1(){

		final Button refresh  =(Button)mActivity.findViewById(R.id.refresh);
		TouchUtils.clickView(this, refresh);
		
		final ListView list = mActivity.getListView();
		int randomChild = r.nextInt(list.getChildCount());
		ActivityMonitor message1Monitor = getInstrumentation().addMonitor(Message1.class.getName(), null, false);
		// this should be before the action of clicking button
		View list_item1 = list.getChildAt(randomChild);
		TouchUtils.clickView(this, list_item1);
		Message1 message1 = (Message1)message1Monitor.waitForActivityWithTimeout(5000);
		assertNotNull("Message1 is null", message1);
		getInstrumentation().removeMonitor(message1Monitor);
		final Button back = (Button)message1.findViewById(R.id.back);
		// create another monitor to monitor the process of going back to MainActivity
		ActivityMonitor mMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
		TouchUtils.clickView(this, back);
		MainActivity mActivity =(MainActivity)mMonitor.waitForActivity();
		assertNotNull("MainActivity is null",mActivity);
		getInstrumentation().removeMonitor(mMonitor);
	}
	public void testScrollNotification(){
		Instrumentation in = getInstrumentation();
		MotionEvent event = MotionEvent.obtain(0, 400, MotionEvent.ACTION_DOWN, 50, 5, 0);
		in.sendPointerSync(event);
	}
}  
