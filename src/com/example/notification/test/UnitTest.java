package com.example.notification.test;

import com.example.notification.MainActivity;
import com.example.notification.Message1;
import com.example.notification.R;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class UnitTest extends ActivityUnitTestCase<MainActivity> {
	private Intent mLaunchIntent;
	private MainActivity mActivity;
	private ListView list;

	public UnitTest() {
		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class);

		// startActivity(mLaunchIntent, null, null);
		// final Button refresh = (Button)mActivity.findViewById(R.id.refresh);

	}

	public void testPreconditions() {
		startActivity(mLaunchIntent, null, null);
		final Button refreshButton = (Button) getActivity().findViewById(
				R.id.refresh);
		assertNotNull("mLaunchActivity is null", getActivity());
		assertNotNull("mLaunchNextButton is null", refreshButton);
	}

	
	public void testNextActivityWasLaunchedWithIntent() {
		startActivity(mLaunchIntent, null, null); // intent bundle object
		final Button refresh = (Button) getActivity().findViewById(R.id.refresh);
	    refresh.performClick();
		list = getActivity().getListView();
		int count = list.getChildCount();
		View list_item1 = list.getChildAt(0);
		// list_item1.performClick();
		final Intent newIntent = getStartedActivityIntent();
		assertNotNull("New Intent is  null", newIntent);
		assertTrue(isFinishCalled());

		final String payload = newIntent
				.getStringExtra(Message1.EXTRAS_FOR_UNITTEST_KEY);
		assertEquals("Payload is empty", MainActivity.EXTRAS_FOR_UNITTEST,
				payload);
	}
}
