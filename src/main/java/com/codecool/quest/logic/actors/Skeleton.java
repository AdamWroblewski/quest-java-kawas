package com.codecool.quest.logic.actors;

import com.codecool.quest.logic.Cell;
import com.codecool.quest.logic.CellType;
import com.codecool.quest.logic.actors.Actor;

public class Skeleton extends Actor {
    private boolean staggered;
    private boolean wasRight;

    public Skeleton(Cell cell) {
        super(cell);
        staggered = false;

        wasRight = true;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    @Override
    public void move(int dx, int dy) {
        if(wasRight) {
            super.move(1, 0);
            wasRight = false;
        } else {
            super.move(-1, 0);
            wasRight = true;
        }


    }
}
