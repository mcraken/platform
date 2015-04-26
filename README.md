# Cradle Platform

You can use cradle to create scaleable and containerless applications. One of the objectives of the platform is to provide out of the box capabilities which are essential for building scaleable and clustered applications. Osgifing vertx made it possible to provide some sort of integration between vertx modules and OSGi bundles through the event bus. The ployglot nature of Vertx made it possible to work with different languages other than java. 

Now, we will tap briefly on each bundle of the cradle framework.

## Gateway

The gateway bundle exposes its services through the gateway interface which gives the capability for clients to register handlers and filters. Here is a sample of how to run a standalone platform and regitser a controller. 
```java
public class SocketMessage {
	private String sender;
	private String text;
	public String getSender() {
		return sender;
	}
	public String getText() {
		return text;
	}

}

class Calculation {
	private int num1;
	private int num2;
	private int result;
	
	public void calcResult() {
		this.result = num1 * num2;
	}
}	
	
class HelloWorldController{
	@HttpMethod(method = Method.GET, path="/hello")
	public String sayHello(org.cradle.platform.httpgateway.HttpAdapter adapter){
		return "Hello, World!";
	}
	
	@HttpMethod(method = Method.POST, path="/calc")
	public Calculation multiply(HttpAdapter adapter, Calculation document){
		
		document.calcResult();
		
		return document;
	}
	
	@HttpMethod(method = Method.MULTIPART_POST, path="/form")
	public Calculation submitForm(HttpAdapter adapter, Calculation form, List<File> files){
		return multiply(adapter, form);
	}
	
	@WebSocket(type=Type.SYNCHRONOUS, path="/socketcalc")
	public Calculation multiplySocket(HttpAdapter adapter, Calculation document){
		
		document.calcResult();
		
		return document;
	} 
	
	@WebSocket(type=Type.RECEIVER, path="/message")
	public void sayHello(HttpAdapter adapter, SocketMessage message){
		
		System.out.println(message.getSender() + ":" + message.getText());
	}
	
	@HttpFilter(pattern="^/(.)*")
	public void filterAny(HttpAdapter adapter){
		System.out.println("Should execute before any http method first");
	}
	
	@HttpFilter(pattern="^/hello", precedence=1)
	public void filterHello(HttpAdapter adapter){
		System.out.println("Should execute before /hello second");
	}
}

CradlePlatform platform = VertxCradlePlatform.createDefaultInstance();
CradleProvider httpGateway = platform.httpGateway();
CradleProvider websocketGateway = platform.sockJsGateway();
HelloWorldController controller = new HelloWorldController();
httpGateway.registerController(controller);
websocketGateway.registerController(controller);
Thread.sleep(2 * 60 * 1000);
platform.shutdown();
```
The above code creates and registers a controller class which exposes three methods:
- sayHello, which controlls GET requests directed to /hello. It returns a string.
- multiply, which controlls POST requests directed to /calc. It returns a JSON representation of Calculation instance.
- multiplySocket, which is a websocket that does the same as multiply. 
- sokcetMessage, which is an oiutput only socket controller.

The path on which the service is accessible is /hello. In just easy 3 line we have a fully operational webservice endpoint running on the highly performant Vertx http server. The default vertx implementation of the cradle platform listenes to port 8080 on localhost. The operational URL of the above example is http://localhost:8080. Try it for yourself.

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

Finally, that concludes our brief introduction to the repository bundle. 

## Schedule

The schedule bundle is an abstraction of time based services. The schedule bundle is built on top of quartz. You can use the services provided by the schedule bundle when you need timed operations that works seamlessly within your system without resorting to DB intergration and / or messaging systems. At the heart of the schedule bundle is the ScheduleService. The schedule service exposes two types of operations buildJob and removeJob. The job building call can be configured by a replacement flag which determines whether the job should replace a current job with the same name and the same parent schedule.

```java
JobBuildResult result = scheduleService.buildJob(jobName, jobSchedule, jobModel, true);
if(!result.scheduled){

	JobBuilder jobBuilder = result.builder;
	jobBuilder
		.daily(hour, minute)
		.scheulde(calendarName);
}
```
The boolean variable at the end of the buildJob function call serves as the replacement flag. The jobSchedule is the parent schedule of a collection of jobs. JobBuildResult is a simple DTO which holds the scheduling state. In case of the replacement flag set to true, a new job will be created and the JobBuilder instance inside the JobBuildResult will be set to null. Otherwise, JobBuildResult.scheduled will be set to false and JobBuilder instance will be included inside the JobBuildResult. 

JobBuilder calls are chainable. In the example above, a daily task is scheduled to run in a certain hour and minute of the day modified by a custom calendar. Take a look at the [JobBuilder](schedule/src/main/java/org/cradle/schedule/JobBuilder.java) interface for more information.

The good thing about the schedule bundle that it provides number of classes that model different scheduling techniques out of the box. A track is an ever repeating task that works within hour, day, week or year time periods. Track is a abstract class that provides three template methods init, schedule and execute. A WeeklyTrack is an abstract class that extends Track and provides an implementation for the schedule function. 

![alt tag](https://cloud.githubusercontent.com/assets/6278849/7105494/6078d558-e11d-11e4-9855-b5b3114c3b03.png)

A session is an operation that is only operable within a pre-defined time period.
