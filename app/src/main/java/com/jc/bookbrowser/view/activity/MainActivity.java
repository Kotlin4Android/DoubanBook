package com.jc.bookbrowser.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.jc.bookbrowser.R;
import com.jc.bookbrowser.common.Constant;
import com.jc.bookbrowser.utils.SharedPrefUtil;
import com.jc.bookbrowser.view.fragment.BaseFragment;
import com.jc.bookbrowser.view.fragment.HomeFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.frame_layout_content)
    FrameLayout frameLayoutContent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    private SwitchCompat themeSwitch;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private BaseFragment currentFragment;
    private FloatingActionButton fab;
    private long lastTime = 0;
    private static final int EXIT_APP_DELAY = 1500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            currentFragment = HomeFragment.getInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_content, currentFragment).commit();
        }
        initNavView();
        initSearchView();
    }

    protected void initSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query)) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);

                } else {
                    Snackbar.make(drawerLayout, R.string.keyword_is_empty, Snackbar.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
            }
        });

        //searchView.setAlpha(0.8f);
        //searchView.setVoiceSearch(true);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] arr = getResources().getStringArray(R.array.default_books);
        searchView.setSuggestions(arr);
    }

    private void initNavView() {
        boolean night = SharedPrefUtil.getrBoolean(Constant.THEME_MODEL, false);
        if (night) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        MenuItem item = navigationView.getMenu().findItem(R.id.nav_theme);
        themeSwitch = (SwitchCompat) MenuItemCompat.getActionView(item).findViewById(R.id.view_switch);
        themeSwitch.setChecked(night);
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPrefUtil.putBoolean(Constant.THEME_MODEL, isChecked);
                themeSwitch.setChecked(isChecked);
                if (isChecked) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            searchView.showSearch(true);
        }

        //noinspection SimplifiableIfStatement

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setToolBar(Toolbar toolBar) {
        if (toolBar != null) {
            this.toolbar = toolBar;
            setSupportActionBar(toolBar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                    drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    public void setFloatingActionBtn(FloatingActionButton fab) {
        this.fab = fab;
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public FloatingActionButton getFloatingActionBar() {
        return this.fab;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_bookshelf) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_theme) {


        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            if (System.currentTimeMillis() - lastTime > EXIT_APP_DELAY) {
                Snackbar.make(drawerLayout, getString(R.string.press_twice_exit), Snackbar.LENGTH_SHORT)
                        .setAction(R.string.exit_directly, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.super.onBackPressed();
                            }
                        }).show();
                lastTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
