<p align="center">
<img src="https://images.cnblogs.com/cnblogs_com/LexMoon/1524728/o_191231122929logo.png" alt="Zeus">
</p>

![](https://img.shields.io/badge/zeus-1.0-orange)       ![](https://img.shields.io/badge/license-apache-brightgreen)

### 介绍

Zeus可以用于服务发现，服务治理，负载均衡，服务容错，服务调用，API网关，配置中心。

理念是用最简单的方式使用。

(项目更多功能正在开发中)

[让我知道您正在使用Zeus。](https://github.com/CasterWx/Zeus/issues/1)

#### 服务注册

![](https://img.shields.io/badge/%40-ZeusRegistry-red)

引入`zeus-client`模块，在启动类添加`@ZeusRegistry`标签即可。

* registryName ：命名空间(集群管理)
* zkAddr ：zookeeper地址
* serverName ：服务名称
* serverAddr ：服务注册地址

```java
@SpringBootApplication
@ZeusRegistry(registryName = "user-center", zkAddr = "192.168.124.16:2181",
        serverName = "server-1", serverAddr = "48.89.13.53:8080")
public class ZeusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }
}
```

#### 服务发现

![](https://img.shields.io/badge/Get-%2Fv1%2Fnode%2Fnamespaces-red)

* http://localhost:5454/v1/node/namespaces

> requestMethod : `Get`

获取所有集群名。

```json
{
  "code":200,
  "message":"OK",
  "data":[
    "user-center2","user-center1","user-center3"
  ]
}
```

![](https://img.shields.io/badge/Get-server-red)

* http://localhost:5454/v1/node/server?namespace=param

> requestMethod : `Get`

获取指定集群下的所有服务节点信息。

> response

```json
{
  "code":200,
  "message":"OK",
  "data":[{
    "nameSpace":"user-center1",
    "serverName":"server-1",
    "data":"126",
    "path":"/registry/user-center1/server-1"
    }]
  }
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

