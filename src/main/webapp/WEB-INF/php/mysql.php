<?php
/** @noinspection SqlDialectInspection */
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/25
 * Time: 14:30
 * @param $url
 * @param $userId
 * @return mixed
 */
function insertBackImg($url, $userId)
{
    $query = 'insert into image(url,userId,gro)values(:url,:userId,:gro)';
    $PDO = getCon();
    $stmt = $PDO->prepare($query);
    $param = array('userId' => $userId, 'url' => $url, 'gro' => 'back');
    $stmt->execute($param);
    $id = $PDO->lastInsertId();
    return getById('image', ImageType::class, $id);
}

/**
 * @param string $ip
 * @param $firstId
 */
function addOrUpdateVisit($ip, $firstId)
{
    $sql = 'insert into ipVisit (id,firstId) values (?,?) ON DUPLICATE KEY UPDATE times=times + 1, modified=now();';
    $stmt = getCon()->prepare($sql);
    $stmt->bindParam(1, $ip);
    $stmt->bindParam(2, $firstId);
    $stmt->execute();
}

/**
 * @param UserType $user
 */
function insertUser($user)
{
    $stmt = getCon()->prepare('insert into user(id, homeImgId, gameImgId) values (?,?,?)');
    $stmt->bindParam(1, $user->id);
    $stmt->bindParam(2, $user->homeImgId);
    $stmt->bindParam(3, $user->gameImgId);
    $stmt->execute();
}

function insertOp($userId, $ip, $type, $value)
{
    $stmt = getCon()->prepare('insert into op_log(userId,ip, type,value) values (?,?,?,?)');
    $stmt->bindParam(1, $userId);
    $stmt->bindParam(2, $ip);
    $stmt->bindParam(3, $type);
    $stmt->bindParam(4, $value);
    $stmt->execute();
}

function addGrade($userId, $num)
{
    $stmt = getCon()->prepare('UPDATE user set grade=grade+? where id=?');
    $num = intval($num);
    $stmt->bindParam(1, $num, PDO::PARAM_INT);
    $stmt->bindParam(2, $userId);
    $stmt->execute();
    return $stmt->rowCount();
}