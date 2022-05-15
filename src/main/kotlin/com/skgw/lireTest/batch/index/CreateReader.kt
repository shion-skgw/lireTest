package com.skgw.lireTest.batch.index

import com.skgw.lireTest.config.LireConfig
import org.springframework.batch.item.ItemReader
import java.nio.file.Files
import java.nio.file.Path

/**
 * インデックス作成対象画像パス取得
 */
class CreateReader(private val imageStoreConfig: LireConfig.ImageStore) : ItemReader<Path> {

    /** 画像パス */
    private lateinit var imagePaths: Iterator<Path>

    /**
     * インデックス作成対象画像パスを取得する
     *
     * @return 画像パス
     */
    override fun read(): Path? {
        // 初期化
        if (!::imagePaths.isInitialized) {
            imagePaths = Files.walk(imageStoreConfig.path).iterator()
        }

        // 画像パスを取得する
        while (imagePaths.hasNext()) {
            return imagePaths.next()
        }

        return null
    }

}
