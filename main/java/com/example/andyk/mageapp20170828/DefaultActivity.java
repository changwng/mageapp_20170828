package com.example.andyk.mageapp20170828;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DefaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);

        MenuItem item = menu.findItem(R.id.menu_item_purchase_service_toggle);
        boolean isOn = PurchaseService.isServiceAlarmOn(this);
        if (isOn) {
            item.setTitle(R.string.menu_item_purchase_service_stop);
        } else {
            item.setTitle(R.string.menu_item_purchase_service_start);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_purchase_service_toggle:
                boolean isOn = PurchaseService.isServiceAlarmOn(this);
                PurchaseService.setPurchaseService(this, isOn);
                invalidateOptionsMenu();
                return true;
        }
        return true;
    }
}
