package com.example.womenshearthealth;


import java.util.List;

import com.fima.chartview.ChartView;
import com.fima.chartview.LinearSeries;
import com.fima.chartview.LinearSeries.LinearPoint;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {
		
	private Activity activity;
	private SQLDatabaseHelper dbHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// load in the fragment_home.xml file and display it on screen
		View homeFragment =  inflater.inflate(R.layout.fragment_home, container, false);
		return homeFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		dbHelper = new SQLDatabaseHelper(activity);
		
	}



	@Override
	public void onStart() {
	super.onStart();
	
	LinearLayout graphCard = (LinearLayout) this.getActivity().findViewById(R.id.graph_card);
	LinearLayout targetHRCard = (LinearLayout) this.getActivity().findViewById(R.id.target_hr_card);
	LinearLayout totalsCard = (LinearLayout) this.getActivity().findViewById(R.id.totals_card);
	
	graphCard.setVisibility(8);
	targetHRCard.setVisibility(8);
	totalsCard.setVisibility(8);
	
	View graphCardDropshadow = (View) this.getActivity().findViewById(R.id.graphCardDropshadow);
	View targetHRCardDropshadow = (View) this.getActivity().findViewById(R.id.targerHRCardDropshadow);
	View totalsCardDropshadow = (View) this.getActivity().findViewById(R.id.totalsCardDropshadow);

	graphCardDropshadow.setVisibility(8);
	targetHRCardDropshadow.setVisibility(8);
	totalsCardDropshadow.setVisibility(8);
	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		startAnimationPopOut(R.id.graph_card);
		startAnimationPopOut(R.id.graphCardDropshadow);
		startAnimationPopOut(R.id.target_hr_card);
		startAnimationPopOut(R.id.targerHRCardDropshadow);
		startAnimationPopOut(R.id.totals_card);
		startAnimationPopOut(R.id.totalsCardDropshadow);
		
		populateUI();
	}
	
	private void startAnimationPopOut(int id) {         
	    View myLayout = (View) getActivity().findViewById(id);
	    myLayout.setVisibility(0);
	    Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up_from_right);

	    animation.setAnimationListener(new AnimationListener() {                  
	        @Override
	        public void onAnimationStart(Animation animation) {

	        }

	        @Override
	        public void onAnimationRepeat(Animation animation) {

	        }

	        @Override
	        public void onAnimationEnd(Animation animation) {

	        }
	    });

	    myLayout.clearAnimation();
	    myLayout.startAnimation(animation);

	}


	/**
	 * Loads saved data and fills in the UI elements
	 */
	private void populateUI() {
		//loads saved data, or -1 when no there is no such saved data yet
		//the passed-in parameter to use SettingsHelper should always be 'this'
		int age = SettingsHelper.getAge(this.getActivity());
		
		int bpm50 = CalculationsHelper.getHeartRateFromAge(age, CalculationsHelper.HRTARGET_50);
		int bpm85 = CalculationsHelper.getHeartRateFromAge(age, CalculationsHelper.HRTARGET_85);
		int bpm100 = CalculationsHelper.getHeartRateFromAge(age, CalculationsHelper.HRTARGET_MAX);
		
		double met = 690.0; // TODO Implement mets
		double cals = CalculationsHelper.getCaloriesFromMet(SettingsHelper.getWeight(this.getActivity()),met);
		
		//Grab text boxes from UI
		TextView BPM1 = (TextView)this.getActivity().findViewById(R.id.BPM1);
		TextView BPM2= (TextView)this.getActivity().findViewById(R.id.BPM2);
		TextView BPM3 = (TextView)this.getActivity().findViewById(R.id.BPM3);
		
		TextView METs = (TextView)this.getActivity().findViewById(R.id.METs);
		TextView Calories = (TextView)this.getActivity().findViewById(R.id.Calories);
		
		//Display text boxes
		BPM1.setText(bpm50 + " BPM \t50% \tMHR");
		BPM2.setText(bpm85 + " BPM \t85% \tMHR");
		BPM3.setText(bpm100 + " BPM \t100% \tMHR");
		
		METs.setText(met + " \tMETs");
		Calories.setText(cals + " \tCal");
		
		TextView targetHR = (TextView)this.getActivity().findViewById(R.id.targertHeartRatesTextView);
		TextView weekTotal = (TextView)this.getActivity().findViewById(R.id.weeksTotalTextView);
		TextView metsLogged = (TextView)this.getActivity().findViewById(R.id.metsLoggedTextView);


		Typeface myTypeface = Typeface.createFromAsset(
		                          getActivity().getAssets(),
		                          "font/roboto.ttf");

		targetHR.setTypeface(myTypeface);
		weekTotal.setTypeface(myTypeface);
		metsLogged.setTypeface(myTypeface);

		buildChart();
	}
	
	/**
	 * Builds the graph
	 */
	private void buildGraph() {
		sandbox();
		// init example series data
		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
			new GraphViewData(0, 1.0d),
			new GraphViewData(1, 2.0d),
			new GraphViewData(2, 3.0d),
			new GraphViewData(3, 4.5d),
			new GraphViewData(4, 6.0d),
			new GraphViewData(5, 7.0d),
			new GraphViewData(6, 10.0d)
			});
		
		LineGraphView graphView = new LineGraphView(this.getActivity(), "GraphViewDemo");
		graphView.addSeries(exampleSeries);
		graphView.setDrawBackground(true);
		
			
	}
	
	private void buildChart() {
		ChartView c = (ChartView)activity.findViewById(R.id.chart_view);
		LinearSeries series = new LinearSeries();
		series.setLineColor(0xFF0099CC);
		series.setLineWidth(2);
		
		List<MetActivity> as = dbHelper.getAllMetActivities();
		int count = as.size();
		for (int i = count-1; i >= 0; i--) {
			MetActivity a = as.get(i);
			series.addPoint(new LinearPoint(i, a.getMetMinutes()));
		}
		
		c.addSeries(series);
		
	}
	
	private void sandbox() {
		List<MetActivity> as = dbHelper.getAllMetActivities();
		for (MetActivity a: as) {
			Log.v("Mets", a.toString());
		}
	}
	
}
