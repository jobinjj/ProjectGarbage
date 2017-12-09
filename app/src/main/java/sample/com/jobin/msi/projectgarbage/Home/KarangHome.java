package sample.com.jobin.msi.projectgarbage.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.tom.icontablayout.AbstractIconTabLayoutViewPagerAdapter;
import me.tom.icontablayout.IconTabLayout;
import me.tom.icontablayout.IconTabLayoutViewPager;
import sample.com.jobin.msi.projectgarbage.Karang.KarangHistory;
import sample.com.jobin.msi.projectgarbage.Karang.KarangInbox;
import sample.com.jobin.msi.projectgarbage.Karang.KarangPickup;
import sample.com.jobin.msi.projectgarbage.Karang.KarangProfile;
import sample.com.jobin.msi.projectgarbage.Login.UserLogin;
import sample.com.jobin.msi.projectgarbage.R;
import sample.com.jobin.msi.projectgarbage.user.UserAddListing;
import sample.com.jobin.msi.projectgarbage.user.UserArchive;
import sample.com.jobin.msi.projectgarbage.user.UserListing;
import sample.com.jobin.msi.projectgarbage.user.UserProfile;

public class  KarangHome extends AppCompatActivity {
    Button logout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karang_home
        );

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        IconTabLayout tabLayout = (IconTabLayout) findViewById(R.id.tabLayout);
        IconTabLayoutViewPager viewPager = (IconTabLayoutViewPager) findViewById(R.id.viewPager);
        DemoPagerAdapter adapter = new DemoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static class DemoPagerAdapter extends AbstractIconTabLayoutViewPagerAdapter {

        public DemoPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new KarangPickup();
                case 1:
                    return new KarangInbox();
                case 2:
                    return new KarangHistory();
                case 3:
                    return new KarangProfile();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
            //        switch (position) {
            //            case 0:
            //                return "Money";
            //            case 1:
            //                return "People";
            //            case 2:
            //                return "Message";
            //            case 3:
            //                return "Me";
            //            default:
            //                return null;
            //        }
        }

        @Override
        public int getPageIconResId(int position) {
            switch (position) {
                case 0:
                    return R.drawable.ic_home;
                case 1:
                    return R.drawable.ic_download;
                case 2:
                    return R.drawable.ic_history;
                case 3:
                    return R.drawable.ic_user2;
                default:
                    return 0;
            }
        }

        @Override
        public int getPageSelectedIconResId(int position) {
            switch (position) {
                case 0:
                    return R.drawable.ic_home_select;
                case 1:
                    return R.drawable.ic_download_select;
                case 2:
                    return R.drawable.ic_history_select;
                case 3:
                    return R.drawable.ic_user2_select;
                default:
                    return 0;
            }
        }
    }
}
