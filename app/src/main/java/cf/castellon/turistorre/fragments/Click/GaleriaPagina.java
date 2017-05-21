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

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;

public class GaleriaPagina extends Fragment {
    Imagen imagen;
    Usuario usuario;
    @BindView(R.id.tvTitImg) TextView tvTitulo;
    @BindView(R.id.ivAvatar) ImageView ivAvatar;
    @BindView(R.id.ivPortada) ImageView ivPortada;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        imagen = getArguments().getParcelable("imagen");
        usuario = getArguments().getParcelable("usuario");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView;

        inflatedView = inflater.inflate(R.layout.mipagefragment_carrusel_layout, container, false);
        ButterKnife.bind(this,inflatedView);
        Uri urlImg = Uri.parse(imagen.getUriStr());
        Uri urlAvatar = Uri.parse(usuario.getAvatar());
        tvTitulo.setText(imagen.getTitulo());
        Glide.with(getContext()).load(urlImg).into(ivPortada);
        Glide.with(getContext()).load(urlAvatar).into(ivAvatar);
        return inflatedView;
    }

}
