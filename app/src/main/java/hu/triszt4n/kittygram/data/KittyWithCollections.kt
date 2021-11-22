package hu.triszt4n.kittygram.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

data class KittyWithCollections(
    @Embedded val kitty: Kitty,
    @Relation(
        parentColumn = "kittyId",
        entityColumn = "collectionId",
        associateBy = Junction(CollectionWithKitties::class)
    )
    val collections: List<Collection>
)
