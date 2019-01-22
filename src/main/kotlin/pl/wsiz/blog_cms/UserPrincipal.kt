package pl.wsiz.blog_cms

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal : UserDetails {
    var id: Long? = null
    var login: String? = null
    var fullName: String? = null

    @JsonIgnore
    private var password: String? = null

    constructor(id: Long, login: String, fullName: String, password: String) {
        this.id = id
        this.login = login
        this.fullName = fullName
        this.password = password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf()
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return login!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}