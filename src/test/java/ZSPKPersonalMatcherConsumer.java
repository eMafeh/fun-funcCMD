import cn.migu.dangjian.gbdh.constants.RedisConstants;
import cn.migu.dangjian.gbdh.dto.MatcherResult;
import cn.migu.dangjian.gbdh.utils.IdUtil;
import cn.migu.dangjian.gbdh.utils.NetUtils;
import cn.migu.dangjian.gbdh.utils.RedissonUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cn.migu.dangjian.gbdh.websocket.cmd.matcher.ZSPKMatchPool.MATCHER_QUIT_LIST;

/**
 * 消费者
 *
 * @author Mingfei
 * @version Created in 7/28/2018
 */
@Service
public class ZSPKPersonalMatcherConsumer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("matcher");

    private static String consumerLockPath = "/PersonalMatchPoolStarter" + "Consumer";

    //排位赛Topic格式
    private static String PERSONAL_TOPIC = "BGBDH_PERSONAL_{0}_{1}_{2}";

    private static final String MATCHER_NOTIFY_PERSONAL_TOPIC = "BGBDH:Match-PERSONAL:MatchResult";

    //玩家匹配过的对手列表，{0}：userId
    String BGBDH_MATCH_RECORD = "BGBDH:MATCH:RECORD:{0}";

    private static final Integer workerId = 15;

    private static final Integer dataCenterId = 15;

    private static final String AI_USER_ID = "-1";

    @Value("${match.personalMatchMaxTime}")
    private int matchMaxTime;

    @Autowired
    protected CuratorFramework zkClient;

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private ZSPKPersonalMatcherPool zspkPersonalMatcherPool;

    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private ZoneId zone = ZoneId.systemDefault();

    //开了的房
    private RScoredSortedSet<String> matchSet;
    //房间的状态
    private RMap<String, Integer> switchMap;
    //强制退出的玩家
    private RList<Long> quitList;
    //万能队列
    private RBlockingQueue<String> queue;

    @Override
    public void afterPropertiesSet() {
        try {
            startService();

        } catch (Exception e) {
            logger.info("排位赛匹配消费者线程已经运行，重启", e);
            try {
                String host = new String(zkClient.getData().forPath(consumerLockPath));
                logger.info("排位赛匹配消费者线程启动锁host[{}]", host);
                if (NetUtils.getLocalHost().equals(host)) {
                    zkClient.delete().forPath(consumerLockPath);
                    startService();
                }
            } catch (Exception e1) {
                logger.error("排位赛匹配消费者线程重新获取启动锁失败", e1);
            }
        }
    }

    /**
     * 尝试启动排位赛线程,zk加 ip锁 防止重入
     */
    private void startService() throws Exception {
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(consumerLockPath);
        zkClient.setData().forPath(consumerLockPath, NetUtils.getLocalHost().getBytes());

        zspkPersonalMatcherPool.delCache();

        logger.info("启动排位赛匹配消费者线程[{}]", NetUtils.getLocalHost());
        matchSet = redissonUtil.getScoredSortedSet(RedisConstants.PK_MATCH_TOTAL_SET, StringCodec.INSTANCE);
        switchMap = redissonUtil.getMap(RedisConstants.PK_MATCH_SWITCH_MAP);
        quitList = redissonUtil.getList(MATCHER_QUIT_LIST);
        queue = redissonUtil.getBlockingQueue(RedisConstants.WAITING_QUEUE);
        Executors.newFixedThreadPool(1).submit(this::matchAndNotify);
    }

    private void matchAndNotify() {
        while (true) {
            //所有开了的房间
            List<ScoredEntry<String>> entries = new ArrayList<>(matchSet.entryRange(0, matchSet.size() - 1));
            for (final ScoredEntry<String> entry : entries) {
                String id = entry.getValue();
                //不存在的,不缺人的房间
                if (!switchMap.containsKey(id) || switchMap.get(id) < 1) {
                    continue;
                }
                //该轮对战 唯一标识
                long unique = IdUtil.getInstance(workerId, dataCenterId).nextId();
                try {
                    matchOne(id, unique);
                } catch (InterruptedException e) {
                    logger.error("[{}]|小组赛匹配异常", unique, e);
                    Thread.currentThread().interrupt();//TODO 匹配异常直接结束线程
                }
            }

            //队列太长 不开更多对战了,GG思密达
            if (matchSet.size() > Integer.MAX_VALUE - 1) {
                break;
            }
        }
    }

    /**
     * @param id     房间创建人&房间 唯一标识
     * @param unique 这次组对的唯一标示
     */
    private void matchOne(String id, final long unique) throws InterruptedException {
        String key = MessageFormat.format(RedisConstants.PK_MATCH_QUEUE, id);
        //房间里的人
        RBlockingQueue<String> matchQueue = redissonUtil.getBlockingQueue(key);

        logger.info("[{}]|等待用户进入匹配队列[{}][{}]...", unique, id, matchQueue);
        //当前房主
        String firstMatcher = matchQueue.poll(1, TimeUnit.SECONDS);
        logger.debug("[{}]|firstMatcher[{}]进入匹配队列, matchQueue[{}], 等待secondMatcher...",
                unique, firstMatcher, matchQueue);

        //当前房主不存在?
        if (firstMatcher == null) {
            return;
        }

        //标记开始等待的时间
        long begin = System.currentTimeMillis();
        //在最大等待时间内等待房老二
        String secondMatcher = matchQueue.poll(matchMaxTime, TimeUnit.SECONDS);
        //等到房老二了,大厅移除这间房子
        switchMap.remove(id);
        logger.debug("[{}]|等待结束，firstMatcher[{}], secondMatcher[{}]", unique, firstMatcher, secondMatcher);

        MatchUsers matchUsers = new MatchUsers(firstMatcher, secondMatcher);
        //递归校验加重新匹配
        if (!matchRecursion(matchUsers, begin, unique)) {
            //玩家全都跑路了,就算了
            return;
        }

        //发布匹配结果
        MatcherResult result = new MatcherResult();
        String topic = MessageFormat.format(PERSONAL_TOPIC, getTime(), matchUsers.user1, matchUsers.user2);
        result.setMatcherTopic(topic);
        result.setFirstMatcher(toLong(matchUsers.user1));
        result.setSecondMatcher(toLong(matchUsers.user2));

        redissonUtil.getTopic(MATCHER_NOTIFY_PERSONAL_TOPIC).publish(result);
        //互相标记玩家今日对战列表
        markerAntiSpam(matchUsers);
        logger.info("[{}]|排位赛匹配结果[Topic={},u1={},u2={}]", unique, topic, matchUsers.user1, matchUsers.user2);
    }

    private Boolean antiSpam(MatchUsers matchers) {
        Assert.isTrue(matchers.user1 == null || matchers.user2 == null, "请检查代码,不允许调用该方法时,有用户不存在");

        //玩家1的今日对战列表
        RList<Long> firstList = getTodayList(matchers.user1);

        //计算用户1的列表里有几个用户2
        int count = 0;
        Long second = toLong(matchers.user2);
        for (Long user : firstList) {
            if (user.equals(second)) {
                count++;
            }
        }
        //超过限制,命中规则
        if (count >= 2) {
            //玩家二自己开新房咯~
            ZSPKPersonalMatcherPool.newQueue(redissonUtil, matchSet, switchMap, matchers.user2, System.currentTimeMillis());
            //玩家二踢出该房间
            matchers.user2 = null;
            return true;
        }

        //没有超过限制
        return false;
    }

    private void markerAntiSpam(MatchUsers matchers) {
        Assert.isTrue(matchers.user1 == null || matchers.user2 == null, "请检查代码,不允许调用该方法时,有用户不存在");
        long millis = getNextEarlyMorning();
        //玩家1的今日对战列表加入玩家二
        getTodayList(matchers.user1, millis).add(toLong(matchers.user2));
        //玩家2的今日对战列表加入玩家一
        getTodayList(matchers.user2, millis).add(toLong(matchers.user1));
    }

    /**
     * 校验玩家里是否有不存在的或是强退的
     * 玩家已经强退的,重新寻找玩家
     * (有效时间内找人,过时了找电脑,没人了不找了)
     * 并递归调用这个方法重新校验
     *
     * @param matchers 对上眼的玩家
     * @param begin    开始匹配的时间
     * @param unique   本次匹配唯一标示
     * @return 是否匹配成功
     * @throws InterruptedException 未知异常
     */
    private Boolean matchRecursion(MatchUsers matchers, long begin, long unique) throws InterruptedException {
        //剩余有效时间
        long leftMills = matchMaxTime * 1000 - (System.currentTimeMillis() - begin);
        logger.info("[{}]|小组赛递归匹配处理[{}|{}|{}]", unique, matchers, quitList, leftMills);
        Long firstUid = toLong(matchers.user1);
        Long secondUid = toLong(matchers.user2);

        boolean firstQuit = isQuitThenRemove(firstUid);
        boolean secondQuit = isQuitThenRemove(secondUid);
        //都不存在,继续下一次循环
        if (firstQuit && secondQuit) {
            matchers.user1 = null;
            matchers.user2 = null;
            return false;
        }

        //都存在
        //且不是同一个用户
        //且没有达到某种其他限制
        // 不作处理
        if (!firstQuit && !secondQuit && !secondUid.equals(firstUid) && !antiSpam(matchers)) {
            return true;
        }

        //以下,有一个条件不满足，重新处理

        //匹配时间用完
        if (leftMills <= 0) {
            logger.info("[{}]|小组赛递归匹配处理，时间已用完，leftMills[{}]", unique, leftMills);
            //第一个用户不存在,则第二个用户一定存在,第二个当房主
            if (firstQuit) {
                matchers.user1 = matchers.user2;
            }
            //再加个AI
            matchers.user2 = AI_USER_ID;
            return true;
        }

        //匹配时间未用完
        logger.info("[{}]|小组赛递归匹配处理，时间未用完，leftMills[{}]", unique, leftMills);
        if (!firstQuit) {
            logger.debug("[{}]|小组赛递归匹配，为用户[{}]等待第二个用户", unique, firstUid);
        } else {
            matchers.user1 = matchers.user2;
            logger.debug("[{}]|小组赛递归匹配，为用户[{}]等待第一个用户", unique, secondUid);
        }
        //剩余时间继续获取下一个用户
        logger.debug("[{}]|小组赛递归匹配，第二次等待用户,timeLeft[{}]", unique, leftMills);
        //跑了人了,开启万能通道
        switchMap.put("-1", 1);

        matchers.user2 = queue.poll(leftMills, TimeUnit.MILLISECONDS);
        logger.debug("[{}]|小组赛递归匹配，第二次等待用户结果[{}]", unique, matchers.user2);
        //找到人了,关闭万能通道
        switchMap.remove("-1");
        return matchRecursion(matchers, begin, unique);
    }

    private String getTime() {
        return FORMAT.format(new Date());
    }

    private Long toLong(String uid) {
        return uid == null ? null : Long.parseLong(uid);
    }

    /**
     * @param uid 用户唯一标示
     * @return true 用户已不存在 false 用户还在
     */
    private boolean isQuitThenRemove(final Long uid) {
        //为空,标识不存在
        if (uid == null) return true;
        //不为空,但是强退列表里有,移除强退列表,标识不存在
        if (quitList.contains(uid)) {
            quitList.remove(uid);
            return true;
        }
        //用户存在
        return false;
    }

    private RList<Long> getTodayList(String uid) {
        return redissonUtil.getList(MessageFormat.format(BGBDH_MATCH_RECORD, uid));
    }

    private RList<Long> getTodayList(String uid, long millis) {
        RList<Long> list = getTodayList(uid);
        list.expire(millis, TimeUnit.MILLISECONDS);
        return list;
    }

    private long getNextEarlyMorning() {
        long endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(zone).toInstant().toEpochMilli();
        return endOfDay - System.currentTimeMillis();
    }

    private static class MatchUsers {
        private String user1;
        private String user2;

        private MatchUsers(final String user1, final String user2) {
            this.user1 = user1;
            this.user2 = user2;
        }
    }
}
