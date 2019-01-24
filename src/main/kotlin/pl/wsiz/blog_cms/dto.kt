package pl.wsiz.blog_cms

class LoginDTO {
    var login: String? = null
    var password: String? = null
}

class RegisterDTO {
    var login: String? = null
    var password: String? = null
    var fullName: String? = null
}


data class UserDTO(
        var id: Long? = null,
        var login: String? = null,
        var fullName: String? = null
)


class ArticleDTO() {

    constructor(id: Long?, title: String?, body: String?, createdBy: Long?) : this() {
        this.id = id
        this.body = body
        this.title = title
        this.createdBy = createdBy
    }

    var id: Long? = null
    var title: String? = null
    var body: String? = null
    var createdBy: Long? = null
}

