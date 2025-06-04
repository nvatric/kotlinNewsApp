package etf.ri.rma.newsfeedapp.dto

import com.google.gson.*
import java.lang.reflect.Type

class SourceDeserializer : JsonDeserializer<Source> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Source {
        return if (json.isJsonObject) {
            val obj = json.asJsonObject
            val id = obj.get("id")?.takeIf { !it.isJsonNull }?.asString
            val name = obj.get("name")?.takeIf { !it.isJsonNull }?.asString
            Source(id, name)
        } else {
            Source(null, json.asString)
        }
    }
}
