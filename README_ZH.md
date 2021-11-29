## 懂你播放器

[English Document](README.md)

* ### 亮点

    * 舒适简约的UI设计

    * 完整播放器功能：支持导入、清空、进度条、上一曲、下一曲

    * 软件支持中英文，自动根据系统语言切换

    * 支持大多数格式媒体文件，目前支持格式如下：

        ```
        "3ga", "669", "a52", "aac", "ac3", "adt", "adts", "aif", "aifc", "aiff",
        "amb", "amr", "aob", "ape", "au", "awb", "caf", "dts", "flac", "it",
        "kar", "m4a", "m4b", "m4p", "m5p", "mka", "mlp", "mod", "mpa", "mp1",
        "mp2", "mp3", "mpc", "mpga", "mus", "oga", "ogg", "oma", "opus", "qcp",
        "ra", "rmi", "s3m", "sid", "spx", "tak", "thd", "tta", "voc", "vqf",
        "w64", "wav", "wma", "wv", "xa", "xm"
        ```

* ### 构建要求

    * Maven

    * JDK11

    * JDK17

* ### 如何使用:

    * 下载: [Releases](https://github.com/min-li/know-you-player/releases)

    * 构建:

        * mvn package

        * 执行 build.bat(要求 JDK17) 或者 java -jar KnowYouPlayer.jar

* ### 主要依赖

    * vlc(https://www.videolan.org)

    * vlcj(https://github.com/caprica/vlcj)

    * ikonli(https://github.com/kordamp/ikonli)

    * hutool(https://github.com/dromara/hutool)

    * log4j2(http://logging.apache.org/log4j/2.x/)
