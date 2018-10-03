# Sync or Async, that is the question?

Two similar webapps, one async (RxJava) and one sync. What are their performance characteristics like?

## Tech Stack

Dropwizard, backed by a Couchbase Server.

```
$ docker run --rm -d --name db -p 8091-8094:8091-8094 -p 11210:11210 couchbase/server:community-5.1.1
```

1. Create a bucket named content.
2. Configure a Couchbase user (content:content) with access to the content bucket.

## Data model

The web service can create and fetch content. A content consists of a bunch of Parts, all stored in their own documents.

