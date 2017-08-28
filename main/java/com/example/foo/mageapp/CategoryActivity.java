package com.example.foo.mageapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foo.mageapp.catalog.Category;
import com.example.foo.mageapp.helper.Preference;
import com.example.foo.mageapp.xmlconnect.CategoryConntect;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    protected static final String TAG = "CategoryActivity";
    protected static final String ARG_CATEGORY_ID = "categoryId";
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListAdapter mDrawerListAdapter;
    protected String mCategoryId;
    protected Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mCategoryId = getIntent().getStringExtra(ARG_CATEGORY_ID);
        new CategoryTask().execute(mCategoryId);

        ActionBar bar = getSupportActionBar();
        final CharSequence actionBarTitle = bar.getTitle().toString();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(actionBarTitle);
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(actionBarTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void updateUi() {
        updateDrawerAdapter();
        updateFragment();
    }

    protected void updateDrawerAdapter() {
        List<Category> items = mCategory.getChildren();
        if (items.isEmpty()) {
            return;
        }
        Category home = new Category();
        home.setLabel("Home");
        items.add(0, home);
        mDrawerListAdapter = new DrawerListAdapter(this, R.layout.drawer_list_item, items);
        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // if first itme is clicked move to home
                    NavUtils.navigateUpFromSameTask(CategoryActivity.this);
                    return;
                }
                // swap the category fragment based on category id
                List<Category> cats = mCategory.getChildren();
                if (cats.isEmpty()) {
                    Category savedCategory = Preference.getCategory(CategoryActivity.this);
                    cats = savedCategory.getChildren();
                } else {
                    Preference.saveCategory(CategoryActivity.this, mCategory);
                }
                Category cat = cats.get(position);
                new CategoryTask().execute(cat.getId());
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }

    protected void updateFragment() {
        Fragment frgmnt = CategoryFragment.getNewInstance(mCategory);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, frgmnt)
                .commit();
    }

    private class DrawerListAdapter extends ArrayAdapter<Category> {
        protected int mResId;
        protected List<Category> mItems;
        public DrawerListAdapter(Context context, int resId, List items) {
            super(context, resId, items);
            mResId = resId;
            mItems = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(mResId, parent, false);
            }
            TextView label = (TextView) convertView.findViewById(R.id.item_label);
            label.setText(mItems.get(position).getLabel());
            return convertView;
        }
    }

    private class CategoryTask extends AsyncTask<String, Void, Category> {
        @Override
        protected Category doInBackground(String... params) {
            String cid = params[0];
            Category c = new CategoryConntect(CategoryActivity.this).fetchCategoryInfoById(cid);
            return c;
        }
        @Override
        protected void onPostExecute(Category cat) {
            mCategory = cat;
            updateUi();
        }
    }
}
