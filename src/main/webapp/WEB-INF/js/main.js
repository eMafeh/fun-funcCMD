window.onload = function () {
    let timeout = 1000;
    const loopTimeElement = document.getElementById("loopTime");
    {
        loopTimeElement.onchange = () => {
            let value = loopTimeElement.value;
            if (Number.isInteger(value) && value > 0)
                loopTimeElement.value = timeout;
            else timeout = value;
            console.log(timeout);
        };
        loopTimeElement.value = timeout;
    }

    class Status {
        constructor() {
            Status.gifInit();
            if (Status.SHAPE_ID === undefined) Status.SHAPE_ID = 0;
            this.shapeId = ++Status.SHAPE_ID;
            this.shapeGroup = ShapeGroup.random();
            this.functionList = [];
            this.index = this.shapeGroup.randomIndex();
            for (let i = 0; i < this.shapeGroup.maxCellSize; i++) {
                const num = Math.min(view.counter.count + 1, Status.GifList.length);
                const randomIndex = Math.floor(Math.random() * num);
                const url = Status.GifList[randomIndex]['url'];
                const gifURL = `url(${url})`;
                this.functionList.push([ele => ele.style.backgroundImage = gifURL,
                    ele => ele.style.backgroundImage = null
                ]);
            }
        }

        static gifInit() {
            if (!Status.GifList) {
                const xhr = new XMLHttpRequest();
                xhr.open('GET', "http://hyu6174190001.my3w.com/php/control.php?method=gifList", false);
                xhr.onreadystatechange = function () {
                    // readyState == 4说明请求已完成
                    if (xhr.readyState === 4 && xhr.status === 200 || xhr.status === 304) {
                        // 从服务器获得数据
                        Status.GifList = JSON.parse(xhr.responseText);
                    }
                };
                xhr.send();
            }
        }

        static markBadGif() {
            const img = Touch.target.style.backgroundImage;
            if (img) {
                const url = img.substr(5, img.length - 7);
                console.log(url);
                const xhr = new XMLHttpRequest();
                xhr.open("POST", "http://hyu6174190001.my3w.com/php/control.php", true);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && (xhr.status === 200 || xhr.status === 304)) {
                        console.log(xhr.responseText);
                    }
                };
                xhr.send("method=badGif&url=" + encodeURIComponent(url));
            }
        }

        setOrigin(origin) {
            this.origin = [origin[0], origin[1]];
            return this;
        }

        nowShape() {
            return this.shapeGroup.getShape(this.index);
        }
    }

    const view = new TetrisView("table", new Counter("allCount"), xNum, yNum, cellLength);
    const minView = new TetrisView("min-table", new Counter(), 4, 4, cellLength * 0.6);

    const viewOrigin = [Math.floor(view.xSize / 2), 0];
    const minViewOrigin = [2, 4];
    let status;
    let nextStatus = new Status().setOrigin(minViewOrigin);

//设定主要功能
    function doKey(name) {
        if (!status) {
            status = nextStatus.setOrigin(viewOrigin);
            nextStatus = new Status().setOrigin(minViewOrigin);
            minView.clear();
            minView.tryMoveCells(nextStatus);
        }
        switch (name) {
            case "w"://上
                if (!auto) return;
                view.toRotation(status);
                return;
            case "a"://左
                if (!auto) return;
                view.toLeft(status);
                return;
            case "d"://右
                if (!auto) return;
                view.toRight(status);
                return;
            case "s"://下
                if (!auto) return;
                if (view.toNext(status) === false) status = null;
                return;
            case "e"://开始暂停
                startGame();
                return;
            case "c"://清空
                startGame(false);
                view.clear();
                minView.clear();
                status = null;
                nextStatus = new Status().setOrigin(minViewOrigin);
                return;
            case "b"://移除坏图
                Status.markBadGif();
                return;
            case "z"://回主页
                DoCss.homeView();
                return;
            case "x"://前往游戏页
                DoCss.gameView();
                return;
        }
    }

//触屏绑定功能
    Touch.listener(view.table, cellLength,
        () => doKey("a"), null,
        () => doKey("d"), null,
        () => doKey("w"), null,
        () => doKey("s"), 2,
        () => doKey("e"), null,
        null,
        null
    );

//按键绑定功能
    document.onkeydown = ev => doKey(ev.key);

//定时触发下降
    ~function () {
        if (auto) doKey('s');
        setTimeout(arguments.callee, timeout);
    }();
};