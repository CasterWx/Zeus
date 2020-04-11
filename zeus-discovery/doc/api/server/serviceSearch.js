/**
 @api {POST} /zeus-server/service/queryService queryService
 @apiName ServiceDiscovery
 @apiGroup ServiceDiscovery
 @apiVersion 1.0.0

 @apiExample {curl} Example usage:
 curl -X POST
 https://api.zeus.com/zeus-server/service/queryService
 -H 'Accept: application/json'


 @apiSuccessExample {json} Success-Response:
 [
        {
                "id":1,"serviceName":"domain","serviceAddr":"127.0.0.1",
                "port":8888,"living":1,"comment":"管理服务"
        },{
                "id":2,"serviceName":"RpcDemo","serviceAddr":"127.0.0.1",
                "port":8881,"living":1,"comment":"远程服务"
        }
 ]
 */
