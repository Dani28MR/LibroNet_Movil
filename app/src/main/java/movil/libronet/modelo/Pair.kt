import java.io.Serializable

data class Pair<out A, out B>(
    val first: A,
    val second: B
) : Serializable {

    override fun toString(): String = "($first, $second)"

    companion object {
        fun <A, B> create(first: A, second: B): Pair<A, B> = Pair(first, second)
    }
}