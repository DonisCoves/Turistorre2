package cf.castellon.turistorre.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static cf.castellon.turistorre.utils.Utils.*;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;
import static cf.castellon.turistorre.utils.Constantes.*;
import cf.castellon.turistorre.bean.UsuarioParcel;
import cf.castellon.turistorre.utils.MiAplicacion;
import cf.castellon.turistorre.bean.Proveedor;


public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginFragment";

    private Activity mActivity;
    private ProgressDialog mAuthProgressDialog;
    @Bind(R.id.ivAvatar) ImageView ivAvatar;
    private List<Proveedor> proveedores;
    SharedPreferences.Editor editor;
    @Bind(R.id.etEmail) EditText email;
    @Bind(R.id.etPassword) EditText password;
    @Bind(R.id.btDesconectar) Button btnNativoDesc;
    @Bind(R.id.ll_login_nativo) LinearLayout layoutLoginNativo;
    @Bind(R.id.ll_registro_nativo) LinearLayout layoutRegistroNativo;
    String emailStr, passwordStr;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Bind(R.id.sign_conectar_f) LoginButton btnFacebook;
    private CallbackManager mFacebookCallbackManager;
    private AccessTokenTracker mFacebookAccessTokenTracker;/* Used to track user logging in/out off Facebook */
    @Bind(R.id.btn_desconectar_g) Button signoutG;
    @Bind(R.id.sign_conectar_g) SignInButton mGoogleLoginButton;
    UsuarioParcel usuario;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proveedores = new ArrayList<Proveedor>();
        mAuth = FirebaseAuth.getInstance();
        mApplication = (MiAplicacion) mActivity.getApplicationContext();
        //[2.- Facebook Callback, cuando cambie el acceso entramos]
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                onFacebookAccessTokenChange(currentAccessToken);
            }  //ARRIBA: Cuando detectamos que nos hemos conectado recogemos ese evento y se lo pasamos a Firebase para que gestione el registro
        };
        mAuthProgressDialog = new ProgressDialog(mActivity);
        mAuthProgressDialog.setTitle("Cargando");
        mAuthProgressDialog.setMessage("Autenticando con ServerTorres...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();
        //  Setup the Google API object to allow Google+ logins
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_web_id_str))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuthProgressDialog.hide();
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    mDataBaseUsersRef.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    usuario = dataSnapshot.getValue(UsuarioParcel.class);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "getUser:onCancelled", databaseError.toException());
                                }
                            });
                }
                if (mFirebaseUser != null && numProvs != mFirebaseUser.getProviderData().size()) {
                    numProvs = mFirebaseUser.getProviderData().size();
                    if (numProvs == 2) {//La primera vez lo guardamos en la bbdd de firebase
                        //Si es mediante email y password:
                        if (mFirebaseUser.getProviderData().get(0).getProviderId().equalsIgnoreCase("password") ||
                                mFirebaseUser.getProviderData().get(1).getProviderId().equalsIgnoreCase("password"))
                            crearUsuarioBBDDFire(mFirebaseUser.getUid(), mFirebaseUser.getEmail(),mFirebaseUser.getPhotoUrl().toString(), "multimedia");
                        else
                            crearUsuarioBBDDFire(mFirebaseUser.getUid(), mFirebaseUser.getDisplayName(), mFirebaseUser.getPhotoUrl().toString(), "multimedia");
                    }
                    setAuthenticatedUser(mFirebaseUser);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    updateUI(Tipo_Proveedor.NATIVO_INICIAL);
                }
            }
        };
    }

    @Override
    public void onStop() {
//          if (user!=null && user.isProveedor("google"))
//              mGoogleApiClient.disconnect();
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onStart() {
        if (mFirebaseUser != null && mFirebaseUser.getProviderId().equalsIgnoreCase("google.com"))
            mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) { //Google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                firebaseAuthWithGoogle(acct);
                //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//                updateUI(true);
            } else {
                // Signed out, show unauthenticated UI.
//                updateUI(null);
            }
        } else
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        mAuthProgressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        if (mFirebaseUser == null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                showErrorDialog(task.getException().getMessage());
                            }
                            mAuthProgressDialog.hide();
                        }
                    });
        } else {
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                showErrorDialog(task.getException().getMessage());
                            }
                            mAuthProgressDialog.hide();

                        }
                    });
        }
    }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {  //Cuando valimos en Facebook, hacemos el registro en Firebase, cuando este en Fire saltamos a AuthResultHandler.onAuthenticated*/
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuthProgressDialog.show();
            if (mFirebaseUser == null) {
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                                if (!task.isSuccessful())
                                    showErrorDialog(task.getException().getMessage());
                                mAuthProgressDialog.hide();
                            }
                        });
            } else {
                mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());
                                if (!task.isSuccessful()) {
                                    showErrorDialog(task.getException().getMessage());
                                    LoginManager.getInstance().logOut();
                                }
                                mAuthProgressDialog.hide();
                            }
                        });
            }
        } else
            logout("facebook.com");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view;
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.login_layout, container, false);
        ButterKnife.bind(this, view);
        // [3.-FACEBOOK LoginButton Permisos]
        btnFacebook.setReadPermissions("public_profile email");
        // [END 3.-FACEBOOK LoginButton Permisos]
        // [4.-FACEBOOK LoginButton If using in a fragment]
        btnFacebook.setFragment(this);
        // [END4.-FACEBOOK LoginButton If using in a fragment]
            /* *************************************
         *               GOOGLE                *
         ***************************************/
        /* Load the Google login button */
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Intentando conectar con Google API");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        if (mFirebaseUser != null) {
            if (isProvider("password"))
                updateUI(Tipo_Proveedor.NATIVO_DESCONECTAR);
            if (isProvider("google.com"))
                updateUI(Tipo_Proveedor.DESCONECTAR_GOOGLE);
        } else
            updateUI(Tipo_Proveedor.NATIVO_INICIAL);
        return view;
    }

    private void setAuthenticatedUser(FirebaseUser user) {
        if (user != null)
            for (UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals("google.com"))
                    updateUI(Tipo_Proveedor.DESCONECTAR_GOOGLE);
                else if (profile.getProviderId().equals("facebook.com"))
                    updateUI(Tipo_Proveedor.DESCONECTAR_FACEBOOK);
                else if (profile.getProviderId().equals("password"))
                    updateUI(Tipo_Proveedor.NATIVO_DESCONECTAR);
            }
    }

    /**
     * Mostramos los errores
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(mActivity)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick({R.id.btnCrearCuenta, R.id.btRegistrar, R.id.btDesconectar, R.id.btn_desconectar_g})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCrearCuenta:
                signInPassword();
                break;
            case R.id.btRegistrar:
                signInRegistrar();  //createUser
                break;
            case R.id.btDesconectar:
                logout("password");
                break;
            case R.id.btn_desconectar_g:
                logout("google.com");
                break;
        }
    }

    private void signInPassword() {
        emailStr = prefs.getString("email", "");
        passwordStr = prefs.getString("password", "");
        if (emailStr.isEmpty() || passwordStr.isEmpty()) //si son vacios primera vez, registramos
            updateUI(Tipo_Proveedor.NATIVO_REGISTRAR);
        else // Ya nos hemos registrado alguna vez
            signInNativo();
    }

    private void logout(final String proveedor) {
        eliminarProveedor(proveedor);
        if (proveedor.equals("facebook.com")) {
            LoginManager.getInstance().logOut();
            updateAvatar();
        } else if (proveedor.equals("google.com")) {
            updateUI(Tipo_Proveedor.CONECTAR_GOOGLE);
            mGoogleApiClient.disconnect();
        } else if (proveedor.equals("password")) {
            updateUI(Tipo_Proveedor.NATIVO_INICIAL);
            /*email.setText("");
            password.setText("");*/
        }
    }

    /***
     * Eliminamos proveedor o desconectamos el usuario si ya no tiene mas proveedores
     * @param proveedor
     */
    private void eliminarProveedor(final String proveedor) {
        if (mAuth.getCurrentUser().getProviderData().size() < 3) {  // Si solo hay 2 (Firebase y otro) no hemos hecho link
            mDataBaseUsersRef.child(mFirebaseUser.getUid()).setValue(null);
            mDataBaseGruposRef.child("Multimedia").child(mFirebaseUser.getUid()).setValue(null);
            mFirebaseUser.delete();
            FirebaseAuth.getInstance().signOut();
            numProvs = 0;
        } else {
            numProvs--;
            mAuth.getCurrentUser().unlink(proveedor).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "unlink: " + proveedor);
                    if (!task.isSuccessful())
                        showErrorDialog(task.getException().getMessage());
                    mAuthProgressDialog.hide();
                }
            });
        }
    }

    private void signInRegistrar() {
        mAuthProgressDialog.show();
        editor = prefs.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.commit();
        if (mFirebaseUser == null) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(mActivity, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mAuthProgressDialog.hide();
                            Log.d(TAG, "createUserWithEmailAndPassword.onComplete");
                        }
                    })
                    .addOnFailureListener(mActivity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mAuthProgressDialog.hide();
                            showErrorDialog(e.getMessage());
                        }
                    });
        } else {
            AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString(), password.getText().toString());
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful())
                                showErrorDialog(task.getException().getMessage());
                            mAuthProgressDialog.hide();
                        }
                    });
        }
    }

    private void signInNativo() {
        mAuthProgressDialog.show();
        if (mFirebaseUser == null) {
            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmailAndPassword.onComplete");
                            if (!task.isSuccessful())
                                showErrorDialog(task.getException().getMessage());
                            mAuthProgressDialog.hide();
                        }
                    });
        } else {
            AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString(), password.getText().toString());
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "linkWithCredential:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful())
                                showErrorDialog(task.getException().getMessage());
                            mAuthProgressDialog.hide();
                        }
                    });
        }
    }

    private void updateUI(Tipo_Proveedor proveedor) {
        switch (proveedor) {
            case NATIVO_INICIAL:
                layoutLoginNativo.setVisibility(View.VISIBLE);
                layoutRegistroNativo.setVisibility(View.GONE);
                btnNativoDesc.setVisibility(View.GONE);
                break;
            case NATIVO_REGISTRAR:
                layoutLoginNativo.setVisibility(View.GONE);
                layoutRegistroNativo.setVisibility(View.VISIBLE);
                btnNativoDesc.setVisibility(View.GONE);
                break;
            case NATIVO_DESCONECTAR:
                layoutLoginNativo.setVisibility(View.GONE);
                layoutRegistroNativo.setVisibility(View.GONE);
                btnNativoDesc.setVisibility(View.VISIBLE);
                break;
            case DESCONECTAR_FACEBOOK:  //Ya estamos conectados
                break;
            case DESCONECTAR_GOOGLE:
                mGoogleLoginButton.setVisibility(View.GONE);
                signoutG.setVisibility(View.VISIBLE);
                break;
            case CONECTAR_GOOGLE:
                mGoogleLoginButton.setVisibility(View.VISIBLE);
                signoutG.setVisibility(View.GONE);
                break;
        }
        updateAvatar();
    }

    private void updateAvatar() {
        String url = "";
        if (mFirebaseUser != null) {
            if (isProvider("facebook.com"))
                url = getUrl("facebook.com");
            else if (isProvider("google.com"))
                url = getUrl("google.com");
            else //Nativo
                Glide.with(mActivity).load(R.drawable.escudo).into(ivAvatar);
            Glide.with(mActivity).load(url).into(ivAvatar);
        } else {
            Glide.with(mActivity).load(R.drawable.escudo).into(ivAvatar);
        }

//        Picasso.with(mActivity).setIndicatorsEnabled(true);
//        Picasso.with(mActivity).load(url).error(R.drawable.error).fit().transform(new RoundedTransformation()).into(imageView);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed:" + result);
        Toast.makeText(mActivity, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CUENTAS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permiso concedido");
//                    getGoogleOAuthTokenAndLogin();
                } else
                    Log.i(TAG, "Permiso denegado");
            }
        }
    }

    private void crearUsuarioBBDDFire(final String userId, final String nombre, final String avatar, final String grupo) {
        mAuthProgressDialog.show();//No me aparece el dialogo
        usuario = new UsuarioParcel(nombre, avatar, userId, grupo);
        mDataBaseUsersRef.child(userId).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuthProgressDialog.hide();
                mDataBaseGruposRef.child(grupo).child(userId).setValue(usuario);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mAuthProgressDialog.hide();
                showErrorDialog(e.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_login, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.it_permisos):
                FragmentTransaction transaccion = getFragmentManager().beginTransaction();
                Permisos permisos = new Permisos();
                Bundle bundle = new Bundle();
                if (mFirebaseUser != null)
                    bundle.putString("GRUPO", usuario.getGrupo());
                else
                    bundle.putString("GRUPO", "anonimo");
                permisos.setArguments(bundle);
                transaccion.replace(R.id.content_frame, permisos).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}