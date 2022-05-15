package com.skgw.lireTest.api.search

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path
import kotlin.io.path.exists

@RestController
class SearchController {

    @Autowired
    lateinit var service: SearchService

    /**
     * 指定したパスの画像を元に、類似する画像を検索する
     *
     * NOTE: 画像自体を受け取れば、Google画像検索が再現できる
     *
     * @param path  画像パス
     * @return      検索結果
     */
    @GetMapping("/search")
    private fun search(@RequestParam("p") path: Path): List<SearchService.Result> {
        return when {
            path.exists() -> service.search(path)
            else -> listOf()
        }
    }

}
