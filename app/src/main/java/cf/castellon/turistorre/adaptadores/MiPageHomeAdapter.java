package cf.castellon.turistorre.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v13.app.FragmentPagerAdapter;  //soporte porque hace falta para la viewpager que tambien es soporte
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pccc on 25/03/2016.
 */

public class MiPageHomeAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentos;

    public MiPageHomeAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentos = new ArrayList<Fragment>();
    }

    @Override
    public int getCount() {
        return fragmentos.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentos.get(position);
    }

    /***
     * Añadimos fragmentos a la lista de fragmentos
     *
     * @param fragment
     */

    public void addFragment(Fragment fragment) {
        fragmentos.add(fragment);
    }

}
