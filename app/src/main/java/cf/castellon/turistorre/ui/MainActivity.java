package cf.castellon.turistorre.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

import static cf.castellon.turistorre.utils.Utils.*;
import static cf.castellon.turistorre.utils.Constantes.*;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.adaptadores.AdaptadorDrawerList;
import cf.castellon.turistorre.adaptadores.DatosDrawerList;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.ActionBar.GenerarTerrat;
import cf.castellon.turistorre.fragments.Principal.AcercaFragment;
import cf.castellon.turistorre.fragments.Principal.AjustesFragment;
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
import cf.castellon.turistorre.bean.Bando;


public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener/*, SelectorFragment.OnListSeccionListener*/
        ,GenerarTerrat.OnPedirPermisosListener, GaleriaRecyclerView.OnPedirPermisosListener {
    /**
     * Es todo el objeto de la navegacion, que incluye el cajon "mDrawerList" y el frame donde
     * se incluirán los fragmentos(tambien se pueden meter activitys, pero con fragments el cambio es mas "armónico".
     */
    private DrawerLayout mDrawerLayout;
    /**
     * La lista que se ve a la izquierda el "drawer", "drawerlist" o el "cajón".
     */
    private ListView mDrawerList;
    /**
     * El fragmento que se insertará cuando pulsamos en la lista.
     */
    private Fragment frSeccion;
    ShareActionProvider mShareActionProvider;


    /*necesario para mostrar los datos en la lista de la drawer: */
    /**
     * Clase que contiene la estructura de datos para la drawer list.
     */
    private DatosDrawerList datosDrawer;

    /**
     * Adaptador utilizado para la drawer list.
     */
    private AdaptadorDrawerList adaptadorDrawer;
    /**
     * Intent para saltat a una activity: en el perfil pej..
     */
    private Intent intent;
    /**
     * Para las interacciones entre el drawer y la action bar
     * Action Bar que tiene los eventos de cerrar y abrir el drawer
     * Esta Action Bar especifica se utiliza porque en el developers
     * dice que cuando la Action Bar está desplegada solo aparecerá
     * el icono y el título de la app, los items de acciones se desplazan
     * al desbordamiento.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Para trabajar con los fragmentos
     */

    private FragmentTransaction fragmentTransaction;
    Map<String,Object> tipoBean;
	Uri fileUri;
    ImageView ivTerrat;
    /**
     * En la creación de la actividad hacemos:
     * 1.-Creamos un acction Bar (ActionBarDrawerToggle) el cual manejará los eventos entre
     * la drawer y nuestra action bar normal . Estos eventos son:
     * onDrawerOpened y onDrawerClosed.- Se abre/cierra el "cajón" con lo que , segun la doc. oficial,
     * deberán desaparecer los iconos de la action bar y el título de la action bar no será el del
     * módulo en el que estemos sino el nombre de la app (Showpping en nuestor caso)
     * (todo esto en onDrawerOpened en el onDrawerClose tendremos que mostrar los botones y el titulo
     * del módulo seleccionado).
     * En estos métodos tendremos que llamar a invalidateOptionsMenu en donde le indicaremos a Android
     * que queremos cambiar la action Bar. invalidateOptionsMenu llama a onPrepareOptionsMenu  y
     * es aquí donde comprobamos si el drawer está abierto o cerrado(ya que tambien podemos
     * entrar a onPrepareOptionsMenu desde el método de cierre del drawer onDrawerClosed). Si el
     * cajón está abierto pues lo dicho hacemos invisibles los botones de la action y
     * mostramos el nombre de la app
     * <p/>
     * 2.- Anulamos los eventos del Drawer,es decir, el drawer ya no manejará cuando se abre/cierra el cajón
     * sino que lo hará el ActionBarDrawerToggle, ¿por qué? Sino tuvieramos Action Bar no pasaría nada,
     * pero al tenerla tenemos que hacer lo de mostrar/ocultar los iconos y el título de la action bar.
     * Para ello se utiliza esta clase ActionBarDrawerToggle que tiene los eventos de apertura/cierre del
     * cajón. Entonces la clase fuente que se encarga de recoger los eventos físicos(la DrawerLayout)
     * cojerá los eventos del ActionBarDrawerToggle.
     * <p/>
     * 3.- Habilitamos el icono de la actionBar para hacer que se abre/esconda  getActionBar().setHomeButtonEnabled(true)
     * <p/>
     * 4.- Habilitamos la navegación hacia arriba del icono de la actionBar getActionBar().setDisplayHomeAsUpEnabled(true)
     * <p/>
     * 5.- Montamos el listview mDrawerList. Con un clase con una estructura de los datos, un xml como recurso
     * que contendrá el diseño de una fila de la lista fila_drawer_list.xml y un adaptador para unirlo todo
     * y luego pasarselo a la listview para que muestre los datos
     * <p/>
     * 6.-Mostramos el fragment inicial
     */
    //Si la app esta en 2ºplano y llega una notificacion saltamos en el fragment pertinente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch (getIntent().getAction()) {
            case ACTION_MAIN:
                frSeccion = new Splash();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (getSupportFragmentManager().findFragmentById(R.id.frSelector) != null) { //tablet
                    fragmentTransaction.add(R.id.flMain, frSeccion).commit();
                } else {
                    fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                    prepareDrawer();
                    mDrawerLayout.setDrawerListener(mDrawerToggle);
                }
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
                                Bando bando = dataSnapshot.getValue(Bando.class);
                                Bundle bund = new Bundle();
                                bund.putString("TITULO",bando.getTitulo());
                                bund.putString("DESCRIPCION",bando.getDescripcion());
                                bund.putString("IMAGEN_URL_STR",bando.getUrl());
                                frSeccion.setArguments(bund);
                                fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "notificacionBando:onFailure: "+ databaseError.getMessage());
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
                final Usuario userCliente = new Usuario(nombre,avatar,uidUser,grupoNuevo);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Conceder Permiso?")
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
                if (getSupportFragmentManager().findFragmentById(R.id.frSelector) != null) { //tablet
                    fragmentTransaction.add(R.id.flMain, frSeccion).commit();
                } else {
                    fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                    prepareDrawer();
                    mDrawerLayout.setDrawerListener(mDrawerToggle);
                }
                break;
            case ACTION_GPO_TERRAT:
                seccion = "Terrat";
                frSeccion = new TerratSeleccionado();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                prepareDrawer();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                //Falta para tablets
                Bundle bund2 = getIntent().getExtras();
                String uidTerrat = bund2.getString("uidTerrat");
                mDataBaseTerratRef.child(uidTerrat).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Imagen pano  = dataSnapshot.getValue(Imagen.class);
                                Usuario usuario = buscarUsuario(pano.getUidUser());
                                Bundle bund = new Bundle();
                                bund.putString("DIRECCION",pano.getTitulo());
                                bund.putString("AVATAR",usuario.getAvatar());
                                bund.putString("PANO_URL_STR",pano.getUriStr());
                                frSeccion.setArguments(bund);
                                fragmentTransaction.add(R.id.content_frame, frSeccion).commit();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "notificacionBando:onFailure: "+ databaseError.getMessage());
                                hideProgressDialog();
                            }
                        });
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* if (racons.isEmpty())
            crearRacons();
        if (usuarios.isEmpty())
            crearUsuarios();
        if (imagenes.isEmpty())
            crearImagenes();*/
    }

    private void prepareDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Explicado en 1.-

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.open, R.string.close) {
            /*    En estos métodos tendremos que llamar a invalidateOptionsMenu en donde le indicaremos a Android
            *    que queremos cambiar la action Bar. invalidateOptionsMenu llama a onPrepareOptionsMenu  */
            @Override
            public void onDrawerOpened(View drawerView) {
                //mostramos el nombre de la app
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(getTitle());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(seccion);
            }
        };
        //Explicado en 3.-
        //Enable or disable the "home" button in the corner of the action bar.
        //(Note that this is the application home/up affordance on the action bar, not the systemwide home button.)
        //This defaults the application should call this method to enable interaction with the home/up affordance.
        //Habilitamos la imagen como botón
        getSupportActionBar().setHomeButtonEnabled(true);

        //Explicado en 4.-
        //Set whether home should be displayed as an "up" affordance.
        //Set this to true if selecting "home" returns up by a single level in your
        //UI rather than back to the top level or front page.
        //Habilitamos navegacion hacia arriba
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adaptadorDrawer = new AdaptadorDrawerList(this);

        /*final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        final String color_fondo_drawer = mFirebaseRemoteConfig.getString("color_fondo_drawer");
        final String color_fondo_drawer2 = mFirebaseRemoteConfig.getString("color_fondo_drawer2");

        int id = getResources().getIdentifier(color_fondo_drawer,"color",getPackageName());

        //iconosDrawer.recycle();  //como no se ejecuta el garbaje tenemos que hacerlo nosotros*/
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(adaptadorDrawer);
        mDrawerList.setOnItemClickListener(this);
//        mDrawerList.setBackgroundColor(getResources().getColor(id));
//        mFirebaseRemoteConfig.activateFetched();
        /*mFirebaseRemoteConfig.fetch(0).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Probes","Color Antes: "+color_fondo_drawer);
                String color_fondo_drawer = mFirebaseRemoteConfig.getString("color_fondo_drawer");
                int id = getResources().getIdentifier(color_fondo_drawer,"color",getPackageName());
                mDrawerList.setBackgroundColor(getResources().getColor(id));
                mFirebaseRemoteConfig.activateFetched();
                color_fondo_drawer = mFirebaseRemoteConfig.getString("color_fondo_drawer");
                id = getResources().getIdentifier(color_fondo_drawer,"color",getPackageName());
                mDrawerList.setBackgroundColor(getResources().getColor(id));
                Log.d("Probes","Color Despues: "+color_fondo_drawer);
            }
        });*/
//        mFirebaseRemoteConfig.activateFetched();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen;
        /*comprobamos si el drawer está abierto o cerrado(ya que tambien podemos
        *    entrar a onPrepareOptionsMenu desde el método de cierre del drawer onDrawerClosed). Si el
        *    cajón está abierto pues lo dicho hacemos invisibles los botones de la action
        *
        */
        if (getSupportFragmentManager().findFragmentById(R.id.frSelector) == null) { //movil
            //deberán desaparecer los iconos de la action bar
            isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            if (isDrawerOpen) {
//                menu.findItem(R.id.action_settings).setVisible(false);
                String nada="asdasd";
            } else { //tendremos que mostrar los botones y el titulo
                //del módulo seleccionado).
//                menu.findItem(R.id.action_settings).setVisible(true);
           /* if (mDrawerList.isSelected()) {
                getActionBar().setTitle(mDrawerList.getSelectedItem().toString());

            }*/
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        seccion = ((TextView) view.findViewById(R.id.tvTitulo)).getText().toString();
        switch (seccion) {
            case "Home":
                frSeccion = new HomeViewPager();
                seccion = "TurisTorre";
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
            case "Fiestas":
                //frSeccion = new FiestasFragment();
                frSeccion = new FiestasRecylerView();
                break;
            case "Ajustes":
                frSeccion = new AjustesFragment();
                break;
            case "Acerca_de":
                frSeccion = new AcercaFragment();
                break;
        }
        mDrawerList.setItemChecked(position, true);
        getSupportActionBar().setTitle(seccion);
        mDrawerLayout.closeDrawer(mDrawerList);
        //fragmentTransaction.remove(getFragmentManager().getFragment());
        fragmentTransaction.replace(R.id.content_frame, frSeccion,seccion).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    protected Intent getDefaultShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Extra Text");
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //	Si el ikono de nuestra barra de accion se ha pulsado devolvemos true
        // para indicar que el evento se ha procesado y que se encargar� el ActionBarDrawerToggle de procesarlo
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path;
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    tipoBean.put("fileUri", fileUri);
                    guardarFotoStorageFire(tipoBean, this, getSupportFragmentManager(),null);
                }
                break;
            case CAPTURE_GALLERY_ACTIVITY_REQUEST_CODE:
                if (resultCode==Activity.RESULT_OK && data!=null){
                    path = obtenerPath(data.getData(),getBaseContext());
                    fileUri = Uri.parse(path);
                    tipoBean = referenciasFire.get(Tablas.Terrats.name());
                    tipoBean.put("fileUri",fileUri);
                    referenciasFire.put(Tablas.Terrats.name(),tipoBean);
                    bitMapStatic = formatearBitmapfromPath(path);
                    ivTerrat.setImageBitmap(bitMapStatic);
                }
                break;
            default: //Acceso Login
                Login fragment = (Login) getSupportFragmentManager().findFragmentByTag(seccion);
                fragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    @SuppressLint("NewAPi")
    public void pedirPermiso(final String permiso, final int permisoRequest, View viewSnack) {
        // Si no tenemos el permiso:(la primera vez no lo tendremos pq no hemos lanzado la pregunta)
        if (ContextCompat.checkSelfPermission(getBaseContext(), permiso) != PackageManager.PERMISSION_GRANTED)
            if (shouldShowRequestPermissionRationale(permiso)) {
                Log.i("permisos", "El usuario ya denegó el permiso anteriormente");
                Snackbar.make(viewSnack, "Para registrate necesito la cámara", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(new String[]{permiso}, permisoRequest);
                            }
                        })
                        .show();
            } else
                requestPermissions(new String[]{permiso}, permisoRequest);
            // Si tenemos el permiso concedido , no tendremos en de la cámara
        else if (numPermisos != 2) {
            pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, viewSnack);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        SharedPreferences.Editor editor;

        switch (requestCode) {
            case PERMISO_CAMARA :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ++numPermisos;
                break;
            case PERMISO_ESCRIBIR_SD :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ++numPermisos;
                    pedirPermiso(Manifest.permission.CAMERA, PERMISO_CAMARA, new View(getBaseContext()));
                }
                break;
        }
        editor = prefs.edit();
        editor.putInt("numPermisos", numPermisos);
        editor.apply();
        if (numPermisos==2)
            goCamera(referenciasFire.get(Tablas.Imagenes.name()));
    }

    @Override
    public void goCamera(Map<String,Object> tipoBean) {
		Intent intent;
		Uri fileUri;

        this.tipoBean = tipoBean;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFile(); // create a file to save the image
		this.fileUri = fileUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void goGaleria(Map<String, Object> tipoBean, ImageView terrat) {
        this.ivTerrat = terrat;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CAPTURE_GALLERY_ACTIVITY_REQUEST_CODE);
    }


}