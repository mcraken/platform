# OSGified Containerless Platform
![alt tag](https://cloud.githubusercontent.com/assets/6278849/5888307/b6428b08-a402-11e4-8305-f4c2fdecaeed.jpg)

A platform to create scaleable and containerless applications. One of the objectives of the platform is to provide out of the box capabilities which are essential for building scaleable and clustered applications. Osgifing vertx made it possible to provide some sort of integration between vertx modules and OSGi bundles through the event bus. The ployglot nature of Vertx made it possible to work with different language other than java.

Apache shiro is used as a mean of session clustering and user based security. Having shiro makes it possible to provide seamless user management between identical nodes.

The schedule bundle is an abstraction of time based services. The schedule bundle is built on top of quartz. It models sessions and tracks scheduling. A session is an operation that is only operable within a pre-defined time period. A track is an ever repeating task that works within hour, day, week or year time periods.

The oauth bundle provides an abstraction for any services accessible using the oauth protocol. Built on top of scribe, the oauth bundle should provide support for multiple social networks. The bundle models oauth services as authorizers and invokers. Authorizers are used to execute the 3-way handshake and obtain a valid oauth access tokens. Oauth invokers use access tokens, previously obtained by authorizers, to access resources protected by oauth providers.

The repository bundle is implemented as a variation of the repository architectural pattern. The repository abstraction provides single interface for searching and modifying databases and searchable index implementations. Currently, the only implementations provided are for cassandra and Solrj.

The platform is built on:
- OSGi. 
- Vertx.io. 
- Cassandra. 
- Ehcache 
- Apache SOLR. 
- Apache Shiro.
- Spring data. 
- LMAX disruptor. 
- Quartz. 

Participants needed to do:
- An initial documentation.
- A parent POM for all the bundles.
- More oauth authorizers implementations (Now Google only).
- More oauth invokers (Now Google profile only).
- ORM/SQL based implementation in the repository bundle.
- Clustered deployment testing.
- Localization bundle implementation.
- Key based SOLR index search (Now Cache and repository supported).
- Support for REST API secure access tokens.
- Support for Web socket channels security.
- Cache index factory.
