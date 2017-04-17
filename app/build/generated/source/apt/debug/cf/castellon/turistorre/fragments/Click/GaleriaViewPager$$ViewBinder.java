// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Click;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GaleriaViewPager$$ViewBinder<T extends cf.castellon.turistorre.fragments.Click.GaleriaViewPager> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689661, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131689661, "field 'mViewPager'");
  }

  @Override public void unbind(T target) {
    target.mViewPager = null;
  }
}
