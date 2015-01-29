# OSGified Containerless Platform
![alt tag](https://cloud.githubusercontent.com/assets/6278849/5888307/b6428b08-a402-11e4-8305-f4c2fdecaeed.jpg)

A platform to create scaleable and containerless applications. One of the objectives of the platform is to provide out of the box capabilities which are essential for building scaleable and clustered applications. Osgifing vertx made it possible to provide some sort of integration between vertx modules and OSGi bundles through the event bus. The ployglot nature of Vertx made it possible to work with different languages other than java.

Starting with the gateway bundle, which is an abstraction of a http gateway. The gateway bundle asbtracts three types of handlers:
- Output only http handlers which are suitable for non-body requests like GET or DELETE.
- Input/Output http handlers which are suitable for requests with body like POST or PUT.
- Multipart https handlers which are suitable for form/file submissions.

![alt tag](https://cloud.githubusercontent.com/assets/6278849/5907358/1820ebd0-a5a7-11e4-8620-949af8b8c7fa.jpg)

All http handlers respond with a service response object. The service response object contains the response itself plus errors and/or messages. The gateway bundle relies on the services provided by the localization bundle to convert the messages and/or erros sent in the response into a localized format. The service response object gets marshalled into a transmittable format using document writers. Document writers use Content-Type found in the header in order to marshal the response into the required format.  

The gateway bundle adds the concept of filters to RESTful services. When you work with handlers you can register any combination of filters to be executed before the handler itself. There are 3 types of filters:
- Session filter which uses JSESSIONID to construct the session object of the caller.
- Path filter which checks whether the caller is privildged to access the target path or not.
- Tracking filter which can be used to collect data about the callers and the services they called.

![alt tag](https://cloud.githubusercontent.com/assets/6278849/5907360/1ae4489e-a5a7-11e4-9a0f-9b9a750de378.jpg)

As it was mentioned before, the http gateway is an abstraction. The vertx bundle is the only bundle that implements the http gatway. HttpAdapter is an example of the classes that hide the details of the http context and leaves it to the implementer to determine how is it going to be implemented. For example, the httpadapter provides APIs to access http headers, parameters, cookies and user objects.  

Finally, The gateway bundle exposes its services through the gateway interface which gives the capability for clients to register handlers and filters.

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
