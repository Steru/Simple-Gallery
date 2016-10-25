package com.stergiadis.simplegallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.stergiadis.simplegallery.model.Image;

import java.util.ArrayList;

public class FullscreenImageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private FullscreenAdapter mFullscreenAdapter;
    private ArrayList<Image> mImageList;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_image);
        mImageList = new ArrayList<>();
        mImageList = getIntent()
                .getParcelableArrayListExtra(MainActivity.PARCELABLE_NAME_IMAGE_LIST);
        mCurrentPosition = getIntent().getIntExtra(MainActivity.PARCELABLE_NAME_POSITION, 0);
        mFullscreenAdapter = new FullscreenAdapter(getSupportFragmentManager(), mImageList);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        if (mViewPager != null) {
            mViewPager.setAdapter(mFullscreenAdapter);
            mViewPager.setCurrentItem(mCurrentPosition);
        }
        setTitle(mImageList.get(mCurrentPosition).getName());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                setTitle(mImageList.get(position).getName());
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState
                .putParcelableArrayList(MainActivity.PARCELABLE_NAME_IMAGE_LIST, mImageList);
        savedInstanceState.putInt(MainActivity.PARCELABLE_NAME_POSITION, mCurrentPosition);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageList       = savedInstanceState
                .getParcelableArrayList(MainActivity.PARCELABLE_NAME_IMAGE_LIST);
        mCurrentPosition = savedInstanceState.getInt(MainActivity.PARCELABLE_NAME_POSITION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fullscreen_activity, menu);
        return true;
    }

    public class FullscreenAdapter extends FragmentPagerAdapter {
        private ArrayList<Image> mImageList = new ArrayList<>();
        public FullscreenAdapter(FragmentManager fm, ArrayList<Image> imageList) {
            super(fm);
            this.mImageList = imageList;
        }
        @Override
        public Fragment getItem(int position) {
            return FullscreenImageFragment.newInstance(position,
                    mImageList.get(position).getName(),
                    mImageList.get(position).getPath());
        }
        @Override
        public int getCount() {
            return mImageList.size();
        }
    }
}
