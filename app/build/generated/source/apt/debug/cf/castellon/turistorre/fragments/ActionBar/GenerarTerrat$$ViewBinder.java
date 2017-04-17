// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GenerarTerrat$$ViewBinder<T extends cf.castellon.turistorre.fragments.ActionBar.GenerarTerrat> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689675, "field 'etDireccion'");
    target.etDireccion = finder.castView(view, 2131689675, "field 'etDireccion'");
    view = finder.findRequiredView(source, 2131689679, "field 'ivTerrat'");
    target.ivTerrat = finder.castView(view, 2131689679, "field 'ivTerrat'");
    view = finder.findRequiredView(source, 2131689676, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689677, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689678, "method 'onClick'");
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
    target.etDireccion = null;
    target.ivTerrat = null;
  }
}
