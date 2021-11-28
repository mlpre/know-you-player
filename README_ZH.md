## 懂你播放器

[English Document](README.md)

* ### 亮点

    * 舒适简约的UI设计

    * 完整播放器功能：支持导入、清空、进度条、上一曲、下一曲

    * 支持自动分析网页音频功能，并且支持导入播放器和下载

    * 软件支持中英文，自动根据系统语言切换

    * 支持大多数格式媒体文件，目前支持格式如下：

        ```
        "3ga","669","a52","aac","ac3","adt","adts","aif","aifc",
        "aiff","amb","amr","aob","ape","au","awb","caf","dts",
        "dsf","dff","flac","it","kar","m4a","m4b","m4p","m5p",
        "mid","mka","mlp","mod","mpa","mp1","mp2","mp3","mpc",
        "mpga","mus","oga","ogg","oma","opus","qcp","ra","rmi",
        "s3m","sid","spx","tak","thd","tta","voc","vqf","w64",
        "wav","wma","wv","xa","xm","3g2","3gp","3gp2","3gpp",
        "amv","asf","avi","bik","bin","crf","dav","divx","drc",
        "dv","dvr-ms","evo","f4v","flv","gvi","gxf","iso","m1v",
        "m2v","m2t","m2ts","m4v","mkv","mov","mp2","mp2v","mp4",
        "mp4v","mpe","mpeg","mpeg1","mpeg2","mpeg4","mpg","mpv2",
        "mts","mtv","mxf","mxg","nsv","nuv","ogg","ogm","ogv",
        "ogx","ps","rec","rm","rmvb","rpl","thp","tod","tp","ts",
        "tts","txd","vob","vro","webm","wm","wmv","wtv","xesc",
        "asx","b4s","cue","ifo","m3u","m3u8","pls","ram","rar",
        "sdp","vlc","xspf","wax","wvx","zip","conf","cdg","idx",
        "srt","sub","utf","ass","ssa","aqt","jss","psb","rt",
        "sami","smi","txt","smil","stl","usf","dks","pjs","mpl2",
        "mks","vtt","tt","ttml","dfxp","scc"
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
