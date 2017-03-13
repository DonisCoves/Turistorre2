package cf.castellon.turistorre.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
 * Created by pccc on 16/03/2016.
 */
public class SelectorAdapter extends BaseAdapter {
    Activity actividad;
    LayoutInflater inflater;
    List<DatosDrawerList> secciones;

    public SelectorAdapter(Activity activity) {
        this.actividad = activity;
        inflater = (LayoutInflater)this.actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inicializaDatos();
    }



    @Override
    public int getCount() {
        return actividad.getResources().getStringArray(R.array.secciones).length;
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
        View inflatedView;
        ImageView ivIcono;
        TextView tvSeccion;
        DatosDrawerList datos;

        inflatedView = convertView;
        datos = secciones.get(position);

        if (inflatedView==null) {
            inflatedView = inflater.inflate(R.layout.fila_drawer_list,parent,false);
        }

        ivIcono = (ImageView)inflatedView.findViewById(R.id.ivIcono);
        tvSeccion = (TextView)inflatedView.findViewById(R.id.tvTitulo);

        ivIcono.setImageDrawable(datos.icono);
        tvSeccion.setText(datos.seccion);
        return inflatedView;
    }

    private void inicializaDatos() {
        String [] titulos;
        TypedArray iconos;

        secciones=new ArrayList<>();
        iconos = actividad.getResources().obtainTypedArray(R.array.iconos);
        titulos = actividad.getResources().getStringArray(R.array.secciones);
        for(int i=0;i<titulos.length;i++){
            secciones.add(new DatosDrawerList(titulos[i],iconos.getDrawable(i)));
        }
    }
}
