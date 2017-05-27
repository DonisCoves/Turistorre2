// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Click;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GaleriaPagina_ViewBinding implements Unbinder {
  private GaleriaPagina target;

  @UiThread
  public GaleriaPagina_ViewBinding(GaleriaPagina target, View source) {
    this.target = target;

    target.ivAvatar = Utils.findRequiredViewAsType(source, R.id.ivAvatar, "field 'ivAvatar'", ImageView.class);
    target.ivPortada = Utils.findRequiredViewAsType(source, R.id.ivPortada, "field 'ivPortada'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GaleriaPagina target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivAvatar = null;
    target.ivPortada = null;
  }
}
