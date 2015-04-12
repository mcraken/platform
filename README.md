# Cradle Framework
![alt tag](https://cloud.githubusercontent.com/assets/6278849/5888307/b6428b08-a402-11e4-8305-f4c2fdecaeed.jpg)

You can use cradle to create scaleable and containerless applications. One of the objectives of the platform is to provide out of the box capabilities which are essential for building scaleable and clustered applications. Osgifing vertx made it possible to provide some sort of integration between vertx modules and OSGi bundles through the event bus. The ployglot nature of Vertx made it possible to work with different languages other than java.

## Gateway
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

## Reposiotry

The repository bundle is implemented as a variation of the repository architectural pattern. The repository abstraction provides single interface for searching and modifying databases and searchable index implementations. Currently, the only implementations provided are for cassandra and Solrj.

The model repository interface supports two types of APIs which are the structured repository APIs and the index repository APIs. 

![alt tag](https://cloud.githubusercontent.com/assets/6278849/7104885/3bc1fa92-e0fe-11e4-832c-cae741e1ae81.png)

The structured repository interface is a generic model for a DAO which supports create, update and delete operations. The structured repository interface provides special type of reading capability based on search keys. Search key is a class that reprsents a verbose reading operation. A typical search key is targeted for a certain model and supported by multiple search criteria and paging configurations. Below is an examplary search key in JSON format:
```json
{
   "resourceName": "news",
   "select": "title,body"
   "start": 5,
   "count": 10,
   "criterias": [
      {
         "propertyName": "1_eq(language)",
         "value": "AR"
      },
      {
         "propertyName": "2_gt(date)",
         "value": "20040501"
      },
      {
         "propertyName": "3_lg(1,2)",
         "value": "and"
      }
   ],
}
```
As you might have deducted already, the demonstrated search operation is targeted for a model annotated with "news". The key has a projection on news title and body only.The dataset starts at entry 5 and extends up to 10 models length. The criteria section of this search key consists of 3 parts where one of them is logical in nature. The search key retrieves all the news marked with an arabic language after the first of May, 2004. Each search criterion consists of a property name and a target value. The structure of the property name is criterionNumber_operationName(field). The search key also demonstrates a logical operation in the thrid criterion, the format of a logical criterion is as follows criterionNumber_lg(criterion1, criterion2 ... criterionN). The value filed of the logical criterion hold the required logical operation.

Now lets take a look at a search operation in action starting with the model repository

![alt tag](https://cloud.githubusercontent.com/assets/6278849/7104882/d9a88fc4-e0fd-11e4-84d2-cc4281d4e273.jpg)

The structured repository supports simple DSL querying format as well. The SimpleSelectionAdapter interface is used to provide chain type calls to build a simple query. 

```java
SimpleSelectionAdapter<Story> selectionAdapter = repository.createSimpleSelectionAdapter("news");

		List<Story> result = selectionAdapter
				.eq("language", "AR")
				.eq("category", 3)
				.and()
				.orderBy(false, "date")
				.result();
```
The simple selection adapter evaluates calls on a postfix basis. In the example above, the language and the category calls are evaluated into an AND criterion when and() is introduced to the chanined calls in the fourth line. Query configuration calls like paging and sorting should be introduced at the end of the chain, otherwise invalid query exception might be thrown but it depends on the actual implementation of the query adapter. Now, lets take a look at the sequence diagram

![alt tag](https://cloud.githubusercontent.com/assets/6278849/7105012/f44da322-e103-11e4-8cdf-1a51e07ca9a2.png)
Each method starts with doXXX is a template method. Do template method are implemented by children of BasicSimpleSelection adapter. The children should provide database specific implementations of the template methods in the parent class. 

The index repository is similar to the structured repository in concept but deals with a totally different monster. It is an abstraction for index type databases like SOLR and ElasticSearch servers.  It provides create, update, delete and DSL adapter queries. 

```java
IndexQueryAdapter<Story> queryAdapter = repository.queryAdapter("news_en");

	List<Story> result = queryAdapter
				.queryAll()
				.filter("date")
				.gt(window)
				.sort("date", false)
				.result();
```

The above code sample creates query adapter for index core named "news_en". A date filter greater than a certain window is applied to a query-all operation. Similar to structured query adapter, the index query DSL is based on postfix format. A sort call based on the date is introduced at the end of the call chain. 

The index repository also introduces the concept of index porters. An index porter is an abstraction for an atomic index transaction executed on a single index core (type). There are three types of operations supported by the index porter whch are port, commit and shutdown. The port operation executed on an index core could be rolledback as long as commit not called. Now, let us look at the sequence diagram

![alt tag](https://cloud.githubusercontent.com/assets/6278849/7105215/8471a7a0-e10e-11e4-86ec-b1fa05d5f614.png)

Apache shiro is used as a mean of session clustering and user based security. Having shiro makes it possible to provide seamless user management between identical nodes.

The schedule bundle is an abstraction of time based services. The schedule bundle is built on top of quartz. It models sessions and tracks scheduling. A session is an operation that is only operable within a pre-defined time period. A track is an ever repeating task that works within hour, day, week or year time periods.

The oauth bundle provides an abstraction for any services accessible using the oauth protocol. Built on top of scribe, the oauth bundle should provide support for multiple social networks. The bundle models oauth services as authorizers and invokers. Authorizers are used to execute the 3-way handshake and obtain a valid oauth access tokens. Oauth invokers use access tokens, previously obtained by authorizers, to access resources protected by oauth providers.

Cradle framework is built on:
- OSGi. 
- Vertx.io. 
- Cassandra. 
- Ehcache 
- Apache SOLR. 
- Apache Shiro.
- Spring data. 
- LMAX disruptor. 
- Quartz. 
