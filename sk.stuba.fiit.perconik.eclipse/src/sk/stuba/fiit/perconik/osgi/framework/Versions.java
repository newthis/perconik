package sk.stuba.fiit.perconik.osgi.framework;

import java.util.List;

import com.google.common.base.Joiner;

import org.osgi.framework.Version;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

public final class Versions {
  private static final String separator = ".";

  private Versions() {}

  public enum Component {
    MAJOR {
      @Override
      public Integer get(final Version version) {
        return version.getMajor();
      }
    },

    MINOR {
      @Override
      public Integer get(final Version version) {
        return version.getMinor();
      }
    },

    MICRO {
      @Override
      public Integer get(final Version version) {
        return version.getMicro();
      }
    },

    QUALIFIER {
      @Override
      public String get(final Version version) {
        return version.getQualifier();
      }
    };

    public abstract Object get(final Version version);

    @Override
    public final String toString() {
      return this.name().toLowerCase();
    }
  }

  public static String separator() {
    return separator;
  }

  public static Version parse(final String value) {
    return Version.parseVersion(value);
  }

  public static String toString(final Version version, final Component ... components) {
    List<Object> values = newArrayListWithCapacity(components.length);

    for (Component component: components) {
      values.add(component.get(version));
    }

    return Joiner.on(separator).join(values);
  }

  public static Version toVersion(final String value) {
    try {
      return Version.parseVersion(value);
    } catch (RuntimeException e) {
      return Version.emptyVersion;
    }
  }
}
