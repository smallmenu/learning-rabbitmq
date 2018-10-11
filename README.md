# learning-rabbitmq

RabbitMQ 快速入门

RabbitMQ 3.7.4 + Erlang 20.3.*

## 基本概念

* vhost。RabbitMQ 支持配置多 vhost 虚拟主机功能，每一个 vhost 就相当于一个 Mini RabbitMQ，均拥有着属于自己的队列、交换机和绑定。而且不同 vhost 之间的命名空间彼此独立、互相隔离，有效的解决了命名冲突的问题
* Exchange。理解为交换机，是消息到达的第一站，并根据 Binding 规则将消息路由给队列。ExchangeType 决定了 Exchange 路由消息的行为，在RabbitMQ中，有 Direct、Fanout和 Topic 三种
* RoutingKey。路由关键字，Exchange 会根据这个关键字进行消息的投递。
* Queue。队列，用于存放消息，每个消息会被投递到一个或多个队列中。
* channel。消息通道，在客户端的每个连接里，可建立多个channel，每个channel代表一个会话任务。

## 示例

### 简单队列

参考：http://www.rabbitmq.com/tutorials/tutorial-one-java.html

快速入门，示例实现最简单的队列（直接声明队列，不需要声明 Exchange，这样会使用 RabbitMQ 的默认空 Exchange，并且 routingkey 作为队列名称）

不具有持久化功能，会自动 ack 确认消息

* hello-send
* hello-recv

### Work 模式

与简单队列类似，示例实现具有持久化队列功能的队列，多个消费者可以公平的共同接收处理消息，并手动 ack 确认

参考：http://www.rabbitmq.com/tutorials/tutorial-two-java.html

* work-task
* work-worker

### 发布/订阅模式

声明 Fanout Exchange，Fanout 类型会自动忽略掉 routingkey。

消费者接收端需要做队列与 Exchange 的绑定，如果绑定了多个队列，就形成了发布订阅模式

示例实现了一个匿名的，非持久化的，独占的，自动删除的临时队列，这样无论何时我们连接 RabbitMQ，都会是从空的队列开始

使用场景：

* 对简单队列进行了扩展，当只绑定了一个队列，实际会退化成简单队列一样的效果
* 考虑未来可能会对消息进行多种不同的处理，比如一个收集 log 的队列，现阶段可能只做日志统计，但未来可能还会对 log 进行分析

参考：http://www.rabbitmq.com/tutorials/tutorial-three-java.html

* pubsub-emit
* pubsub-recv

### 直接（路由）模式

声明 Direct Exchange，指定 routingkey 来实现消息的路由分发。

消费者接收端需要队列做 Exchange 的绑定，同时指定 routingkey。

使用场景：

* 比如一个收集 log 的队列，我们希望只接受消息的子集，比如分离 log.error 与 log.info， 然后针对不同的子集进行处理（使用routingkey）
* 可以使用多个队列，绑定相同的 routingkey，这样可实现了具有路由的发布/订阅模式

参考：http://www.rabbitmq.com/tutorials/tutorial-four-java.html

* direct-emit
* direct-recv

### 主题模式

使用 Topic Exchange，通过 routringkey 的模糊匹配来实现路由的分发。

使用场景：

* 在一个复杂的多系统环境，比如有 mq.log.error, web.log.error, mq.log.info, web.log.info 的消息，Topic 模式可以更灵活的定制我们接收到的消息

参考：http://www.rabbitmq.com/tutorials/tutorial-five-java.html

* topic-emit
* topic-recv

## 集成 Springboot

参考我的另一个项目：

https://github.com/smallmenu/learning-springboot/tree/master/rabbitmq



