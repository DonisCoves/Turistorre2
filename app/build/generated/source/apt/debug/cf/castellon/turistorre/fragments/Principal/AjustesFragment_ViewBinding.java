// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AjustesFragment_ViewBinding implements Unbinder {
  private AjustesFragment target;

  @UiThread
  public AjustesFragment_ViewBinding(AjustesFragment target, View source) {
    this.target = target;

    target.spNotificaciones = Utils.findRequiredViewAsType(source, R.id.spNotificaciones, "field 'spNotificaciones'", AppCompatSpinner.class);
    target.spIdiomas = Utils.findRequiredViewAsType(source, R.id.spIdiomas, "field 'spIdiomas'", AppCompatSpinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AjustesFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.spNotificaciones = null;
    target.spIdiomas = null;
  }
}
