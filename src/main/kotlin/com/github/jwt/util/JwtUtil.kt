package com.github.jwt.util

import com.github.jwt.Token
import com.github.jwt.exception.JwtException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Header
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

/**
 * Method to change long type minutes to milliseconds.
 *
 * @param minute Minute as a [Long]
 * @return milliseconds as a [Long]
 */
fun minutesToMillis(minute: Long): Long = TimeUnit.MINUTES.toMillis(minute)

/**
 * Method to retrieve bearer token from header.
 *
 * @receiver Header string
 * @return Token as a [String]
 */
fun String.toBearerToken(): Token =
    if (startsWith("Bearer ")) substring(7) else throw JwtException("Authorization header is invalid.")

/**
 * Method to convert string to secret key.
 *
 * @receiver Secret string
 * @return Secret key
 */
fun String.toSecretKey(): SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this))

/**
 * Method to convert header to map.
 *
 * @receiver Header
 * @return [Map]
 */
fun Header<*>.toMap(): Map<String, String> =
    entries.associate { it.key to it.value.toString() }

/**
 * Method to convert payload to map.
 *
 * @receiver Payload
 * @return [Map]
 */
fun Claims.toMap(): Map<String, String> =
    entries.associate { it.key to it.value.toString() }
