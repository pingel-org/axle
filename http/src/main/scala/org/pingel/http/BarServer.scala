package org.pingel.http

import org.scalatra._
import com.mongodb.casbah.Imports._


class BarServer extends ScalatraServlet {

	final val mongoHost = "localhost"
	final val mongoPort = 27017
	final val mongoDb = "test"
	final val mongoCollection = "bar"
  
	val bars = MongoConnection(mongoHost,mongoPort)(mongoDb)(mongoCollection)
	
	get("/") {
		<h1>Hello, world!</h1>
	}

  post("/bars") {
    val builder = MongoDBObject.newBuilder
    params.get("body").foreach(bar => {
      builder += ("body" -> bar)
      bars += builder.result
    })
    redirect("/bars")
  }	
	
  get("/bars") {
    <body>
      <ul>
        {for (bar <- bars) yield <li>{bar.get("body")}</li>}
      </ul>
      <form method="POST" action="/bars">
        <input type="text" name="body"/>
        <input type="submit"/>
      </form>
    </body>
  }	
}
