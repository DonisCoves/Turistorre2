// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FiestasGridView$$ViewBinder<T extends cf.castellon.turistorre.fragments.ActionBar.FiestasGridView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689642, "field 'gridView'");
    target.gridView = finder.castView(view, 2131689642, "field 'gridView'");
  }

  @Override public void unbind(T target) {
    target.gridView = null;
  }
}
