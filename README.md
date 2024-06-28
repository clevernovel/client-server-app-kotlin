Client-Server Приложение

Этот проект демонстрирует простое клиент-серверное приложение для шифрования и дешифрования данных с использованием
пользовательского TLV (Tag-Length-Value) кодирования и различных алгоритмов шифрования.

Структура проекта

Клиент
Клиентское приложение захватывает ввод пользователя, кодирует данные и отправляет их на сервер для обработки.
Затем оно получает обработанные данные с сервера, декодирует их и сохраняет или отображает результат.

Сервер
Сервер принимает закодированные запросы от клиента, декодирует их, обрабатывает данные (шифрование/дешифрование)
и отправляет обратно закодированный ответ.

DTO
Объекты передачи данных (DTO) используются для представления данных, передаваемых между клиентом и сервером.

Утилиты

CryptoUtils
Утилита для шифрования и дешифрования данных.

TLVUtils
Утилита для кодирования и декодирования данных в формате TLV.

Как запустить
Убедитесь, что у вас установлены JDK и Kotlin.
Запустите серверное приложение из файла server.MainKt.
Запустите клиентское приложение из файла client.MainKt.
Следуйте инструкциям в клиентском приложении для ввода данных и выполнения операций шифрования/дешифрования.