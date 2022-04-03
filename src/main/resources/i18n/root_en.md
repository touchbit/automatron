Trainer for beginner autotesters.

The following resources are available:

- [/doc](current_host_port/doc) - service documentation (swagger)
- [/api](current_host_port/api) - API to be tested
- [/h2](current_host_port/h2) - database UI Console
- [/admin](current_host_port/admin/wallboard) - admin panel for configuring the application

To access the database and the admin panel, a password is **not required**.

### Bugs

Documented bugs have been intentionally added to the service. If you received a `bug-id` header in the response from the service, then the application worked with an error. Errors can be related to security, specification and implementation. In such cases, you have to catch the bug. To do this, you have at your disposal quite detailed service logs, access to the database and, most importantly, documentation.

<details>
<summary>Example</summary>

![](./bug_header_example.png)

</details>

And if it is not at all clear what the problem is, then you can request detailed information on the defect `/api/bug?id=1`.   
Remember that not all defects can be detected using an autotest, but this is definitely worth striving for.

### Database

In addition to the [UI console](current_host_port/h2), you can connect to the database via tcp `jdbc:h2:tcp://localhost:9092/./h2/automatron;USER=admin;PASSWORD=;`

If you somehow broke the service with incorrect data in the database, then delete the `./h2` folder and restart the service. When the service starts, the database will be re-created.   
If you started the service using docker, then delete the container and start again.

```bash
docker container ls -a -f ancestor=touchbit/automatron
# CONTAINER ID   IMAGE ....
# 6f1fa2a978dd   touchbit/automatron ....
docker rm -f -v 6f1fa2a978dd
```

### Admin panel

<details>
<summary>Set default language.</summary>

![](./admin_conf_lang.gif)

</details>

<details>
<summary>Show/hide common blocks in the Swagger documentation.</summary>

![](./admin_conf_common_blocks_visible.gif)

</details>

<details>
<summary>Change logging level.</summary>

![](./admin_conf_log_level.gif)

</details>

### Links

- [Author of this miracle](https://shaburov.github.io/)
- [Telegram group](https://t.me/automatron_qa)
- [GitHub repository](https://github.com/touchbit/automatron)
- [DockerHub repository](https://hub.docker.com/r/touchbit/automatron)

---

Copyright © 2022 [Shaburov Oleg](https://shaburov.github.io/)
<details>
<summary>Distributed under MIT License.</summary>
  openapi_description_licence
</details>
