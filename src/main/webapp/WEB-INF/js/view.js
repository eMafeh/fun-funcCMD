class TetrisView {
    constructor(tableId, xSize, ySize, cellSize, removeLineFn, endFn) {
        console.assert(Number.isInteger(xSize));
        console.assert(Number.isInteger(ySize));
        this.tableId = tableId;
        this.xSize = xSize;
        this.ySize = ySize;
        this.removeLineFn = typeof removeLineFn === 'function' ? removeLineFn : EMPTY_FUNCTION;
        this.endFn = typeof endFn === 'function' ? endFn : EMPTY_FUNCTION;

        this.table = document.getElementById(this.tableId);
        this.cells = [];
        this.shapeMap = {};
        let cells = "";
        for (let iy = 1; iy <= this.ySize; iy++)
            for (let ix = 1; ix <= this.xSize; ix++)
                cells += `<div class="cell" style="height:${cellSize}px; width:${cellSize}px;" id="${this.tableId}_${ix}_${iy}"></div>`;
        this.table.style.width = this.xSize * cellSize + "px";
        this.table.style.height = this.ySize * cellSize + "px";
        this.table.innerHTML = cells;
        //记录所有的元胞格子 td = tdElements[x][y]
        for (let x = 1; x <= this.xSize; x++) {
            const cellX = this.cells[x] = [];
            for (let y = 1; y <= this.ySize; y++)
                cellX[y] = {
                    ele: document.getElementById(`${this.tableId}_${x}_${y}`),
                    belong: null,
                    showFunc: null,
                    hiddenFunc: null
                }
        }
    }

    clear() {
        this.shapeMap = {};
        for (let yKey = 1; yKey <= this.ySize; yKey++) this.lineForEach(yKey, TetrisView.removeCell);
    }

    toRotation(status) {
        status.index++;
        if (this.tryMoveCells(status) !== true) status.index--;
    }

    toLeft(status) {
        status.origin[0]--;
        if (this.tryMoveCells(status) !== true) status.origin[0]++;
    }

    toRight(status) {
        status.origin[0]++;
        if (this.tryMoveCells(status) !== true) status.origin[0]--;
    }

    toNext(status) {
        status.origin[1]++;
        if (this.tryMoveCells(status) !== true) {
            status.origin[1]--;
            if (status.origin[1] === 0) this.endFn();
            else {
                this.shapeMap[status.shapeId] = null;
                this.tryRemoveLine(status.origin[1]);
                return false;
            }
        }
    }

    tryMoveCells(status) {
        let xIndex = status.origin[0];
        let yIndex = status.origin[1];
        // 该形状一定超过了最下面,nothing to do
        if (yIndex > this.ySize) return;

        const shapeId = status.shapeId;
        const shape = status.nowShape();

        //检查左右
        const left = shape.getLeft() + xIndex - 1;
        //最左边必须大于1
        if (left < 0) xIndex -= left;
        const right = shape.getRight() + xIndex - this.xSize;
        //最右边必须小于 xSize
        if (right > 0) xIndex -= right;
        //左边不大于1 , 说明shape 形状太大
        if (xIndex < 1) {
            console.log("shape size is big than view", shape, status.origin);
            return;
        }

        //检查每个 cell 是否可用
        const newCells = [];
        for (const shapeCell of shape.cssCells) {
            const xKey = shapeCell.xIndex + xIndex;
            const yKey = shapeCell.yIndex + yIndex;
            if (yKey < 1) {
                //不需要展示,跳过该块
                continue;
            }
            const cell = this.cells[xKey][yKey];
            if (cell.belong && cell.belong !== shapeId) {
                // 依赖到被占用的块,什么也不做
                return;
            }
            newCells.push([xKey, yKey]);
        }

        //更新 origin 对象
        status.origin[0] = xIndex;
        status.origin[1] = yIndex;

        //实际移动 cell 记录移动情况
        this.moveCells(status, newCells, shapeId);
        //成功操作
        return true;
    }

    moveCells(status, newCells, shapeId) {
        const oldCells = this.shapeMap[shapeId];
        if (oldCells)
            for (const oldCell of oldCells) {
                const cell = this.cells[oldCell[0]][oldCell[1]];
                cell.hiddenFunc(cell.ele);
                TetrisView.cellStatus(cell);
            }
        for (let i = 0; i < newCells.length; i++) {
            const newCell = newCells[i];
            const cell = this.cells[newCell[0]][newCell[1]];
            const fns = status.functionList[i];
            TetrisView.cellStatus(cell, shapeId, fns[0], fns[1]);
            cell.showFunc(cell.ele);
        }
        this.shapeMap[shapeId] = newCells;
    }

    tryRemoveLine(yIndex) {
        let num = 0;
        let yKey = Math.min(yIndex, this.ySize);
        for (; yKey > 0 && yKey > yIndex - 4; yKey--) {
            if (this.isFullLine(yKey)) {
                this.lineForEach(yKey, TetrisView.removeCell);
                num++;
            } else if (num !== 0)
                this.lineForEach(yKey, TetrisView.downCell(num));
        }
        if (num === 0) return;
        this.removeLineFn(num);
        for (; yKey > 0; yKey--)
            this.lineForEach(yKey, TetrisView.downCell(num));

    }

    isFullLine(yKey) {
        for (let xKey = 1; xKey <= this.xSize; xKey++)
            if (!this.cells[xKey][yKey].belong) return false;
        return true;
    }

    lineForEach(yKey, cellFunc) {
        for (let xKey = 1; xKey <= this.xSize; xKey++) cellFunc(this.cells, xKey, yKey);
    }

    static downCell(count) {
        return (cells, xKey, yKey) => {
            const cell = cells[xKey][yKey];
            const downCell = cells[xKey][yKey + count];
            if (cell.belong) {
                console.assert(!downCell.belong, "downCell is occupied");
                TetrisView.cellStatus(downCell, cell.belong, cell.showFunc, cell.hiddenFunc);
                cell.hiddenFunc(cell.ele);
                downCell.showFunc(downCell.ele);
                TetrisView.cellStatus(cell);
            }
        };
    }

    static removeCell(cells, xKey, yKey) {
        const cell = cells[xKey][yKey];
        if (cell.belong) {
            cell.hiddenFunc(cell.ele);
            TetrisView.cellStatus(cell);
        }
    }

    static cellStatus(cell, belong, showFunc, hiddenFunc) {
        if (!belong) {
            cell.showFunc = null;
            cell.hiddenFunc = null;
            cell.belong = null;
        } else {
            console.assert(showFunc && hiddenFunc, "cell's showFunc & hiddenFunc must be null");
            cell.showFunc = showFunc;
            cell.hiddenFunc = hiddenFunc;
            cell.belong = belong;
        }
    }
}
