package sk.stuba.fiit.perconik.core.ui.preferences;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.widgets.Table;

import static com.google.common.collect.Maps.newLinkedHashMap;

import static sk.stuba.fiit.perconik.utilities.MoreMaps.putAll;

abstract class MapEntrySorter<K, V> extends AbstractTableSorter<Entry<K, V>> {
  MapEntrySorter(final Table table) {
    super(table);
  }

  @Override
  final Set<Entry<K, V>> loadData() {
    return this.loadMap().entrySet();
  }

  @Override
  final void updateData(final List<Entry<K, V>> data) {
    Map<K, V> update = newLinkedHashMap();

    putAll(update, data);

    this.updateMap(update);
  }

  abstract Map<K, V> loadMap();

  abstract void updateMap(Map<K, V> map);
}
