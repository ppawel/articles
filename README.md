# Articles

## Examples with curl

Creating an article:

`curl -v -X PUT --user editor:s3cr3t -H "Content-Type: application/json" -d @example.json http://localhost:8080/api/articles`

Listing all by author:

`curl -v http://localhost:8080/api/articles?author=author1`

Searching with a keyword:

`curl -v http://localhost:8080/api/search?keyword=title`

Deleting an article:

`curl -v -X DELETE --user editor:s3cr3t -H "Content-Type: application/json" http://localhost:8080/api/articles/1`
