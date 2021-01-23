package frc.robot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteCSVFile {

    // delimiters for the CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\n";

    // file header
    private static final String HEADER = "imuAngle,imuRate,imuXAccel,imuYAccel,imuZAccel,lEncPos,lEncVel,rEncPos,rEncVel";

    private static int count = 0;

    public static String writeData(ArrayList<Double> imuAngle, ArrayList<Double> imuRate, ArrayList<Double> imuXAccel,
            ArrayList<Double> imuYAccel, ArrayList<Double> imuZAccel, ArrayList<Double> lEncPos,
            ArrayList<Double> lEncVel, ArrayList<Double> rEncPos, ArrayList<Double> rEncVel) {

        FileWriter fileWriter = null;
        String output = "";
        count++;
        
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

        /* try {
            fileWriter = new FileWriter("" + count + ".csv"); */

            /* // adding the header
            fileWriter.append(HEADER);
            // newline after header
            fileWriter.append(LINE_SEPARATOR); */

            /* for (int i = 0; i < imuAngle.size(); i++) {
                fileWriter.append("" + imuAngle.get(i));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("" + imuRate.get(i));
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append("" + imuXAccel.get(i));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("" + imuYAccel.get(i));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("" + imuZAccel.get(i));
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append("" + lEncPos.get(i));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("" + lEncVel.get(i));
                fileWriter.append(COMMA_DELIMITER);

                fileWriter.append("" + rEncPos.get(i));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("" + rEncVel.get(i));
                fileWriter.append(LINE_SEPARATOR);
            } */

            /* fileWriter.append(output);

            System.out.println("Write to CSV file successful");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ie) {
                System.out.println("Error occurred while closing the fileWriter");
                ie.printStackTrace();
            }
        } */

        return output;
    }
}
