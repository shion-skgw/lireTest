# LIRE + Spring Boot + Kotlin テスト

画像解析ライブラリ [LIRE](https://github.com/dermotte/LIRE) を利用して、画像の類似検索を行う。

Kotlin, Spring Boot, Spring Batch の学習用プロジェクト。

## ビルドについて

実務を想定して、以下の動作環境別にビルドできるように実装。

- ローカル環境
- 開発環境
- 本番環境

精神衛生上、他環境向けの設定ファイルが本番デプロイされることを避ける。

ビルド実行前に、以下のgradleタスクを実行することで、各環境に対応した設定ファイルを配置できるようにした。

| gradleタスク | 環境 |
| -- | --- |
| `changeLocal` | ローカル環境 |
| `changeDevelop` | 開発環境 |
| `changeProduct` | 本番環境 |

Jenkinsなりなんなり、ビルド・デプロイ手順に上記タスクの実行を含めて、リリース事故を予防する。
