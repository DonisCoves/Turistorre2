package cf.castellon.turistorre.fragments.Click.Click;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.MyFireAdapterGaleriaEventoRecyclerView;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.Click.GaleriaEventosViewPager;
import cf.castellon.turistorre.ui.MainActivity;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

public class FiestasEventosGaleriaRecyclerView extends Fragment {
    private MyFireAdapterGaleriaEventoRecyclerView adaptador;
    private GridLayoutManager manager;
    private FirebaseUser mFirebaseUser ;
    @BindView(R.id.rvGaleriaEventos) RecyclerView recView;
    private String uidEvento,uidDiaFiesta;
    OnPedirPermisosListener mCallback;
    AppCompatActivity mActivity;
    String uidUser;

    public interface OnPedirPermisosListener {
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack, String tabla, ImageView imageView, boolean camara);
        void goCamera(String tipoBean,ImageView ivImagen);
        void goGaleria(String tipoBean, ImageView terrat);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
            try {
                mCallback = (OnPedirPermisosListener) mActivity;
            } catch (ClassCastException e) {
                throw new ClassCastException(mActivity.toString() + " debe implementar OnHeadlineSelectedListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference ref;

        uidEvento = getArguments().getString("UIDEvento");
        uidDiaFiesta = getArguments().getString("UIDDia");
        ref = mDataBaseDiasFiestaRef.child(uidDiaFiesta).child("eventos").child(uidEvento).child("imagenes");
        adaptador = new MyFireAdapterGaleriaEventoRecyclerView(Imagen.class,
                                                               R.layout.fila_eventos_galeria,
                                                               MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder.class,
                                                               ref , uidDiaFiesta, uidEvento
                                                               );
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        uidUser = prefs.getString("uidUser","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fiestaseventosgaleria_recyclerview_layout,container,false);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        manager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        recView.setLayoutManager(manager);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen imagen = adaptador.getItem(recView.getChildAdapterPosition(v));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                GaleriaEventosViewPager galeriaEventosViewPager = new GaleriaEventosViewPager();
                Bundle bund = new Bundle();
                bund.putParcelable("imagen", imagen);
                bund.putString("uidEvento", uidEvento);
                bund.putString("uidDiaFiesta", uidDiaFiesta);
                galeriaEventosViewPager.setArguments(bund);
                fragmentTransaction.replace(R.id.content_frame, galeriaEventosViewPager).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final Imagen imagen;
                Usuario user;

                imagen = adaptador.getItem(recView.getChildAdapterPosition(v));
                user = buscarUsuario(uidUser);
                if (user!=null && (imagen.getUidUser().equals(uidUser) || user.getGrupo().equalsIgnoreCase("administrador"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.deleteImg)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DatabaseReference refEliminar = adaptador.getRef(recView.getChildAdapterPosition(v));
                                    refEliminar.removeValue();
                                    eliminarImagenDeEvento(imagen,uidDiaFiesta,uidEvento);
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

        recView.setAdapter(adaptador);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_eventos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String arreglo;
        arreglo = Tablas.DiasFiestas.name()+" "+uidDiaFiesta+ " "+uidEvento;
        if (mFirebaseUser!=null)
            switch (item.getItemId()){
                case R.id.it_camera:
                    if (numPermisos==2)
                        mCallback.goCamera(arreglo,null);
                    else
                        mCallback.pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, recView, arreglo, null, true);
                    break;
                case R.id.it_galeriaEventos:
                    if (numPermisos==2)
                        mCallback.goGaleria(arreglo,null);
                    else
                        mCallback.pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, null,arreglo, null, false);
                    break;
            }
        else
            Toast.makeText(getContext(),"Registrate para enviar fotos",Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}
