package com.stergiadis.simplegallery;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GridRecyclerView extends AppCompatActivity {

    private static final int MAX_NUMBER_OF_GRID_ELEMENTS = 30;

    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;

    private String[]    mFileNameString;
    private File        mFileDir;
    private List<File>  mFileList;
    private TextView    mTempTextView;


    String[] testDataSet = {"one", "oni", "bum", "test", "test2", "fsdfb", "sfdg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recycler_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mTempTextView = (TextView) findViewById(R.id.test_textview);

        mFileList = new ArrayList<File>();

        getFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(),
                     mFileList);

        if(mFileList.size() > MAX_NUMBER_OF_GRID_ELEMENTS){
            long seed = System.nanoTime();
            Collections.shuffle(mFileList, new Random(seed));
            mFileList = mFileList.subList(0,MAX_NUMBER_OF_GRID_ELEMENTS);
        }

//        mAdapter = new DataAdapter(testDataSet);
//        mAdapter = new DataAdapter(mFileNameString);
        mAdapter = new DataAdapter(mFileList);
        mRecyclerView.setAdapter(mAdapter);


        setGridListeners();


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



    // Search the dirName directory for files and also search all of the subdirectories
    private void getFiles(String dirName, List<File> fileList){
//        mFileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


        File dir = new File(dirName);
        File[] fileTab = dir.listFiles();

        for(File file : fileTab) {
            if(file.isFile()) {
                fileList.add(file);
            } else if(file.isDirectory()){
                getFiles(file.getAbsolutePath(), fileList);
            }
        }

//
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


    }
}
