// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Login_ViewBinding implements Unbinder {
  private Login target;

  private View view2131689688;

  @UiThread
  public Login_ViewBinding(final Login target, View source) {
    this.target = target;

    View view;
    target.ivAvatar = Utils.findRequiredViewAsType(source, R.id.ivAvatar, "field 'ivAvatar'", ImageView.class);
    target.layoutGoogle = Utils.findRequiredViewAsType(source, R.id.rlGoogle, "field 'layoutGoogle'", RelativeLayout.class);
    target.layoutFacebook = Utils.findRequiredViewAsType(source, R.id.rlFacebook, "field 'layoutFacebook'", RelativeLayout.class);
    target.btnFacebook = Utils.findRequiredViewAsType(source, R.id.sign_conectar_f, "field 'btnFacebook'", LoginButton.class);
    view = Utils.findRequiredView(source, R.id.btn_desconectar_g, "field 'signoutG' and method 'onClick'");
    target.signoutG = Utils.castView(view, R.id.btn_desconectar_g, "field 'signoutG'", Button.class);
    view2131689688 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mGoogleLoginButton = Utils.findRequiredViewAsType(source, R.id.sign_conectar_g, "field 'mGoogleLoginButton'", SignInButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Login target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivAvatar = null;
    target.layoutGoogle = null;
    target.layoutFacebook = null;
    target.btnFacebook = null;
    target.signoutG = null;
    target.mGoogleLoginButton = null;

    view2131689688.setOnClickListener(null);
    view2131689688 = null;
  }
}
