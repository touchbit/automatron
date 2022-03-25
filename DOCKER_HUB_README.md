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

[Download](https://raw.githubusercontent.com/touchbit/automatron/docker_push_by_tag/docker-compose.yml) `docker-compose.yml`
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

## Usage

The service is available by default on port 8080.  
The service database is available by default on port 9092.   

- Swagger documentation is available at:
    - http://localhost:8080/
    - http://localhost:8080/swagger-ui/index.html
- Database:
    - http://localhost:8080/h2 - UI console (`admin` no password required)
    - `jdbc:h2:tcp://localhost:9092/./h2/automatron;USER=admin;PASSWORD=;` jdbc connection 

## Example

![](https://github.com/touchbit/automatron/blob/docker_push_by_tag/.doc/swaggerExample.png)
![](https://github.com/touchbit/automatron/blob/main/.doc/swaggerExample.png)
