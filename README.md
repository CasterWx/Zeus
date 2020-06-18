<p align="center">
<img src="https://images.cnblogs.com/cnblogs_com/LexMoon/1524728/o_191231122929logo.png" alt="Zeus">
</p>

![](https://img.shields.io/badge/zeus-2.0-orange)       ![](https://img.shields.io/badge/license-apache-brightgreen)

### 介绍

Zeus可以用于API网关，服务发现，服务调用，熔断限流，日志统计。

理念是用最简单的方式使用。

(该项目的另一种思路，实现最强大的后台代理，高可用的翻墙服务，https://github.com/CasterWx/Back-end-Broker)


#### API网关

运行部署zeus-gateway项目，分别添加需要的前置/后置过滤器。

访问： 

http://api.zeus.com/zeus-gateway/api/{serviceName}/{serviceRequestPath}?params

#### 服务注册

![](https://img.shields.io/badge/%40-ZeusRegistry-red)

引入`zeus-client`模块，在启动类添加`@ZeusRegistry`标签即可。

* serverName ：服务名
* serverAddr ：注册中心地址
* comment ：服务描述

```java
@SpringBootApplication
@ZeusProperty(serverName = "domain", 
    serverAddr = "127.0.0.1", comment = "管理服务")
public class ZeusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }
}
```

#### 服务发现

![](https://img.shields.io/badge/Get-%2Fservice%2FqueryService-red)

* https://api.zeus.com/zeus-server/service/queryService

> requestMethod : `Get`

获取所有集群名。

```json
[
    {
        "id": 1,
        "serviceName": "domain",
        "serviceAddr": "127.0.0.1",
        "port": 8888,
        "living": 1,
        "comment": "管理服务"
    },
    {
        "id": 2,
        "serviceName": "RpcDemo",
        "serviceAddr": "127.0.0.1",
        "port": 8881,
        "living": 1,
        "comment": "远程服务"
    }
]
```


#### 接口监控

![](https://img.shields.io/badge/%40-FlowMonitor-red)

为你的方法或是API添加`@FlowMonitor(name)`注释，你的该方法访问将会得到记录。

```java
@RequestMapping(value = "/hello")
@FlowMonitor(name = "hello")
public String hello() {
    return "Hello! " ;
}
```

#### Rpc远程调用

![](https://img.shields.io/badge/%40-RpcService-red)  ![](https://img.shields.io/badge/RpcClient-addr,port-red) 

存在一个server-1，地址为`localhost:8080`，还存在一个server-2，地址为`localhost:8090`。

server-1中有一个方法，假如它的作用是查询数据库返回用户信息，我们就可以给这个service类加上`@RpcService`方法来启动远程调用。

```java
@RpcService
public class DoSomethingImpl implements DoSomething {

    private static List<String> users = new ArrayList<>();

    @Override
    public List<String> doHello() {
        users.add("user-1");
        users.add("user-2");
        users.add("user-3");
        return users;
    }
}
```

在server-2中如果我们想要调用server-1的doHello方法获取用户信息，就可以创建一个简单的RpcClient来调用。

```java

@RestController
public class HelloService {

    @RequestMapping(value = "/hello")
    public String hello() throws InterruptedException {
        RpcClient rpcClient = new RpcClient();
        rpcClient.doConnect("localhost", 18868); // port默认均为18868
        Object object = rpcClient.send(new RpcRequest("1", "com.antzuhl.zeusdemo2.service.impl.DoSomethingImpl", "doHello", null, null));

        return object.toString();
    }
}
```

此时访问这个`/hello`接口，可以得到返回的json结果。

```json
{"code":0,"data":["user-1","user-2","user-3"],"requestId":"1"}
```

![](https://images.cnblogs.com/cnblogs_com/LexMoon/1524728/o_191231123606request.png)

RpcRequest中参数为消息ID，实例类，方法，参数类型列表，参数值列表。
