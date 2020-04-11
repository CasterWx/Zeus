/**
 @api {POST} /oauth/token getToken
 @apiName token
 @apiGroup token
 @apiVersion 1.0.0

 @apiExample {curl} Example usage:
 curl -X POST
 --user user:password
 -D "username=user1&password=123456&grant_type=password&scope=read write"
 https://api.zeus.com/oauth/token
 -H 'Accept: application/x-www-form-urlencoded'

 @apiParam {String} username 用户名
 @apiParam {String} password 密码
 @apiParam {String} grant_type token类型
 @apiParam {String} scope 权限


 @apiSuccessExample {json} Success-Response:
 {
    "access_token": "620e5f04-5ae1-4bca-99f2-a16c4614fade",
    "token_type": "bearer",
    "refresh_token": "171b085c-0383-4308-8dd4-19117aeb73d0",
    "expires_in": 3599,
    "scope": "read write"
}

 @apiErrorExample {json} Error-Response:
 {
    "timestamp": "2020-04-08T09:48:29.751+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Unauthorized",
    "path": "/oauth/token"
}
 */
