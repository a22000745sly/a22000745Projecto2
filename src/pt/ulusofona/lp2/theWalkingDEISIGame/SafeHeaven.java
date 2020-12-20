package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.util.ArrayList;
import java.util.List;

public class SafeHeaven extends Posicao{
    protected List<Humano> humanos = new ArrayList<>();
    public SafeHeaven(int x, int y){
        super(x,y);
    }
    public boolean addHuman(Humano humano){
        humanos.add(humano);
        return true;
    }
    public List<Humano> hosSafe(){
        return humanos;
    }
}
