package hu.triszt4n.kittygram.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["kittyId", "collectionId"])
data class KittyCollectionCrossRef(
    val kittyId: Long,
    val collectionId: Long
)
