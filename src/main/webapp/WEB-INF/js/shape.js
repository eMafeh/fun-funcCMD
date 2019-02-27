class ShapeCell {
    constructor(xIndex, yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }
}

class Shape {
    constructor(...shapeCells) {
        this.cssCells = shapeCells;
        this.cellSize = shapeCells.length;
        let left = 0;
        let right = 0;
        let down = Number.MIN_SAFE_INTEGER;
        for (const shapeCell of shapeCells) {
            left = Math.min(shapeCell.xIndex, left);
            right = Math.max(shapeCell.xIndex, right);
            down = Math.max(shapeCell.yIndex, down);
        }
        if (down !== 0) {
            console.log("fix down index to 0", shapeCells);
            for (const cell of shapeCells)
                cell.yIndex -= down;
        }
        this.getLeft = () => left;
        this.getRight = () => right;
    }
}

class ShapeGroup {
    constructor(...shapeList) {
        this.shapeList = shapeList;
        this.maxCellSize = 0;
        for (const shape of shapeList) this.maxCellSize = Math.max(shape.cellSize, this.maxCellSize);
        if (!ShapeGroup.groups) ShapeGroup.groups = [];
        ShapeGroup.groups.push(this);
    }

    randomIndex() {
        return Math.floor(Math.random() * this.shapeList.length);
    }

    getShape(index) {
        return this.shapeList[(index + 1) % this.shapeList.length];
    }

    static random() {
        return ShapeGroup.groups[Math.floor(Math.random() * ShapeGroup.groups.length)];
    }
}

//四方
new ShapeGroup(
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(0, -1),
        new ShapeCell(1, 0),
        new ShapeCell(1, -1)
    )
);
//直线
new ShapeGroup(
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(0, -1),
        new ShapeCell(0, -2),
        new ShapeCell(0, -3)
    ),
    new Shape(
        new ShapeCell(-1, 0),
        new ShapeCell(0, 0),
        new ShapeCell(1, 0),
        new ShapeCell(2, 0)
    )
);
//L直角
new ShapeGroup(
    new Shape(
        new ShapeCell(0, -2),
        new ShapeCell(0, -1),
        new ShapeCell(0, 0),
        new ShapeCell(1, 0)
    ),
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(1, 0),
        new ShapeCell(2, 0),
        new ShapeCell(2, -1)
    ),
    new Shape(
        new ShapeCell(1, 0),
        new ShapeCell(1, -1),
        new ShapeCell(1, -2),
        new ShapeCell(0, -2)
    ),
    new Shape(
        new ShapeCell(2, -1),
        new ShapeCell(1, -1),
        new ShapeCell(0, -1),
        new ShapeCell(0, 0)
    )
);
//J直角
new ShapeGroup(
    new Shape(
        new ShapeCell(1, -2),
        new ShapeCell(1, -1),
        new ShapeCell(1, 0),
        new ShapeCell(0, 0)
    ),
    new Shape(
        new ShapeCell(0, -1),
        new ShapeCell(1, -1),
        new ShapeCell(2, -1),
        new ShapeCell(2, 0)
    ),
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(0, -1),
        new ShapeCell(0, -2),
        new ShapeCell(1, -2)
    ),
    new Shape(
        new ShapeCell(2, 0),
        new ShapeCell(1, 0),
        new ShapeCell(0, 0),
        new ShapeCell(0, -1)
    )
);
//左勾 Z
new ShapeGroup(
    new Shape(
        new ShapeCell(0, -1),
        new ShapeCell(1, -1),
        new ShapeCell(1, 0),
        new ShapeCell(2, 0)
    ),
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(0, -1),
        new ShapeCell(1, -1),
        new ShapeCell(1, -2)
    )
);
//右勾
new ShapeGroup(
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(1, 0),
        new ShapeCell(1, -1),
        new ShapeCell(2, -1)
    ),
    new Shape(
        new ShapeCell(0, -2),
        new ShapeCell(0, -1),
        new ShapeCell(1, -1),
        new ShapeCell(1, 0)
    )
);
//山
new ShapeGroup(
    new Shape(
        new ShapeCell(-1, 0),
        new ShapeCell(0, 0),
        new ShapeCell(1, 0),
        new ShapeCell(0, -1)
    ),//上
    new Shape(
        new ShapeCell(0, 0),
        new ShapeCell(0, -1),
        new ShapeCell(0, -2),
        new ShapeCell(-1, -1)
    ),//左
    new Shape(
        new ShapeCell(1, -1),
        new ShapeCell(0, -1),
        new ShapeCell(-1, -1),
        new ShapeCell(0, 0)
    ),//下
    new Shape(
        new ShapeCell(0, -2),
        new ShapeCell(0, -1),
        new ShapeCell(0, 0),
        new ShapeCell(1, -1)
    )//右
);


