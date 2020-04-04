package com.noahedu.demo;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.noahedu.demo.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager和Fragment混合使用的Demo
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-11-14
 */
public class ViewPagerDemo extends FragmentActivity {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String>   titleList    = new ArrayList<String>();

    private Button trineaInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_demo);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        ViewPager vp = (ViewPager)findViewById(R.id.view_pager);
        for (int i = 0; i < 3; i++) {
            ViewPagerFragment viewPagerFragment1 = new ViewPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("upImageId", 0);
            bundle.putString("text", "Page " + i);
            viewPagerFragment1.setArguments(bundle);
            titleList.add("title " + i);
            fragmentList.add(viewPagerFragment1);
        }

        vp.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
    }

    /**
     * 定义适配器
     * 
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-11-15
     */
    class myPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private List<String>   titleList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList){
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.size() > position) ? titleList.get(position) : "";
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
