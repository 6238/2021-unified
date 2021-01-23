package frc.robot;

import java.util.ArrayList;

public class SaveData {

    // delimiters for the CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    // file header
    private static final String HEADER = "imuAngle,imuRate,imuXAccel,imuYAccel,imuZAccel,lEncPos,lEncVel,rEncPos,rEncVel";

    private static int count = 0;

    public static void writeData(ArrayList<Double> imuAngle, ArrayList<Double> imuRate, ArrayList<Double> imuXAccel,
            ArrayList<Double> imuYAccel, ArrayList<Double> imuZAccel, ArrayList<Double> lEncPos,
            ArrayList<Double> lEncVel, ArrayList<Double> rEncPos, ArrayList<Double> rEncVel) {

        String output = "";
        System.out.println(count++);
        
        output += HEADER;
        output += LINE_SEPARATOR;
        
        for (int i = 0; i < imuAngle.size(); i++) {
            output += "" + imuAngle.get(i);
            output += COMMA_DELIMITER;
            output += "" + imuRate.get(i);
            output += COMMA_DELIMITER;

            output += "" + imuXAccel.get(i);
            output += COMMA_DELIMITER;
            output += "" + imuYAccel.get(i);
            output += COMMA_DELIMITER;
            output += "" + imuZAccel.get(i);
            output += COMMA_DELIMITER;

            output += "" + lEncPos.get(i);
            output += COMMA_DELIMITER;
            output += "" + lEncVel.get(i);
            output += COMMA_DELIMITER;

            output += "" + rEncPos.get(i);
            output += COMMA_DELIMITER;
            output += "" + rEncVel.get(i);
            output += LINE_SEPARATOR;
        }

        System.out.println(output);
    }
}
