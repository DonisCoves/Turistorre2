// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.pruebas;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ProbesActivity$$ViewBinder<T extends cf.castellon.turistorre.pruebas.ProbesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689673, "field 'imagen'");
    target.imagen = finder.castView(view, 2131689673, "field 'imagen'");
  }

  @Override public void unbind(T target) {
    target.imagen = null;
  }
}
