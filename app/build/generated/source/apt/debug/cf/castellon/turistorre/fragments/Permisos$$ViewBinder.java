// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Permisos$$ViewBinder<T extends cf.castellon.turistorre.fragments.Permisos> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689728, "field 'tvPermiso'");
    target.tvPermiso = finder.castView(view, 2131689728, "field 'tvPermiso'");
    view = finder.findRequiredView(source, 2131689729, "field 'tvPoder'");
    target.tvPoder = finder.castView(view, 2131689729, "field 'tvPoder'");
    view = finder.findRequiredView(source, 2131689730, "field 'tvNoPoder'");
    target.tvNoPoder = finder.castView(view, 2131689730, "field 'tvNoPoder'");
    view = finder.findRequiredView(source, 2131689731, "field 'spiner'");
    target.spiner = finder.castView(view, 2131689731, "field 'spiner'");
    view = finder.findRequiredView(source, 2131689732, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.tvPermiso = null;
    target.tvPoder = null;
    target.tvNoPoder = null;
    target.spiner = null;
  }
}
