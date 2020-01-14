package com.bank.it.jobs.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bank.it.jobs.Fragments.ByOthersFragment;
import com.bank.it.jobs.Fragments.CommentFragment;
import com.bank.it.jobs.Fragments.DatabaseFragment;
import com.bank.it.jobs.Fragments.HomeFragment;
import com.bank.it.jobs.Fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private int mNoOfTabs;
    public PostDetailsAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new CommentFragment();
            case 1:
                return new DatabaseFragment();
            case 2:
                return new HomeFragment();
            case 3:
                return new ProfileFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
