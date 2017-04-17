package cf.castellon.turistorre.fragments.Principal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.castellon.turistorre.adaptadores.MyFireAdapterGaleriaRecyclerView;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.fragments.Click.GaleriaViewPager;
import cf.castellon.turistorre.ui.MainActivity;

import static cf.castellon.turistorre.utils.Constantes.*;
import static cf.castellon.turistorre.utils.Utils.*;

public class GaleriaRecyclerView extends Fragment{
    private MyFireAdapterGaleriaRecyclerView adaptador;
    private GridLayoutManager manager;
    private GaleriaViewPager carruselGaleriaFragment;
    private Bundle bund;
    private FragmentTransaction fragmentTransaction;
    Intent shareIntent;
    @BindView(R.id.rvGaleria) RecyclerView recView;
    public AppCompatActivity mActivity;
    OnPedirPermisosListener mCallback;
    public interface OnPedirPermisosListener {
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack);
        void goCamera(Map<String,Object> tipoBean);
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
        adaptador = new MyFireAdapterGaleriaRecyclerView(Imagen.class,R.layout.fila_fire_recycle,MyFireAdapterGaleriaRecyclerView.MyFireViewHolder.class,mDataBaseImgRef,getActivity());
        manager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        view = inflater.inflate(R.layout.galeria_recyclerview_layout,container,false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen imagen = adaptador.getItem(recView.getChildAdapterPosition(v)/*recView.getChildPosition(v)*/);
                fragmentTransaction = getFragmentManager().beginTransaction();
                carruselGaleriaFragment = new GaleriaViewPager();
                bund = new Bundle();
                bund.putParcelable("imagen", imagen);
                carruselGaleriaFragment.setArguments(bund);
                fragmentTransaction.replace(R.id.content_frame, carruselGaleriaFragment).commit();
            }
        });

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Imagen imagen = adaptador.getItem(recView.getChildAdapterPosition(v));

                if (usuario!=null && (imagen.getUidUser().equals(usuario.getUidUser()) || usuario.getGrupo().equalsIgnoreCase("administrador"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("¿Estas seguro de eliminar la imagen?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DatabaseReference refEliminar = adaptador.getRef(recView.getChildAdapterPosition(v));
                                    refEliminar.removeValue();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
               }
                else
                    showWarning(getActivity(),"No puedes eliminar una imagen que no es tuya");
                return true;
            }
        });

        recView.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ShareActionProvider mShareActionProvider;
        MenuItem menuItem;

        inflater.inflate(R.menu.menu_galeria, menu);
        menuItem = menu.findItem(R.id.itSharedGaleria);
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
        shareIntent =new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Desde TurisTorre");
        mShareActionProvider.setShareIntent(shareIntent);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser !=null)
            switch (item.getItemId()){
                case R.id.it_foto:
                    if (numPermisos==2)
                        mCallback.goCamera(referenciasFire.get(Tablas.Imagenes.name()));
                    else
                        mCallback.pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, recView);
                    break;
            }
        else
            showWarning(getActivity(),"Registrate para subir fotos");
        return super.onOptionsItemSelected(item);
    }

}