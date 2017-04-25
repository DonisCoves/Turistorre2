package cf.castellon.turistorre.fragments.Principal;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.SelectorAdapter;
import cf.castellon.turistorre.ui.MainActivity;

import static cf.castellon.turistorre.utils.Utils.*;

public class SelectorFragment extends ListFragment {
    Activity actividad;
    OnListSeccionListener mCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new SelectorAdapter(actividad));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            actividad = (MainActivity) context;
            try {
                mCallback=(OnListSeccionListener)actividad;
            } catch (ClassCastException e) {
                throw new ClassCastException(actividad.toString() + " debe implementar OnHeadlineSelectedListener");
            }
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

    private interface OnListSeccionListener {
        void onItemSelected(String seccionStr);
    }

}
