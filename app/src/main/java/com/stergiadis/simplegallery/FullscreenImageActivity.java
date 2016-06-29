package com.stergiadis.simplegallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FullscreenImageActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private FullscreenAdapter mFullscreenAdapter;
    private ArrayList<Image> mImageList;
    private int mCurrentPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_image);
        mImageList = new ArrayList<>();

        mImageList = getIntent().getParcelableArrayListExtra(GridRecyclerView.PARCELABLE_NAME_IMAGE_LIST);
        mCurrentPosition = getIntent().getIntExtra(GridRecyclerView.PARCELABLE_NAME_POSITION, 0);

        mFullscreenAdapter = new FullscreenAdapter(getSupportFragmentManager(), mImageList);
//        mViewPager.setPageTransformer();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mFullscreenAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //noinspection ConstantConditions
//                setTitle(mImageList.get(position).getName());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class FullscreenAdapter extends FragmentPagerAdapter {
        private ArrayList<Image> mImageList = new ArrayList<>();
        public FullscreenAdapter(FragmentManager fm, ArrayList<Image> imageList) {
            super(fm);
            this.mImageList = imageList;

        }

        @Override
        public Fragment getItem(int position) {
            return FullscreenImageFragment.newInstance(position, mImageList.get(position).getName(), mImageList.get(position).getPath());
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }
    }
}
