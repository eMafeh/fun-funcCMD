import cn.migu.dangjian.gbdh.command.ioc.bean.base.BaseVo;
import cn.migu.dangjian.gbdh.constants.RedisConstants;
import cn.migu.dangjian.gbdh.dto.MatcherResult;
import cn.migu.dangjian.gbdh.websocket.cmd.matcher.annotation.MatchPool;
import cn.migu.dangjian.gbdh.websocket.session.NettyWebsocketChannelClient;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RTopic;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 个人赛匹配
 *
 * @author Mingfei
 * @version Created in 7/27/2018
 */
@MatchPool("Personal")
public class ZSPKPersonalMatcherPool extends ZSPKBaseMatcherPool {
    private static final Logger logger = LoggerFactory.getLogger("matcher");

    @Value("${match.personalMatchMaxTime}")
    private int matchMaxTime;

    @Value("${gbdh.powerDecrFactor}")
    private int powerDecrFactor;

    @Override
    public void joinMatchPool(NettyWebsocketChannelClient channelClient, BaseVo vo)
            throws InterruptedException {
        /*//校验当前体力值
        if (!hasEnoughPower(channelClient))
        {
            String flag = redissonUtil.get(RedisConstants.UPDATE_POWER + channelClient.getUid()).getData();
            if(null == flag)
            {
                flag = "0";
                redissonUtil.set(RedisConstants.UPDATE_POWER + channelClient.getUid(), flag);
            }
            channelClient.send(new BaseVo("Match", "JoinMatchFailed", new JoinMatchResponse(false,flag,"体力值不足"
            )));
            return;
        }*/

        //删除上局对战监听，监听匹配结果
        channelClient.uninstallBattleListener(redissonUtil);
        //监听匹配结果
        listenMatchPool(channelClient, vo);

        Long uid = channelClient.getUid();
        String shortId = channelClient.getChannelShortId();

        RBlockingQueue<String> waitQueue = redissonUtil.getBlockingQueue(RedisConstants.WAIT_QUEUE);
        String userSerial = uid + "";

        redissonUtil.getList(MATCHER_QUIT_LIST).remove(uid);
        clear(channelClient, userSerial);
        waitQueue.put(userSerial);
        logger.info("[{}]|用户[{}]加入等待队列[{}]", shortId, userSerial, waitQueue);
    }

    /**
     * 校验用户体力值
     *
     * @param client NettyWebsocketChannelClient
     * @return 校验用户体力值结果
     */
    private boolean hasEnoughPower(NettyWebsocketChannelClient client) {
        String userPKInfoKey = MessageFormat.format(RedisConstants.PK_USER, client.getUid());
        RMap<String, Integer> userInfo = redissonUtil.getMap(userPKInfoKey);
        Integer power = userInfo.get("power");
        logger.debug("用户体力值:[{}]", power);
        if (null == power || power < powerDecrFactor) {
            return false;
        }

        return true;
    }

    @Override
    public void quitMatchQueue(NettyWebsocketChannelClient channelClient) {

        Long uid = channelClient.getUid();
        String shortId = channelClient.getChannelShortId();

        String userSerial = channelClient.getUid() + "";
        clear(channelClient, userSerial);

        logger.info("[{}]|用户[{}]退出清理工作完成！", shortId, uid);
    }

    private void clear(NettyWebsocketChannelClient channelClient, String userSerial) {
        Long uid = channelClient.getUid();
        String shortId = channelClient.getChannelShortId();

        //清除等待队列
        RBlockingQueue<String> waitQueue = redissonUtil.getBlockingQueue(RedisConstants.WAIT_QUEUE);
        if (waitQueue.contains(userSerial)) {
            logger.debug("[{}]|用户[{}]已在等待队列中，将其从队列中移除", shortId, uid);
            waitQueue.remove(userSerial);
        }

        //清除匹配队列
        String matchQueueKey = MessageFormat.format(RedisConstants.PK_MATCH_QUEUE, uid);
        redissonUtil.delete(matchQueueKey);

        //清除匹配集合
        RScoredSortedSet<String> matchSet =
                redissonUtil.getScoredSortedSet(RedisConstants.PK_MATCH_TOTAL_SET, StringCodec.INSTANCE);
        List<ScoredEntry<String>> entries = new ArrayList<>(matchSet.entryRange(0, matchSet.size() - 1));
        Iterator<ScoredEntry<String>> it = entries.iterator();
        while (it.hasNext()) {
            ScoredEntry<String> entry = it.next();
            String matchSerial = entry.getValue();
            if (matchSerial.equals(userSerial)) {
                matchSet.remove(matchSerial);
            }
        }
        logger.debug("[{}]|清除用户[{}]匹配遗留数据完成", shortId, userSerial);
    }

    @Override
    protected RTopic<MatcherResult> getMatchResultTopic() {
        return redissonUtil.getTopic(MATCHER_NOTIFY_PERSONAL_TOPIC);
    }

    protected void delCache() {
        redissonUtil.delete(RedisConstants.WAIT_QUEUE);
        redissonUtil.delete(RedisConstants.WAITING_QUEUE);
        redissonUtil.delete(RedisConstants.PK_MATCH_TOTAL_SET);
        redissonUtil.delete(RedisConstants.PK_MATCH_SWITCH_MAP);
        logger.debug("清除小组赛匹配遗留数据完成！");
    }

    public static void newQueue(final RedissonUtil redissonUtil, final RScoredSortedSet<String> matchSet, final RMap<String, Integer> switchMap, final String uid, final Long time) {
        //不是真的用户不允许开房
        if (StringUtils.isEmpty(uid) || Long.parseLong(uid) <= 0) {
            return;
        }
        // 该用户自己开房
        String matchQueueKey = MessageFormat.format(RedisConstants.PK_MATCH_QUEUE, uid);
        // 旧房间删了
        redissonUtil.delete(matchQueueKey);
        // 新房间创建
        RBlockingQueue<String> matchQueue = redissonUtil.getBlockingQueue(matchQueueKey);
        // 放入新房间成为房主
        matchQueue.add(uid);
        // 这个房主也想找人了
        matchSet.add(time, uid);

        //这个房间开始找人了
        switchMap.put(uid, 1);
        logger.info("[{}]|用户[{}]新建了匹配队列", unique, uid);
    }
}
