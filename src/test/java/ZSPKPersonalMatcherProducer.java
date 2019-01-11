import cn.migu.dangjian.gbdh.constants.RedisConstants;
import cn.migu.dangjian.gbdh.utils.IdUtil;
import cn.migu.dangjian.gbdh.utils.NetUtils;
import cn.migu.dangjian.gbdh.utils.RedissonUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * 生产者
 *
 * @author Mingfei
 * @version Created in 7/28/2018
 */
@Service
public class ZSPKPersonalMatcherProducer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("matcher");

    private static String producerLockPath = "/PersonalMatchPoolStarter" + "Producer";

    private static final Integer workerId = 15;

    private static final Integer dataCenterId = 15;

    @Autowired
    protected CuratorFramework zkClient;

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private ZSPKPersonalMatcherPool zspkPersonalMatcherPool;

    //开了的房,且房间创建时间未过期,房间标识为新开这间房的玩家标识
    private RScoredSortedSet<String> matchSet;
    //开了的房间的状态  <key uid, value> 1 新房 2 对战中
    private RMap<String, Integer> switchMap;

    @Override
    public void afterPropertiesSet() {
        try {
            startService();
        } catch (Exception e) {
            logger.info("排位赛匹配生产者线程已经运行，重启", e);
            try {
                String host = new String(zkClient.getData().forPath(producerLockPath));
                logger.info("排位赛匹配生产者线程启动锁host[{}]", host);
                if (NetUtils.getLocalHost().equals(host)) {
                    zkClient.delete().forPath(producerLockPath);
                    startService();
                }
            } catch (Exception e1) {
                logger.error("排位赛匹配生产者线程重新获取启动锁失败", e1);
            }
        }
    }

    /**
     * 尝试启动排位赛线程,zk加 ip锁 防止重入
     */
    private void startService() throws Exception {
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(producerLockPath);
        zkClient.setData().forPath(producerLockPath, NetUtils.getLocalHost().getBytes());

        zspkPersonalMatcherPool.delCache();
        Executors.newFixedThreadPool(1).submit(this::waitQueueScanner);
    }

    private void waitQueueScanner() {
        logger.info("启动排位赛匹配生产者线程[{}]", NetUtils.getLocalHost());
        //排队等待的队列,玩家点击排队后,放入该排队队列
        final RBlockingQueue<String> waitQueue = redissonUtil.getBlockingQueue(RedisConstants.WAIT_QUEUE);
        matchSet = redissonUtil.getScoredSortedSet(RedisConstants.PK_MATCH_TOTAL_SET, StringCodec.INSTANCE);
        //万能队列,如果有人开了房,好不容易等到的人跑了,就直接从这个队列里捞个人补上
        final RBlockingQueue<String> queue = redissonUtil.getBlockingQueue(RedisConstants.WAITING_QUEUE);
        switchMap = redissonUtil.getMap(RedisConstants.PK_MATCH_SWITCH_MAP);

        while (true) {
            //该轮匹配唯一标识
            long unique = IdUtil.getInstance(workerId, dataCenterId).nextId();

            logger.info("[{}]|等待用户进入等待队列...", unique);
            //用户唯一标识
            String uid;
            Long time;

            try {
                //将排队的人取出,匹配对手
                uid = waitQueue.take();
                //匹配开始时间
                time = System.currentTimeMillis();
                logger.info("[{}]|扫描到等待队列用户[{}]", unique, uid);
            } catch (Exception e) {
                logger.error("[{}]|等待用户进入等待队列异常！重新等待", unique, e);
                continue;
            }

            //优先进入万能队列,队列标识为 "-1"
            if (switchMap.containsKey("-1") && switchMap.get("-1") == 1) {
                queue.add(uid);
                logger.debug("[{}]|用户[{}]进入万能队列", unique, uid);
                continue;
            }

            //去大厅
            lobbyQueue(unique, time, uid);
        }
    }

    /**
     * 进入游戏大厅,参与别人的队列房间,找不到则自己新开一个房间
     *
     * @param unique 业务唯一标示
     * @param time   开始等待时间
     * @param uid    用户唯一标示
     */
    private void lobbyQueue(final long unique, final Long time, final String uid) {
        try {
            //有房人士列表
            List<ScoredEntry<String>> entries = new ArrayList<>(matchSet.entryRange(0, matchSet.size() - 1));
            for (final ScoredEntry<String> entry : entries) {
                //房间唯一标示
                String matchUid = entry.getValue();
                //房间确实在招人
                if (switchMap.containsKey(matchUid) && switchMap.get(matchUid) == 1) {
                    //取房间 key
                    String matchQueueKey = MessageFormat.format(RedisConstants.PK_MATCH_QUEUE, matchUid);
                    //房间里的玩家列表
                    RBlockingQueue<String> matchQueue = redissonUtil.getBlockingQueue(matchQueueKey);
                    //列表加入这个倒霉蛋
                    matchQueue.add(uid);
                    //这个房间不再找人了
                    matchSet.remove(uid);//TODO 这里有bug 应该在有房列表中 移除 matchUid 这个房主, uid这时候又没房间
                    //这个房间不找人了可以尝试进入对战了
                    switchMap.put(matchUid, 2);
                    logger.info("[{}]|用户[{}]加入到匹配队列[{}]中", unique, uid, matchUid);
                    return;
                }
            }

            //找不到房自己开新房咯~
            ZSPKPersonalMatcherPool.newQueue(redissonUtil, matchSet, switchMap, uid, time);
        } catch (Exception e) {
            logger.error("[{}]|用户[{}]加入匹配队列异常", unique, uid, e);
        }
    }
}
