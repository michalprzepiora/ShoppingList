package pl.com.przepiora.shoppinglist.model

data class Entry(var isDone: Boolean, var text: String) {
    override fun equals(other: Any?): Boolean {
    if (other is Entry){
        return text == other.text
    }
        return false
    }
}
