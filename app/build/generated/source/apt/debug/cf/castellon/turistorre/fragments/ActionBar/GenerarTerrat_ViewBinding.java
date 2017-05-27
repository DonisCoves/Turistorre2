// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GenerarTerrat_ViewBinding implements Unbinder {
  private GenerarTerrat target;

  private View view2131689680;

  private View view2131689681;

  private View view2131689682;

  @UiThread
  public GenerarTerrat_ViewBinding(final GenerarTerrat target, View source) {
    this.target = target;

    View view;
    target.etDireccion = Utils.findRequiredViewAsType(source, R.id.etDirTerrat, "field 'etDireccion'", EditText.class);
    target.ivTerrat = Utils.findRequiredViewAsType(source, R.id.ivGenTerrat, "field 'ivTerrat'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btnGalTerrat, "method 'onClick'");
    view2131689680 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnEnvTerrat, "method 'onClick'");
    view2131689681 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCanTerrat, "method 'onClick'");
    view2131689682 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    GenerarTerrat target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etDireccion = null;
    target.ivTerrat = null;

    view2131689680.setOnClickListener(null);
    view2131689680 = null;
    view2131689681.setOnClickListener(null);
    view2131689681 = null;
    view2131689682.setOnClickListener(null);
    view2131689682 = null;
  }
}
