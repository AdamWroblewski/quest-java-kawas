package com.codecool.quest.logic.actors;

import com.codecool.quest.Main;
import com.codecool.quest.logic.Cell;
import com.codecool.quest.logic.CellType;
import com.codecool.quest.logic.Directions;
import com.codecool.quest.logic.inventory.Keys;

import java.security.Key;


public class Player extends Actor {

    private Directions direction = Directions.INPLACE;
    private int[] coordinates = new int[2];
    private int countMonsters = 0;

    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    @Override
    public void move(int dx, int dy){

        Cell nextCell = cell.getNeighbor(dx, dy);
        Actor actor = nextCell.getActor();
        if ((nextCell.getType().equals(CellType.FLOOR) && actor == null) || nextCell.getType().equals(CellType.OPENEDDOOR)) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else if(actor != null && !actor.isPlayer() ){
            Enemy monster = (Enemy) actor;
            monster.setFightOn();
        } else if ((nextCell.getType().equals(CellType.CLOSEDDOOR))&& Main.items.contains("key")){
            Main.items.remove("key");
            nextCell.setType(CellType.OPENEDDOOR);
        }
        direction.setDirection(dx, dy);
    }

    private Enemy neighbourMonster(int dx, int dy){
        Cell cellCheck = cell.getNeighbor(dx, dy);
        Enemy monster = null;

        if(cellCheck != null){
            monster = (Enemy) cellCheck.getActor();
            if(monster != null){
                coordinates[0] = dx;
                coordinates[1] = dy;
                countMonsters++;
            }
        }

        return monster;
    }
    public Enemy shoot(){
        Enemy monster, monsterShooted = null;
        countMonsters = 0;
        coordinates[0] = coordinates[1] = 0;

        monster = neighbourMonster(0, -1);
        if(monster != null){
            monsterShooted = monster;
        }
        monster = neighbourMonster(1, 0);
        if(monster != null){
            monsterShooted = monster;
        }
        monster = neighbourMonster(0, 1);
        if(monster != null){
            monsterShooted = monster;
        }
        monster = neighbourMonster(-1, 0);
        if(monster != null){
            monsterShooted = monster;
        }

        if(countMonsters < 1){
            return null;
        } else if(countMonsters == 1){
            if(!monsterShooted.canBeStunned() )
                cell.getNeighbor(coordinates[0], coordinates[1]).setActor(null);

            direction.setDirection(coordinates[0], coordinates[1]);

            return monsterShooted;
        }

        Cell cellCheck = cellByDirection();
        if(cellCheck == null)
            return null;

        monster = (Enemy) cellCheck.getActor();
        if(monster != null && !monster.canBeStunned() )
            cellCheck.setActor(null);

        return monster;
    }
    public Actor gloryKill(){
        Cell cellCheck = cellByDirection();
        if(cellCheck == null)
            return null;

        Enemy monster = (Enemy) cellCheck.getActor();
        if(monster != null && monster.canBeStunned() && monster.isStunned() ){
            cellCheck.setActor(null);
            return monster;
        }

        return null;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    private Cell cellByDirection(){
        Cell cellCheck;

        switch( direction.getDirection() ){
            case 1:
                cellCheck = cell.getNeighbor(0, -1);
                break;
            case 2:
                cellCheck = cell.getNeighbor(1, 0);
                break;
            case 3:
                cellCheck = cell.getNeighbor(0, 1);
                break;
            case 4:
                cellCheck = cell.getNeighbor(-1, 0);
                break;
            default:
                return null;
        }

        return cellCheck;
    }

    public void pickUpItem(){
        try{
            if(cell.getItem().getTileName() != null){
                Main.items.add(cell.getItem().getTileName());
                cell.setItem(null);
            }
        }catch(NullPointerException e){
            System.out.println(e + " caused by pickUpItem method when no item is on current cell");
        }
    }

}
