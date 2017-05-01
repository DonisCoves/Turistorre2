// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.ActionBar.Click;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FiestasGaleriaViewPager_ViewBinding implements Unbinder {
  private FiestasGaleriaViewPager target;

  @UiThread
  public FiestasGaleriaViewPager_ViewBinding(FiestasGaleriaViewPager target, View source) {
    this.target = target;

    target.mViewPager = Utils.findRequiredViewAsType(source, R.id.vpHome, "field 'mViewPager'", ViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FiestasGaleriaViewPager target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mViewPager = null;
  }
}
