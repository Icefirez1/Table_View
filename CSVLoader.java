import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

/*
*   Load a CSV file into an ArrayList.
*   The ArrayList contains a String[]. Each String[] has the same length.
*   This is mostly compliant with RFC 4180, which can be found at:
*   https://datatracker.ietf.org/doc/html/rfc4180
*/
public class CSVLoader {
    /**
     * Parses the given CSV file and returns an ArrayList of each row as a
     * String[].
     * @param file The path of the file to parse
     * @param skipRows The number of rows to skip when parsing the file.
     * @return An ArrayList of String[], where each String[] is a row
     * @throws FileNotFoundException if the given file does not exist
     * @throws IOException if anything goes wrong in opening the file
     * @throws CSVParseException if any parse errors occur, such as one row
     * having too many or too few entries
     */
    public static ArrayList<String[]> loadCSV(String file, int skipRows)
        throws FileNotFoundException, IOException, CSVParseException {

        // Technically this parser ignores RFC 4180 section 2.1
        // This parser does not force CRLF to be used. It will accept
        //   other line breaks.
        // Section 2.1 is silly, so I'm not going to enforce CRLF line breaks

        // TODO: implement RFC 4180 section 2.6:
        // "Fields containing line breaks, double quotes, and commas
        //   should be enclosed in double quotes."
        // So records != lines. For example:
        //   1,2,3
        //   a,"b\n",c
        //   ",",;,'
        // should be perfectly valid, if "\n" was actually replaced by a newline

        // TODO: enforce RFC 4180 section 2.7:
        // "If double quotes are used to enclose fields, then a 
        //   double quote appearing inside a field must be escaped
        //   by preceeding it with another double quote."
        // Ex. "aaa","b""bb","ccc" is the same as aaa,bbb,ccc


        // Ask Paul about validate line

        // Open a buffered reader
        BufferedReader br = Files.newBufferedReader(Path.of(file));

        // Create an ArrayList to store the lines
        ArrayList<String[]> data = new ArrayList<>();

        // Skip the number of lines specified
        for (int i = 0; i < skipRows; i++) {
            br.readLine();
        }

        // Parse the first non-skipped line separately to get row length
        String line = br.readLine();
        
        // Use the separator to parse the first line
        // According to RFC 4180 section 2.4 "spaces are considered part of a
        // field and should not be ignored".
        String[] splitLine = line.split(",");

        // Do some validation on the line
        // This line will throw a CSVParseException if something is invalid
        // uhm validateLine(splitLine);

        // Use the first row to calculate the expected length of other rows
        int expectedLineLength = splitLine.length;

        // Append the line to the data
        data.add(splitLine);

        // Loop over the other lines, parsing the info
        while ((line = br.readLine()) != null) {
            // Split the line
            splitLine = line.split(",");

            // Validate the line, including length
            // uhm validateLine(splitLine, expectedLineLength);

            // Otherwise, add it to the data
            data.add(splitLine);
        }

        // Close the reader
        br.close();

        return data;
    }

    /**
     * Parses the given CSV file and returns an ArrayList of each row as a
     * String[].
     * @param file The path of the file to parse
     * @return An ArrayList of String[], where each String[] is a row
     * @throws FileNotFoundException if the given file does not exist
     * @throws IOException if anything goes wrong in opening the file
     * @throws CSVParseException if any parse errors occur, such as one row
     * having too many or too few entries
     */
    public static ArrayList<String[]> loadCSV(String file)
        throws FileNotFoundException, IOException, CSVParseException {
        
        return loadCSV(file, 0);
    }

    // Validates some information about the line:
    //  - RFC 4180 section 2.5: fields may or may not include double quotes
    //    - If a field contains double quotes, it must be enclosed
    //        in double quotes
    //  
    private static void validateLine(String[] line) throws CSVParseException {
        for (String val : line) {
            // Enforce RFC 4180 section 2.5:
            // "Each field may or may not be enclosed in double quotes"
            // "If fields are not enclosed with double quotes, then double
            //   quotes may not appear inside the fields."
            // The RFC also indicates that this does not have to be consistent
            // This does not even have to be consistent within a row

            // Check if the val starts and ends with quotes
            int firstQuoteIndex = val.indexOf('"');
            int lastQuoteIndex = val.lastIndexOf('"');

            // Only validate if quotes are actually present
            if (firstQuoteIndex != -1) {
                // If the first quote isn't at the start or the
                //   the last quote isn't at the end throw an error
                if (firstQuoteIndex != 0 || lastQuoteIndex != val.length()-1) {
                    throw new CSVParseException("Values with quotes must be enclosed in quotes");
                }

                // Edge case: if a field is just a single quote then it is invalid
                //   even though the above boolean statement will be true
                if (firstQuoteIndex == lastQuoteIndex) {
                    throw new CSVParseException("Values with quotes must be enclosed in quotes");
                }
            }
        }
    }

    // Validates all the same information as validateLine(String[])
    // However it also validates line length (how many values are in a line)
    private static void validateLine(String[] line, int lineLength) throws CSVParseException {
        // If the line is the wrong length, throw a parse exception
        if (line.length != lineLength) {
            throw new CSVParseException("All rows must have the same length.");
        }

        // Valdiate everything else
        // uhm validateLine(line);
    }

    public static void main(String[] args) {
        ArrayList<String[]> data = null;
        try {
            data = loadCSV("freshman_lbs.csv");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        if (data != null) {
            for (String[] row : data) {
                System.out.println(Arrays.toString(row));
            }
        }
    }
}
