package net.frontlinesms.android.model;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class AnalyticsService {

	private static GoogleAnalyticsTracker tracker;
	
	public static void startAnalyticsManager(Context cxt) {
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-22492507-1", cxt);
	}

	public static void trackPageView(String page, Context cxt) {
		tracker.trackPageView(page);
	}

	public static void dispatchAnalytics() {
		tracker.dispatch();
	}

}
