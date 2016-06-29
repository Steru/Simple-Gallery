package com.stergiadis.simplegallery;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridRecyclerView extends AppCompatActivity {

    private static final int MAX_NUMBER_OF_GRID_ELEMENTS = 30;
    private static final String[] EXTENSIONS_STRING_TAB = {"jpg", "bmp", "png", "jpeg"};
    private static final int GRID_SPAN_COUNT = 3;

    public static final String PARCELABLE_NAME_IMAGE_LIST = "ImageList";
    public static final String PARCELABLE_NAME_POSITION = "position";

    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;

    private SwipeRefreshLayout          mSwipeRefreshLayout;

    private List<File>     mFileList;
    private ArrayList<Image>    mImageList;
    private ArrayList<Image>    mImageSubList; //MAX_NUMBER_OF_GRID_ELEMENTS images drawn from mImageList


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recycler_view);


        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getApplicationContext(), GRID_SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //todo zabezpieczenia w razie pustego folderu lub braku karty itp
        mFileList = new ArrayList<>();
        getFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                     mFileList);


        //write filenames and paths from List<File> to List<Image>
        mImageList = new ArrayList<>();
        for(File f : mFileList){
            Image img = new Image();
            img.setName(f.getName());
            img.setPath(f.getAbsolutePath());
            mImageList.add(img);
        }

        mImageSubList = new ArrayList<>();
        if(savedInstanceState != null){
            mImageSubList = savedInstanceState.getParcelableArrayList(GridRecyclerView.PARCELABLE_NAME_IMAGE_LIST);
        } else { //only n files showing, user can shuffle those with swipe down (swipeRefresh)
            if (mImageList.size() > MAX_NUMBER_OF_GRID_ELEMENTS) {
                Collections.shuffle(mImageList);
                mImageSubList = new ArrayList<>(mImageList.subList(0, MAX_NUMBER_OF_GRID_ELEMENTS));
            }
        }

        mAdapter = new DataAdapter(mImageSubList, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        setGridListeners();

        setSwipeRefresh();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelableArrayList(GridRecyclerView.PARCELABLE_NAME_IMAGE_LIST, mImageSubList);

        super.onSaveInstanceState(savedInstanceState);
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
                    Toast.makeText(getApplicationContext(), mFileList.get(position).getName(), Toast.LENGTH_SHORT).show();

//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("fileList", mImageList);

                    if (mImageList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "mImageListIsEmpty", Toast.LENGTH_LONG);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), FullscreenImageActivity.class);
                        intent.putParcelableArrayListExtra(PARCELABLE_NAME_IMAGE_LIST, mImageSubList);
                        intent.putExtra(PARCELABLE_NAME_POSITION, position);
                        startActivity(intent);
                    }
                    //getArguments().getSerializable("fileList");

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
                mImageSubList = new ArrayList<>(mImageList.subList(0,MAX_NUMBER_OF_GRID_ELEMENTS));
//                mAdapter.notifyDataSetChanged();
                mAdapter = new DataAdapter(mImageSubList, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });

    }



    // Search the dirName directory and subdirectories for image files
    private void getFiles(String dirName, List<File> fileList){

        File dir = new File(dirName);


        File[] fileTab = dir.listFiles();

        for(File file : fileTab) {
            //check if it's a directory or a file, and check if the file has the right extension
            if(file.isFile() && fileIsAnImage(file.getName())) {
                fileList.add(file);
            } else if(file.isDirectory()){
                getFiles(file.getAbsolutePath(), fileList);
            }
        }

//


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



//        if (mFileDir.isDirectory()) {
//            mFileList = mFileDir.listFiles();
//
//            mFileNameString = new String[mFileTab.length];
//
//            String tmp;
//            for (int i = 0; i < mFileTab.length; i++) {
//                mFileNameString[i] = mFileTab[i].getName();
//
//            }
//
//            tmp = mFileTab.length + "\n" + mFileNameString[1];
//
//            mTempTextView.setText(tmp);
//
//        } else {
//            mTempTextView.setText("mFile is not a directory!" + mFileDir);
//        }
