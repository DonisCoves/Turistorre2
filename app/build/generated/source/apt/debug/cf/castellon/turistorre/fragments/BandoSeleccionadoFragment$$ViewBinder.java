// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BandoSeleccionadoFragment$$ViewBinder<T extends cf.castellon.turistorre.fragments.BandoSeleccionadoFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689616, "field 'tvTitulo'");
    target.tvTitulo = finder.castView(view, 2131689616, "field 'tvTitulo'");
    view = finder.findRequiredView(source, 2131689617, "field 'tvDescripcion'");
    target.tvDescripcion = finder.castView(view, 2131689617, "field 'tvDescripcion'");
    view = finder.findRequiredView(source, 2131689618, "field 'ivImagen'");
    target.ivImagen = finder.castView(view, 2131689618, "field 'ivImagen'");
  }

  @Override public void unbind(T target) {
    target.tvTitulo = null;
    target.tvDescripcion = null;
    target.ivImagen = null;
  }
}
