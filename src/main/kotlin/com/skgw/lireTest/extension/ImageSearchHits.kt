package com.skgw.lireTest.extension;

import net.semanticmetadata.lire.searchers.ImageSearchHits

data class ImageSearchHit(val score: Double, val documentID: Int)

inline fun <R> ImageSearchHits.map(transform: (ImageSearchHit) -> R): List<R> {
    val list = arrayListOf<R>()
    for (index in 0 until this.length()) {
        val hit = ImageSearchHit(this.score(index), this.documentID(index))
        list.add(transform(hit))
    }
    return list
}
