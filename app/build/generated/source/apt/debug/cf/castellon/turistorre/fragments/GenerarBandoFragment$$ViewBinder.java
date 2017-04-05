// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GenerarBandoFragment$$ViewBinder<T extends cf.castellon.turistorre.fragments.GenerarBandoFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689662, "field 'etTitulo'");
    target.etTitulo = finder.castView(view, 2131689662, "field 'etTitulo'");
    view = finder.findRequiredView(source, 2131689663, "field 'etDescripcion'");
    target.etDescripcion = finder.castView(view, 2131689663, "field 'etDescripcion'");
    view = finder.findRequiredView(source, 2131689667, "field 'ivBando'");
    target.ivBando = finder.castView(view, 2131689667, "field 'ivBando'");
    view = finder.findRequiredView(source, 2131689666, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689665, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689664, "method 'onClick'");
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
    target.ivBando = null;
  }
}
