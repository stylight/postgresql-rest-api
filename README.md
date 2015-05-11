PostgreSQL HTTP Rest API
======

This is an HTTP Rest API(or REST like, as it is not completed yet) for PostgreSQL databases. This application allows you fetch your data from your database immediately via Rest API as in JSON format without developing any web middle layer.

* [Why](#why)
* [API](#api)
  * [Functions](#functions)
  * [Response](#response)
* [Installation](#installation)
  * [Configuration](#configuration)
  * [Authentication](#authentication)
* [TODO](#todo)


#Why?<a id="why"></a>
Benefit of an HTTP API is to allow easy access to existing data. This means accessing data without bulding a web API (e.g. accessing data from Mobile app, rapid prototyping, for public data), furthermore allows you to access to data with tools that don't support the PostgreSQL protocol such as curl, web browsers.

You can check following article, it is where the idea comes from:
[http://wiki.postgresql.org/wiki/HTTP_API](http://wiki.postgresql.org/wiki/HTTP_API)





#API<a id="api"></a>


```
# Returns all tables
GET http://localhost:9000/tables

# Returns all rows from the books table
GET  http://localhost:9000/table/books

# Returns all rows from book table where subject_id is equal to 2
GET  http://localhost:9000/table/books/subject_id/2
 
# Returns 50 rows from the books table
GET  http://localhost:9000/table/books?limit=50

# Returns 10 rows from the books table sorted by subject_id descending
GET  http://localhost:9000/table/books?limit=10&by=subject_id+desc

# Returns 50 rows from book table where subject_id is equal to 2
GET  http://localhost:9000/table/books/subject_id/2?limit=50

# Returns total number of rows
GET  http://localhost:9000/table/books?f=count

# Returns total number of rows where subject_id is equal to 2
GET  http://localhost:9000/table/books/subject_id/2?f=count

# Returns smallest value of price field in the table
GET  http://localhost:9000/table/books?f=min&field=price

# Returns smallest value of price field, where subject_id is equal to 2
GET  http://localhost:9000/table/books/subject_id/2?f=min&field=price

# Full text search on field title 
GET  http://localhost:9000/table/books/title/python?f=search

# Full text search on field title with language support
GET  http://localhost:9000/table/books/title/python?f=search&language=english


# Will execute any query and return the result
POST http://localhost:9000/query
Format:
{
    "query": "SELECT * FROM books WHERE id=7808"
}


# Will return all the columns in the table
GET  http://localhost:9000/table/books?f=columns

```

###Functions<a id="functions"></a>

* count 
* min 
* max 
* avg
* sum
* length
* md5



### Response<a id="response"></a>
Here is an example response from an API call.

```
[
    {
        "id": 41473,
        "title": "Programming Python",
        "author_id": 7805,
        "subject_id": 4
    },
    {
        "id": 41477,
        "title": "Learning Python",
        "author_id": 7805,
        "subject_id": 4
    }
]
```


Currently API returns following HTTP status codes

* 200 OK
* 204 No Content
* 400 Bad Request
* 404 Not Found
 




## Installation<a id="installation"></a>
You start the application with a configuraiton file, which contains your database credentials. Only other requirement is JDK 6 or later. 

1. Download the latest release from [relese-page](https://github.com/enginyoyen/postgresql-rest-api/releases) and uncompress it
2. Prepare your configuration file (e.g. your database settings)
3. Set the port and config file path and run it 


```
/pg-rest-api-*.*.*/bin/pg-rest-api  -Dhttp.port=9000 -Dconfig.file=/path/to/config.conf 
```



### Configuration File<a id="configuration"></a>

This is an example configuration file that you can use it.


```
application.secret="IN_PRODUCTION_CHANGE_THIS_TO_A_LONG_RANDOM_STRING"

application.baseUrl="http://localhost:9000"
evolutionplugin=disabled
application.mode=prod

db = {
  default {
    driver="org.postgresql.Driver"
    url="jdbc:postgresql://localhost:5432/booktown"
    user="postgres"
    password="postgres"
  }
}

logger.application = INFO

basic.auth.username="password"

```


### Authentication<a id="authentication"></a>
Application support Basic HTTP authenticaiton. To enable authentication you can add user credentials into the configuration file, which will automaticly enable authentication. If there is no credentials given at the start of application there will be no authentication. 


```
basic.auth.jane="password" 
```

In case authentication is enabled, the Authorization header is constructed as follows: ([wikipedia](http://en.wikipedia.org/wiki/Basic_access_authentication))

1. Username and password are combined into a string "username:password"
2. The resulting string is then encoded using with Base64
3. The authorization method and a space i.e. "Basic " is then put before the encoded string.

For example, if the user agent uses 'Aladdin' as the username and 'open sesame' as the password then the header is formed as follows:

Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==

[http://en.wikipedia.org/wiki/Basic_access_authentication]


## TODO<a id="todo"></a>
* Find more things to add in todo !




[travis-ci_status_img]: https://travis-ci.org/enginyoyen/postgresql-rest-api.svg?branch=master
[travis-ci_status]: https://travis-ci.org/enginyoyen/postgresql-rest-api
[coveralls_status_img]: https://img.shields.io/coveralls/enginyoyen/postgresql-rest-api.svg
[coveralls_status]: https://coveralls.io/r/enginyoyen/postgresql-rest-api?branch=master

[relese-page]: https://github.com/enginyoyen/postgresql-rest-api/releases/tag/v0.1.0
