package sk.stuba.fiit.perconik.activity.data.core;

import sk.stuba.fiit.perconik.activity.data.ObjectData;
import sk.stuba.fiit.perconik.activity.data.platform.BundleData;
import sk.stuba.fiit.perconik.core.plugin.Activator;
import sk.stuba.fiit.perconik.core.services.Services;

public class StandardCoreProbe implements CoreProbe {
  public StandardCoreProbe() {}

  public CoreData get() {
    CoreData data = new CoreData(Activator.defaultInstance());

    data.setClassResolver(ObjectData.of(Activator.classResolver()));
    data.setExtensionContributors(Activator.extensionContributors());
    data.setContributingBundles(BundleData.of(Activator.contributingBundles()));

    data.setResourceService(ResourceServiceData.of(Services.getResourceService()));
    data.setListenerService(ListenerServiceData.of(Services.getListenerService()));

    return data;
  }
}
