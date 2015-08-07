package butterknife.internal;

final class FieldResourceBinding {
  private final String id;
  private final String name;
  private final String method;

  FieldResourceBinding(String id, String name, String method) {
    this.id = id;
    this.name = name;
    this.method = method;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getMethod() {
    return method;
  }
}
