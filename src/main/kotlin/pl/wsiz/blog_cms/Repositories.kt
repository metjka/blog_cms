package pl.wsiz.blog_cms

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity()
@Table(name = "USERS")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
        @Column(unique = true)
        var login: String? = null,
        var password: String? = null,
        var fullName: String? = null,
        @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
        var articles: Collection<Article> = ArrayList<Article>()
)


interface UserRepository : JpaRepository<User, Int> {
    fun findFirstByLogin(login: String): User
    fun findFirstById(id: Long): User
}

@Table(name = "ARTICLES")
@Entity()
data class Article(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
        var title: String? = null,
        var body: String? = null,
        @ManyToOne() @JoinColumn(name = "createdBy", nullable = false) var createdBy: User? = null
)

interface ArticleRepository : JpaRepository<Article, Long> {

    fun findArticleByCreatedBy(createdBy: User): MutableCollection<Article>
}