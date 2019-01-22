package pl.wsiz.blog_cms

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class TokenService {

    private var logger = LoggerFactory.getLogger(javaClass)
    private var algorithm: Algorithm? = null
    private var verifier: JWTVerifier? = null

    init {
        this.algorithm = Algorithm.HMAC256("secret")
        this.verifier = JWT.require(algorithm).acceptExpiresAt(0).build()
    }

    fun encode(auth: Authentication): String {
        val user = auth.principal as UserPrincipal

        val now = LocalDateTime.now()
        try {
            return JWT.create()
                    .withIssuer("projekt")
                    .withSubject(user.id.toString())
                    .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault())
                            .toInstant()))
                    .withExpiresAt(Date.from(now.plusSeconds(5000)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()))
                    .withClaim("usr", user.fullName)
                    .sign(algorithm)
        } catch (ex: JWTCreationException) {
            logger.error("Cannot properly create token", ex)
            throw RuntimeException("Cannot properly create token", ex)
        }
    }

    fun getUserIdFromJWT(token: String): Long {
        val decodedJWT: DecodedJWT = verifier!!.verify(token)
        return decodedJWT.subject.toLong()
    }

    fun validateToken(token: String): Boolean {
        return try {
            this.verifier?.verify(token)
            true
        } catch (e: Exception) {
            print("bad token" + e.toString())
            false
        }
    }

}