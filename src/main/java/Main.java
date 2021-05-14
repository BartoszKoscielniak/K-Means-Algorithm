import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args){

        double startTime = System.nanoTime();

        int k = 3;//ilosc skupien
        Double[][] objects = new Double[0][];
        Double[][] distance;
        Double[][] testObjects = new Double[k][];
        Integer[]  decCounter = new Integer[k];
        Integer[]  decision = new Integer[0];
        int rows = 0;
        int col = 0;
        int changes;
        int q = 0;
        boolean changeOccured = true;

        try(CSVReader csvReader = new CSVReader(new FileReader("src\\main\\resources\\test.csv"))) {
            List<String[]> array = csvReader.readAll();
            rows = array.size();
            col = array.get(0).length;

            objects = new Double[rows][col];
            testObjects = new Double[k][col];
            decision = new Integer[rows];
            for (int x = 0; x < rows; x++){
                for (int y = 0; y < col; y++){
                    objects[x][y] = Double.parseDouble(array.get(x)[y]);
                }
            }

            //for (int draw = 0; draw < k; draw++){
                Random random = new Random();
                int rand = random.nextInt(rows - 1);
                for (int b = 0; b < col; b++){
                    testObjects[0][b] = objects[0][b];
                    testObjects[1][b] = objects[22][b];
                    testObjects[2][b] = objects[38][b];
                }
            //}

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        while (changeOccured){//petla iteracji

            distance = new Double[rows][k];
            q++;
            for (int x = 0; x < rows; x++){//petla wierszy
                for (int p = 0; p < k; p ++) {//liczenie odleglosci od kazdego z centrow
                    double result = 0;
                    for (int y = 0; y < objects[x].length; y++) {//petla kolumn
                        result += Math.pow((objects[x][y] - testObjects[p][y]), 2);
                    }
                    distance[x][p] = Math.sqrt(result);
                }
            }

            changes = 0;
            changeOccured = false;
            for (int m = 0; m < distance.length; m++){//okreslenie decyzji
                double min = Double.MAX_VALUE;
                int minIndex = -1;
                for (int n = 0; n < distance[m].length; n++){
                    if(min > distance[m][n]){
                        min = distance[m][n];
                        minIndex = n + 1;
                    }
                }
                if (decision[m] == null || decision[m] != minIndex) {
                    changes++;
                    changeOccured = true;
                }
                decision[m] = minIndex;
            }

            testObjects = new Double[k][col];
            for (int g = 0; g < testObjects.length; g++){
                for (int s = 0; s < testObjects[g].length; s++){
                    testObjects[g][s] = 0.0;
                }
            }

            Arrays.fill(decCounter,0);
            for (int newCentroid = 0; newCentroid < decision.length; newCentroid++){//okreslanie nowych centrow
                for (int xP = 0; xP < k; xP++){
                    if (decision[newCentroid] == (xP + 1)){
                        decCounter[xP] += 1;
                        for (int yP = 0; yP < testObjects[xP].length; yP++){
                            testObjects[xP][yP] += objects[newCentroid][yP];
                        }
                    }
                }
            }

            for (int xP = 0; xP < testObjects.length; xP++){
                for (int yP = 0; yP < testObjects[xP].length; yP++){
                    testObjects[xP][yP] = testObjects[xP][yP]/decCounter[xP];
                }
            }
        }
        for (int m = 0; m < objects.length; m++){
            System.out.print((m + 1) + ". ");
            for (int n = 0; n < objects[m].length; n++){
                System.out.print(objects[m][n] + ", ");
            }
            System.out.print("decyzja: " + decision[m]);
            System.out.println();
        }

        double elapsedTime = System.nanoTime() - startTime;
        System.out.println("Wykonanie programu zajelo: " + elapsedTime/1000000000 + " sekundy");
    }
}
