// Generated code from Butter Knife. Do not modify!
package cf.castellon.turistorre.adaptadores;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyFireAdapterGaleriaRecyclerView$MyFireViewHolder$$ViewBinder<T extends cf.castellon.turistorre.adaptadores.MyFireAdapterGaleriaRecyclerView.MyFireViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689656, "field 'imageView'");
    target.imageView = finder.castView(view, 2131689656, "field 'imageView'");
  }

  @Override public void unbind(T target) {
    target.imageView = null;
  }
}
