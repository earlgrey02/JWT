## JWT

> **JWT(JSON Web Token) module for Spring framework**

## How to use

### build.gradle.kts

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.earlgrey02:JWT:${version}")
}
```

Add the dependency after add JitPack repository to your build file.

### application.yml

```yaml
jwt:
  secretKey: ...
  accessTokenExpire: 30
  refreshTokenExpire: 120
```

Set secret key and token expiration period in minutes.