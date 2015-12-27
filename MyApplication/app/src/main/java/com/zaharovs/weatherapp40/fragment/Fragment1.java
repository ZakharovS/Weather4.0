package com.zaharovs.weatherapp40.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zaharovs.weatherapp40.R;
import com.zaharovs.weatherapp40.ClickInterface;
import com.zaharovs.weatherapp40.Helper.MyAdapter;
import com.zaharovs.weatherapp40.Helper.RealmOneForecast;
import com.zaharovs.weatherapp40.activity.MainActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment1 extends Fragment {

    private ClickInterface myInterface;
    private Realm realm;
    private ListView listView;
    private MyAdapter adapter;

    public Fragment1() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            myInterface = (ClickInterface) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString() + " Must implement CallbackInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getInstance(getContext());
        RealmResults<RealmOneForecast> results = realm.where(RealmOneForecast.class).findAll();

        if (!MainActivity.isConnected) {
            if (results.size() == 0) {
                Snackbar.make(view, R.string.no_internet_no_db, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
            }
        }

       listView = (ListView) view.findViewById(R.id.listview);
       adapter = new MyAdapter(getContext(), results, true);
       listView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myInterface.clickItem(position);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }
}