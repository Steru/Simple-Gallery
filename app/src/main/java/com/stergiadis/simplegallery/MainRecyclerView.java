package com.stergiadis.simplegallery;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stergiadis.simplegallery.adapters.GridDataAdapter;
import com.stergiadis.simplegallery.adapters.LinearDataAdapter;
import com.stergiadis.simplegallery.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainRecyclerView extends AppCompatActivity {

    private static final int        MAX_NUMBER_OF_GRID_ELEMENTS = 30;
    private static final String[]   EXTENSIONS_STRING_TAB = {"jpg", "bmp", "png", "jpeg"};
    private static final int        GRID_SPAN_COUNT = 3;


    public static final String PARCELABLE_NAME_IMAGE_LIST = "ImageList";
    public static final String PARCELABLE_NAME_POSITION = "position";
    public static final String SAVED_INSTANCE_STATE_NAME_GRIDLIST_VIEW_TRIGGER = "grid_list_boolean";

    private int mMaxImageSubListItems;

    private int mMaxImageSubListItems;

    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;

    private SwipeRefreshLayout          mSwipeRefreshLayout;

    private boolean mListViewToggled;

    private List<File>          mFileList;
    private ArrayList<Image>    mImageList;
    private ArrayList<Image>    mImageSubList; //MAX_NUMBER_OF_GRID_ELEMENTS images drawn from mImageList

    private TextView mErrortxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recycler_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mFileList = new ArrayList<>();
        mErrortxt = (TextView) findViewById(R.id.error_textview);

        boolean cardIsPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(cardIsPresent == false) {
            mErrortxt.setVisibility(View.VISIBLE);
            mErrortxt.setText(R.string.error_msg_no_sdcard_found);

        } else {
            getFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                    mFileList);

            if (mFileList.isEmpty()) {
                mErrortxt.setVisibility(View.VISIBLE);
                mErrortxt.setText(R.string.error_msg_no_files_found);
            } else {

                //write filenames and paths from List<File> to List<Image>
                mImageList = new ArrayList<>();
                for (File f : mFileList) {
                    Image img = new Image();
                    img.setName(f.getName());
                    img.setPath(f.getAbsolutePath());
                    mImageList.add(img);
                }

                if (mImageList.size() > MAX_NUMBER_OF_GRID_ELEMENTS) {
                    mMaxImageSubListItems = MAX_NUMBER_OF_GRID_ELEMENTS;
                } else{
                    mMaxImageSubListItems = mImageList.size();
                }

                mImageSubList = new ArrayList<>();
                if (savedInstanceState != null) {
                    mImageSubList = savedInstanceState.getParcelableArrayList(MainRecyclerView.PARCELABLE_NAME_IMAGE_LIST);
                    mListViewToggled = savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_NAME_GRIDLIST_VIEW_TRIGGER);
                } else {
                    //only n files showing, user can shuffle those with swipe down (swipeRefresh)
                    Collections.shuffle(mImageList);
                    mImageSubList = new ArrayList<>(mImageList.subList(0, mMaxImageSubListItems));
                }

                if(mListViewToggled){
                    mAdapter = new LinearDataAdapter(mImageSubList, getApplicationContext());
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                } else {
                    mAdapter = new GridDataAdapter(mImageSubList, getApplicationContext());
                    mLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_COUNT);
                }

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                setGridListeners();

                setSwipeRefresh();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList(PARCELABLE_NAME_IMAGE_LIST, mImageSubList);
        savedInstanceState.putBoolean(SAVED_INSTANCE_STATE_NAME_GRIDLIST_VIEW_TRIGGER, mListViewToggled);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_toggle_view) {
            //if it was a list, make it a grid. And other way around.
            if(mListViewToggled){
                mListViewToggled = false;
                mAdapter = new GridDataAdapter(mImageSubList, getApplicationContext());
                mLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_COUNT);
            } else {
                mListViewToggled = true;
                mAdapter = new LinearDataAdapter(mImageSubList, getApplicationContext());
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
            }

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setGridListeners(){
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    if (mImageList.isEmpty()){
                        Toast.makeText(getApplicationContext(), R.string.error_msg_no_files_found , Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), FullscreenImageActivity.class);
                        intent.putParcelableArrayListExtra(PARCELABLE_NAME_IMAGE_LIST, mImageSubList);
                        intent.putExtra(PARCELABLE_NAME_POSITION, position);
                        startActivity(intent);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
    }

    private void setSwipeRefresh(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Collections.shuffle(mImageList);

                mImageSubList = new ArrayList<>(mImageList.subList(0,mMaxImageSubListItems));
                if(mListViewToggled){
                    mAdapter = new LinearDataAdapter(mImageSubList, getApplicationContext());
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                } else {
                    mAdapter = new GridDataAdapter(mImageSubList, getApplicationContext());
                    mLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_COUNT);
                }
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);


                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    // Search the dirName directory and subdirectories for image files
    private void getFiles(String dirName, List<File> fileList){
        File dir = new File(dirName);

        File[] fileTab = dir.listFiles();

        if(fileTab != null) {
            for (File file : fileTab) {
                //check if it's a directory or a file, and check if the file has the right extension
                if (file.isFile() && fileIsAnImage(file.getName())) {
                    fileList.add(file);
                } else if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), fileList);
                }
            }
        }

    }

    private boolean fileIsAnImage(String filename) {
        for (String extString : EXTENSIONS_STRING_TAB){
            if (filename.endsWith("." + extString)) {
                return true;
            }
        }
        return false;
    }
}

