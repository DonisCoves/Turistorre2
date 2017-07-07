package cf.castellon.turistorre.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cf.castellon.turistorre.fragments.Click.RaconsPagina;

public class FragmentPageCarruselAdapter extends FragmentPagerAdapter implements View.OnLongClickListener{
    private List<Fragment> fragmentos;
    private View.OnLongClickListener listenerLong;

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
        Fragment fragment;
        RaconsPagina fragmentRacons;
        try {
            fragmentRacons = (RaconsPagina)fragmentos.get(position);
            fragmentRacons.setOnLongClickListener(listenerLong);
            return fragmentRacons;
        } catch (ClassCastException e){
            fragment = fragmentos.get(position);
            return fragment;
        }
    }

    public void addFragment(Fragment fragment) {
        fragmentos.add(fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(Fragment fragment) {
        fragmentos.remove(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (fragmentos.contains(object))
            return fragmentos.indexOf(object);
        else
            return POSITION_NONE;
    }

    @Override
    public boolean onLongClick(View v) {
        if(listenerLong != null)
            listenerLong.onLongClick(v);
        return true;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.listenerLong = listener;
    }
}
