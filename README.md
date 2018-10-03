# Sync or Async, that is the question?

Two similar webapps, one async (RxJava) and one sync. What are their performance characteristics like?

The web applications does some meaningless work (serializing back and fourth from json to beans), and
then splits the Content up in its parts for storage.

## Content

A piece of content consists of a number of named parts, each of which is a string:string map.

## Tech Stack

Dropwizard, backed by a Couchbase Server.

```
$ docker run --rm -d --name db -p 8091-8094:8091-8094 -p 11210:11210 couchbase/server:community-5.1.1
```

1. Create a bucket named content.
2. Configure a Couchbase user (content:content) with access to the content bucket.

## Data model

The web service can create and fetch content. A content consists of a bunch of Parts, all stored in their own documents.

## Example usage

```
$ curl -XPUT -H'Content-Type: application/json' localhost:8080/content/walter -d'{"description":"fajshdfasldfhalkjsdhflkahsldfhasfasldfhjahsdfjkasdklfhkaljsdhfahsdfklhsakdfhkajlsdfhlasdf", "parts":{"foo":{"bar":"baz"}, "qux": {"quux": "quuz"}}}'
```

```
$ curl -s localhost:8080/content/walter | jq
{
  "id": "walter",
  "description": "fajshdfasldfhalkjsdhflkahsldfhasfasldfhjahsdfjkasdklfhkaljsdhfahsdfklhsakdfhkajlsdfhlasdf",
  "parts": {
    "qux": {
      "quux": "quuz"
    },
    "foo": {
      "bar": "baz"
    }
  }
}
```