package com.skgw.lireTest.common

import com.skgw.lireTest.extension.map
import com.skgw.lireTest.extension.nameIdentifier
import net.semanticmetadata.lire.builders.DocumentBuilder
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexNotFoundException
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.FSDirectory
import java.awt.image.BufferedImage
import java.io.Closeable
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

class ImageIndex(indexPath: Path, writable: Boolean = false) : Closeable {

    private val indexDirectory: FSDirectory
    private val indexReader: IndexReader?
    private val indexWriter: IndexWriter?

    init {
        // インデックスディレクトリを開く
        val indexDirectory: FSDirectory = FSDirectory.open(indexPath)

        // インデックスリーダーを開く
        var indexReader: IndexReader? = null
        try {
            indexReader = DirectoryReader.open(indexDirectory)
        } catch (e: IndexNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            indexReader?.close()
            indexDirectory.close()
            throw e
        }

        // インデックスライターを開く
        var indexWriter: IndexWriter? = null
        try {
            val writerConfig = IndexWriterConfig(WhitespaceAnalyzer())
            if (writable) indexWriter = IndexWriter(indexDirectory, writerConfig)
        } catch (e: IOException) {
            indexWriter?.close()
            indexReader?.close()
            indexDirectory.close()
            throw e
        }

        // 初期化
        this.indexDirectory = indexDirectory
        this.indexReader = indexReader
        this.indexWriter = indexWriter
    }

    override fun close() {
        this.indexWriter?.close()
        this.indexReader?.close()
        this.indexDirectory.close()
    }

    data class SearchResult(val score: Double, val path: Path)

    fun search(image: BufferedImage, maxHits: Int = 10): List<SearchResult> {
        if (indexReader == null) throw IllegalArgumentException("")

        return GenericFastImageSearcher(maxHits, CEDD::class.java)
            .search(image, indexReader)
            .map { hit ->
                val path = Paths.get(indexReader.nameIdentifier(hit.documentID))
                return@map SearchResult(hit.score, path)
            }
    }

    fun create(imagePath: Path) {
        println("Create index: $imagePath")
        if (indexWriter == null) throw IllegalArgumentException("")

        val document = createDocument(imagePath)
        indexWriter.addDocument(document)
    }

    fun update(imagePath: Path) {
        println("Update index: $imagePath")
        if (indexWriter == null) throw IllegalArgumentException("")

        val term = Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, imagePath.toString())
        val document = createDocument(imagePath)
        indexWriter.updateDocument(term, document)
    }

    fun delete(imagePath: Path) {
        println("Delete index: $imagePath")
        if (indexWriter == null) throw IllegalArgumentException("")

        val term = Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, imagePath.toString())
        indexWriter.deleteDocuments(term)
    }

    fun createDocument(imagePath: Path): Document {
        val documentBuilder = GlobalDocumentBuilder(false, false)
        documentBuilder.addExtractor(CEDD::class.java)
        documentBuilder.addExtractor(FCTH::class.java)
        documentBuilder.addExtractor(AutoColorCorrelogram::class.java)

        val image = ImageIO.read(imagePath.toFile()) ?: throw IOException("不明な画像ファイル: $imagePath")
        return documentBuilder.createDocument(image, imagePath.toString())
    }

    fun exists(imagePath: Path): Boolean {
        if (indexReader == null) return false

        val searcher = IndexSearcher(indexReader)
        val query = TermQuery(Term(DocumentBuilder.FIELD_NAME_IDENTIFIER, imagePath.toString()))
        return searcher.search(query, 1).totalHits > 0
    }

    fun isAllowFile(imagePath: Path): Boolean {
        val fileName = imagePath.toString().lowercase()
        return when {
            !fileName.endsWith("jpg") && !fileName.endsWith("jpeg") -> false
            !Files.exists(imagePath) -> false
            !Files.isRegularFile(imagePath) -> false
            else -> true
        }
    }

}
