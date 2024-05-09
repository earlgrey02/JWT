package com.github.jwt

import com.github.jwt.config.JwtConfiguration
import com.github.jwt.exception.JwtException
import com.github.jwt.fixture.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.string.shouldNotBeBlank

class JwtProviderTest : BehaviorSpec() {
    private val jwtProvider = JwtConfiguration().jwtProvider(
        secret = SECRET,
        accessTokenExpire = ACCESS_TOKEN_EXPIRE,
        refreshTokenExpire = REFRESH_TOKEN_EXPIRE
    )

    init {
        Given("인증 정보가 주어진 경우") {
            val authentication = createDefaultJwtAuthentication()

            When("액세스 토큰을 발급하면") {
                val accessToken = jwtProvider.createAccessToken(authentication)

                Then("액세스 토큰이 주어진다.") {
                    accessToken.shouldNotBeBlank()
                }
            }

            When("리프레쉬 토큰을 발급하면") {
                val refreshToken = jwtProvider.createRefreshToken(authentication)

                Then("리프레쉬 토큰이 주어진다.") {
                    refreshToken.shouldNotBeBlank()
                }
            }
        }

        Given("유효한 토큰이 주어진 경우") {
            val authentication = createDefaultJwtAuthentication()
            val accessToken = jwtProvider.createAccessToken(authentication)

            When("인증 정보를 추출하면") {
                val extractedAuthentication = jwtProvider.getAuthentication(accessToken)

                Then("토큰에 대한 인증 정보가 주어진다.") {
                    extractedAuthentication shouldBeEqualToComparingFields authentication
                }
            }
        }

        Given("유효하지 않은 토큰이 주어진 경우") {
            val invalidToken = INVALID_TOKEN

            When("인증 정보를 추출하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<JwtException> { jwtProvider.getAuthentication(invalidToken) }
                }
            }
        }
    }
}
