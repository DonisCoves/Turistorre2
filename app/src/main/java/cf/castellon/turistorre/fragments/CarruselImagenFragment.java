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

/**
 * Created by pccc on 25/03/2016.
 */

public class CarruselImagenFragment extends Fragment {
    String url_img;
    String id_img;
    String url_avatar;
    Bundle args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        args = getArguments();
        this.url_img = args.getString("URL_IMG");
        this.id_img = args.getString("ID_IMG");
        this.url_avatar = args.getString("AVATAR");
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
        Uri urlImg = Uri.parse(url_img);
        Uri urlAvatar = Uri.parse(url_avatar);
        Glide.with(getContext()).load(urlImg).into(ivPortada);
        Glide.with(getContext()).load(urlAvatar).into(ivAvatar);
        return inflatedView;
    }

}
