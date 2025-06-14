package game

object WordRepository {
    private val words: List<String> = listOf("яблоко", "помидор", "банан")

    fun getRandomWord(): String {
        return words.random()
    }
}