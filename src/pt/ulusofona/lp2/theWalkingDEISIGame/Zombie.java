package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Zombie extends Creature{
    protected int number;
    protected int equipamentosDestruidos = 0;
    protected int maxMovesPerTurn;
    public Zombie(int x, int y, int creatureID,int tipe,String nome, int number){
        super(x,y,creatureID,tipe,nome);
        switch (tipe) {
            case 0:
                maxMovesPerTurn = 1;
            case 1:
                maxMovesPerTurn = 2;
            case 2:
                maxMovesPerTurn = 3;
            case 3:
                maxMovesPerTurn = 1;
            case 4:
                maxMovesPerTurn = 2;
        }
        this.number = number;
    }
    public int howManyEquipementsDestroyd(){return  equipamentosDestruidos;}
    public void destroyEquipment(){ equipamentosDestruidos++;}
    public String getImagePNG(){
        return null;
    }
    public String toString(){
        return  creatureID + " | Zombie | Os Outros | " + nome +" " + equipamentosDestruidos + " @ (" + x + ", " + y + ")";
    }
}
