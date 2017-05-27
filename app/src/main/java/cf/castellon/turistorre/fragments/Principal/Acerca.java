package cf.castellon.turistorre.fragments.Principal;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;


public class Acerca extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acerca_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.ivfacebook,R.id.ivgplus,R.id.ivgmail,R.id.ivlinkedin,R.id.ivyoutube,R.id.ivtwitter})
    public void onClick(View v) {
        Uri uri;
        switch (v.getId()) {
            case R.id.ivfacebook:
                uri = Uri.parse("https://www.facebook.com/profile.php?id=100015595724230");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            case R.id.ivgplus:
                uri = Uri.parse("https://plus.google.com/u/0/114903624794912121568");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            case R.id.ivgmail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: turistorre17@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Enviar mail"));
                break;
            case R.id.ivlinkedin:
                uri = Uri.parse("https://www.linkedin.com/in/javier-mu%C3%B1oz-064b394b/");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            case R.id.ivyoutube:
                uri = Uri.parse("https://www.youtube.com/channel/UC6PKVkxPuEjcLTp7IXr17uA");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            case R.id.ivtwitter:
                uri = Uri.parse("https://twitter.com/TurisTorre");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
        }
    }
}
