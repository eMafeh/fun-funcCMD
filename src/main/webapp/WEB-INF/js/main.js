window.onload = function () {
    User.loadUser(RemoteUser);
};
const RemoteGifList = ImageList.filter(a => a.gro === 'game');
const RemoteBackImgList = ImageList.filter(a => a.gro === 'back');

class Game {
    static showGame() {
        Game.showGame = () => DoCss.showView($viewGame);
        Game.showGame();

        class Status {
            constructor() {
                if (Status.SHAPE_ID === undefined) Status.SHAPE_ID = 0;
                this.shapeId = ++Status.SHAPE_ID;
                this.shapeGroup = ShapeGroup.random();
                this.functionList = [];
                this.index = this.shapeGroup.randomIndex();
                const num = Math.min(User.remoteUser.gameGrade + 1, RemoteGifList.length);
                const randomIndex = Math.floor(Math.random() * num);
                const url = RemoteGifList[randomIndex]['url'];
                const gifURL = `url(${url})`;
                for (let i = 0; i < this.shapeGroup.maxCellSize; i++) {
                    this.functionList.push([
                        ele => ele.style.backgroundImage = gifURL,
                        ele => ele.style.backgroundImage = null
                    ]);
                }
            }

            static markBadGif() {
                const img = Touch.target.style.backgroundImage;
                if (img) {
                    const url = img.substr(5, img.length - 7);
                    Ajax.post('badGif', {url: url});
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


        const view = new TetrisView($table, xNum, yNum, cellLength,
            User.grade_add, () => {
                startGame(false);
                Layout.alert(`<div>GAME</div><div>OVER</div>`, () => doKey('c'));
            });
        const minView = new TetrisView($minTable, 4, 4, minCellLength);

        const viewOrigin = [Math.floor(view.xSize / 2), 0];
        const minViewOrigin = [2, 4];
        let status;
        let nextStatus = new Status().setOrigin(minViewOrigin);

//设定主要功能
        function doKey(name) {
            switch (name) {
                case "e"://开始暂停
                    startGame();
                    return;
                case "c"://清空
                    startGame(false);
                    view.clear();
                    minView.clear();
                    User.grade_clear();
                    status = null;
                    nextStatus = new Status().setOrigin(minViewOrigin);
                    return;
                case "b"://移除坏图
                    Status.markBadGif();
                    return;
                case "z"://回主页
                    DoCss.showView($viewHome);
                    return;
                case "x"://前往游戏页
                    Game.showGame();
                    return;
            }

            if (!auto) return;
            if (!status) {
                status = nextStatus.setOrigin(viewOrigin);
                nextStatus = new Status().setOrigin(minViewOrigin);
                minView.clear();
                minView.tryMoveCells(nextStatus);
            }
            switch (name) {
                case "w"://上
                    view.toRotation(status);
                    return;
                case "a"://左
                    view.toLeft(status);
                    return;
                case "d"://右
                    view.toRight(status);
                    return;
                case "s"://下
                    if (view.toNext(status) === false) status = null;
                    return;
            }
        }

//触屏绑定功能
        TouchUtil.listen($table, cellLength,
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
        const loop = function () {
            if (auto) doKey('s');
            setTimeout(loop, User.remoteUser.autoTime);
        };
        loop();
    };
}


class ImageHandler {
    static showImage(backFn) {
        ImageHandler.showImage = backFn => {
            ImageHandler.backFn = backFn;
            DoCss.showView($viewImage);
        };
        ImageHandler.showImage(backFn);


        const addImg = function (backImg) {
            if (!backImg.url) {
                alert("图片上传失败" + backImg.name);
                return;
            }
            const img = document.createElement('img');
            img.onclick = () => {
                DoCss.backView();
                ImageHandler.backFn(backImg);
            };
            img.alt = backImg.name;
            img.src = backImg.url;
            $viewImage.insertBefore(img, span.nextSibling);
        };
        const span = document.createElement('span');
        span.classList.add('flex', 'fa', 'fa-plus');
        span.onclick = () => {
            ImageHandler.$input.click();
        };
        $viewImage.appendChild(span);

        ImageHandler.$input = document.createElement('input');
        ImageHandler.$input.type = 'file';
        ImageHandler.$input.hidden = true;
        ImageHandler.$input.accept = "image/*";
        $viewImage.appendChild(ImageHandler.$input);
        ImageHandler.$input.onchange = () => {
            const file = ImageHandler.$input.files[0];
            if (!file) return;
            if (file.size > 1024 * 1024) {
                alert("图片最大1M");
                return;
            }
            span.onclick = EMPTY_FUNCTION;
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function () {
                Ajax.post('uploadFile', {userId: User.remoteUser.id, imgBase64: this.result},
                    data => {
                        addImg(data);
                        span.onclick = () => ImageHandler.$input.click();
                    });
            }
        };
        for (const backImg of RemoteBackImgList) {
            addImg(backImg);
        }
    }
}