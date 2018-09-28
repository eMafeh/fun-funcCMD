package com.qr.solo;

import com.qr.solo.infer.*;
import com.qr.solo.model.Cell;

/**
 * @author QianRui
 * 2018/9/28
 */
public class SoloBoot {
    public static void main(String[] args) {
        Cell.init(SoloConst.target);
        Cell.detailShow();

        CellStrategy.doInfer();

        Exhaustion.compute(0, SoloConst.d);

        System.out.println("end        " + System.currentTimeMillis());
    }
}
