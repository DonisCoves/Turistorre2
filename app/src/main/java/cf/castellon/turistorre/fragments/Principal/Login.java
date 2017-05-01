
package cf.castellon.turistorre.fragments.Principal;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.castellon.turistorre.R;
import static cf.castellon.turistorre.utils.Constantes.*;
import cf.castellon.turistorre.bean.Usuario;
import cf.castellon.turistorre.fragments.ActionBar.Permisos;
import cf.castellon.turistorre.utils.MiAplicacion;

@SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
public class Login extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LoginFragment";

    private ProgressDialog mAuthProgressDialog;
    @BindView(R.id.ivAvatar) ImageView ivAvatar;
    SharedPreferences.Editor editor;
    @BindView(R.id.etEmail) EditText email;
    @BindView(R.id.etPassword) EditText password;
    @BindView(R.id.btDesconectar) Button btnNativoDesc;
    @BindView(R.id.ll_login_nativo) LinearLayout layoutLoginNativo;
    @BindView(R.id.ll_registro_nativo) LinearLayout layoutRegistroNativo;
    String emailStr, passwordStr;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.sign_conectar_f) LoginButton btnFacebook;
    private CallbackManager mFacebookCallbackManager;
    @BindView(R.id.btn_desconectar_g) Button signoutG;
    @BindView(R.id.sign_conectar_g) SignInButton mGoogleLoginButton;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = getActivity();
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mApplication = (MiAplicacion) mActivity.getApplicationContext();
        //[2.- Facebook Callback, cuando cambie el acceso entramos]
        mFacebookCallbackManager = CallbackManager.Factory.create();
        new AccessTokenTracker() {
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
        if (mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuthProgressDialog.hide();
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null && numProvs != mFirebaseUser.getProviderData().size()) {
                    numProvs = mFirebaseUser.getProviderData().size();
                    if (numProvs == 2) {//La primera vez lo guardamos en la bbdd de firebase
                        //Si es mediante email y password:
                        if (mFirebaseUser.getPhotoUrl()!=null)
                            if (mFirebaseUser.getProviderData().get(0).getProviderId().equalsIgnoreCase("password") ||
                                    mFirebaseUser.getProviderData().get(1).getProviderId().equalsIgnoreCase("password"))
                                crearUsuarioBBDDFire(mFirebaseUser.getEmail(), mFirebaseUser.getEmail(),mFirebaseUser.getEmail(), mFirebaseUser.getPhotoUrl().toString(), "multimedia");
                            else
                                crearUsuarioBBDDFire(mFirebaseUser.getEmail(), mFirebaseUser.getDisplayName(),mFirebaseUser.getEmail(), mFirebaseUser.getPhotoUrl().toString(), "multimedia");
                        else
                            showError(getActivity(),getClass().getName(),"mFirebaseUser.getPhotoUrl().toString()","El metodo es nulo");
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
        super.onStop();
        if (mFirebaseUser!=null) {
            mGoogleApiClient.disconnect();
        }
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
        mAuthProgressDialog.dismiss();
    }

    @Override
    public void onStart() {
        if (mFirebaseUser != null) //&& mFirebaseUser.getProviderId().equalsIgnoreCase("google.com"))
            mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_FACEBOOK ){ //
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                firebaseAuthWithGoogle(acct);
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
                            if (!task.isSuccessful())
                                showError(getContext(),getClass().getName(),task.getException().getStackTrace()[0].getMethodName(),task.getException().getMessage());
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
                                showError(getContext(),getClass().getName(),task.getException().getStackTrace()[0].getMethodName(),task.getException().getMessage());
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
        if (token != null) {  //Cuando valimos en Facebook, hacemos el registro en Firebase, cuando este en Fire saltamos a AuthResultHandler.onAuthenticated*//*

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuthProgressDialog.show();
            if (mFirebaseUser == null) {
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                                if (!task.isSuccessful())
                                    showError(getContext(),getClass().getName(),task.getException().getStackTrace()[0].getMethodName(),task.getException().getMessage());
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
                                    showError(getContext(),getClass().getName(),task.getException().getStackTrace()[0].getMethodName(),task.getException().getMessage());
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
        btnFacebook.setReadPermissions("email");
        // [END 3.-FACEBOOK LoginButton Permisos]
        // [4.-FACEBOOK LoginButton If using in a fragment]
        //btnFacebook.setFragment(this);
        // [END4.-FACEBOOK LoginButton If using in a fragment]

/* *************************************
         *               GOOGLE                *
         ***************************************//*


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
        switch (proveedor) {
            case "facebook.com":
                LoginManager.getInstance().logOut();
                updateAvatar();
                break;
            case "google.com":
                updateUI(Tipo_Proveedor.CONECTAR_GOOGLE);
                mGoogleApiClient.disconnect();
                break;
            case "password":
                updateUI(Tipo_Proveedor.NATIVO_INICIAL);
                break;
        }
    }


/***
     * Eliminamos proveedor o desconectamos el usuario si ya no tiene mas proveedores
     * @param proveedor el proveedor
     */

    private void eliminarProveedor(final String proveedor) {
        if (mAuth.getCurrentUser().getProviderData().size() < 3) {  // Si solo hay 2 (Firebase y otro) no hemos hecho link
            //mDataBaseUsersRef.child(mFirebaseUser.getUid()).setValue(null);
            mDataBaseGruposRef.child("Multimedia").child(mFirebaseUser.getUid()).setValue(null);
            mFirebaseUser.delete();
            FirebaseAuth.getInstance().signOut();
            usuario=null;
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
        editor.apply();
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        showError(getContext(),getClass().getName(),result.getClass().getName(),result.getErrorMessage());
     }

    private void crearUsuarioBBDDFire(final String userId, final String nombre,final String email, final String avatar, final String grupo) {
        mAuthProgressDialog.show();//No me aparece el dialogo
        editor = prefs.edit();
        editor.putString("uidUser", userId);
        editor.apply();

        if (nombre.equalsIgnoreCase("TurisTorre Turistorre")){
            usuario = new Usuario(nombre, avatar, email,  userId, "administrador");
            FirebaseMessaging.getInstance().subscribeToTopic("administrador");
        }
        else
            usuario = new Usuario(nombre, avatar, email, userId, grupo);
        anyadirUsuario(usuario);
        mDataBaseUsersRef.child(parserId(userId)).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mAuthProgressDialog.hide();
                mDataBaseGruposRef.child(usuario.getGrupo()).child(parserId(userId)).setValue(usuario);
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
