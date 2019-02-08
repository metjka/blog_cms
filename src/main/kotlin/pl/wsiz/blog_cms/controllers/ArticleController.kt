package pl.wsiz.blog_cms.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import pl.wsiz.blog_cms.*

@RestController()
@RequestMapping("/article")
class ArticleController {

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @GetMapping("")
    fun getAll(): List<ArticleDTO> {
        return articleRepository.findAll()
                .map { ArticleDTO(it.id, it.title, it.body, it.createdBy?.id)}
    }

    @GetMapping("/my")
    fun getAllMy(): List<ArticleDTO> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return articleRepository.findArticleByCreatedBy(User(id = principal.id)).map { it -> ArticleDTO(it.id, it.title, it.body, it.createdBy?.id) }
    }

    @PostMapping("")
    fun createArticle(@RequestBody article: ArticleDTO): ArticleDTO {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        val saved = articleRepository.save(Article(title = article.title, body = article.body, createdBy = User(principal.id)))
        return ArticleDTO(saved.id, saved.title, saved.body, saved.createdBy?.id)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody article: ArticleDTO): ResponseEntity<ArticleDTO> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return articleRepository.findById(id).map {
            if (it.createdBy?.id !== principal.id) {
                return@map ResponseEntity.status(HttpStatus.FORBIDDEN).build<ArticleDTO>()
            }
            val newArticle = articleRepository.save(Article(id, article.title, article.body, User(id = id)))
            return@map ResponseEntity.ok(ArticleDTO(newArticle.id!!, newArticle.title!!, newArticle.body!!, newArticle.createdBy?.id!!))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {
        val principal = SecurityContextHolder.getContext().authentication.principal as UserPrincipal
        return articleRepository.findById(id).map {
            if (it.createdBy?.id !== principal.id) {
                return@map ResponseEntity.status(HttpStatus.FORBIDDEN).build<Unit>()
            }
            articleRepository.deleteById(id)
            return@map ResponseEntity.ok().build<Unit>()
        }.orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): ResponseEntity<ArticleDTO> {
        return articleRepository.findById(id).map { it: Article? ->
            return@map ResponseEntity.ok(ArticleDTO(it?.id, it?.title, it?.body, it?.createdBy?.id))
        }.orElse(ResponseEntity.notFound().build())
    }
}
