// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GaleriaRecyclerView_ViewBinding implements Unbinder {
  private GaleriaRecyclerView target;

  @UiThread
  public GaleriaRecyclerView_ViewBinding(GaleriaRecyclerView target, View source) {
    this.target = target;

    target.recView = Utils.findRequiredViewAsType(source, R.id.rvGaleria, "field 'recView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GaleriaRecyclerView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recView = null;
  }
}
