package cf.castellon.turistorre.pruebas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;

import static cf.castellon.turistorre.utils.Constantes.mDataBaseFiestasRef;
import static cf.castellon.turistorre.utils.Utils.*;

public class ProbesActivity extends AppCompatActivity {
    Bitmap bitmap;
    @Bind(R.id.ivFilaRecycleTerrat) ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fila_fire_terrat_recycle);

       DatabaseReference ref = mDataBaseFiestasRef.child("probes");
        ref.orderByChild("orden").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ola ="ola";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       
    }
}