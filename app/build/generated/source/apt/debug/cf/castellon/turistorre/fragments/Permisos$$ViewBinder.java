// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Permisos$$ViewBinder<T extends cf.castellon.turistorre.fragments.Permisos> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689732, "field 'tvPermiso'");
    target.tvPermiso = finder.castView(view, 2131689732, "field 'tvPermiso'");
    view = finder.findRequiredView(source, 2131689733, "field 'tvPoder'");
    target.tvPoder = finder.castView(view, 2131689733, "field 'tvPoder'");
    view = finder.findRequiredView(source, 2131689734, "field 'tvNoPoder'");
    target.tvNoPoder = finder.castView(view, 2131689734, "field 'tvNoPoder'");
    view = finder.findRequiredView(source, 2131689735, "field 'spiner'");
    target.spiner = finder.castView(view, 2131689735, "field 'spiner'");
    view = finder.findRequiredView(source, 2131689736, "field 'btEnviar'");
    target.btEnviar = finder.castView(view, 2131689736, "field 'btEnviar'");
  }

  @Override public void unbind(T target) {
    target.tvPermiso = null;
    target.tvPoder = null;
    target.tvNoPoder = null;
    target.spiner = null;
    target.btEnviar = null;
  }
}
