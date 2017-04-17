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

public class BandoRecyclerView_ViewBinding implements Unbinder {
  private BandoRecyclerView target;

  @UiThread
  public BandoRecyclerView_ViewBinding(BandoRecyclerView target, View source) {
    this.target = target;

    target.rvBando = Utils.findRequiredViewAsType(source, R.id.rvBando, "field 'rvBando'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BandoRecyclerView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvBando = null;
  }
}
