# Sync or Async, that is the question?

Two similar webapps, one async (RxJava) and one sync. What are their performance characteristics like?

## Tech Stack

Dropwizard, backed by a Couchbase Server.

## Data model

The web service can create and fetch content. A content consists of a bunch of Parts, all stored in their own documents.
