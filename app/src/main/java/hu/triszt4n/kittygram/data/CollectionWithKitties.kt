package hu.triszt4n.kittygram.data

import androidx.room.Embedded
import androidx.room.Relation
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

data class CollectionWithKitties(
    @Embedded val collection: Collection,
    @Relation(
         parentColumn = "id",
         entityColumn = "collectionId"
    )
    val kitties: List<Kitty>
)
