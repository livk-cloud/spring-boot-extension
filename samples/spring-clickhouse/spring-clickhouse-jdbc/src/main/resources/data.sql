create table if not exists user
(
    id       Int32,
    app_id   String,
    version  String,
    reg_time Date
) engine = MergeTree PARTITION BY toYYYYMM(reg_time)
      ORDER BY id
      SETTINGS index_granularity = 8192;
