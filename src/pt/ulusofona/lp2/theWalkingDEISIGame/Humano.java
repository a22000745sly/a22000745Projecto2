package pt.ulusofona.lp2.theWalkingDEISIGame;

public class Humano extends Creature {
    protected Equipamento equipamento;
    protected int equipamentosUsados = 0;
    protected int maxMovesPerTurn;

    public Humano(int x, int y, int creatureID, int tipe, String nome) {
        super(x, y, creatureID, tipe, nome);
        switch (tipe) {
            case 5:
                maxMovesPerTurn = 1;
            case 6:
                maxMovesPerTurn = 2;
            case 7:
                maxMovesPerTurn = 3;
            case 8:
                maxMovesPerTurn = 1;
            case 9:
                maxMovesPerTurn = 2;
        }
    }

    public Equipamento equipementInUse() {
        return equipamento;
    }

    public void destroyEquipment() {
        equipamento = null;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public int equipementsUsed() {
        return equipamentosUsados;
    }

    public void addEquipmentUsed() {
        equipamentosUsados++;
    }

    public String getImagePNG() {
        return null;
    }

    public String toString() {
        return creatureID + " | Humano | Os Vivos | " + nome + " " + equipamentosUsados + " @ (" + x + ", " + y + ")";
    }

}
