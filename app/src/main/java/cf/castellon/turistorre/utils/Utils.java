package cf.castellon.turistorre.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import cf.castellon.turistorre.R;
import cf.castellon.turistorre.bean.DiaFiesta;
import cf.castellon.turistorre.bean.DiaFiestaMeta;
import cf.castellon.turistorre.bean.Fiestas;
import cf.castellon.turistorre.bean.Imagen;
import cf.castellon.turistorre.bean.Panoramica;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.bean.UsuarioParcel;

import static cf.castellon.turistorre.utils.Constantes.*;

/**
 * Utiles sin necesidad de iniciar la clase (objeto)
 */
public final class Utils {
    public static ProgressDialog mProgressDialog;
    public static String seccion;
    public static MiAplicacion mApplication;
    public static SharedPreferences prefs;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    public static boolean mGoogleLoginClicked;
    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    public static boolean mGoogleIntentInProgress;
    /* Client used to interact with Google APIs. */
    public static GoogleApiClient mGoogleApiClient;
    //Lista para actualizar los logins(enabled,disabled)
    public static List<Boolean> estados = new ArrayList<Boolean>(6);
    //Cuando tengamos todos los datos de la imagen (las 2 url i el id usuario) la guardamos en la bbdd
    public static boolean datosImagenOk;
    public static Uri fileUri;
    public static boolean colorCambiado;
    public static String tokenFireBase;
    public static UsuarioParcel usuario;

    /**
     * GALERIAFRAGMENT
     */
    //Variable que utilizaremos para seguir con la app si tenemos todos los permisos
    public static int numPermisos;
    public static int contImg; //Numero de imagenes introducidas por el usuario
    private static String rutaImgUser;
    public static List<Imagen> imagenes = new ArrayList<>();
    public static List<Panoramica> panoramicas = new ArrayList<>();

    /********************************************
     * FIESTAS
     *****************************************/

    public static Map<String, Fiestas> fiestasMap = new HashMap<String, Fiestas>();
    public static Map<String, DiaFiesta> diasFiestas = new HashMap<String, DiaFiesta>();
    public static Map<String, DiaFiesta> diasFiestasSel = new HashMap<String, DiaFiesta>(); //De la fiesta en particular
    public static Map<String, DiaFiestaMeta> diasFiestasMeta = new HashMap<String, DiaFiestaMeta>();
    public static long tiempoInicialWeb, tiempoDatosRecogidos, tiempoEspera;

    /** METODOS FIESTAS **/

    /**
     * @return la lista de los titulos de las fiestas
     */
    //Extrae los uids de FireBase del nodo fiestas(el uidFiestas)
    public static List<String> getUidsFiestas() {
        List<String> titulos = new ArrayList<String>();
        for (String titulo : fiestasMap.keySet()) {
            titulos.add(titulo);
        }
        if (titulos.isEmpty())
            return null;
        else
            return titulos;
    }


    public static Date formatearDia(String tituloDiaFiesta, String key) {
        int dia;
        String mes, anyo, diat;
        String diaFormateado;
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd MMMM yyyy");
        Date fecha = null;
        anyo = key.substring(key.length() - 4);
        StringTokenizer tokens = new StringTokenizer(tituloDiaFiesta, " ");
        tokens.nextToken(); //Domingo
//        dia= Integer.parseInt(tokens.nextToken()); //1
        diat = tokens.nextToken();
        tokens.nextToken(); //de
        mes = tokens.nextToken(); //enero
//        diaFormateado = String.format("%d %s %d", dia, mes, Integer.parseInt(anyo));  //1 enero 2017
        diaFormateado = String.format("%s %s %d", diat, mes, Integer.parseInt(anyo));  //1 enero 2017
        try {
            fecha = formatoDelTexto.parse(diaFormateado);
        } catch (ParseException ex) {

            ex.printStackTrace();
        }
        return fecha;
    }


    /**
     * Metodo de ordenacion Buble Sort
     *
     * @param fechasDia
     *//*

    public static void burbuja(List<DiaFiestaMetaFecha> fechasDia) {
        DiaFiestaMetaFecha temp ;

        for (int i = 1; i < fechasDia.size(); i++) {
            for (int j = 0; j < fechasDia.size() - i; j++)
            {
                if (fechasDia.get(j).getFecha().after(fechasDia.get(j + 1).getFecha()))
                {
                    temp = fechasDia.get(j);
                    fechasDia.set(j, fechasDia.get(j + 1));
                    fechasDia.set(j + 1, temp);
                }
            }
        }
    }
*/

    /**
     * Dado un id de un usuario nos devuelve todos sus datos (Siempre existe)
     *
     * @param uidUser
     * @return
     */
    public static Usuario buscarUsuario(String uidUser) {

        Usuario usuario = null;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            usuario = usuarios.get(i++);
            if (usuario.getUidUser().equals(uidUser))
                encontrado = true;
        }
        return usuario;
    }

    public static UsuarioParcel buscarUsuarioParcel(String uidUser) {

        UsuarioParcel usuario = null;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            usuario = usuariosParcel.get(i++);
            if (usuario.getUidUser().equals(uidUser))
                encontrado = true;
        }
        return usuario;
    }

    //Dado un id nos devuelve si existe
    public static boolean existeUsuario(String uidUser) {
        boolean encontrado = false;
        Usuario usuario = null;
        int i = 0;

        while (!encontrado) {
            usuario = usuarios.get(i++);
            if (usuario.getUidUser().equals(uidUser))
                encontrado = true;
        }
        return encontrado;
    }

    public static void anyadirUsuario(String uidUser) {
        mDataBaseUsersRef.child(uidUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.add(dataSnapshot.getValue(Usuario.class));
                usuariosParcel.add(dataSnapshot.getValue(UsuarioParcel.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public static void showProgressDialog(Context context, String mensaje) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(mensaje + " ...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static void goCamera(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFile(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        fragment.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * CARRUSELGALERIAFRAGMENT
     */


    @Nullable
    public static Imagen buscarImagen(String uidImagen) {
        for (Imagen img : imagenes)
            if (img.getUidImg().equals(uidImagen))
                return img;
        return null;
    }

    public static void crearImagenes() {
        mDataBaseImgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Imagen imagen = postSnapshot.getValue(Imagen.class);
                    imagenes.add(imagen);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void crearUsuarios() {
        mDataBaseUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = postSnapshot.getValue(Usuario.class);
                    UsuarioParcel usuarioParcel = postSnapshot.getValue(UsuarioParcel.class);
                    usuarios.add(usuario);
                    usuariosParcel.add(usuarioParcel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Nullable
    public static Panoramica buscarPanoramica(String uidPanoramica) {
        for (Panoramica pano : panoramicas)
            if (pano.getUidImg().equals(uidPanoramica))
                return pano;
        return null;
    }

    /*public static ArrayList<String> getIdsImagenes(){


        mDataBaseImgRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap map = (HashMap)dataSnapshot.getValue();
                        List ids = new ArrayList (map.keySet());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }
*/

    /**
     * Listado con todos los datos de las imagenes guardadas
     */


    /**
     * Dado un id de usuario devuelve la ruta(referencia) donde tiene almacenadas las imagenes
     * @param id
     * @return
     */
    /*public static String getPathUserStorage(final String id){
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario user = dataSnapshot.getValue(Usuario.class);
                rutaImgUser = user.nombre + " - " + id;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("sfsdf", "loadPost:onCancelled", databaseError.toException());
            }
        });


        return rutaImgUser;
    }*/


    /**
     * LOGINFRAGMENT
     */
    public static FirebaseUser mFirebaseUser;
    public static int numProvs = 0;
    public static FirebaseAuth mAuth;/* Datos desde el usuario autenticado*/
    public static List<Usuario> usuarios;
    public static List<UsuarioParcel> usuariosParcel;

    /**
     * Al reves porque el ultimo proveedor en regisrarse estara al final y para eliminarlo no nos causara problemas el avatar
     *
     * @param providerId
     * @return
     */
    public static boolean isProvider(String providerId) {
        for (int i = mFirebaseUser.getProviderData().size()-1; i > 0; i--) {
            UserInfo profile = mFirebaseUser.getProviderData().get(i);
            if (profile.getProviderId().equals(providerId))
                return true;
        }
        return false;
    }
        /*for (UserInfo profile : mFirebaseUser.getProviderData())
            if (profile.getProviderId().equals(providerId))
                return true;
        return false;
    }*/

    /**
     * @param providerId
     * @return
     */

    public static String getUrl(String providerId) {
        String url = "";
        for (UserInfo profile : mFirebaseUser.getProviderData())
            if (profile.getProviderId().equals(providerId))
                url = profile.getPhotoUrl().toString();
        return url;
    }

    //  **************                  BANDOFRAGMENT **************************


    /**
     * Dada la ruta de una imagen existente, crea otra en la misma ubicacion cambiando su tamaño (w,h) y lo devuelve un array de bytes para poderlo subir a FireBaseStorage
     *
     * @param pathUri
     * @param w
     * @param h
     * @return
     */

    public static byte[] escalarImagen(Uri pathUri, int w, int h) {
        byte[] data;
//        1.- Crear un bitmap desde una uri existente
        Bitmap bitmap = BitmapFactory.decodeFile(pathUri.getPath());
//        2.-Escalar bitmap
        int width = bitmap.getWidth(); //2340
        int height = bitmap.getHeight(); //4160
        int newWidth = w;
        int newHeight = h;
        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // para poder manipular la imagen  debemos crear una matriz
        Matrix matrix = new Matrix();
        matrix.postScale(1 / 4f, 1 / 4f);
        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//        3.- Bitmap a byte []
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            data = baos.toByteArray();
            baos.flush();
            baos.close();
        } catch (Exception e) {
            data = null;
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] escalarImagen(Uri pathUri, float escalado) {
        byte[] data;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathUri.getPath());

        } catch (OutOfMemoryError er) { //Si la imagen es muy grande
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = BitmapFactory.decodeFile(pathUri.getPath(), options);
        } catch (Exception e) {
            Log.e("sds", e.getMessage());
        }
        int width = bitmap.getWidth(); //2340
        int height = bitmap.getHeight(); //4160
        Matrix matrix = new Matrix();
        /*if (height > width)
            matrix.postRotate(90);*/
        matrix.postScale(escalado, escalado);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            data = baos.toByteArray();
            baos.flush();
            baos.close();
        } catch (Exception e) {
            data = null;
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] escalarPanoramica(Uri pathUri) {
        byte[] data;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathUri.getPath());

        } catch (OutOfMemoryError er) { //Si la imagen es muy grande
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = BitmapFactory.decodeFile(pathUri.getPath(), options);
        } catch (Exception e) {
            Log.e("sds", e.getMessage());
        }
        int width = bitmap.getWidth(); //2340
        int height = bitmap.getHeight(); //4160
        Matrix matrix = new Matrix();
        if (height > width)
            matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            data = baos.toByteArray();
            baos.flush();
            baos.close();
        } catch (Exception e) {
            data = null;
            e.printStackTrace();
        }
        return data;
    }

    public static Uri getOutputMediaFile() {
        File mediaFile;
        File mediaStorageDir;
        String timeStamp;

        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Turisme");
        mediaStorageDir.mkdirs();
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }

    /**
     * Le pasamos un conjunto de vistas y una lista de booleans y establece la visibilidad en funcion de la lista
     */
    public static final ButterKnife.Setter<View, List<Boolean>> UPDATEVIEW = new ButterKnife.Setter<View, List<Boolean>>() {
        @Override
        public void set(View view, List<Boolean> value, int index) {
            if (value.get(index))
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }
    };

    //A partir de 6.0 hay que pedir los permisos "peligrosos" en tiempo de ejecucion

    /**
     * @param activity
     * @param permiso        el que queremos solicitar para su aprobacion
     * @param permisoRequest . para luego en el callback onRequestPermissionsResult para tratarlo
     * @param viewSnack      . La vista sobre la que asentaremos la snack
     */
    public static void pedirPermiso(final Activity activity, final String permiso, final int permisoRequest, View viewSnack) {
        // Si no tenemos el permiso:(la primera vez no lo tendremos pq no hemos lanzado la pregunta)
        if (ContextCompat.checkSelfPermission(activity, permiso) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permiso)) {
                Log.i("permisos", "El usuario ya denegó el permiso anteriormente");
                Snackbar.make(viewSnack, "Para registrate necesito la cámara", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(activity, new String[]{permiso}, permisoRequest);
                            }
                        })
                        .show();
            } else
                ActivityCompat.requestPermissions(activity, new String[]{permiso}, permisoRequest);
            // Si tenemos el permiso concedido
        else
            numPermisos = 2;
    }

    public static void pedirPermiso2(final Fragment fragment, final String permiso, final int permisoRequest, View viewSnack) {
        // Si no tenemos el permiso:(la primera vez no lo tendremos pq no hemos lanzado la pregunta)
        if (ContextCompat.checkSelfPermission(fragment.getContext(), permiso) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), permiso)) {
                Log.i("permisos", "El usuario ya denegó el permiso anteriormente");
                Snackbar.make(viewSnack, "Para registrate necesito la cámara", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{permiso}, permisoRequest);
                                fragment.requestPermissions(new String[]{permiso}, permisoRequest);
                            }
                        })
                        .show();
            } else
                fragment.requestPermissions(new String[]{permiso}, permisoRequest);
//                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{permiso}, permisoRequest);
        // Si tenemos el permiso concedido

    }


    public static void pedirPermiso3(final Fragment fragment, final String[] permisos, final int[] permisosRequest, View viewSnack) {
        // Si no tenemos el permiso:(la primera vez no lo tendremos pq no hemos lanzado la pregunta)
        for (int i = 0; i < permisos.length; i++) {
            if (ContextCompat.checkSelfPermission(fragment.getContext(), permisos[i]) != PackageManager.PERMISSION_GRANTED)
                if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), permisos[i])) {
                    Log.i("permisos", "El usuario ya denegó el permiso anteriormente");
                    Snackbar.make(viewSnack, "Necesito el permiso", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                ActivityCompat.requestPermissions(fragment.getActivity(), new String[]{permiso}, permisoRequest);
                                    int j = 0;
                                    fragment.requestPermissions(new String[]{permisos[j++]}, permisosRequest[j++]);
                                }
                            })
                            .show();
                } else
                    fragment.requestPermissions(new String[]{permisos[i]}, permisosRequest[i]);
            else {
                numPermisos++;
                if (numPermisos == 2)
                    goCamera(fragment);
            }
        }
    }

   /* public static void verifyStoragePermissions(final Activity activity, View viewSnack) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("permisos", "El usuario ya denegó el permiso anteriormente");
                Snackbar.make(viewSnack, "Para registrate necesito poder escribir en la sd", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,PERMISO_ESCRIBIR_SD);
                            }
                        })
                        .show();
            } else
                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,PERMISO_ESCRIBIR_SD);
        //else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        if (numPermisos==1)  //Si entramos aquí tenemos solo el de escritura y faltará el de la cámara
            pedirPermiso(activity, Manifest.permission.CAMERA, PERMISO_CAMARA, viewSnack);
    }*/

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private static void logSd(File mediaStorageDir, Activity m) {
        Log.d("MainActivity", ">> Let's debug why this directory isn't being created: ");
        Log.d("MainActivity", "Is it working?: " + mediaStorageDir.mkdirs());
        Log.d("MainActivity", "Is it available?: " + isExternalStorageAvailable());
        Log.d("MainActivity", "Does it exist?: " + mediaStorageDir.exists());
        Log.d("MainActivity", "What is the full URI?: " + mediaStorageDir.toURI());
        Log.d("MainActivity", "--");
        Log.d("MainActivity", "Can we write to this file?: " + mediaStorageDir.canWrite());
        if (!mediaStorageDir.canWrite()) {
            Log.d("MainActivity", ">> We can't write! Do we have WRITE_EXTERNAL_STORAGE permission?");
            if (m.getBaseContext().checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED) {
                Log.d("MainActivity", ">> We don't have permission to write - please add it.");
            } else {
                Log.d("MainActivity", "We do have permission - the problem lies elsewhere.");
            }
        }
        Log.d("MainActivity", "Are we even allowed to read this file?: " + mediaStorageDir.canRead());
        Log.d("MainActivity", "--");
        Log.d("MainActivity", ">> End of debugging.");
    }

    /**
     * GLOBAL
     */

    public static String obtenerFecha() {
        int dia, diaSemana, mes, anyo;
        String nombreMes, nombreDiaSemana, salida;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        dia = cal.get(Calendar.DAY_OF_MONTH);
        diaSemana = cal.get(Calendar.DAY_OF_WEEK);
        mes = cal.get(Calendar.MONTH);
        anyo = cal.get(Calendar.YEAR);

        cal.set(Calendar.MONTH, mes);
        nombreMes = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es", "ES"));
        nombreMes = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1); //primera letra en mayuscula

        cal.set(Calendar.DAY_OF_WEEK, diaSemana);
        nombreDiaSemana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("es", "ES"));
        nombreDiaSemana = nombreDiaSemana.substring(0, 1).toUpperCase() + nombreDiaSemana.substring(1);

        salida = String.format("%s %d de %s del %d", nombreDiaSemana, dia, nombreMes, anyo);  //Domingo 27 de Noviembre del 2016
        return salida;
    }

    public static void cambiarColor(Context context, View view) {
        GradientDrawable gradient = ((GradientDrawable) view.getBackground());
        if (!colorCambiado)
            gradient.setColor(context.getResources().getColor(R.color.verde_claro));
        else
            gradient.setColor(context.getResources().getColor(R.color.amarillo_claro));
        colorCambiado = !colorCambiado;
    }

    public static void cambiarColorFondoTV(View view) {
        if (!colorCambiado)
            view.setBackgroundColor(view.getResources().getColor(R.color.verde_claro));
        else
            view.setBackgroundColor(view.getResources().getColor(R.color.amarillo_claro));
        colorCambiado = !colorCambiado;
    }


    /**
     * Como el server esta en heroku y tenenemos un dyno free lo tendremos que activar
     * sino no se envia la notifificacion
     * Esta opcion se quitará si algun dia pagamos para tener un dyno que no se duerma
     *
     * @param context
     */
    public static void activarServer(Context context) {
        RequestQueue requestQueue;
        StringRequest mStringRequest;

        requestQueue = Volley.newRequestQueue(context);
        mStringRequest = new StringRequest(Request.Method.GET, HEROKU_URL, null, null);
        requestQueue.add(mStringRequest);
    }

    /***
     * Envia una notificacion de que se ha creado un bando
     *
     * @param
     */
   /* public static void activarNotificacionBando(final String titulo, final String fecha, final String refBando, final Context context){
        RequestQueue requestQueue;
        StringRequest mStringRequest;

        requestQueue= Volley.newRequestQueue(context);
        mStringRequest  = new StringRequest(Request.Method.GET, HEROKU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {  // No esta dormido
                        generarNotificacionBando(titulo,fecha,refBando);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  // Esta dormido, lo activamos y volvemos a lanzar
                activarNotificacionBando(titulo, fecha,refBando,context);
            }
        });*/

        /*requestQueue= Volley.newRequestQueue(context);
        mStringRequest  = new StringRequest(Request.Method.GET, HEROKU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {  // No esta dormido
                        generarNotificacionBando(titulo,fecha,refBando);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  // Esta dormido
                generarNotificacionBando(titulo,fecha,refBando);
            }
        });*/

//        requestQueue.add(mStringRequest);

//    }
    public static void generarNotificacionBando(String titulo, String fecha, String refBando) {
        FirebaseMessaging fm;
        Random random;

        random = new Random();
        fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(ID_REMITENTE + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(random.nextInt()))
                .addData("titulo", titulo)
                .addData("fecha", fecha)
                .addData("uidBando", refBando)
                .addData("action", ACTION_GPO_BANDO)
                .build());
    }

    public static void generarNotificacionTerrat(String direccion, String uidTerrat) {
        FirebaseMessaging fm;
        Random random;

        random = new Random();
        fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(ID_REMITENTE + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(random.nextInt()))
                .addData("direccion", direccion)
                .addData("uidTerrat", uidTerrat)
                .addData("action", ACTION_GPO_TERRAT)
                .build());
    }

    public static void establecerPreferenciasIniciales(Context context) {
        prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        int notificacionesPrefs = prefs.getInt("notificaciones", TODAS);
        switch (notificacionesPrefs) {
            case TODAS:
                FirebaseMessaging.getInstance().subscribeToTopic("Bando");
                FirebaseMessaging.getInstance().subscribeToTopic("Terrat");
                FirebaseMessaging.getInstance().subscribeToTopic("Imagenes");
                break;
            case BANDOS:
                FirebaseMessaging.getInstance().subscribeToTopic("Bando");
                break;
            case TERRATS:
                FirebaseMessaging.getInstance().subscribeToTopic("Terrat");
                break;
            case IMAGENES:
                FirebaseMessaging.getInstance().subscribeToTopic("Imagenes");
                break;
            case NINGUNA:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Bando");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Terrat");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Imagenes");
                break;
        }
    }


}
