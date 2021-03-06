Тренажер для начинающих автотестеров.

Доступны следующие ресурсы:

- [/doc](current_host_port/doc) - документация к сервису (swagger)
- [/api](current_host_port/api) - API которое предстоит тестировать
- [/h2](current_host_port/h2) - UI консоль базы данных
- [/admin](current_host_port/admin/wallboard) - панель администратора для настройки приложения

Для доступа в базу данных и в админку пароль **не требуется**.

### Баги

В сервис преднамеренно добавлены задокументированные ошибки. Если вы получили в ответе от сервиса заголовок `bug-id`, то приложение отработало с ошибкой. Ошибки могут быть связанные с безопасностью, спецификацией и реализацией. Вот тут вам и предстоит отловить баг. Для этого в вашем распоряжении есть достаточно подробные логи сервиса, доступ к базе данных и главное - документация.

<details>
<summary>Пример</summary>

![](./bug_header_example.png)

</details>

А если совсем не понятно в чём проблема, то можно запросить подробную информацию по дефекту `/api/bug?id=1`.   
Помните, что не все дефекты можно обнаружить при помощи автотеста, но к этому определенно стоит стремиться.

### База данных

Помимо [UI консоли](current_host_port/h2) к базе данных можно подключиться по tcp
`jdbc:h2:tcp://localhost:9092/./h2/automatron;USER=admin;PASSWORD=;`

Если вы каким-то образом сломали сервис некорректными данными в БД, то удалите папку `./h2` и перезапустите сервис. При старте сервиса БД будет заново создана.    
Если вы запустили сервис при помощи docker, то удалите контейнер и запустите заново.

```bash
docker container ls -a -f ancestor=touchbit/automatron
# CONTAINER ID   IMAGE ....
# 6f1fa2a978dd   touchbit/automatron ....
docker rm -f -v 6f1fa2a978dd
```

### Админка

<details>
<summary>Задать язык по умолчанию.</summary>

![](./admin_conf_lang.gif)

</details>

<details>
<summary>Показать/скрыть общие блоки в Swagger документации.</summary>

![](./admin_conf_common_blocks_visible.gif)

</details>

<details>
<summary>Изменить уровень логирования.</summary>

![](./admin_conf_log_level.gif)

</details>

### Ссылки

- [Автор сего чуда](https://shaburov.github.io/)
- [Группа в телеграмм](https://t.me/automatron_qa)
- [GitHub репозиторий](https://github.com/touchbit/automatron)
- [DockerHub репозиторий](https://hub.docker.com/r/touchbit/automatron)

---

Copyright © 2022 [Shaburov Oleg](https://shaburov.github.io/)
<details>
<summary>Распространяется по лицензии MIT.</summary>
  openapi_description_licence
</details>
