## Spinnaker Plugin Distribution Plugin.

This **gradle** plugin allows Spinnaker developers to bundle, publish and register **_"spinnaker plugins"_** with spinnaker.

### Usage

```groovy
plugins {
   id 'com.netflix.spinnaker.gradle.pf4j.spinnakerpf4j'
}
```

### Notes

* Expects multi-module gradle project where each module implements extensions targeting a single spinnaker service.
