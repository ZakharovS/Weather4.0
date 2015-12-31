package com.zaharovs.weatherapp40.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zaharovs.weatherapp40.R;
import com.zaharovs.weatherapp40.ClickInterface;
import com.zaharovs.weatherapp40.fragment.Fragment2;
import com.zaharovs.weatherapp40.fragment.Fragment1;
import com.zaharovs.weatherapp40.services.NotificationService;
import com.zaharovs.weatherapp40.services.WeatherUpdateService;

public class MainActivity extends AppCompatActivity implements ClickInterface {

    static final String POSITION_SELECTED = "position";
    private int position = -1;
    public static boolean isConnected;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        isConnected = isOnline();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar myActionBar = getSupportActionBar();
        if (myActionBar != null) {
            myActionBar.setTitle(R.string.app_name);
        }

        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Fragment1 mList = new Fragment1();
            manager.beginTransaction()
                    .add(R.id.list_container, mList)
                    .commit();
            Intent notification = new Intent(this, NotificationService.class);
            this.startService(notification);

            Intent intentUpdate = new Intent(this, WeatherUpdateService.class);
            this.startService(intentUpdate);
        }

        if (findViewById(R.id.details_container) != null) {
            Fragment2 mDetails = new Fragment2();
            if (savedInstanceState != null) {
                position = savedInstanceState.getInt(POSITION_SELECTED);
                if (position != -1) {
                    mDetails.setItemContent(position);
                    manager.beginTransaction()
                            .replace(R.id.details_container, mDetails)
                            .commit();
                }
            }
        } else {
            if (savedInstanceState != null) {
                position = savedInstanceState.getInt(POSITION_SELECTED);
            }
        }

    }

    @Override
    public void clickItem(int position) {

        Fragment2 newFragmentItem = new Fragment2();
        newFragmentItem.setItemContent(position);
        this.position = position;
        if (findViewById(R.id.details_container) != null) {
            manager.beginTransaction()
                    .replace(R.id.details_container, newFragmentItem)
                    .commit();
        } else {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra(POSITION_SELECTED, position);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(POSITION_SELECTED, position);
        super.onSaveInstanceState(savedInstanceState);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

}