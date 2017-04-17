// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BandoRecyclerView$$ViewBinder<T extends cf.castellon.turistorre.fragments.Principal.BandoRecyclerView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689615, "field 'rvBando'");
    target.rvBando = finder.castView(view, 2131689615, "field 'rvBando'");
  }

  @Override public void unbind(T target) {
    target.rvBando = null;
  }
}
