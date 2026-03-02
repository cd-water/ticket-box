local key = KEYS[1]
local code = ARGV[1]
local codeTTL = tonumber(ARGV[2])

if redis.call('EXISTS', key) == 1 then
    local remainingTTL = redis.call('TTL', key)
    if remainingTTL >= codeTTL - 60 then
        return 1 -- 触发验证码防刷
    end
end

redis.call('SET', key, code, 'EX', codeTTL)
return 0 -- 成功保存验证码