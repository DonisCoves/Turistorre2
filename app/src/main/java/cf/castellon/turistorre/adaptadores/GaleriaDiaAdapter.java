package cf.castellon.turistorre.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.List;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;

public class GaleriaDiaAdapter extends BaseAdapter {
   private Activity actividad;
    private LayoutInflater inflater;
    private List<Imagen> imagenes;

    public GaleriaDiaAdapter(Activity activity, List<Imagen> imagenes) {
        this.actividad = activity;
        inflater = (LayoutInflater)this.actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public Imagen getItem(int position) {
        return imagenes.get(position);
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
        url = Uri.parse(imagenes.get(position).getUriStr());
        Glide
                .with(actividad)
                .load(url)
                .thumbnail(0.1f)
                .crossFade()
                .into(imageView);
        return inflatedView;
    }
}
