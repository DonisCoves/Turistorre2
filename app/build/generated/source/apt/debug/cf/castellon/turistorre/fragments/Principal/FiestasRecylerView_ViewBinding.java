// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FiestasRecylerView_ViewBinding implements Unbinder {
  private FiestasRecylerView target;

  @UiThread
  public FiestasRecylerView_ViewBinding(FiestasRecylerView target, View source) {
    this.target = target;

    target.spFiestas = Utils.findRequiredViewAsType(source, R.id.spFiestas, "field 'spFiestas'", Spinner.class);
    target.recView = Utils.findRequiredViewAsType(source, R.id.rvFiestasDias, "field 'recView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FiestasRecylerView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.spFiestas = null;
    target.recView = null;
  }
}
