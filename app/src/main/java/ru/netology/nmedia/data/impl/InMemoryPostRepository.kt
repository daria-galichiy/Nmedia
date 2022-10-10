package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var nextId = GENERATED_POSTS_AMOUNT.toLong()

    private var posts
        get() = checkNotNull(data.value) {
            "Data value shouldn't be null"
        }
        set(value) {
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val initialPosts = listOf(
            Post(
                id = 1,
                author = "Нетология. Университет интернет-профессий. Меняем карьеру через образование",
                content = "Наш главный помощник в мире переизбытка ресурсов — критическое мышление. Оно помогает сориентироваться в потоке информации и не попасть в когнитивные ловушки. Как критическое мышление помогает нам в повседневной жизни, рассказали в Медиа: https://netolo.gy/i8E",
                published = "17 января в 11:23",
                video = "https://www.youtube.com/watch?v=C2DBDZKkLss"
            ),
            Post(
                id = 2,
                author = "Нетология. Университет интернет-профессий. Меняем карьеру через образование",
                content = "Товарный бизнес перестраивается: одни прогорели, другие растут на уходе брендов. Что изменилось? Как продавать на Wildberries и Ozon? Что будет с качеством товара? Как делать выгодные покупки? На эти и другие вопросы отвечаем в видео.",
                published = "15 августа в 21:53",
                likedByUser = true,
                likes = 1
            ),
            Post(
                id = 3,
                author = "Нетология. Университет интернет-профессий. Меняем карьеру через образование",
                content = "Иногда кажется, что мы сами создаём себе проблемы — боимся попросить повышения, недооцениваем свои заслуги. Причиной такого поведения могут стать когнитивные искажения, или ловушки мышления. Разбираемся, как не попасться на уловки нашего мозга, которые мешают строить карьеру и влияют на другие сферы жизни.",
                published = "25 июля в 9:35"
            ),
            Post(
                id = 4,
                author = "Нетология. Университет интернет-профессий. Меняем карьеру через образование",
                content = "Заполняем пустые окошки в Google-календаре на неделю! Выбирайте открытое занятие.\n" +
                        "\n" +
                        "― 22 августа, 19:00 ― SMM-менеджер и таргетолог в 2022\n" +
                        "Поймёте, какие задачи решают SMM-менеджер и таргетолог. Узнаете об особенностях работы в российских соцсетях. Выясните, какая сфера подходит именно вам и как начать в ней развиваться ― https://netolo.gy/i8U\n" +
                        "\n" +
                        "― 23 августа, 19:00 ― Профессия копирайтер: как создавать тексты, которые дочитывают\n" +
                        "Узнаете, что такое вовлекающий текст и как его написать. Разберёте, как с помощью заголовка выразить проблему читателя. Поймёте, как работать удалённо и где искать первых заказчиков ― https://netolo.gy/i8V\n" +
                        "\n" +
                        "― 24 августа, 19:00 ― UX-копирайтинг: как писать тексты интерфейсов\n" +
                        "Обсудим, как тексты интерфейса влияют на продукт и удобство работы с ним. Разберём примеры хороших и плохих текстов. Узнаете, как с помощью короткого сообщения познакомить пользователя с продуктом ― https://netolo.gy/i8W",
                published = "20 августа в 10:00"
            ),
            Post(
                id = 5,
                author = "Нетология. Университет интернет-профессий. Меняем карьеру через образование",
                content = "Вы отправили сообщение коллеге, а ответа нет. Не переживайте, возможно, ваш собеседник прямо сейчас спасает котика и пока недоступен. Или ему нужно чуть больше времени подумать над ответом.\n" +
                        "\n" +
                        "Пока ждёте сообщение, расскажите, за какое время нужно ответить на деловое сообщение или письмо? Выберите вариант из списка, а если не нашли подходящий, поделитесь в комментариях.",
                published = "вчера в 19:00"
            ),
            Post(
                id = 6,
                author = "Нетология. Университет интернет-профессий. Меняем карьеру через образование",
                content = "Стажировка ― прекрасный шанс проявить себя и попасть на работу в компанию мечты. В первые дни вам доверят не совсем рабочие задачки: сбегать за кофе, сделать копии документов, забрать почту. В эти моменты важно показать себя. Так ваш наставник поймёт, что может поручить дела посложнее.\n" +
                        "\n" +
                        "Расскажите, какой вы стажёр или каким были в начале карьеры. Выберите вариант из списка или поделитесь в комментариях.",
                published = "12 августа в 18:00"
            )
        )
        data = MutableLiveData(initialPosts)
    }

    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(
                likedByUser = !post.likedByUser,
                likes = if (post.likedByUser) post.likes - 1 else post.likes + 1
            )
            else post
        }
    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(
                shares = post.shares + 1
            )
            else post
        }
    }

    override fun delete(postId: Long) {
        posts = posts.filterNot { it.id == postId }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = ++nextId)) + posts
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private companion object {
        const val GENERATED_POSTS_AMOUNT = 6
    }
}