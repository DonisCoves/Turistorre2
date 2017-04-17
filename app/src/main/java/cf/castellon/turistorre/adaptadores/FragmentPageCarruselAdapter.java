package cf.castellon.turistorre.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPageCarruselAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentos;

    public FragmentPageCarruselAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentos = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return fragmentos.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentos.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragmentos.add(fragment);
    }

}
