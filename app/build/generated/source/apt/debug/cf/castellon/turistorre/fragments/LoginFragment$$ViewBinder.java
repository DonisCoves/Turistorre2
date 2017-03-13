// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginFragment$$ViewBinder<T extends cf.castellon.turistorre.fragments.LoginFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689693, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131689693, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131689697, "field 'email'");
    target.email = finder.castView(view, 2131689697, "field 'email'");
    view = finder.findRequiredView(source, 2131689698, "field 'password'");
    target.password = finder.castView(view, 2131689698, "field 'password'");
    view = finder.findRequiredView(source, 2131689700, "field 'btnNativoDesc' and method 'onClick'");
    target.btnNativoDesc = finder.castView(view, 2131689700, "field 'btnNativoDesc'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689694, "field 'layoutLoginNativo'");
    target.layoutLoginNativo = finder.castView(view, 2131689694, "field 'layoutLoginNativo'");
    view = finder.findRequiredView(source, 2131689696, "field 'layoutRegistroNativo'");
    target.layoutRegistroNativo = finder.castView(view, 2131689696, "field 'layoutRegistroNativo'");
    view = finder.findRequiredView(source, 2131689703, "field 'btnFacebook'");
    target.btnFacebook = finder.castView(view, 2131689703, "field 'btnFacebook'");
    view = finder.findRequiredView(source, 2131689702, "field 'signoutG' and method 'onClick'");
    target.signoutG = finder.castView(view, 2131689702, "field 'signoutG'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689701, "field 'mGoogleLoginButton'");
    target.mGoogleLoginButton = finder.castView(view, 2131689701, "field 'mGoogleLoginButton'");
    view = finder.findRequiredView(source, 2131689695, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131689699, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.email = null;
    target.password = null;
    target.btnNativoDesc = null;
    target.layoutLoginNativo = null;
    target.layoutRegistroNativo = null;
    target.btnFacebook = null;
    target.signoutG = null;
    target.mGoogleLoginButton = null;
  }
}
