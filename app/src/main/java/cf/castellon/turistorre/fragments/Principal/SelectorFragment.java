package cf.castellon.turistorre.fragments.Principal;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static cf.castellon.turistorre.utils.Utils.*;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.SelectorAdapter;

import static cf.castellon.turistorre.utils.Utils.seccion;


public class SelectorFragment extends ListFragment {
    Activity actividad;
    OnListSeccionListener mCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new SelectorAdapter(actividad));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(actividad);
        this.actividad = activity;
        try {
            mCallback=(OnListSeccionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " ha de implementar OnListSeccionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.selector_layout, container, false));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        seccion = ((TextView) v.findViewById(R.id.tvTitulo)).getText().toString();
        mCallback.onItemSelected(seccion);
    }

    public interface OnListSeccionListener {
        public void onItemSelected(String seccionStr);
    }

}
