package com.restaurant.alpha.alphanavigation;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

public class SelectLocationByNameActivity extends AppCompatActivity {
    private final int maxSearchSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_by_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        final EditText editText = (EditText) findViewById(R.id.name_keyword);

        assert(recyclerView != null);
        assert(editText != null);

        final SearchByNameAdapter searchByNameAdapter = new SearchByNameAdapter(this);
        recyclerView.setAdapter(searchByNameAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String keyword = v.getText().toString();

                    TMapData tMapData = new TMapData();
                    tMapData.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                        @Override
                        public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchByNameAdapter.deleteAllLocation();
                                    if (arrayList.size() != 0) {
                                        int size = maxSearchSize;
                                        if (arrayList.size() < maxSearchSize) size = arrayList.size();
                                        for (int i = 0; i < size; i++) {
                                            searchByNameAdapter.addLocation(arrayList.get(i));
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "No result about " + keyword, Toast.LENGTH_SHORT).show();
                                    }
                                    searchByNameAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.action_send) {

        //}
        return super.onOptionsItemSelected(item);
    }
}
