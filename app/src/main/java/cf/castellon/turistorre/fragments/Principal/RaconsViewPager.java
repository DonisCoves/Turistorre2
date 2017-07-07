package cf.castellon.turistorre.fragments.Principal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.FragmentPageCarruselAdapter;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.Click.RaconsPagina;
import cf.castellon.turistorre.fragments.ActionBar.GenerarRaco;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Utils.baseDatos;

@SuppressWarnings("unchecked")
public class RaconsViewPager extends Fragment {
    private FragmentPageCarruselAdapter adaptador;
    @BindView(R.id.vpRacons) ViewPager viewPager;
    String uidUser;
    Usuario user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        uidUser = prefs.getString("uidUser","");
        if (!uidUser.isEmpty())
            user = buscarUsuario(uidUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        HashSet racons;

        view = inflater.inflate(R.layout.racons_viewpager_layout,container,false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        adaptador = new FragmentPageCarruselAdapter(getChildFragmentManager());
        racons = baseDatos.get(Tablas.Racons.name());
        crearPaginasRacons(racons);
        viewPager.setAdapter(adaptador);

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Imagen raco;
                final Fragment frSeleccionado ;
                Bundle bund;

                frSeleccionado = adaptador.getItem(viewPager.getCurrentItem());
                bund = frSeleccionado.getArguments();
                raco = bund.getParcelable("raco");
                if (user!=null && (user.getGrupo().equalsIgnoreCase("administrador"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.deleteImg)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDataBaseRacoRef.child(raco.getUidImg()).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            DatabaseReference refEliminar = dataSnapshot.getRef();
                                            refEliminar.removeValue();
                                            adaptador.removeFragment(frSeleccionado);
                                            eliminarRaco(raco);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                    showWarning(getActivity(),R.string.deleteImgErr);
                return true;
            }
        });
        return view;
    }

    private void crearPaginasRacons(HashSet<Imagen> racons) {
        RaconsPagina pagina;

        if (racons !=null)
            for (Imagen raco:racons){
                Bundle bundle = new Bundle();
                bundle.putParcelable("raco", raco);

                pagina = new RaconsPagina();
                pagina.setArguments(bundle);
                adaptador.addFragment(pagina);
            }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_racons, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction;

        if (user!=null && (user.getGrupo().equalsIgnoreCase("administrador")))
            switch (item.getItemId()){
                case R.id.it_gen_raco:
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,new GenerarRaco()).commit();
                    fragmentTransaction.addToBackStack(null);
                    break;
            }
        else
            showWarning(getContext(),R.string.notPermision);
        return super.onOptionsItemSelected(item);
    }
}