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

public class GenerarBando_ViewBinding implements Unbinder {
  private GenerarBando target;

  private View view2131689667;

  private View view2131689666;

  private View view2131689665;

  @UiThread
  public GenerarBando_ViewBinding(final GenerarBando target, View source) {
    this.target = target;

    View view;
    target.etTitulo = Utils.findRequiredViewAsType(source, R.id.etTituloBando, "field 'etTitulo'", EditText.class);
    target.etDescripcion = Utils.findRequiredViewAsType(source, R.id.etDescBando, "field 'etDescripcion'", EditText.class);
    target.ivBando = Utils.findRequiredViewAsType(source, R.id.ivGenerarBando, "field 'ivBando'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btnConf, "method 'onClick'");
    view2131689667 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnMemoria, "method 'onClick'");
    view2131689666 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCamara, "method 'onClick'");
    view2131689665 = view;
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
    GenerarBando target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etTitulo = null;
    target.etDescripcion = null;
    target.ivBando = null;

    view2131689667.setOnClickListener(null);
    view2131689667 = null;
    view2131689666.setOnClickListener(null);
    view2131689666 = null;
    view2131689665.setOnClickListener(null);
    view2131689665 = null;
  }
}
