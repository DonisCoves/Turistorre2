package cf.castellon.turistorre.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.ImagenParcel;

/**
 * Created by pccc on 16/03/2016.
 */
public class GaleriaDiaAdapter extends BaseAdapter {
    Activity actividad;
    LayoutInflater inflater;
    List<ImagenParcel> imagenes;

    public GaleriaDiaAdapter(Activity activity, List<ImagenParcel> imagenes) {
        this.actividad = activity;
        inflater = (LayoutInflater)this.actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return imagenes.size();
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
        ImageView imageView;
        Uri url;

        inflatedView = convertView;
        if (inflatedView==null) {
            inflatedView = inflater.inflate(R.layout.elemento_gridview_galeriadia,parent,false);
        }

        imageView = (ImageView)inflatedView.findViewById(R.id.ivElementoGridView);
        //imageView.setTag(urlsList.get(position));
        url = Uri.parse(imagenes.get(position).getUriStrPre());
        Glide
                .with(actividad)
                .load(url)
//                    .centerCrop()
//                    .fitCenter()
                .placeholder(R.drawable.escudo)
                .crossFade()
                .into(imageView);
        return inflatedView;
    }
}
