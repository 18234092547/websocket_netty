package net;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangjianghong on 2017-3-16.
 */
public class TestSingleRedis {
    //单例
    private  static Jedis jedis;
    //分片 提高了并行 sharding机制：即通常所说的“分片”，允许数据存放在不同的物理机器上
    private static ShardedJedis sharde;
    //池化
    private static ShardedJedisPool pool;

    public  static void StartRedis(){
        //单个节点
        jedis = new Jedis("127.0.0.1",6379);

        //分片 可以有一组 多个实例 这里用一个
        List<JedisShardInfo> shards = Arrays.asList(new JedisShardInfo("127.0.0.1",6379));
        sharde = new ShardedJedis(shards);
        GenericObjectPoolConfig gcConfig = new GenericObjectPoolConfig();
        //最大空闲连接数
        gcConfig.setMaxIdle(100);
        //最大连接数
        gcConfig.setMaxTotal(20);
        gcConfig.setMaxWaitMillis(-1);
        //在获取连接的时候检查有效性
        gcConfig.setTestOnBorrow(true);
        //把配置和分片连一起
        pool = new ShardedJedisPool(gcConfig,shards);
    }
    public static Jedis getJedis(){
        return jedis;
    }

//    public static void jedisStop(){
//        jedis.disconnect();
//        sharde.disconnect();
//        pool.destroy();
//    }

}
