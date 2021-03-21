## KnowYouPlayer

[Chinese Document(中文文档)](README_ZH.md)

* ### Highlights

    * Comfortable and simple UI design

    * Complete player function: support import, clear, progress bar, previous, next

    * The software supports Chinese and English, automatically switch according to the system language
    
    * It supports most formats of audio files. Currently, it supports the following formats:
    
        ```
        "3ga","669","a52","aac","ac3","adt","adts","aif","aifc","aiff",
        "amb","amr","aob","ape","au","awb","caf","dts","dsf","dff",
        "flac","it","kar","m4a","m4b","m4p","m5p","mid","mka","mlp",
        "mod","mpa","mp1","mp2","mp3","mpc","mpga","mus","oga","ogg",
        "oma","opus","qcp","ra","rmi","s3m","sid","spx","tak","thd",
        "tta","voc","vqf","w64","wav","wma","wv","xa","xm"
        ```
    
* ### Require

    * Maven
    
    * JDK11

* ### How to use it:
    
    * mvn package
    
    * jar -jar know-you-player-1.0.jar

* ### Main Dependencies

    1. vlc(https://www.videolan.org)
    
    2. vlcj(https://github.com/caprica/vlcj)
    
    3. ikonli(https://github.com/kordamp/ikonli)
    
    4. hutool(https://github.com/dromara/hutool)
    
    5. fastjson(https://github.com/alibaba/fastjson)        

    6. log4j2(http://logging.apache.org/log4j/2.x/)
