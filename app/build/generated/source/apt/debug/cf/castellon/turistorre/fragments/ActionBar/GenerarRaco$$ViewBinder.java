// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GenerarRaco$$ViewBinder<T extends cf.castellon.turistorre.fragments.ActionBar.GenerarRaco> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689668, "field 'etTitulo'");
    target.etTitulo = finder.castView(view, 2131689668, "field 'etTitulo'");
    view = finder.findRequiredView(source, 2131689669, "field 'etDescripcion'");
    target.etDescripcion = finder.castView(view, 2131689669, "field 'etDescripcion'");
    view = finder.findRequiredView(source, 2131689674, "field 'ivRaco'");
    target.ivRaco = finder.castView(view, 2131689674, "field 'ivRaco'");
    view = finder.findRequiredView(source, 2131689670, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689671, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689672, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689673, "method 'onClick'");
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
    target.etTitulo = null;
    target.etDescripcion = null;
    target.ivRaco = null;
  }
}
