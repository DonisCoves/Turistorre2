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

public class MyFireAdapterTerratsRecyclerView$MyFireViewHolder_ViewBinding implements Unbinder {
  private MyFireAdapterTerratsRecyclerView.MyFireViewHolder target;

  @UiThread
  public MyFireAdapterTerratsRecyclerView$MyFireViewHolder_ViewBinding(MyFireAdapterTerratsRecyclerView.MyFireViewHolder target,
      View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.ivFilaRecycleTerrat, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFireAdapterTerratsRecyclerView.MyFireViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
  }
}
