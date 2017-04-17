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
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TerratSeleccionado_ViewBinding implements Unbinder {
  private TerratSeleccionado target;

  @UiThread
  public TerratSeleccionado_ViewBinding(TerratSeleccionado target, View source) {
    this.target = target;

    target.panoWidgetView = Utils.findRequiredViewAsType(source, R.id.vrTerrat, "field 'panoWidgetView'", VrPanoramaView.class);
    target.tvTitulo = Utils.findRequiredViewAsType(source, R.id.tvTitTerrat, "field 'tvTitulo'", TextView.class);
    target.ivAvatar = Utils.findRequiredViewAsType(source, R.id.ivAvatarTerrat, "field 'ivAvatar'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TerratSeleccionado target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.panoWidgetView = null;
    target.tvTitulo = null;
    target.ivAvatar = null;
  }
}
