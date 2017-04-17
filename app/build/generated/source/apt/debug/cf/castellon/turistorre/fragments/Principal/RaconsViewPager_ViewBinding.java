// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments.Principal;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cf.castellon.turistorre.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RaconsViewPager_ViewBinding implements Unbinder {
  private RaconsViewPager target;

  @UiThread
  public RaconsViewPager_ViewBinding(RaconsViewPager target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.vpRacons, "field 'viewPager'", ViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RaconsViewPager target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
  }
}
