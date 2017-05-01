// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.adaptadores;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyFireAdapterGaleriaEventoRecyclerView$MyFireViewHolder_ViewBinding implements Unbinder {
  private MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder target;

  @UiThread
  public MyFireAdapterGaleriaEventoRecyclerView$MyFireViewHolder_ViewBinding(MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder target,
      View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.ivEvGaleria, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFireAdapterGaleriaEventoRecyclerView.MyFireViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
  }
}
