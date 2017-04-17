// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FiestasRecylerView$$ViewBinder<T extends cf.castellon.turistorre.fragments.Principal.FiestasRecylerView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689643, "field 'spFiestas'");
    target.spFiestas = finder.castView(view, 2131689643, "field 'spFiestas'");
    view = finder.findRequiredView(source, 2131689644, "field 'recView'");
    target.recView = finder.castView(view, 2131689644, "field 'recView'");
  }

  @Override public void unbind(T target) {
    target.spFiestas = null;
    target.recView = null;
  }
}
