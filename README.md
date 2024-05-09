## JWT

> **JWT(JSON Web Token) module for Spring framework**

## Settings

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

### application.yaml

```yaml
jwt:
  secretKey: ...
  accessTokenExpire: 30
  refreshTokenExpire: 120
```

Set secret key and token expiration period in minutes.

## JWT Authorization

### Spring Web MVC

```kotlin
@EnableWebSecurity
@Configuration
class SecurityConfiguration {
    @Bean
    fun filterChain(
        http: HttpSecurity,
        jwtFilter: JwtFilter
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
    fun jwtFilter(jwtProvider: DefaultJwtProvider): JwtFilter = JwtFilter(jwtProvider)
}
```

Register `JwtFilter` to `SecurityFilterChain`.

```kotlin
@RestController
class TestController {
    @GetMapping("/")
    fun test(
        @AuthenticationPrincipal
        Principal principal
    ): ResponseEntity<Int> {
        val jwtAuthentication = principal as DefaultJwtAuthentication

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
    fun jwtFilter(jwtProvider: DefaultJwtProvider): ReactiveJwtFilter =
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
