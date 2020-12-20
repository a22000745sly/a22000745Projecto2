package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Equipamento extends Posicao {
    protected int equippementID;
    protected int idTipo;// se for 0 é um escudo se for 1 é uma espada
    protected int utilizacoes = -1;
    protected String nome;
    protected boolean eDefencivo = false;
    private int countEscudo = 0;

    public Equipamento(int x, int y, int equippementID, int idTipo) {
        super(x, y);
        this.equippementID = equippementID;
        this.idTipo = idTipo;
        switch (idTipo) {
            case 0: {
                utilizacoes = 1;
                eDefencivo = true;
            }
            case 2:
                utilizacoes = 3;
            case 3:
                eDefencivo = true;
            case 4:
                eDefencivo = true;
            case 5:
                eDefencivo = true;
            case 7: {
                utilizacoes = 3;
                eDefencivo = true;
            }
            case 8: {
                eDefencivo = true;
                utilizacoes = 2;
            }
            case 9: {
                eDefencivo = true;
                utilizacoes = 1;
            }
            case 10:
                eDefencivo = true;
        }
    }

    public String toString() {
        if (idTipo == 0) {
            return "id: " + equippementID + ", tipo: Escudo de madeira, esta na posiçao (" + getPosition()[0] + "," + getPosition()[1] + ")";

        } else {
            return "id: " + equippementID + ", tipo: Espada samurai, esta na posiçao (" + getPosition()[0] + "," + getPosition()[1] + ")";
        }
    }

    public String getNome() {
        return "" + nome;
    }

    public int getId() {
        return equippementID;
    }

    public int getUtilizacoes() {
        return utilizacoes;
    }

    public int equipmentTipe() {
        return idTipo;
    }

    public void equipmentWasUsed() {
        utilizacoes--;
    }
    public boolean itsForDefence(){
        return eDefencivo;
    }
    public String getImagePNG(){ return null;}
    public void addUsage() {
        countEscudo++;
        if (countEscudo == 0) {
            utilizacoes++;
        }
    }
}
