package com.courtneyetc.hearts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;

public class WatchfaceActivity extends Activity {

    private static final IntentFilter INTENT_FILTER_TIME;

    static {
        INTENT_FILTER_TIME = new IntentFilter();
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
    }

    private static final int[] HEART_IDS = new int[] {
            R.id.heart0, R.id.heart1,
            R.id.heart2, R.id.heart3,
            R.id.heart4, R.id.heart5,
            R.id.heart6, R.id.heart7,
            R.id.heart8, R.id.heart9,
            R.id.heart10, R.id.heart11
    };

    private LinearLayout mContainer;

    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            populateHearts();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchface);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mContainer = (LinearLayout) stub.findViewById(R.id.container);
            }
        });

        mTimeInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_TIME));
        registerReceiver(mTimeInfoReceiver, INTENT_FILTER_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
    }

    private void populateHearts() {
        if (mContainer == null) return;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        // Render minutes
        for (int i = 0; i < HEART_IDS.length; i++) {
            ImageView ivHeart = (ImageView) mContainer.findViewById(HEART_IDS[i]);

            if (i < hour) {
                // Render hours
                ivHeart.setImageResource(R.drawable.heart_full);
            } else if (i == hour) {
                // Render minute
                if (minute >= 15 && minute < 30) {
                    ivHeart.setImageResource(R.drawable.heart_quarter);
                } else if (minute >= 30 && minute < 45) {
                    ivHeart.setImageResource(R.drawable.heart_half);
                } else if (minute >= 45 && minute <= 59) {
                    ivHeart.setImageResource(R.drawable.heart_three_quarter);
                } else {
                    ivHeart.setImageResource(R.drawable.heart_empty);
                }
            } else {
                // Render the rest
                ivHeart.setImageResource(R.drawable.heart_empty);
            }
        }
    }
}
