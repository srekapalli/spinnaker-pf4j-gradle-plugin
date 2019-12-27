## Gradle plugin for registering spinnaker extension point implementations.

This **gradle** plugin allows Spinnaker developers to bundle, publish and register **_"spinnaker plugins"_** with spinnaker.

### Usage

```groovy
plugins {
   id 'com.netflix.spinnaker.gradle.pf4j.spinnakerpf4j'
}
```

### Notes

* Expects multi-module gradle project where each module implements extensions targeting a single spinnaker service.
* Storage for plugin artifacts(bundled zip) can be like S3, GCS, jCenter or artifactory etc. ????