package cf.castellon.turistorre.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cf.castellon.turistorre.R;

public class MiPageFragment extends Fragment {
    String titulo;
    Drawable portada;
    Drawable avatar;
    static int num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.titulo = getResources().getStringArray(R.array.titulosHome)[num];
        this.portada=getContext().getResources().obtainTypedArray(R.array.fotosHome).getDrawable(num);
        this.avatar = getContext().getResources().getDrawable(R.drawable.escudo);
        num++;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        TextView tvTitulo;
        ImageView ivAvatar,ivPortada;

        inflatedView = inflater.inflate(R.layout.home_pageadapter_layout, container, false);
        tvTitulo = (TextView) inflatedView.findViewById(R.id.tvTitImagen);
        ivPortada = (ImageView) inflatedView.findViewById(R.id.ivPortadaImagen);
        ivAvatar = (ImageView) inflatedView.findViewById(R.id.ivAvatarImagen);

        tvTitulo.setText(this.titulo);
        ivPortada.setImageDrawable(this.portada);
        ivAvatar.setImageDrawable(this.avatar);
        
        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        this.num = 0;
        super.onDestroyView();
    }
}
