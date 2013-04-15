package com.example.womenshearthealth.test;

import java.util.Date;
import java.util.List;

import com.example.womenshearthealth.*;
import com.example.womenshearthealth.helpers.*;
import com.example.womenshearthealth.models.*;

import android.test.ActivityInstrumentationTestCase2;

public class DbHelperTest extends ActivityInstrumentationTestCase2<AllMetsPrintedActivity> {

	private AllMetsPrintedActivity mActivity;
	private SQLDatabaseHelper mSqlDatabaseHelper;
	
	public DbHelperTest() {
		this("DbHelperTest");
	}
	
	public DbHelperTest(String name) {
		super("com.example.womenshearthealth", AllMetsPrintedActivity.class);
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mSqlDatabaseHelper = new SQLDatabaseHelper(mActivity);
	}
	
	public final void testPreconitions() {
		assertNotNull(mActivity);
		assertNotNull(mSqlDatabaseHelper);
	}
	
	public final void testAddMetActivity() {
		String metActivityName = "Activity Example 1";
		Double metActivityMets = 5.5;
		int    metActivityMins = 35;
		Date   metActivityDate = new Date();
		
		MetActivity metActivity = new MetActivity(null, metActivityName, metActivityMets, metActivityMins);
		
		mSqlDatabaseHelper.saveMetActivity(metActivity);
		
		List<MetActivity> listActivities = mSqlDatabaseHelper.getAllMetActivities();
		
		System.out.println(metActivity.toString());
		
		boolean foundActivity = false;
		MetActivity addedRecord = null;
		for (MetActivity tempActivity : listActivities) {
			System.out.println(tempActivity.toString());
			
			String tempActivityName = tempActivity.getName();
			Double tempActivityMets = tempActivity.getMetsvalue();
			int    tempActivityMins = tempActivity.getMinutes();
			Date   tempActivityDate = tempActivity.getDateSaved();
			
			if (tempActivityName.equalsIgnoreCase(metActivityName) &&
					tempActivityMets.equals(metActivityMets) &&
					tempActivityMins == metActivityMins /*&&
					tempActivityDate.getYear() == metActivityDate.getYear() &&
					tempActivityDate.getMonth() == metActivityDate.getMonth() &&
					tempActivityDate.getDay() == metActivityDate.getDay() &&
					tempActivityDate.getHours() == metActivityDate.getHours() &&
					tempActivityDate.getMinutes() == metActivityDate.getMinutes()*/){
				
				foundActivity = true;
				addedRecord = tempActivity;
				break;				
			}					
		}
		
		mSqlDatabaseHelper.deleteMetActivity(addedRecord);
		
		//fail(metActivity.getUUID());
		assertTrue("error", foundActivity);
	}

}
