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

public class GenerarRaco_ViewBinding implements Unbinder {
  private GenerarRaco target;

  private View view2131689670;

  private View view2131689671;

  private View view2131689672;

  private View view2131689673;

  @UiThread
  public GenerarRaco_ViewBinding(final GenerarRaco target, View source) {
    this.target = target;

    View view;
    target.etTitulo = Utils.findRequiredViewAsType(source, R.id.etTituloRaco, "field 'etTitulo'", EditText.class);
    target.etDescripcion = Utils.findRequiredViewAsType(source, R.id.etDescRaco, "field 'etDescripcion'", EditText.class);
    target.ivRaco = Utils.findRequiredViewAsType(source, R.id.ivGenerarRaco, "field 'ivRaco'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btnCamaraRaco, "method 'onClick'");
    view2131689670 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnMemoriaRaco, "method 'onClick'");
    view2131689671 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnConfRaco, "method 'onClick'");
    view2131689672 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCancRaco, "method 'onClick'");
    view2131689673 = view;
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
    GenerarRaco target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etTitulo = null;
    target.etDescripcion = null;
    target.ivRaco = null;

    view2131689670.setOnClickListener(null);
    view2131689670 = null;
    view2131689671.setOnClickListener(null);
    view2131689671 = null;
    view2131689672.setOnClickListener(null);
    view2131689672 = null;
    view2131689673.setOnClickListener(null);
    view2131689673 = null;
  }
}
