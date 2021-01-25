package com.asw.alllistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button empButton;

    private ListView listView;
    private TextView headerText;
    private Toolbar toolbar;

    private PackageManager packageManager;
    private ArrayAdapter<ApplicationInfo> adapter;
    private ArrayList<ApplicationInfo> applicationInfos;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageManager = getPackageManager();
        applicationInfos = new ArrayList<>();

        //find View by id
        listView = (ListView) findViewById(R.id.list_view);

        //show progress dialog
        progressDialog = ProgressDialog.show(this, "Loading All Apps", "Loading application info...");

        //set list view adapter
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.layout_lv_header, listView, false);
        headerText = (TextView) header.findViewById(R.id.text_header);
        listView.addHeaderView(header, null, false);

        //initializing and set adapter for listview
        adapter = new ApplicationAdapter(this, R.layout.item_listview, applicationInfos);
        listView.setAdapter(adapter);

        /*empButton = findViewById(R.id.emp);
        empButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.asw.webadminapplication");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(MainActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener()); // text changed listener
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //filter adapter and update ListView
                adapter.getFilter().filter(s);

                return false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //invoke asynctask
        new GetAllAppsTask(this, applicationInfos, packageManager).execute();
    }


    public void callBackDataFromAsynctask(List<ApplicationInfo> list) {
        applicationInfos.clear();

        for (int i = 0; i < list.size(); i++) {
            applicationInfos.add(list.get(i));
        }

        headerText.setText("All Apps (" + applicationInfos.size() + ")");
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    public void updateUILayout(String content) {
        headerText.setText(content);
    }
}
