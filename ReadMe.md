# Прототип Android-составляющей приложения FLT

Прототип Android-составляющей приложения FLT (Foreign Language Training)

## Окружение

- OS: MS Windows >= 10 / Linux >= 4
- JDK: Open JDK / Oracle JDK >= 21
- Android SDK >= 14

## Настройка

- обеспечить наличие запущенной серверной составляющей системы

- в файле строк в каталоге _\<корневой_каталог_проекта\>_**/app/src/main/res/values/strings.xml** задать под ключом _network_base_server_url_ актуальное значение адреса серверной составляющей системы

## Построение приложения и установочного файла *.apk

Построение Android-приложения в Android Studio:

**Build** -> **Rebuild Project**

Выполните построение установочного файла тестируемого Android-приложения в Android Studio:

**Build** -> **Build App Bundle(s) / APK(s) -> Build APK(s)**

Установочный файл с расширением apk должен появиться в каталоге

_\<корневой_каталог_проекта\>_**app/build/outputs/apk/debug**

## Тестирование

- запуск всех тестов:

**./gradlew clean test**

## Отчёты

Отчёты строятся автоматически. После выполнения тестов открыть для просмотра в браузере файл, расположенный по пути:

_\<корневой_каталог_проекта\>_**/app/build/reports/tests/testDebugUnitTest/index.html**