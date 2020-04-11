/**
 @api {POST} /zeus/service/ping ping
 @apiName Service
 @apiGroup Service
 @apiVersion 1.0.0

 @apiHeader {String} Device-Info-Zeus EXAMPLE: {"netType": 1, "appType": 2, "clientType": 2, "deviceName":"iphone 7", "osVersion":"ios 13.1", "appVersion":"18.8.1", "imei":"","mac":"", "idfa":"A9E9752D-66B2-4E17-9C3A-FC1CC57D8F58"}

 @apiExample {curl} Example usage:
 curl -X POST
 https://api.zeus.com/zeus/service/ping
 -H 'Accept: application/json'
 -H 'Authorization-ZS: eyJraWQiOiIwIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJuaWNrIjoi5p2O54Oo6Z2ZIiwiYXBwVHlwZSI6MiwibmFtZSI6ImRlbW9fbGl5ZWppbmdfc3R1ZGVudCIsImV4cCI6MTU0NTAxMTE5NywianRpIjoiMTAwIn0.eHuSRgId6Qk5J7q699lWGz4mwTCNZhDpaS6bYvWU-uE'
 -H 'Content-Type: application/json'
 -H 'User-Agent-QS: ZEUS'

 @apiSuccessExample {json} Success-Response:
 {
    "code":200,
    "message":"服务存活",
    "data":{
        "serviceId":null,
        "serviceName":"domain",
        "serviceAddr":"47.35.124.54",
        "living":1,
        "comment":"订单服务"
        }
}
 */
