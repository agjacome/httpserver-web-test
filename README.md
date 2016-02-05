HttpServer web test
===================

[![Build Status](https://travis-ci.org/agjacome/httpserver-web-test.svg)](https://travis-ci.org/agjacome/httpserver-web-test)
[![Coverage Status](https://coveralls.io/repos/github/agjacome/httpserver-web-test/badge.svg?branch=develop)](https://coveralls.io/github/agjacome/httpserver-web-test)
[![MIT License](https://img.shields.io/badge/license-MIT-orange.svg)](https://github.com/agjacome/httpserver-web-test/blob/master/LICENSE.md)
[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

Simple [HttpServer](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html) web application prototype.

### Build and test

Clone this GitHub repository:

    git clone https://github.com/agjacome/httpserver-web-test.git

Build and test through Gradle Wrapper:

    cd httpserver-web-test
    ./gradlew build check -s -i

Construct JAR package and run it:

    ./gradlew jar
    java -jar ./build/libs/httpserver-web-test.jar

### Main example appplication

The main example (see ```com.github.agjacome.httpserver.Main```) bounds the HTTP
server to ```http://localhost:9000/httpserver```. The example application
contains four pages besides login, logout and a simple index, and a total of
six users with different roles to access these pages.

##### Users and Roles

Current pages are correlated with their respective roles. So ```PAGE_1``` role
implies that the user can access Page 1. Roles can be combined, so a user can
have access to multiple pages through his multiple roles. And page privileges
can also be combined, so a page can be accessed by multiple roles (see
page_23). There also exists an ```ADMIN``` special role that can access any
page. 

1. username: admin - password: adminpass - role: ADMIN
2. username: page1 - password: page1pass - role: PAGE_1
3. username: page2 - password: page2pass - role: PAGE_2
4. username: page3 - password: page3pass - role: PAGE_3
5. username: page12 - password: page12pass - role: PAGE_1 and PAGE2
5. username: page13 - password: page13pass - role: PAGE_1 and PAGE2

Usernames are case-insensitive, so ```admin``` and ```ADMIN``` are the same
user. Passwords, of course, are case sensitive.

### TODO

* Tests, the code is almost completely untested at the moment.
* API for User resource, with read-only access for users and write access
  exclusive for ADMIN role.
* Content-negotiation on API resources (text/plain and application/json at
  least).
