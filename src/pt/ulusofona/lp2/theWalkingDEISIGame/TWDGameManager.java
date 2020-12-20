package pt.ulusofona.lp2.theWalkingDEISIGame;

import java.util.Scanner;
import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
import java.util.Random;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;

public class TWDGameManager {
    int initialTeamID;
    int currentTeamID;
    int turn = 0;
    boolean eDia = true;
    Posicao[][] map;
    int[] worldSize = new int[2];
    List<Humano> humanos = new ArrayList<>();
    List<Zombie> zombies = new ArrayList<>();
    List<Creature> allCreatures = new ArrayList<>();
    List<Creature> theLost = new ArrayList<>();
    List<SafeHeaven> safeHeaven = new ArrayList<>();
    List<Equipamento> equipamentos = new ArrayList<>();
    HashMap<Humano, Integer> envenenados = new HashMap<Humano, Integer>();
    public void clearData(){
        map = null;
        eDia = true;
        turn = 0;
        worldSize = new int[2];
        humanos = new ArrayList<>();
        zombies = new ArrayList<>();
        allCreatures = new ArrayList<>();
        theLost = new ArrayList<>();
        safeHeaven = new ArrayList<>();
        equipamentos = new ArrayList<>();
    }
    public boolean readFile(File ficheiroInicial) {
        clearData();
        int numberOfCreatures;
        int numberOfEquippements;
        String linha;
        try {
            Scanner leitorFicheiro = new Scanner(ficheiroInicial);
            linha = leitorFicheiro.nextLine();
            String[] dados = linha.split(" ");
            worldSize[0] = Integer.parseInt(dados[0]);
            worldSize[1] = Integer.parseInt(dados[1]);
            map = new Posicao[worldSize[0]][worldSize[1]];
            linha = leitorFicheiro.nextLine();
            initialTeamID = Integer.parseInt(linha);
            linha = leitorFicheiro.nextLine();
            numberOfCreatures = Integer.parseInt(linha);
            int id, tipo, x, y;
            String oneName;
            int count = 0;
            for (int a = 0; a < numberOfCreatures; a++) {
                linha = leitorFicheiro.nextLine();
                String[] dados1 = linha.split(" : ");
                id = Integer.parseInt(dados1[0]);
                tipo = Integer.parseInt(dados1[1]);
                oneName = dados1[2];
                x = Integer.parseInt(dados1[3]);
                y = Integer.parseInt(dados1[4]);
                if (tipo < 5) {
                    Zombie zombie = new Zombie(x, y, id, tipo, oneName, count);
                    map[x][y] = zombie;//mete o zombie na posição do mapa
                    zombies.add(zombie);
                    count++;
                } else {
                    Humano humano = new Humano(x, y, id, tipo, oneName);
                    map[x][y] = humano;//mete o humano na posição do mapa
                    humanos.add(humano);
                }
            }
            linha = leitorFicheiro.nextLine();
            numberOfEquippements = Integer.parseInt(linha);
            for (int a = 0; a < numberOfEquippements; a++) {
                linha = leitorFicheiro.nextLine();
                String[] dados2 = linha.split(" : ");
                id = Integer.parseInt(dados2[0]);
                tipo = Integer.parseInt(dados2[1]);
                x = Integer.parseInt(dados2[2]);
                y = Integer.parseInt(dados2[3]);
                System.out.println("esta um equipamento na posiçao (" + x + "," + y + ")");
                Equipamento equipamento = new Equipamento(x, y, id, tipo);
                map[x][y] = equipamento;
                System.out.println(equipamento);
                equipamentos.add(equipamento);
            }
            linha = leitorFicheiro.nextLine();
            int numberOfDoors = Integer.parseInt(linha);
            for (int a = 0; a < numberOfDoors; a++) {
                linha = leitorFicheiro.nextLine();
                String[] dados2 = linha.split(" : ");
                x = Integer.parseInt(dados2[0]);
                y = Integer.parseInt(dados2[1]);
                SafeHeaven door = new SafeHeaven(x, y);
                map[x][y] = door;
                safeHeaven.add(door);
            }
            leitorFicheiro.close();
        } catch (FileNotFoundException exception) {
            String mensagem = "Erro: o ficheiro " + ficheiroInicial.getName() + " nao foi encontrado.";
            System.out.println(mensagem);
        }
        return true;
    }

    public boolean startGame(File ficheiroInicial) {
        /*
        antes ->Os Vivos (ID = 0)
        agora ->Os Vivos (ID = 10)
        antes ->Os Outros (ID = 1)
        agora ->Os Outros (ID = 20)
         */
        if (readFile(ficheiroInicial)) {
            currentTeamID = initialTeamID;
            allCreatures.addAll(humanos);
            allCreatures.addAll(zombies);
            for (int a = 0; a < humanos.size(); a++) {
                System.out.println(humanos);
            }
            for (int a = 0; a < zombies.size(); a++) {
                System.out.println(zombies);
            }
            for (int a = 0; a < equipamentos.size(); a++) {
                System.out.println(equipamentos);
            }
            if (currentTeamID == 20) {
                vezDosZombies();
            }
            return true;
        } else {
            return false;
        }
    }

    public int[] getWorldSize() {
        return worldSize;
    }

    public int getInitialTeam() {
        return initialTeamID;
    }

    public List<Creature> getCreatures() {
        allCreatures.clear();
        allCreatures.addAll(humanos);
        allCreatures.addAll(zombies);
        return allCreatures;
    }

    public List<Humano> getHumans() {
        return humanos;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public boolean isOutOfLimits(int x, int y) {
        return x < 0 || x >= worldSize[0] || y < 0 || y >= worldSize[1];
    }

    public void vezDosZombies() {
        Random gerador = new Random();
        int numero = gerador.nextInt(zombies.size());
        int movement;
        int xO;
        int yO;
        for (Zombie zombie : zombies) {
            if (zombie.number == numero) {
                xO = zombie.getPosition()[0];
                yO = zombie.getPosition()[1];
                boolean again = false;
                do {
                    if(zombie.getTipe() == 0 ||zombie.getTipe() == 3){
                        movement = gerador.nextInt(4);
                    }else {
                        movement = gerador.nextInt(8);
                    }
                    switch (movement) {
                        case 0: {// move up
                            again = !move(xO, yO, xO, (yO - 1));
                        }
                        break;

                        case 1: {// move right
                            again = !move(xO, yO, xO + 1, yO);
                        }
                        break;

                        case 2: {//move down
                            again = !move(xO, yO, xO, yO + 1);
                        }
                        break;

                        case 3: { //move left
                            again = !move(xO, yO, xO - 1, yO);
                        }
                        break;
                        case 4: { //move nordeste
                            again = !move(xO, yO, xO + 1, yO -1);
                        }
                        break;
                        case 5: { //move sudeste
                            again = !move(xO, yO, xO + 1, yO + 1);
                        }
                        break;
                        case 6: { //move sudoeste
                            again = !move(xO, yO, xO - 1, yO + 1);
                        }
                        break;
                        case 7: { //move noroeste
                            again = !move(xO, yO, xO - 1, yO -1 );
                        }
                        break;
                    }
                } while (again);
            }
        }
    }

    public void podeMover(int xO, int yO, int xD, int yD, Equipamento equipamento, int posicao) {
        if (currentTeamID == 20) {//vez do zombie
            map[xO][yO] = null;
            map[xD][yD] = zombies.get(posicao);
            zombies.get(posicao).changePosition(xD,yD);
            System.out.println("o zombie com o id " + zombies.get(posicao).getId() + " conseguio mover-se para a posição (" + zombies.get(posicao).getPosition()[0] + "," + zombies.get(posicao).getPosition()[1] + ")");
            currentTeamID -= 10;
        } else {//vez do humano
            if (equipamento == null) {//nao tem equipamento
                map[xO][yO] = null;
                map[xD][yD] = humanos.get(posicao);
                humanos.get(posicao).changePosition(xD,yD);
            } else { //tem equipamento
                map[xO][yO] = equipamento;
                map[xD][yD] = humanos.get(posicao);
                humanos.get(posicao).changePosition(xD,yD);
            }
            currentTeamID += 10;
            System.out.println("o humano conseguio mover-se para a posição (" + humanos.get(posicao).getPosition()[0] + "," + humanos.get(posicao).getPosition()[1] + ")");
            vezDosZombies();
        }
        turn++;
    }


    /*
    zombie -> tipodeObjecto = 0
    humanos -> tipodeObjecto = 1
    equipamento -> tipodeObjecto = 2
    safeHeaven -> tipodeObjecto = 3
     */
    public int encontraPosiçao(int x, int y, int tipodeObjecto) {
        switch (tipodeObjecto) {
            case 0: {
                for (int a = 0; a < zombies.size(); a++) {
                    if (zombies.get(a).getPosition()[0] == x && zombies.get(a).getPosition()[1] == y) {
                        return a;
                    }
                }
                return -1;
            }
            case 1: {
                for (int a = 0; a < humanos.size(); a++) {
                    if (humanos.get(a).getPosition()[0] == x && humanos.get(a).getPosition()[1] == y) {
                        return a;
                    }
                }
                return -1;
            }
            case 2: {
                for (int a = 0; a < equipamentos.size(); a++) {
                    if (equipamentos.get(a).getPosition()[0] == x && equipamentos.get(a).getPosition()[1] == y) {
                        return a;
                    }
                }
                return -1;
            }
            case 3: {
                for (int a = 0; a < safeHeaven.size(); a++) {
                    if (safeHeaven.get(a).getPosition()[0] == x && safeHeaven.get(a).getPosition()[1] == y) {
                        return a;
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public boolean usarEquipamento(Humano humano) {
        int equipmentPosition = encontraPosiçao(humano.equipementInUse().getPosition()[0], humano.equipementInUse().getPosition()[1], 2);
        switch (humano.equipementInUse().getUtilizacoes()) {
            case 1: {
                equipamentos.remove(equipmentPosition);
                humano.destroyEquipment();
                return true;
            }
            case 2:
            case 3: {
                equipamentos.get(equipmentPosition).equipmentWasUsed();
                return true;
            }
            case -1: {
                return true;
            }
        }
        return false;
    }

    public boolean didTheHumanWon(int zombiePosition, int humanPosition) {
        if (humanos.get(humanPosition).equipementInUse() != null) {
            if (zombies.get(zombiePosition).getTipe() == 4) {//é um zombie vampiro?
                if (humanos.get(humanPosition).equipementInUse().equipmentTipe() == 5) { //o humano esta a usar alho?
                    return true;
                } else {
                    return false;
                }
            }
            switch (humanos.get(humanPosition).getTipe()) {
                case 5: {//é uma criança
                    if (zombies.get(zombiePosition).getTipe() == 0) {//uma criança  zombie
                        usarEquipamento(humanos.get(humanPosition));
                        return true;
                    }
                }
                case 8:
                case 6: {//é um adulto
                    if (usarEquipamento(humanos.get(humanPosition))) {
                        return true;
                    }
                }
                case 7: {//é um militar
                    if (humanos.get(humanPosition).equipementInUse().idTipo == 0) {//se for um escudo de madeira
                        humanos.get(humanPosition).equipementInUse().addUsage();
                    }
                    if (usarEquipamento(humanos.get(humanPosition))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void makeZombie(Humano humano) {
        humanos.remove(humano);
        int newId = allCreatures.size() + 1;
        int newTipe = -1;
        switch (humano.getTipe()) {
            case 5:
                newTipe = 0;
            case 6:
                newTipe = 1;
            case 7:
                newTipe = 2;
            case 8:
                newTipe = 3;
        }
        theLost.add(humano);
        zombies.add(new Zombie(humano.getPosition()[0], humano.getPosition()[1], newId, newTipe, humano.theName(), newId));
    }

    public boolean movemetTests(int xO, int yO, int xD, int yD, int tipe) {
        boolean eNaDiagonal = false;
        boolean eNaVertical = false;
        boolean eNaHorizontal = false;
        int numeroDePosicoes = 10;
        if (map[xO][yO] == null) {
            return false;
        }
        if (xO == xD && yD == yO) {
            return false;
        }
        if (isOutOfLimits(xD, yD)) {//ve se esta a ir para fora do mapa
            return false;
        }
        for (int a = 1; a < 4; a++) {
            if (xO == (xD + a) && yO == (yD + a)) {
                eNaDiagonal = true;
                numeroDePosicoes = a;
            }
            if (xO == (xD - a) && yO == (yD - a)) {
                eNaDiagonal = true;
                numeroDePosicoes = a;
            }
            if (xO == (xD - a) && yO == yD) {
                eNaHorizontal = true;
                numeroDePosicoes = a;
            }
            if (xO == (xD + a) && yO == yD) {
                eNaHorizontal = true;
                numeroDePosicoes = a;
            }
            if (xO == xD && yO == (yD - a)) {
                eNaVertical = true;
                numeroDePosicoes = a;
            }
            if (xO == xD && yO == (yD + a)) {
                eNaVertical = true;
                numeroDePosicoes = a;
            }
        }
        switch (tipe) {
            case 0://crianca zombie
            case 5: {//criança
                if (1 >= numeroDePosicoes && (eNaHorizontal || eNaVertical)) {
                    return true;
                }
            }
            case 3://idoso zombie
            case 8: {//idoso
                if (1 >= numeroDePosicoes && (eNaHorizontal || eNaVertical) && isDay()) {
                    return true;
                }
            }
            case 1://adulto zombie
            case 6: {//adulto
                if (2 >= numeroDePosicoes && (eNaHorizontal || eNaVertical || eNaDiagonal)) {
                    return true;
                }
            }
            case 2://militar zombie
            case 7: {//militar
                if (3 >= numeroDePosicoes && (eNaHorizontal || eNaVertical || eNaDiagonal)) {
                    return true;
                }
            }
            case 4: {//vampiro zombie
                if (2 >= numeroDePosicoes && (eNaHorizontal || eNaVertical || eNaDiagonal) && !isDay()) {
                    return true;
                }
            }
            case 9: {
                if (2 >= numeroDePosicoes && eNaDiagonal) {
                    return true;
                }
            }
        }
        return false;
    }
    public void adicionarEquipamento(int humanPosition, int equipmentPosition){
        humanos.get(humanPosition).addEquipmentUsed();
        if(equipamentos.get(equipmentPosition).equipmentTipe() == 8){
            envenenados.put(humanos.get(humanPosition),turn);
            theLost.add(humanos.get(humanPosition));
        }
    }
    public boolean move(int xO, int yO, int xD, int yD) {
        int zombiePosition = encontraPosiçao(xO, yO, 0);
        int humanPosition = encontraPosiçao(xO, yO, 1);
        int equipmentPosition = encontraPosiçao(xD, yD, 2);
        int safeHeavenPosition = encontraPosiçao(xD,yD,3);
        if (getCurrentTeamId() == 20) {
            System.out.println("--------->turno do zombie");
            if (zombiePosition != -1 && movemetTests(xO, yO, xD, yD, zombies.get(zombiePosition).getTipe())) {//encontrou o zombie
                if (zombies.get(zombiePosition).getTipe() == 4 && equipamentos.get(equipmentPosition).equipmentTipe() == 5) {//se for um zombie vampiro nao se pode mover se no destino tiver alho
                    return false;
                }
                if ((encontraPosiçao(xD, yD, 1) != -1 && humanos.get(encontraPosiçao(xD, yD, 1)).getTipe() == 9) || encontraPosiçao(xD, yD, 0) != -1) {//se for um cao ou um zombie no destino
                    return false;
                }
                if (equipmentPosition != -1) {//encontra o equipamento
                    equipamentos.remove(equipamentos.get(equipmentPosition));
                    zombies.get(zombiePosition).destroyEquipment();
                    podeMover(xO, yO, xD, yD, null, zombiePosition);
                    return true;
                }
                if (encontraPosiçao(xD, yD, 1) != -1) {//no destino esta um humano
                    if (didTheHumanWon(zombiePosition, encontraPosiçao(xD, yD, 1))) {
                        if (humanos.get(encontraPosiçao(xD, yD, 1)).equipementInUse().itsForDefence()) {
                            return true;
                        } else {
                            zombies.remove(zombiePosition);//se o humano ganhou e matou o zombie
                            return true;
                        }
                    } else {//o humano perdeu e vai ser transformado
                        makeZombie(humanos.get(encontraPosiçao(xD, yD, 1)));
                    }
                }
                if(safeHeavenPosition != -1){//se no destino tiver um safe heaven
                    return false;
                }
                //se não tiver um equipamento na posição destino
                podeMover(xO, yO, xD, yD, null, zombiePosition);
                return true;
            }
        }
        if (getCurrentTeamId() == 10) {//confirma se é humano
            System.out.println("--------->turno do humano");
            System.out.println("a posição de origem é ("+ xO + "," + yO + ") e a posiçao destino é ("+ xD + "," + yD +")");
            if (humanPosition != -1 && movemetTests(xO, yO, xD, yD, humanos.get(humanPosition).getTipe())) {//encontra o humano
                if (encontraPosiçao(xD, yD, 1) != -1) {//ve se no destino esta outro humano
                    return false;
                }
                if(envenenados.get(humanos.get(humanPosition)) != null && envenenados.get(humanos.get(humanPosition)) > (turn +2)){
                    makeZombie(humanos.get(humanPosition));
                    envenenados.remove(humanos.get(humanPosition));
                    vezDosZombies();
                    return true;
                }
                if (humanos.get(humanPosition).getTipe() == 8 && humanos.get(humanPosition).equipementInUse() != null) {//antes de se mover o idoso larga o equipamento
                    Equipamento equipamentoParaLargar = humanos.get(humanPosition).equipementInUse();
                     humanos.get(humanPosition).destroyEquipment();
                    podeMover(xO, yO, xD, yD, equipamentoParaLargar, humanPosition);
                    return true;
                }
                if (equipmentPosition != -1) { // confirma se no destino está um equipamento
                    System.out.println("no  destino está um equipamento");
                    if (humanos.get(humanPosition).equipementInUse() == null) {//caso ainda nao tenha equipamento
                        humanos.get(humanPosition).setEquipamento(equipamentos.get(equipmentPosition));
                        adicionarEquipamento(humanPosition,equipmentPosition);
                        podeMover(xO, yO, xD, yD, null, humanPosition);
                        return true;
                    } else { //caso ja tenha o equipamento
                        for (Equipamento equipamento : equipamentos) {
                            if (equipamento == humanos.get(humanPosition).equipementInUse()) {
                                Equipamento equipamentoDestino = equipamentos.get(equipmentPosition);
                                humanos.get(humanPosition).setEquipamento(equipamentoDestino);
                                adicionarEquipamento(humanPosition,equipmentPosition);
                                podeMover(xO, yO, xD, yD, equipamento, humanPosition);
                                return true;
                            }
                        }
                    }
                }
                if (encontraPosiçao(xD, yD, 0) != -1) {//no destino está um zombie
                    if (humanos.get(humanPosition).equipementInUse() == null) {
                        return false;//se não tiver equipamento nao pode se mover para o zombie
                    } else {
                        if (didTheHumanWon(encontraPosiçao(xD, yD, 0), humanPosition)) {
                            if (humanos.get(humanPosition).equipementInUse().itsForDefence()) {
                                vezDosZombies();
                                return true;//so se defendeu do zombie
                            } else {
                                zombies.remove(zombiePosition);//destruio o zombie
                                podeMover(xO, yO, xD, yD, null, humanPosition);
                                return true;
                            }

                        } else {//se perder a batalha contra o zombie
                            makeZombie(humanos.get(humanPosition));
                            vezDosZombies();
                            return true;
                        }
                    }
                }
                if(safeHeavenPosition != -1){//verifica se no destino tem uma porta
                    safeHeaven.get(safeHeavenPosition).addHuman(humanos.get(humanPosition));
                    if(envenenados.get(humanos.get(humanPosition))!= null){
                        theLost.remove(humanos.get(humanPosition));
                    }
                    humanos.remove(humanPosition);
                    vezDosZombies();
                    return true;
                }
                podeMover(xO, yO, xD, yD, null, humanPosition);
                return true;
            }
        }
        return false;
    }

    public boolean gameIsOver() {
        System.out.println("turn = "+ turn);
        return turn >= 6 || (zombies == null && humanos == null);
    }


    public List<String> getAuthors() {
        List<String> autores = new ArrayList<String>();
        autores.add("Pedro Martins");
        return autores;
    }

    public int getCurrentTeamId() {
        return currentTeamID;
    }

    public int getElementId(int x, int y) {

        if (encontraPosiçao(x, y, 0) != -1) { //se for 1 zombie
            return zombies.get(encontraPosiçao(x, y, 0)).getId();
        }
        if (encontraPosiçao(x, y, 1) != -1) { //se for 1 humano
            return humanos.get(encontraPosiçao(x, y, 1)).getId();
        }
        if (encontraPosiçao(x, y, 2) != -1) { //se for 1 equipamento
            return equipamentos.get(encontraPosiçao(x, y, 2)).getId();
        }
        return 0;
    }

    public List<String> getGameResults() {
        List<String> survivors = new ArrayList<>();
        survivors.add("" + turn);
        survivors.add("");
        survivors.add("Ainda pelo bairro:");
        survivors.add("");
        survivors.add("Os VIVOS");
        for (Humano humano : humanos) {
            survivors.add("" + humano.getId() + " " + humano.nome);
        }
        survivors.add("");
        survivors.add("OS OUTROS");
        for (Zombie zombie : zombies) {
            survivors.add("" + zombie.getId() + " " + zombie.nome);
        }
        survivors.add("Num safe haven:");
        survivors.add("");
        survivors.add("OS VIVOS");
        for (SafeHeaven safeHeaven : safeHeaven) {
            for (Humano humano : safeHeaven.hosSafe()) {
                survivors.add("" + humano.getId() + " " + humano.nome);
            }
        }
        survivors.add("");
        survivors.add("Envenenados / Destruidos");
        survivors.add("");
        survivors.add("OS VIVOS");
        for (Creature creature : theLost) {
            if (creature.tipe < 4) {//vivos
                survivors.add("" + creature.getId() + " " + creature.nome);
            }
        }
        survivors.add("");
        survivors.add("OS OUTROS");
        for (Creature creature : theLost) {
            if (creature.tipe < 5) {//zombies
                survivors.add("" + creature.getId() + " " + creature.nome);
            }
        }
        return survivors;
    }

    public boolean isDay() {
        if (turn != 1 && (turn - 1) % 2 == 0) {
            eDia = !eDia;
        }
        return eDia;
    }

    public int getEquipmentId(int creatureId) {
        for (Humano humano : humanos) {
            if (humano.creatureID == creatureId && humano.equipamento != null) {
                return humano.equipamento.idTipo;
            }
        }
        return 0;
    }

    public List<Integer> getIdsInSafeHaven() {
        List<Integer> ids = new ArrayList<>();
        for (SafeHeaven percorrer : safeHeaven) {
            for (Humano humano : percorrer.hosSafe()) {
                ids.add(humano.getId());
            }
        }
        return ids;
    }

    public boolean isDoorToSafeHaven(int x, int y) {
        return encontraPosiçao(x, y, 3) != -1;
    }

    public int getEquipmentTypeId(int equipmentId) {
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.equipmentTipe() == equipmentId) {
                return equipamento.equipmentTipe() ;
            }
        }
        return 0;
    }

    public String getEquipmentInfo(int equipmentId) {
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getId() == equipmentId) {
                return equipamento.getNome() + " | " + equipamento.getUtilizacoes();//todo
            }
        }
        return null;
    }/*
    public boolean saveGame(File fich){
        if(map==null|| fich == null){
            return  false;
        }
        int numberCreatures = (humanos.size() +zombies.size());
        try {
            FileWriter writer = new FileWriter(fich.getName());
            writer.write("" + worldSize[0] + " " + worldSize[1]);
            writer.write("" + currentTeamID);
            writer.write("" + numberCreatures);
            for (Humano humano : humanos){
                writer.write(""+ humano.getId() + " : " + humano.getTipe() + " : " + humano.theName() + " : " + humano.getPosicao().x + " : " + humano.getPosicao().y);
            }
            writer.write("" + equipamentos.size());
            for(Equipamento equipamento : equipamentos){
                writer.write(""+ equipamento.seeEquippementID() + " : " + equipamento.idTipo+ " : " + equipamento.);
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }*/
}
