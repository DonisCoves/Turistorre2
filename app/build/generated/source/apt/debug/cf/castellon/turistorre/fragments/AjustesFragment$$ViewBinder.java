// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AjustesFragment$$ViewBinder<T extends cf.castellon.turistorre.fragments.AjustesFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689613, "field 'spNotificaciones'");
    target.spNotificaciones = finder.castView(view, 2131689613, "field 'spNotificaciones'");
    view = finder.findRequiredView(source, 2131689614, "field 'spIdiomas'");
    target.spIdiomas = finder.castView(view, 2131689614, "field 'spIdiomas'");
  }

  @Override public void unbind(T target) {
    target.spNotificaciones = null;
    target.spIdiomas = null;
  }
}
