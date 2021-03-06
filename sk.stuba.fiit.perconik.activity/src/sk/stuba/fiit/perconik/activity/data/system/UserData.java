package sk.stuba.fiit.perconik.activity.data.system;

import java.nio.file.Path;

import sk.stuba.fiit.perconik.data.AnyStructuredData;

public class UserData extends AnyStructuredData {
  protected String name;

  protected Path home;

  protected Path directory;

  public UserData() {}

  public void setName(final String name) {
    this.name = name;
  }

  public void setHome(final Path home) {
    this.home = home;
  }

  public void setDirectory(final Path directory) {
    this.directory = directory;
  }

  public String getName() {
    return this.name;
  }

  public Path getHome() {
    return this.home;
  }

  public Path getDirectory() {
    return this.directory;
  }
}
