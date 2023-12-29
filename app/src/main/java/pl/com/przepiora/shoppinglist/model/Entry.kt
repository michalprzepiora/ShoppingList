package pl.com.przepiora.shoppinglist.model

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Entry(var isDone: Boolean, var text: String) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is Entry) {
            return text == other.text
        }
        return false
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + text.hashCode()
        return result
    }


}
