## Technologies

* __Spring Framework__ + __Spring Boot__ - well, what else? :-)

* __Spring Data JPA__ - for rapid development of the data access layer (database mapping, CRUD repository support, JPQL query for search and listing of articles - all done declaratively!). *(Note: I chose to map authors and keywords as `List<String>` instead of doing full-blown data modeling to focus on other aspects of the task but keep in mind that even with this simple `@ElementCollection` mapping those are kept in a separate table so refactoring / expanding the model and JPA mapping shouldn't be a big problem in the future.)*

* __Spring Security__ - HTTP Basic security for editor-only parts of the API and for the management API. Method security for the service layer (see `ArticleService`).

* __REST Assured__ - for REST API testing (see `RestApiTests`).

## System requirements

To build the code you need JDK 8 and Maven >= 3.2.

## Building

1. Clone repo.
2. `mvn clean package`

This builds a Spring Boot executable jar file with embedded Tomcat.

## Running

1. `cd target`
2. `java -jar articles-0.0.1-SNAPSHOT.jar`

This should start the embedded Tomcat and the application will be available at http://localhost:8080/.

## Articles API Examples

### Creating an article

`curl -v -X PUT --user editor:s3cr3t -H "Content-Type: application/json" -d @example.json http://localhost:8080/api/articles`

### Listing all by author

`curl -v http://localhost:8080/api/articles?author=author1`

### Searching with a keyword

`curl -v http://localhost:8080/api/search?keyword=title`

### Deleting an article

`curl -v -X DELETE --user editor:s3cr3t -H "Content-Type: application/json" http://localhost:8080/api/articles/1`

## Monitoring API Examples

After log in you will be able to access Spring Actuator endpoints, e.g. http://localhost:8080/dump for thread dump, health indicators and much more. Full list of endpoints can be found [here](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html).

### Health Endpoint

`curl --user editor:s3cr3t http://localhost:8080/health`

```
{
  "status": "UP",
  "diskSpace": {
    "status": "UP",
    "total": 117747277824,
    "free": 13068156928,
    "threshold": 10485760
  },
  "db": {
    "status": "UP",
    "database": "HSQL Database Engine",
    "hello": 1
  }
}
```

### Metrics Endpoint

`curl --user editor:s3cr3t http://localhost:8080/metrics`

```
{
  "mem": 489070,
  "mem.free": 210976,
  "processors": 4,
  "instance.uptime": 596152,
  "uptime": 606689,
  "systemload.average": 0.84,
  "heap.committed": 411648,
  "heap.init": 124928,
  "heap.used": 200671,
  "heap": 1749504,
  "nonheap.committed": 79168,
  "nonheap.init": 2496,
  "nonheap.used": 77427,
  "nonheap": 0,
  "threads.peak": 23,
  "threads.daemon": 21,
  "threads.totalStarted": 27,
  "threads": 23,
  "classes": 10408,
  "classes.loaded": 10408,
  "classes.unloaded": 0,
  "gc.ps_scavenge.count": 10,
  "gc.ps_scavenge.time": 363,
  "gc.ps_marksweep.count": 2,
  "gc.ps_marksweep.time": 181,
  "httpsessions.max": -1,
  "httpsessions.active": 11,
  "datasource.primary.active": 0,
  "datasource.primary.usage": 0,
  "gauge.response.dump": 98,
  "gauge.response.metrics": 5,
  "gauge.response.api.articles": 18,
  "gauge.response.api.search": 20,
  "gauge.response.unmapped": 1,
  "gauge.response.api.articles.id": 12,
  "counter.status.405.unmapped": 3,
  "counter.status.415.api.articles": 1,
  "counter.status.200.api.search": 1,
  "counter.status.200.metrics": 2,
  "counter.status.200.api.articles.id": 1,
  "counter.status.200.api.articles": 3,
  "counter.status.404.api.articles.id": 1,
  "counter.status.200.dump": 1,
  "counter.status.201.api.articles": 4,
  "counter.status.401.unmapped": 2
}
```
