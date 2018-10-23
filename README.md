# Introduction

This is a test application to demonstrate an issue I'm having with [Micronaut](https://micronaut.io/).

I'm finding that Micronaut `RxHttpClient` is only setting the HTTP `host` header to the hostname and dropping any custom ports. So I did the below example code, and then simply ran it against netcat so I could easily demonstrate the issue - see below.

Clients should include non-standard ports in the `host` header as per [section 14.23 of the HTTP spec](https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.23).

## Usage

1. Start an netcat instance to act as an endpoint: `nc -l -p 8081`
2. Then run the app: `./gradlew run`

Errors will occur in the running of the app, only because netcat does not respond
- but that is not the issue. (It was the simplest way to demonstrate this issue.)

The key is what is seen in the output of headers in netcat - read below.

## Description

When the `RxHttpClient` is connecting to an endpoint you see headers such as:

```
POST / HTTP/1.1
host: localhost
connection: close
content-type: application/json
content-length: 7

testing
```

I'd expext the `host:` line to read:

    host: localhost:8081

As an example, if I use:

    curl -X POST http://localhost:8081/ -d "testing"

Then I see:

```
$ nc -l -p 8081
POST / HTTP/1.1
Host: localhost:8081
User-Agent: curl/7.61.0
Accept: */*
Content-Length: 7
Content-Type: application/x-www-form-urlencoded

testing
```

And lastly, this is what one sees if they do a packet capture of the micronaut client request with `ngrep`:

```
T 127.0.0.1:43506 -> 127.0.0.1:8081 [AP] #37955 POST / HTTP/1.1..host: localhost..connection: close..content-type: application/json..content-length: 7....testing
```