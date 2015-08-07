Butter Knife
============

![Logo](website/static/logo.png)

Field and method binding for Android views which uses annotation processing to generate boilerplate
code for you.

 * Eliminate `findViewById` calls by using `@Bind` on fields.
 * Group multiple views in a list or array. Operate on all of them at once with actions,
   setters, or properties.
 * Eliminate anonymous inner-classes for listeners by annotating methods with `@OnClick` and others.
 * Eliminate resource lookups by using resource annotations on fields.

```java
class ExampleActivity extends Activity {
  @Bind(R.id.user) EditText username;
  @Bind(R.id.pass) EditText password;

  @BindString(R.string.login_error)
  String loginErrorMessage;

  @OnClick(R.id.submit) void submit() {
    // TODO call server...
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    ButterKnife.bind(this);
    // TODO Use fields...
  }
}
```

 * Also supports using string resource names
 * This is the required method to bind objects inside library modules

```java
class LibraryActivity extends Activity {
  @Bind(res = "user") EditText username;
  @Bind(res = "pass") EditText password;

  @BindString(res = "login_error")
  String loginErrorMessage;

  @OnClick(res = "submit") void submit() {
    // TODO call server...
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    ButterKnife.bind(this);
    // TODO Use fields...
  }
}
```

For documentation and additional information see [the website][3].

__Remember: A butter knife is like [a dagger][1] only infinitely less sharp.__



Download
--------

For the SNAPSHOT version:
```xml
<dependency>
  <groupId>com.nextfaze</groupId>
  <artifactId>butterknife</artifactId>
  <version>7.0.2-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>com.nextfaze</groupId>
  <artifactId>butterknife-compiler</artifactId>
  <version>7.0.2-SNAPSHOT</version>
  <optional>true</optional>
</dependency>
```
or Gradle:
```groovy
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.6'
  }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {
  compile 'com.nextfaze:butterknife:7.0.2-SNAPSHOT'
  apt 'com.nextfaze:butterknife-compiler:7.0.2-SNAPSHOT'
}
```

Library modules require additional parameters to be specified. In most cases:

```groovy
apt {
    arguments {
        androidManifestFile variant.outputs[0].processResources.manifestFile
    }
}
```

Will successfully retrieve the package name to use when referencing resources.
If this doesn't work for your project, you can specify the name directly:

```groovy
apt {
    arguments {
        resourcePackageName 'com.group.package'
    }
}
```

Snapshots are available in [Sonatype's `snapshots` repository][snap].


License
-------

    Copyright 2013 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: http://square.github.com/dagger/
 [2]: https://search.maven.org/remote_content?g=com.jakewharton&a=butterknife&v=LATEST
 [3]: http://jakewharton.github.com/butterknife/
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
