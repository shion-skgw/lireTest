package com.skgw.lireTest.extension

import net.semanticmetadata.lire.builders.DocumentBuilder
import org.apache.lucene.index.IndexReader

fun IndexReader.nameIdentifier(documentID: Int): String {
    val fields: Set<String> = setOf(DocumentBuilder.FIELD_NAME_IDENTIFIER)
    return this.document(documentID, fields)
        .getField(DocumentBuilder.FIELD_NAME_IDENTIFIER)
        .stringValue()
}
