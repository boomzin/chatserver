Команда для запуска чат клиента
java -cp /home/boomzin/projects/Media-tel/icc/chatserver/src/main/java ru.mediatel.chatserver.chatclient.ChatClient


### Клиент

Первым параметром можно указать server url (optional, default "http://localhost:8080/chat")

#### Сборка bash
```shell
javac -d out src/main/java/ru/mediatel/chatserver/chatclient/ChatClient.java
```

#### Запуск bash
```shell
PROJECT_PATH=$PWD
#PROJECT_PATH=/mnt/c/main/work/icc/chatserver
#echo $PROJECT_PATH
java -cp "$PROJECT_PATH/out" ru.mediatel.chatserver.chatclient.ChatClient "http://localhost:8080/chat"
```


#### Смена кодировки windows консоли (не обязательно)
utf-8
```shell
chcp 65001
```


#### Сборка windows (ps)
```shell
javac -encoding UTF-8 -d out src\main\java\ru\mediatel\chatserver\chatclient\ChatClient.java
```

#### Запуск windows (ps)
```shell
$PROJECT_PATH = $pwd
#$PROJECT_PATH = "C:\main\work\icc\chatserver"
#$PROJECT_PATH
java -cp "$PROJECT_PATH\out" ru.mediatel.chatserver.chatclient.ChatClient "http://localhost:8080/chat"
```


#### Сборка windows (cmd)
```shell
javac -encoding UTF-8 -d out src\main\java\ru\mediatel\chatserver\chatclient\ChatClient.java
```

#### Запуск windows (cmd)
```shell
set PROJECT_PATH=%cd%
#set PROJECT_PATH="C:\main\work\icc\chatserver"
#echo %PROJECT_PATH%
java -cp "%PROJECT_PATH%\out" ru.mediatel.chatserver.chatclient.ChatClient "http://localhost:8080/chat"
```