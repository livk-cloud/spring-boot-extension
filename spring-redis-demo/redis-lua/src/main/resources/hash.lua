local result = redis.call('hmset', KEYS[1], 'code', ARGV[1], 'expire', ARGV[2])
if result['ok'] == 'OK' then
    return 0
else
    return 1
end
