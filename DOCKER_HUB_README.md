# Automatron

Service for training API autotest development skills.   
See [GitHub repository](https://github.com/touchbit/automatron) for more documentation.

## Docker run

```bash
docker pull touchbit/automatron:latest
docker run -p 8080:8080 -p 9092:9092 touchbit/automatron:latest
# press Cmd+С to stop execution
```

## Docker-compose run

[Download](https://raw.githubusercontent.com/touchbit/automatron/main/docker-compose.yml) `docker-compose.yml`
file or create
<details>
<summary>with the following content</summary>

```yaml
version: "3.9"
services:
  automatron:
    image: touchbit/automatron:latest
    ports:
      - "8080:8080"
      - "9092:9092"
```

</details>

Service start

```bash
docker-compose pull # get latest version
docker-compose up # run automatron
# press Cmd+С to stop execution
```

## Examples

<details>
<summary><u>Root page ⇲</u></summary>

![](https://raw.githubusercontent.com/touchbit/automatron/main/.doc/root_page_example.png)

</details>

<details>
<summary><u>Swagger doc ⇲</u></summary>

![](https://raw.githubusercontent.com/touchbit/automatron/main/.doc/swagger_page_example.png)

</details>

<details>
<summary><u>DB UI Console ⇲</u></summary>

![](https://raw.githubusercontent.com/touchbit/automatron/main/.doc/h2_db_console_page_example.png)

</details>

<details>
<summary><u>Admin UI ⇲</u></summary>

![](https://raw.githubusercontent.com/touchbit/automatron/main/.doc/admin_page_example.png)

</details>
