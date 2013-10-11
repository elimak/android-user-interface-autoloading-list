package com.elimak.chap10autoloading;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    private static final String FRAGMENT_TAG = "autoloadingListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FragmentManager fm = getFragmentManager();
        Fragment f = fm.findFragmentByTag(FRAGMENT_TAG);

        if(f == null){
            f = new AutoloadingListFragment();
            fm.beginTransaction().add(android.R.id.content, f, FRAGMENT_TAG).commit();
        }
    }
}
