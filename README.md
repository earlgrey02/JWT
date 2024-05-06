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

### Create token

```kotlin
val map = mapOf("username" to "earlgrey02")
jwtProvider.createToken(map, 100000)
```

You can use `JwtProvider` to create basic tokens as well as access and refresh tokens.

## JWT Authorization

### Spring Web MVC

```kotlin
@EnableWebSecurity
@Configuration
class SecurityConfiguration {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        jwtProvider: JwtProvider
    ): SecurityFilterChain {
        http {
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            logout { disable() }
            exceptionHandling { authenticationEntryPoint = HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED) }
            authorizeRequests { authorize(anyRequest, authenticated) }
            addFilterAt<UsernamePasswordAuthenticationFilter>(jwtFilter)
        }

        return http.build()
    }

    @Bean
    fun jwtFilter(jwtProvider: JwtProvider): JwtFilter = JwtFilter(jwtProvider)
}
```

register `JwtFilter` to `SecurityFilterChain`.

```kotlin
@RestController
class TestController {
    @GetMapping("/")
    fun test(
        @AuthenticationPrincipal
        Principal principal
    ): ResponseEntity<Int> {
        val jwtAuthentication = principal as JwtAUthentication

        return ResponseEntity.ok()
            .body(jwtAuthentication.id)
    }
}
```

You can get `JwtAuthentication` from the `principal` in controller.

### Spring WebFlux

```kotlin
@EnableWebFluxSecurity
@Configuration
class ReactiveSecurityConfiguration {
    @Bean
    fun filterChain(
        http: ServerHttpSecurity,
        jwtFilter: ReactiveJwtFilter
    ): SecurityWebFilterChain =
        http {
            csrf { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            logout { disable() }
            exceptionHandling { authenticationEntryPoint = HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED) }
            authorizeExchange { authorize(anyExchange, authenticated) }
            addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHORIZATION)
        }

    @Bean
    fun jwtFilter(jwtProvider: JwtProvider): ReactiveJwtFilter =
        ReactiveJwtFilter(jwtProvider)
}
```

register `ReactiveJwtFilter` to `SecurityWebFilterChain`.

```kotlin
@Component
class TestHandler {
    fun test(request: ServerRequest): Mono<ServerResponse> =
        request.principal()
            .cast<JwtAuthentication>()
            .flatMap {
                ServerResponse.ok()
                    .bodyValue(it.id)
            }
}
```

You can get `JwtAuthentication` from the `principal()` in WebFlux.fn.
