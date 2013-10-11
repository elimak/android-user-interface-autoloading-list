package com.elimak.chap10autoloading;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by elimak on 10/7/13.
 */
public class AutoloadingListFragment extends ListFragment implements AbsListView.OnScrollListener {

    private final int AUTOLOAD_TRESHOLD = 4;
    private final int MAXIMUM_ITEMS = 52;
    private SimpleAdapter mAdapter;
    private View mFooterView;
    private Handler mHandler;
    private boolean mIsLoading = false;
    private boolean mMoreDataAvailable = true;
    private boolean mWasLoading = false;

    private Runnable mAddItemsRunnable = new Runnable() {
        @Override
        public void run() {
            mAdapter.addMoreItems(10);
            mIsLoading = false;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Context context = getActivity();
        mHandler = new Handler();
        mAdapter = new SimpleAdapter(context, android.R.layout.simple_list_item_1);
        mFooterView = LayoutInflater.from(context).inflate(R.layout.loading_view, null);
        getListView().addFooterView(mFooterView, null, false);
        setListAdapter(mAdapter);
        getListView().setOnScrollListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mWasLoading) {
            mWasLoading = false;
            mIsLoading = true;
            mHandler.postDelayed(mAddItemsRunnable, 1000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mAddItemsRunnable);
        mWasLoading = mIsLoading;
        mIsLoading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(!mIsLoading && mMoreDataAvailable){
            if(totalItemCount >= MAXIMUM_ITEMS){
                mMoreDataAvailable = false;
                getListView().removeFooterView(mFooterView);
            }
            else if(totalItemCount - AUTOLOAD_TRESHOLD <= firstVisibleItem + visibleItemCount ){
                mIsLoading = true;
                mHandler.postDelayed(mAddItemsRunnable, 1000);
            }
        }
    }

    private static class SimpleAdapter extends BaseAdapter {

        private int mCount = 20;
        private final LayoutInflater mLayoutInflater;
        private final String mPositionString;
        private final int mTextViewResourceId;

        SimpleAdapter(Context context, int textViewResourceId) {
            mLayoutInflater = LayoutInflater.from(context);
            mPositionString = context.getString(R.string.position)+" ";
            mTextViewResourceId = textViewResourceId;
        }

        public void addMoreItems(int count){
            mCount += count;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public String getItem(int position) {
            return mPositionString + position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final TextView tv;
            if(convertView == null){
                tv = (TextView) mLayoutInflater.inflate(mTextViewResourceId, null);
            }
            else{
                tv = (TextView) convertView;
            }

            tv.setText(getItem(position));
            return tv;
        }
    }

}
