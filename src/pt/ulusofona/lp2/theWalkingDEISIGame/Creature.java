package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Creature extends Posicao{
    protected int creatureID;
    protected int tipe;
    protected String nome;
    public Creature(int x,int y,int creatureID,int tipe,String nome){
        super(x,y);
        this.creatureID = creatureID;
        this.tipe = tipe;
        this.nome = nome;
    }
    public String theName(){ return nome;}
    public int getId(){ return creatureID;}
    public String getImagePNG(){ return null;}
    public int getTipe(){ return tipe;}
}
