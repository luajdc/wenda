package com.example.wenda.service;


import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId)))
            return 1;
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1:0;
    }

    public long like(int userId,int entityType,int entityId,int entityOwnerId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        String userLikeKey = RedisKeyUtil.getUserLikeKye(entityType,entityOwnerId);
        jedisAdapter.sadd(userLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }


    public long disLike(int userId,int entityType,int entityId,int entityOwnerId){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        String userLikeKey = RedisKeyUtil.getUserLikeKye(entityType,entityOwnerId);
        jedisAdapter.srem(userLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long getUserLikeCount(int entityType,int userId){
        String userLikeCount = RedisKeyUtil.getUserLikeKye(entityType,userId);
        return jedisAdapter.scard(userLikeCount);
    }
}
