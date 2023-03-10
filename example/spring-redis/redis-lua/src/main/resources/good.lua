local key = KEYS[1];
local subNum = tonumber(ARGV[1]);
local surplusStock = tonumber(redis.call('get', key));
if (surplusStock <= 0) then
    return 0
elseif (subNum > surplusStock) then
    return 1
else
    redis.call('incrby', key, -subNum)
    return 2
end
