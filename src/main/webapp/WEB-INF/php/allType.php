<?php

/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/28
 * Time: 10:46
 */

class AbstractType
{
    public $id;
    public $modified;
}

class ImageType extends AbstractType
{
    public $name;
    public $url;
    public $userId;
    public $gro;
}

class IpVisitType extends AbstractType
{
    public $userId;
    public $firstId;
    public $times;
}

class UserType
{
    public $name;
    public $password;
    public $homeImgId = 1;
    public $gameImgId = 2;
    public $starLine = true;
    public $grade = 0;
    public $autoTime = 1000;
}
