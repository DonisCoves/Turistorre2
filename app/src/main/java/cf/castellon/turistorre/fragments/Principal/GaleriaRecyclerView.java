package cf.castellon.turistorre.fragments.Principal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
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
    AppCompatActivity mActivity;
    OnPedirPermisosListener mCallback;
    public interface OnPedirPermisosListener {
        void pedirPermiso(String permiso, int permisoRequest, View viewSnack, String tabla, ImageView ivImagen, boolean camara);
        void goCamera(String tipoBean,ImageView ivImagen);
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
        adaptador = new MyFireAdapterGaleriaRecyclerView(Imagen.class,R.layout.fila_fire_recycle,MyFireAdapterGaleriaRecyclerView.MyFireViewHolder.class,mDataBaseImgRef);
        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        view = inflater.inflate(R.layout.galeria_recyclerview_layout,container,false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        recView.setHasFixedSize(true);
        manager = new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        recView.setLayoutManager(manager);
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagen imagen = adaptador.getItem(recView.getChildAdapterPosition(v));
                fragmentTransaction = getFragmentManager().beginTransaction();
                carruselGaleriaFragment = new GaleriaViewPager();
                bund = new Bundle();
                bund.putParcelable("imagen", imagen);
                carruselGaleriaFragment.setArguments(bund);
                fragmentTransaction.replace(R.id.content_frame, carruselGaleriaFragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Imagen imagen = adaptador.getItem(recView.getChildAdapterPosition(v));

                if (usuario!=null && (imagen.getUidUser().equals(usuario.getUidUser()) || usuario.getGrupo().equalsIgnoreCase("administrador"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.deleteImg)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DatabaseReference refEliminar = adaptador.getRef(recView.getChildAdapterPosition(v));
                                    refEliminar.removeValue();
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
        ShareActionProvider mShareActionProvider;
        MenuItem menuItem;

        inflater.inflate(R.menu.menu_galeria, menu);
        menuItem = menu.findItem(R.id.itSharedGaleria);
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
        shareIntent =new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        String archivo = "android.resource://"+  getContext().getPackageName() + "/drawable/escudo";
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(archivo));

        mShareActionProvider.setShareIntent(shareIntent);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFirebaseUser !=null)
            switch (item.getItemId()){
                case R.id.it_foto:
                    if (numPermisos==2)
                        mCallback.goCamera(Tablas.Imagenes.name(),null);
                    else
                        mCallback.pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, recView, Tablas.Imagenes.name(), null, true);
                    break;
            }
        else
            showWarning(getActivity(),R.string.registrate);
        return super.onOptionsItemSelected(item);
    }

}