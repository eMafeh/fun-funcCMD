<?php
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/28
 * Time: 11:07
 */

/**
 * @param string $userId
 * @param string $ip
 * @return UserType
 */
function getUser($userId, $ip)
{
    javaAssert($ip !== null, "ip 信息获取为空");
    //用户信息不存在
    if ($userId === null) {
        //创建零时用户id
        $uuid = uuid();
        addOrUpdateVisit($ip, $uuid);
        $ipVisit = getById('ipVisit', IpVisitType::class, $ip);
        $db_userId = $ipVisit->userId;
        if ($db_userId !== null) {
            $userType = getById('user', UserType::class, $db_userId);
        } else {
            $userType = newUser($uuid);
            if ($uuid === $ipVisit->firstId) {
                //本线程负责初始化用户信息至 user 表,ipVisit 表
                insertUser($userType);
                updateVisit($ip, $uuid);
            }
        }
    } else {
        addOrUpdateVisit($ip, $userId);
        updateVisit($ip, $userId);
        $userType = getById('user', UserType::class, $userId);
    }
    javaAssert(is_object($userType), "用户数据不存在");
    return $userType;
}

/**
 * @param string $uuid
 * @return UserType
 */
function newUser($uuid)
{
    $userType = new UserType();
    $userType->id = $uuid;
    $userType->homeImgId = 1;
    $userType->gameImgId = 2;
    return $userType;
}