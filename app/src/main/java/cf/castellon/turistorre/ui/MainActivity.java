package cf.castellon.turistorre.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;
import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.AdaptadorDrawerList;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.ActionBar.GenerarBando;
import cf.castellon.turistorre.fragments.ActionBar.GenerarRaco;
import cf.castellon.turistorre.fragments.ActionBar.GenerarTerrat;
import cf.castellon.turistorre.fragments.Click.Click.FiestasEventosGaleriaRecyclerView;
import cf.castellon.turistorre.fragments.Principal.Ajustes;
import cf.castellon.turistorre.fragments.Principal.BandoRecyclerView;
import cf.castellon.turistorre.fragments.Click.BandoSeleccionado;
import cf.castellon.turistorre.fragments.Principal.FiestasRecylerView;
import cf.castellon.turistorre.fragments.Principal.GaleriaRecyclerView;
import cf.castellon.turistorre.fragments.Principal.HomeViewPager;
import cf.castellon.turistorre.fragments.Principal.Login;
import cf.castellon.turistorre.fragments.Principal.RaconsViewPager;
import cf.castellon.turistorre.fragments.Principal.Splash;
import cf.castellon.turistorre.fragments.Click.TerratSeleccionado;
import cf.castellon.turistorre.fragments.Principal.TerratsRecyclerView;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener/*, SelectorFragment.OnListSeccionListener*/
        ,GenerarTerrat.OnPedirPermisosListener, GaleriaRecyclerView.OnPedirPermisosListener
        , GenerarRaco.OnPedirPermisosListener, GenerarBando.OnPedirPermisosListener
        , FiestasEventosGaleriaRecyclerView.OnPedirPermisosListener{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Fragment frSeccion;
    private AdaptadorDrawerList adaptadorDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentTransaction fragmentTransaction;
    String tabla;
	Uri fileUri;
    ImageView imageView;
    boolean camara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch (getIntent().getAction()) {
            case ACTION_MAIN:
                frSeccion = new Splash();
                seccion = "Home";
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                prepareDrawer();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                break;
            case ACTION_GPO_BANDO:
                seccion = "Bando";
                frSeccion = new BandoSeleccionado();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                prepareDrawer();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                //Falta para tablets
                Bundle bund = getIntent().getExtras();
                String uidBand = bund.getString("uidBando");
                mDataBaseBandoRef.child(uidBand).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null) {
                                    Imagen bando = dataSnapshot.getValue(Imagen.class);
                                    Bundle bund = new Bundle();
                                    bund.putParcelable("bando", bando);
                                    frSeccion.setArguments(bund);
                                    fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                hideProgressDialog();
                            }
                        });
                break;
            case ACTION_CAMBIO_GRUPO:
                // No es necesario comprobar que al usuario que recibe la peticion es el administros ya que eso
                // nos lo hace FireBase. Si esta suscrito al topic es que será el administrador
                seccion = "Home";
                frSeccion = new HomeViewPager();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                prepareDrawer();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                Bundle bundle = getIntent().getExtras();
                final String uidUser = bundle.getString("uidUser");
                String nombre = bundle.getString("nombre");
                final String grupoNuevo = bundle.getString("grupoNuevo");
                final String grupoViejo = bundle.getString("grupoViejo");
                final String avatar = bundle.getString("avatar");
                final Usuario userCliente = new Usuario(nombre,avatar,nombre, uidUser,grupoNuevo);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Conceder Permiso "+grupoNuevo+" ?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mDataBaseGruposRef.child(grupoViejo).child(uidUser).setValue(null);
                                mDataBaseGruposRef.child(grupoNuevo).child(uidUser).setValue(userCliente);
                                mDataBaseUsersRef.child(uidUser).setValue(userCliente);
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
                fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                prepareDrawer();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                break;
            case ACTION_GPO_TERRAT:
                seccion = "Terrat";
                frSeccion = new TerratSeleccionado();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                prepareDrawer();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                Bundle bund2 = getIntent().getExtras();
                String uidTerrat = bund2.getString("uidTerrat");
                Imagen pano  = buscarTerrat(uidTerrat);
                Usuario usuario = buscarUsuario(pano.getUidUser());
                Bundle bund3 = new Bundle();
                bund3.putParcelable("imagen",pano);
                bund3.putParcelable("usuario",usuario);
                frSeccion.setArguments(bund3);
                fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
        }
    }

    private void prepareDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(getTitle());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(seccion);
            }
        };
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adaptadorDrawer = new AdaptadorDrawerList(this);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(adaptadorDrawer);
        mDrawerList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        seccion = ((TextView) view.findViewById(R.id.tvTitulo)).getText().toString();
        switch (seccion) {
            case "Home":
                frSeccion = new HomeViewPager();
                seccion = "Home";
                break;
            case "Racons":
                frSeccion = new RaconsViewPager();
                break;
            case "Galeria":
                frSeccion = new GaleriaRecyclerView();
                break;
            case "Bandos":
                frSeccion = new BandoRecyclerView();
                break;
            case "Terrats":
                frSeccion = new TerratsRecyclerView();
                break;
            case "Login":
                frSeccion = new Login();
                break;
            case "Festes":
                frSeccion = new FiestasRecylerView();
                break;
            case "Ajustes":
                frSeccion = new Ajustes();
                break;
        }
        mDrawerList.setItemChecked(position, true);
        getSupportActionBar().setTitle(seccion);
        mDrawerLayout.closeDrawer(mDrawerList);
        fragmentTransaction.replace(R.id.content_frame, frSeccion,seccion).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path;
        Map<String,Object> referenciaFire;
        String tablaOk;

        referenciaFire = parsearParametrosRFire(tabla);
        tablaOk = parsearTabla(tabla);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    referenciaFire.put("fileUri", fileUri);
                    referenciasFire.put(tablaOk,referenciaFire);
                    switch (tablaOk){
                        case "Bandos":
                            imageView.setImageURI(fileUri);
                            break;
                        case "Racons":
                            imageView.setImageURI(fileUri);
                            break;
                        case "Imagenes":
                            guardarFotoStorageFire(referenciaFire, this, getSupportFragmentManager(),null,null);
                            break;
                        case "DiasFiestas":
                            guardarFotoStorageFire(referenciaFire, this, getSupportFragmentManager(),null,null);
                            break;
                    }
                }
                break;
            case CAPTURE_GALLERY_ACTIVITY_REQUEST_CODE:
                if (resultCode==Activity.RESULT_OK && data!=null){
                    path = obtenerPath(data.getData(),getBaseContext());
                    fileUri = Uri.parse(path);
                    referenciaFire.put("fileUri",fileUri);
                    referenciasFire.put(tablaOk,referenciaFire);
                    switch (tablaOk){
                        case "Terrats":
                            //bitMapStatic = formatearBitmapfromPath(path);
                            bitMapStatic = loadImage(path);
                            imageView.setImageBitmap(bitMapStatic);
                            break;
                        case "Bandos":
                            bitMapStatic = formatearBitmapfromPath(path);
                            imageView.setImageBitmap(bitMapStatic);
                            break;
                        case "Racons":
                            bitMapStatic = formatearBitmapfromPath(path);
                            imageView.setImageBitmap(bitMapStatic);
                            break;
                        case "DiasFiestas":
                            guardarFotoStorageFire(referenciaFire, this, getSupportFragmentManager(),null,null);
                            break;
                    }
                }
                break;
            default: //Acceso Login
                Login fragment = (Login) getSupportFragmentManager().findFragmentByTag(seccion);
                fragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @SuppressLint("NewAPi")
    public void pedirPermiso(final String permiso, final int permisoRequest, View viewSnack, String tabla, ImageView imageView, boolean camara) {
        this.tabla = tabla;
        this.imageView = imageView;
        this.camara = camara;
        if (Build.VERSION.SDK_INT>22) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permiso) != PackageManager.PERMISSION_GRANTED)
                if (shouldShowRequestPermissionRationale(permiso)) {
                    Snackbar.make(viewSnack, R.string.camaraReg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermissions(new String[]{permiso}, permisoRequest);
                                }
                            })
                            .show();
                } else
                    requestPermissions(new String[]{permiso}, permisoRequest);
            else if (numPermisos != 2)
                if (this.camara)
                    pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, viewSnack, tabla, imageView, this.camara);
                else
                    pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, viewSnack, tabla, imageView, this.camara);
        }
        else {
            if (camara)
                goCamera(tabla,imageView);
            else
                goGaleria(tabla,imageView);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        Editor editor;

        switch (requestCode) {
            case PERMISO_CAMARA :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ++numPermisos;
                    pedirPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISO_ESCRIBIR_SD, new View(getBaseContext()), tabla, imageView, camara);
                }
                break;
            case PERMISO_ESCRIBIR_SD :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ++numPermisos;
                    pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, new View(getBaseContext()), tabla, imageView, camara);
                }
                break;
        }
        editor = prefs.edit();
        editor.putInt("numPermisos", numPermisos);
        editor.apply();
        if (numPermisos==2)
            if (camara)
                goCamera(tabla,imageView);
            else
                goGaleria(tabla,imageView);
    }

    @Override
    public void goGaleria(String tabla, ImageView imageView) {
        this.imageView =  imageView;
        this.tabla = tabla;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CAPTURE_GALLERY_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void goCamera(String tabla, ImageView imageView) {
        Intent intent;
        Uri fileUri;

        this.imageView =  imageView;
        this.tabla = tabla;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFile(); // create a file to save the image
        this.fileUri = fileUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}