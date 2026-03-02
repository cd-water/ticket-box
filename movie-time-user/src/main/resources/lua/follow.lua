local followerKey = KEYS[1]
local fanKey = KEYS[2]
local targetId = ARGV[1]
local userId = ARGV[2]

if redis.call('SISMEMBER', followerKey, targetId) == 1 then
    redis.call('SREM', followerKey, targetId)
    redis.call('SREM', fanKey, userId)
    return 0 -- 已关注，进行取关
else
    redis.call('SADD', followerKey, targetId)
    redis.call('SADD', fanKey, userId)
    return 1 -- 未关注，进行关注
end