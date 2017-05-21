// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Permisos_ViewBinding implements Unbinder {
  private Permisos target;

  private View view2131689734;

  @UiThread
  public Permisos_ViewBinding(final Permisos target, View source) {
    this.target = target;

    View view;
    target.tvPermiso = Utils.findRequiredViewAsType(source, R.id.tvPermiso, "field 'tvPermiso'", TextView.class);
    target.tvPoder = Utils.findRequiredViewAsType(source, R.id.tvPoder, "field 'tvPoder'", TextView.class);
    target.tvNoPoder = Utils.findRequiredViewAsType(source, R.id.tvNoPoder, "field 'tvNoPoder'", TextView.class);
    target.spiner = Utils.findRequiredViewAsType(source, R.id.spPermisos, "field 'spiner'", Spinner.class);
    view = Utils.findRequiredView(source, R.id.btEnvPermiso, "method 'onClick'");
    view2131689734 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Permisos target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvPermiso = null;
    target.tvPoder = null;
    target.tvNoPoder = null;
    target.spiner = null;

    view2131689734.setOnClickListener(null);
    view2131689734 = null;
  }
}
