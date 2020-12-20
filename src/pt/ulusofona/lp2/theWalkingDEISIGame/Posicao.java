package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Posicao {
    protected int x;
    protected int y;
    public Posicao(int x,int y){
        this.x = x;
        this.y = y;
    }
    public int[] getPosition(){
        int[] a = new int[2];
        a[0] = x;
        a[1] = y;
        return  a;
    }
    public void changePosition(int x, int y){
        this.x = x;
        this.y = y;
    }
}
