# learning-rabbitmq

RabbitMQ 快速入门

RabbitMQ 3.7.4 + Erlang 20.3.*

## 简单队列

http://www.rabbitmq.com/tutorials/tutorial-one-java.html

* hello-send
* hello-recv

## Work 队列

普通队列，多个接收端共同接受消息。

http://www.rabbitmq.com/tutorials/tutorial-two-java.html

* work-task
* work-worker

## 发布订阅模式

使用 fanout exchange，fanout 会自动忽略 routingkey。

http://www.rabbitmq.com/tutorials/tutorial-three-java.html

* pubsub-emit
* pubsub-recv

## 路由模式

使用 direct exchange，通过 routingkey 的绑定来实现消息的路由分发。

http://www.rabbitmq.com/tutorials/tutorial-four-java.html

* routing-emit
* routing-recv

## 主题模式

使用 topic exchange，通过 routringkey 的模糊匹配来分发。

http://www.rabbitmq.com/tutorials/tutorial-five-java.html

* topic-emit
* topic-recv

