package com.skgw.lireTest.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.nio.file.Path

/**
 * LIRE定義
 */
@ConfigurationProperties("lire")
@ConstructorBinding
data class LireConfig(
    /** LIRE Index定義 */
    val index: Index,
    /** LIRE 画像関連定義 */
    val imageStore: ImageStore
) {
    /**
     * LIRE Index定義
     */
    data class Index(
        /** LIRE Index 保管パス */
        val path: Path,
        /** LIRE Index 重複画像スコア閾値 */
        val duplicateScore: Double
    )

    /**
     * LIRE 画像関連定義
     */
    data class ImageStore(
        /** LIRE 画像保管パス */
        val path: Path
    )
}
