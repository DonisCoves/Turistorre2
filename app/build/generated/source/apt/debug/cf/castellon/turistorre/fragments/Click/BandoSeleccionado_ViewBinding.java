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

public class BandoSeleccionado_ViewBinding implements Unbinder {
  private BandoSeleccionado target;

  @UiThread
  public BandoSeleccionado_ViewBinding(BandoSeleccionado target, View source) {
    this.target = target;

    target.tvTitulo = Utils.findRequiredViewAsType(source, R.id.tvTitSelecBando, "field 'tvTitulo'", TextView.class);
    target.tvDescripcion = Utils.findRequiredViewAsType(source, R.id.tvDescSelecBando, "field 'tvDescripcion'", TextView.class);
    target.ivImagen = Utils.findRequiredViewAsType(source, R.id.ivSelecBando, "field 'ivImagen'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BandoSeleccionado target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvTitulo = null;
    target.tvDescripcion = null;
    target.ivImagen = null;
  }
}
