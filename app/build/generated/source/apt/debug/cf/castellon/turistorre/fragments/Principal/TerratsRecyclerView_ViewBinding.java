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

public class TerratsRecyclerView_ViewBinding implements Unbinder {
  private TerratsRecyclerView target;

  @UiThread
  public TerratsRecyclerView_ViewBinding(TerratsRecyclerView target, View source) {
    this.target = target;

    target.recView = Utils.findRequiredViewAsType(source, R.id.rvTerrats, "field 'recView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TerratsRecyclerView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recView = null;
  }
}
