// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FiestasGridView_ViewBinding implements Unbinder {
  private FiestasGridView target;

  @UiThread
  public FiestasGridView_ViewBinding(FiestasGridView target, View source) {
    this.target = target;

    target.gridView = Utils.findRequiredViewAsType(source, R.id.gvGaleriaDia, "field 'gridView'", GridView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FiestasGridView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.gridView = null;
  }
}
