/**
 @api {POST} /zeus/auditlog/getAuditLogs getAuditLogs
 @apiName AuditLog
 @apiGroup AuditLog
 @apiVersion 1.0.0

 @apiHeader {String} Device-Info-Zeus EXAMPLE: {"netType": 1, "appType": 2, "clientType": 2, "deviceName":"iphone 7", "osVersion":"ios 13.1", "appVersion":"18.8.1", "imei":"","mac":"", "idfa":"A9E9752D-66B2-4E17-9C3A-FC1CC57D8F58"}

 @apiParam {String} name
 @apiParam {String} password
 @apiParam {String} ssnToken 用户授权临时票据code
 @apiParam {String} phone

 @apiExample {curl} Example usage:
 curl -X POST
 https://api.qingshuxuetang.com/v6_3/account/login
 -H 'Accept: application/json'
 -H 'Authorization-ZS: eyJraWQiOiIwIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJuaWNrIjoi5p2O54Oo6Z2ZIiwiYXBwVHlwZSI6MiwibmFtZSI6ImRlbW9fbGl5ZWppbmdfc3R1ZGVudCIsImV4cCI6MTU0NTAxMTE5NywianRpIjoiMTAwIn0.eHuSRgId6Qk5J7q699lWGz4mwTCNZhDpaS6bYvWU-uE'
 -H 'Content-Type: application/json'
 -H 'User-Agent-QS: ZEUS'
 -H 'Device-Info-QS: {"netType": 1, "appType": 2, "clientType": 2, "deviceName":"iphone 7", "osVersion":"ios 13.1", "appVersion":"18.8.1", "imei":"","mac":"", "idfa":"A9E9752D-66B2-4E17-9C3A-FC1CC57D8F58"}'
 // netType 网络类型 0:没有网络 1:wifi 2:流量
 // appType app类型 1:QSXT 2:QSJS 3:COLLEGE
 // clientType 客户端类型 1:IOS 2:Android 3:pcweb 4:msite

 @apiParamExample {json} Request-Example 1:
 {
	    "type": 1,
	    "name": "17368695025",
	    "password": "123dd55"
	}

 @apiParamExample {json} Request-Example 2:
 {
	    "type": 2,
	    "phone": "17368695025",
	    "validation": {
	        "type": 1,
	        "sessionId": "23j2l3kl232oi3ioidjfakee",
	        "userInput": "384797"
	    }
	}

 @apiParamExample {json} Request-Example 3:
 {
	    "type": 3,
	    "ssnToken": "asdf8292022k22222j33hh",
	    "ssnType": 1
	}

 @apiSuccess {String} data authToken

 @apiSuccessExample {json} Success-Response:
 {
	    "hr": 0,
	    "message": "成功",
	    "data": "eyJraWQiOiIwIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJuaWNrIjoi5Y-25a2QMStwcGUiLCJhcHBUeXBlIjoyLCJuYW1lIjoicHhfeWV6aSIsImV4cCI6MTUzMzQzMjQwMCwianRpIjoiMTIwMTcxNSJ9.N-C7Gz_RCNyXEgdQZ1r6uDzPHSJwLI0qpjxBqVr4rug"
 }

 */
