package com.skgw.lireTest.api.search

import com.skgw.lireTest.common.ImageIndex
import com.skgw.lireTest.config.LireConfig
import org.springframework.stereotype.Service
import java.nio.file.Path
import javax.imageio.ImageIO

@Service
class SearchService(private val config: LireConfig) {

    /**
     * 検索結果オブジェクト
     *
     * @property score  スコア
     * @property path   画像パス
     */
    data class Result(
        val score: Double,
        val path: Path
    )

    fun search(target: Path): List<Result> {
        // 検索対象の画像
        val targetImage = ImageIO.read(target.toFile())

        // 検索実行
        return ImageIndex(config.index.path).use { imageIndex ->
            return@use imageIndex.search(targetImage)
                .map { return@map Result(it.score, it.path) }
                .filter { target != it.path }
        }
    }

}
