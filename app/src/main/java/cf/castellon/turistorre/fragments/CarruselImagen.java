package cf.castellon.turistorre.fragments;

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
import cf.castellon.turistorre.bean.ImagenParcel;
import cf.castellon.turistorre.bean.UsuarioParcel;

/**
 * Created by pccc on 25/03/2016.
 */

public class CarruselImagen extends Fragment {
   ImagenParcel imagen;
    UsuarioParcel usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        imagen = getArguments().getParcelable("imagen");
        usuario = getArguments().getParcelable("usuario");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;
        TextView tvTitulo;
        ImageView ivAvatar,ivPortada;

        inflatedView = inflater.inflate(R.layout.mipagefragment_carrusel_layout, container, false);
        tvTitulo = (TextView) inflatedView.findViewById(R.id.tvTitImg);
        ivAvatar = (ImageView) inflatedView.findViewById(R.id.ivAvatar);
        ivPortada = (ImageView) inflatedView.findViewById(R.id.ivPortada);

        tvTitulo.setText("no hjghj");
        Uri urlImg = Uri.parse(imagen.getUriStr());
        Uri urlAvatar = Uri.parse(usuario.getAvatar());
        Glide.with(getContext()).load(urlImg).into(ivPortada);
        Glide.with(getContext()).load(urlAvatar).into(ivAvatar);
        return inflatedView;
    }

}
