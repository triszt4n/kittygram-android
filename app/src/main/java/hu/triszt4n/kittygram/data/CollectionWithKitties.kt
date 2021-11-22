package hu.triszt4n.kittygram.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

data class CollectionWithKitties(
    @Embedded val collection: Collection,
    @Relation(
         parentColumn = "collectionId",
         entityColumn = "kittyId",
         associateBy = Junction(CollectionWithKitties::class)
    )
    val kitties: List<Kitty>
)
