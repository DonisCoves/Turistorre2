// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RaconsViewPager$$ViewBinder<T extends cf.castellon.turistorre.fragments.Principal.RaconsViewPager> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689732, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131689732, "field 'viewPager'");
  }

  @Override public void unbind(T target) {
    target.viewPager = null;
  }
}
