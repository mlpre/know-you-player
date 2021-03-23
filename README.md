## KnowYouPlayer

[Chinese Document(中文文档)](README_ZH.md)

* ### Highlights

    * Comfortable and simple UI design

    * Complete player function: support import, clear, progress bar, previous, next

    * The software supports Chinese and English, automatically switch according to the system language
    
    * It supports most formats of media files. Currently, it supports the following formats:
    
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
    
* ### Build Require

    * Maven
    
    * JDK11

* ### How to use it:
    
    * Download: [Releases](https://github.com/min-li/know-you-player/releases)
    
    * Build:
    
        * mvn package
        
        * execute build.bat(Require JDK16) or java -jar know-you-player-1.0.jar

* ### Main Dependencies

    * vlc(https://www.videolan.org)
    
    * vlcj(https://github.com/caprica/vlcj)
    
    * ikonli(https://github.com/kordamp/ikonli)
    
    * hutool(https://github.com/dromara/hutool)
    
    * fastjson(https://github.com/alibaba/fastjson)        

    * log4j2(http://logging.apache.org/log4j/2.x/)
