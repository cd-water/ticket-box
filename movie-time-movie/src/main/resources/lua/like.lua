local likeUserKey = KEYS[1]
local userId = ARGV[1]

if redis.call('SISMEMBER', likeUserKey, userId) == 1 then
    redis.call('SREM', likeUserKey, userId)
    return 0 -- 已点赞，取消点赞
else
    redis.call('SADD', likeUserKey, userId)
    return 1 -- 未点赞，点赞
end