local seatKey = KEYS[1]

for i = 1, #ARGV do
    local hasLocked = redis.call("GETBIT", seatKey, tonumber(ARGV[i]))
    if hasLocked == 1 then
        return 1 -- 座位已锁定
    end
end

for i = 1, #ARGV do
    redis.call("SETBIT", seatKey, tonumber(ARGV[i]), 1)
end

return 0