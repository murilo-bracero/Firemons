package POJO;
import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    private String name;
    private ArrayList<String> type;
    private Integer total;
    private Integer hp;
    private Integer attack;
    private Integer defense;
    private Integer spAtk;
    private Integer spDef;
    private Integer speed;
    private Integer generation;
    private Boolean legendary;

    public Pokemon(String row){
        type = new ArrayList<String>();
        String[] csvRow = row.split(",");

        name = csvRow[0];
        type.add(csvRow[1]);
        type.add(csvRow[2]);
        total = Integer.parseInt(csvRow[3]);
        hp = Integer.parseInt(csvRow[4]);
        attack = Integer.parseInt(csvRow[5]);
        defense = Integer.parseInt(csvRow[6]);
        spAtk = Integer.parseInt(csvRow[7]);
        spDef = Integer.parseInt(csvRow[8]);
        speed = Integer.parseInt(csvRow[9]);
        generation = Integer.parseInt(csvRow[10]);
        legendary = Boolean.parseBoolean(csvRow[11]);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public Integer getTotal() {
        return total;
    }
    public Integer getHp() {
        return hp;
    }
    public Integer getAttack() {
        return attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getSpAtk() {
        return spAtk;
    }

    public Integer getSpDef() {
        return spDef;
    }
    public Integer getSpeed() {
        return speed;
    }

    public Integer getGeneration() {
        return generation;
    }

    public Boolean getLegendary() {
        return legendary;
    }
}
