package cf.castellon.turistorre.fragments.Click;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;


public class HomePagina extends Fragment {
    Imagen imagen;
    Usuario user;
    static int num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagen = getArguments().getParcelable("imagen");
        user = getArguments().getParcelable("usuario");
        num++;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        TextView tvTitulo;
        ImageView ivAvatar,ivPortada;

        inflatedView = inflater.inflate(R.layout.home_pagina_layout, container, false);
        tvTitulo = (TextView) inflatedView.findViewById(R.id.tvTitImagen);
        ivPortada = (ImageView) inflatedView.findViewById(R.id.ivPortadaImagen);
        ivAvatar = (ImageView) inflatedView.findViewById(R.id.ivAvatarImagen);

        tvTitulo.setText(imagen.getTitulo());
        Uri urlImg = Uri.parse(imagen.getUriStr());
        Uri urlAvatar = Uri.parse(user.getAvatar());
        Glide
                .with(getContext())
                .load(urlImg)
                .thumbnail(0.1f)
                .crossFade()
                .into(ivPortada);
        Glide
                .with(getContext())
                .load(urlAvatar)
                .thumbnail(0.1f)
                .crossFade()
                .into(ivAvatar);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        this.num = 0;
        super.onDestroyView();
    }
}
