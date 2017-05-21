package cf.castellon.turistorre.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v13.app.FragmentPagerAdapter;  //soporte porque hace falta para la viewpager que tambien es soporte
import java.util.ArrayList;
import java.util.List;


public class MiPageHomeAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentos;

    public MiPageHomeAdapter(FragmentManager fm) {
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

    /***
     * Añadimos fragmentos a la lista de fragmentos
     *
     */

    public void addFragment(Fragment fragment) {
        fragmentos.add(fragment);
    }

}
