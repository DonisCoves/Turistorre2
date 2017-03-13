// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.pruebas;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ActivityChatJava$$ViewBinder<T extends cf.castellon.turistorre.pruebas.ActivityChatJava> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689605, "field 'salidaTxt'");
    target.salidaTxt = finder.castView(view, 2131689605, "field 'salidaTxt'");
    view = finder.findRequiredView(source, 2131689607, "field 'contador'");
    target.contador = finder.castView(view, 2131689607, "field 'contador'");
    view = finder.findRequiredView(source, 2131689606, "field 'texto'");
    target.texto = finder.castView(view, 2131689606, "field 'texto'");
    view = finder.findRequiredView(source, 2131689604, "field 'boton' and method 'onClick'");
    target.boton = finder.castView(view, 2131689604, "field 'boton'");
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
    target.salidaTxt = null;
    target.contador = null;
    target.texto = null;
    target.boton = null;
  }
}
