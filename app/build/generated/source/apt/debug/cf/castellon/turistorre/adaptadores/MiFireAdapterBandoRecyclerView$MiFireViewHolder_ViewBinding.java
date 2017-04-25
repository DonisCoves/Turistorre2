// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.adaptadores;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MiFireAdapterBandoRecyclerView$MiFireViewHolder_ViewBinding implements Unbinder {
  private MiFireAdapterBandoRecyclerView.MiFireViewHolder target;

  @UiThread
  public MiFireAdapterBandoRecyclerView$MiFireViewHolder_ViewBinding(MiFireAdapterBandoRecyclerView.MiFireViewHolder target,
      View source) {
    this.target = target;

    target.tvTitulo = Utils.findRequiredViewAsType(source, R.id.tvTituloBando, "field 'tvTitulo'", TextView.class);
    target.tvFecha = Utils.findRequiredViewAsType(source, R.id.tvFecha, "field 'tvFecha'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MiFireAdapterBandoRecyclerView.MiFireViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvTitulo = null;
    target.tvFecha = null;
  }
}
