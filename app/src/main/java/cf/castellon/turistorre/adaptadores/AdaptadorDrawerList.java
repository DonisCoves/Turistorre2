package cf.castellon.turistorre.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import cf.castellon.turistorre.R;

/**
 * Adaptador que utilizaremos para rellenar la lista de la Navigation Drawer
 * Esta drawer la cargamos con un icono y un titulo que estarán englobados
 * en la clase DatosDrawerList. Luego tendremos una lista de estos datos
 * que se lo pasaremos a nuestro adaptador. A medida que se muestran
 *
 * @author Donis
 */

public class AdaptadorDrawerList extends BaseAdapter {

    private LayoutInflater inflater;
    /** Lista de los datos de la drawer list que le pasaremos al adaptador para que lo muestre. */
    public static List<DatosDrawerList> listaDatosDrawer;
    private Activity actividad;

    public AdaptadorDrawerList(Activity activity){
        this.actividad = activity;
        inicializaDatos();
        inflater = (LayoutInflater)actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaDatosDrawer.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila ;
        DatosDrawerList datosDrawerList;
        TextView seccion;
        ImageView icono;

        fila= convertView;

        if (fila==null)
            fila = inflater.inflate(R.layout.fila_drawer_list, parent, false);
        datosDrawerList = listaDatosDrawer.get(position);
        seccion = (TextView)fila.findViewById(R.id.tvTitulo);
        icono = (ImageView)fila.findViewById(R.id.ivIcono);

        seccion.setText(datosDrawerList.seccion);
        icono.setImageDrawable(datosDrawerList.icono);
        return fila;
    }

    private void inicializaDatos(){
        //Explicado en 5.-
        //Preparamos los datos para el adapter para la drawer list

        /** los titulos de las secciones de navegacion(drawer). */
        String[] seccionesDrawer;
        /** los iconos de las secciones de navegacion(drawer). */
        TypedArray iconosDrawer;
        DatosDrawerList datosDrawer;

        listaDatosDrawer = new ArrayList<>();
        iconosDrawer=actividad.getResources().obtainTypedArray(R.array.iconos);
        seccionesDrawer=actividad.getResources().getStringArray(R.array.secciones);
        listaDatosDrawer=new ArrayList<>(seccionesDrawer.length);

        for (int i=0;i<seccionesDrawer.length;i++) {
            datosDrawer = new DatosDrawerList(seccionesDrawer[i], iconosDrawer.getDrawable(i));
            listaDatosDrawer.add(datosDrawer);
        }
    }
}
