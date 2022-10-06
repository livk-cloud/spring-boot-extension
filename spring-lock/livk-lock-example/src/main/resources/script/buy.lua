local key = KEYS[1]
local numKey = KEYS[2]
local buySucCountKey = KEYS[3]
local buyCount = KEYS[4]
local count = tonumber(ARGV[1])
redis.call('HINCRBY',key,buyCount,1)
local num = tonumber(redis.call('HGET',key,numKey))
if(num <= 0 or num < count) then
    return 0
else
    redis.call('HSET',key,numKey,num-count)
    redis.call('HINCRBY',key,buySucCountKey,1)
    return 1
end
