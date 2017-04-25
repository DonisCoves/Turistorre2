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

public class MyFireAdapterEventosRecyclerView$MyFireViewHolder_ViewBinding implements Unbinder {
  private MyFireAdapterEventosRecyclerView.MyFireViewHolder target;

  @UiThread
  public MyFireAdapterEventosRecyclerView$MyFireViewHolder_ViewBinding(MyFireAdapterEventosRecyclerView.MyFireViewHolder target,
      View source) {
    this.target = target;

    target.horaTv = Utils.findRequiredViewAsType(source, R.id.tvTituloEvento, "field 'horaTv'", TextView.class);
    target.tituloTv = Utils.findRequiredViewAsType(source, R.id.tvHora, "field 'tituloTv'", TextView.class);
    target.descripcionTv = Utils.findRequiredViewAsType(source, R.id.tvDescripcion, "field 'descripcionTv'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFireAdapterEventosRecyclerView.MyFireViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.horaTv = null;
    target.tituloTv = null;
    target.descripcionTv = null;
  }
}
