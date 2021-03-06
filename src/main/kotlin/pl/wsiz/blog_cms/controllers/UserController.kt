package pl.wsiz.blog_cms.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.wsiz.blog_cms.*

@RestController()
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @PostMapping("/register")
    fun registerUser(@RequestBody registerDTO: RegisterDTO): ResponseEntity<*> {
        if (registerDTO.fullName !== null && registerDTO.password !== null && registerDTO.login !== null) {
            val user = this.userRepository.save(User(
                    login = registerDTO.login,
                    password = this.passwordEncoder.encode(registerDTO.password),
                    fullName = registerDTO.fullName))
            return ok(UserDTO(user.id, user.login, user.fullName))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build<Unit>()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginDTO.login,
                        loginDTO.password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenService.encode(authentication)
        return ok(LoginResponseDTO(authToken = jwt))
    }

    data class LoginResponseDTO(
            var authToken: String? = null,
            var expiresIn: Long? = 5000
    )

    @GetMapping("")
    fun getUser(): UserDTO {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        val user = this.userRepository.findFirstById(principal.id!!)
        return UserDTO(user.id, user.login, user.fullName)
    }

}
