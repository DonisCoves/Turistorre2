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

public class MyFireAdapterGaleriaRecyclerView$MyFireViewHolder_ViewBinding implements Unbinder {
  private MyFireAdapterGaleriaRecyclerView.MyFireViewHolder target;

  @UiThread
  public MyFireAdapterGaleriaRecyclerView$MyFireViewHolder_ViewBinding(MyFireAdapterGaleriaRecyclerView.MyFireViewHolder target,
      View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.ivFilaRecycle, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFireAdapterGaleriaRecyclerView.MyFireViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
  }
}
