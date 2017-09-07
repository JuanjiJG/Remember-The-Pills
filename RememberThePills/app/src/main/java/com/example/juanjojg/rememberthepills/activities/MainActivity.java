package com.example.juanjojg.rememberthepills.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.juanjojg.rememberthepills.R;
import com.example.juanjojg.rememberthepills.adapters.PillAdapter;
import com.example.juanjojg.rememberthepills.database.PillOperations;
import com.example.juanjojg.rememberthepills.models.Pill;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String EXTRA_ADD_UPDATE = "add_update_mode";
    private static final String TAG = "MainActivity";
    private static final int SETTINGS_ACTION = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PillOperations mPillOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Creating activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load and set RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.pills_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Get the data from the database
        mPillOperations = new PillOperations(this);
        mPillOperations.open();
        ArrayList<Pill> mPillsDataSet = mPillOperations.getAllPills();
        mPillOperations.close();

        // Create and set the adapter
        mAdapter = new PillAdapter(this, mPillsDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "Creating options menu");
        getMenuInflater().inflate(R.menu.actions_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Item menu selected");
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, SETTINGS_ACTION);
                return true;
            case R.id.add_pill_menu_item:
                Intent i = new Intent(this, AddUpdatePillActivity.class);
                i.putExtra(EXTRA_ADD_UPDATE, "Add");
                startActivity(i);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "Getting activity result");
        Log.i(TAG, "El result code es " + String.valueOf(resultCode));
        if (requestCode == SETTINGS_ACTION) {
            Log.i(TAG, "primer if");
            if (resultCode == SettingsActivity.RESULT_CODE_DARK_THEME) {
                Log.i(TAG, "segundo if");
                finish();
                startActivity(getIntent());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
