copy ".\know-you-player-core\target\KnowYouPlayer.jar" ".\build"
jpackage --name "KnowYouPlayer" --input ./build --icon ./build/know-you-player.ico --type "exe" --main-jar ./KnowYouPlayer.jar --java-options -Dfile.encoding=UTF-8 --vendor minli --win-dir-chooser --win-menu --win-menu-group "KnowYouPlayer" --win-per-user-install --win-shortcut
del ".\build\KnowYouPlayer.jar"
