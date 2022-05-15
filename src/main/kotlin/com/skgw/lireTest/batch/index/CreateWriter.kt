package com.skgw.lireTest.batch.index

import com.skgw.lireTest.common.ImageIndex
import com.skgw.lireTest.config.LireConfig
import org.springframework.batch.item.ItemWriter
import java.io.IOException
import java.nio.file.Path

/**
 * インデックス作成実行処理
 */
class CreateWriter(private val indexConfig: LireConfig.Index) : ItemWriter<Path> {

    /**
     * インデックス作成実行処理
     *
     * @param items 画像パス
     */
    override fun write(items: MutableList<out Path>) {
        println("start " + items.size)

        // インデックス作成
        ImageIndex(indexConfig.path, true).use { imageIndex ->
            items.forEach { path ->
                try {
                    // バリデーション
                    if (!imageIndex.isAllowFile(path)) throw IOException("不正なファイル形式: $path")

                    // インデックスが存在する場合は更新、なければ新規作成
                    when (imageIndex.exists(path)) {
                        true -> imageIndex.update(path)
                        false -> imageIndex.create(path)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}
