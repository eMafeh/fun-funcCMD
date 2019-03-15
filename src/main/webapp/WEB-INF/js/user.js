class User {
    static loadUser(remoteUser) {
        User.remoteUser = remoteUser;
        User.remoteUser.grade = Number.parseInt(User.remoteUser.grade);
        User.remoteUser.autoTime = Number.parseInt(User.remoteUser.autoTime);
        const homeImg = Collection.getTarget(RemoteBackImgList, 'id', User.remoteUser.homeImgId);
        if (homeImg) {
            $showHomeImg.src = homeImg.url;
            $viewHome.style.backgroundImage = `url("${homeImg.url}")`;
        }
        const gameImg = Collection.getTarget(RemoteBackImgList, 'id', User.remoteUser.gameImgId);
        if (gameImg) {
            $showGameImg.src = gameImg.url;
            $viewGame.style.backgroundImage = `url("${gameImg.url}")`;
        }
        console.log(remoteUser);
        User.name_show();
        User.grade_clear();
        User.autoTime_show();
        work = remoteUser.starLine === "1";
        Star.auto = work ? Star.work : Star.stop;
    }


    static name_show() {
        $userInfo.innerHTML = User.remoteUser.name ? "欢迎:" + User.remoteUser.name : "";
    }

    static name_set(name) {
        return User.remoteUser.name;
    }

    static grade_clear() {
        User.remoteUser.gameGrade = 0;
        User.grade_show();
    }

    static grade_add(num) {
        Assert.isTrue(typeof num === 'number');
        if (num < 1) return;

        const remoteUser = User.remoteUser;
        remoteUser.grade += num;
        remoteUser.gameGrade += num;
        User.grade_show();
        Ajax.post('addGrade', {userId: remoteUser.id, num: num});
    }

    static grade_show() {
        $allCount.innerHTML = User.remoteUser.grade + "<br/>" + User.remoteUser.gameGrade;
    }

    static autoTime_show() {
        $autoTime.value = User.remoteUser.autoTime;
    }

    static home_change(img) {
        if (!img) return;
        const remoteUser = User.remoteUser;
        if (remoteUser.homeImgId === img.id) return;
        Ajax.post('updateUser', {userId: remoteUser.id, field: 'homeImgId', value: img.id});
        remoteUser.homeImgId = img.id;
        $showHomeImg.src = img.url;
        $viewHome.style.backgroundImage = `url("${img.url}")`;
    }

    static game_change(img) {
        if (!img) return;
        const remoteUser = User.remoteUser;
        if (remoteUser.gameImgId === img.id) return;
        Ajax.post('updateUser', {userId: remoteUser.id, field: 'gameImgId', value: img.id});
        remoteUser.gameImgId = img.id;
        $showGameImg.src = img.url;
        $viewGame.style.backgroundImage = `url("${img.url}")`;
    }

}

$autoTime.onchange = () => {
    let value = $autoTime.value;
    const remoteUser = User.remoteUser;
    if (Number.isInteger(value) && value > 0)
        $autoTime.value = remoteUser.autoTime;
    else {
        remoteUser.autoTime = value;
        Ajax.post('updateUser', {userId: remoteUser.id, field: 'autoTime', value: value});
    }
};

$starLine.onclick = function () {
    const remoteUser = User.remoteUser;
    work = remoteUser.starLine = !remoteUser.starLine;
    Star.auto = work ? Star.work : Star.stop;
    $canvas.style.display = work ? 'block' : 'none';
    Ajax.post('updateUser', {userId: remoteUser.id, field: 'starLine', value: work ? 1 : 0});
};