// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Click;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RaconsPagina_ViewBinding implements Unbinder {
  private RaconsPagina target;

  @UiThread
  public RaconsPagina_ViewBinding(RaconsPagina target, View source) {
    this.target = target;

    target.tvTitulo = Utils.findRequiredViewAsType(source, R.id.tvTituloRacons, "field 'tvTitulo'", TextView.class);
    target.tvDescripcion = Utils.findRequiredViewAsType(source, R.id.tvDescripcionRacons, "field 'tvDescripcion'", TextView.class);
    target.ivPano = Utils.findRequiredViewAsType(source, R.id.ivRaco, "field 'ivPano'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RaconsPagina target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvTitulo = null;
    target.tvDescripcion = null;
    target.ivPano = null;
  }
}
