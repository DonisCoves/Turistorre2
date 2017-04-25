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

public class MyFireAdapterDiasFiestaRecyclerView$MyFireViewHolder_ViewBinding implements Unbinder {
  private MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder target;

  @UiThread
  public MyFireAdapterDiasFiestaRecyclerView$MyFireViewHolder_ViewBinding(MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder target,
      View source) {
    this.target = target;

    target.textoTv = Utils.findRequiredViewAsType(source, R.id.tvTituloDiaFiesta, "field 'textoTv'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFireAdapterDiasFiestaRecyclerView.MyFireViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textoTv = null;
  }
}
