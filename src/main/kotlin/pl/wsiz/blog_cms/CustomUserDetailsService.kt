package pl.wsiz.blog_cms

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findFirstByLogin(username)
        return UserPrincipal(user.id!!, user.login!!, user.fullName!!, user.password!!)
    }

    fun loadUserById(id: Long): UserPrincipal {
        val user: User = userRepository.findFirstById(id)
        return UserPrincipal(user.id!!, user.login!!, user.fullName!!, user.password!!)
    }
}