import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: update RFC enforcement after commas are allowed in quotes
// TODO: update RFC enforcement after quotes are allowed in quotes
/*
*   Load a CSV file into an ArrayList.
*   The ArrayList contains a String[]. Each String[] has the same length.
*   This is based off of RFC 4180, which can be found at:
*   https://datatracker.ietf.org/doc/html/rfc4180
*   However, many of the requirements are loosened to allow for more CSVs
*   to be parsed without error.
*   
*   Section 2.1 - ignored; non-CRLF line endings are allowed
*   Section 2.2 - enforced
*   Section 2.3 - enforced; an arbitrary number of rows can be skipped
*   Section 2.4 - partially enforced; all records must have the same number of
*                 fields; spaces at the beginning of a field may be ignored if
*                 it is detected that a ", " separator is being used; a
*                 trailing comma will result in a parse error
*   Section 2.5 - enforced
*   Section 2.6 - ignored; line endings and double quotes in fields
*                 will result in parsing errors
*   Section 2.7 - ignored; two double quotes will be treated as two double
*                 quotes instead of a single escaped double quote
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
        //String[] splitLine = line.split(separator);
        String[] splitLine = splitLine(line);

        // Do some validation on the line
        // This line will throw a CSVParseException if something is invalid
        validateLine(splitLine);

        // Use the first row to calculate the expected length of other rows
        int expectedLineLength = splitLine.length;

        // Append the line to the data
        data.add(splitLine);

        // Loop over the other lines, parsing the info
        while ((line = br.readLine()) != null) {
            // Split the line
            splitLine = splitLine(line);

            // Validate the line, including length
            validateLine(splitLine, expectedLineLength);

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

    // Split the line using an autodetected separator
    // If double quotes are used, separators used within them will be ignored
    private static String[] splitLine(String line) {
        // Detect if ", " or "," is being used
        String separator = ",";
        if (line.indexOf(", ") != -1) {
            separator = ", ";
        }

        // If no quotes are used, just use line.split() to make things easier
        if (line.indexOf('"') == -1) {
            return line.split(separator);
        }

        // If quotes are present, iterate over the line manually
        
        // Store if quotes are active
        boolean quotesActive = false;

        // Store the split parts of the line
        ArrayList<String> splitLine = new ArrayList<>();

        // Store the current field
        StringBuilder currentField = new StringBuilder();

        // How to split when the separator is ", "?
        // Two characters, so can't detect single character anymore
        // Loop over and split on "," even if ", " is used?
        //  - Afterwards if ", " was detected remove at most 1 leading
        //    space from all but the first fields 

        // Loop over the line
        for (char c : line.toCharArray()) {
            // If the character is a comma and quotes arent' active, end the field
            if (c == ',' && !quotesActive) {
                splitLine.add(currentField.toString());
                currentField = new StringBuilder();

                continue;
            }

            // If the character is a double quote, toggle the quote flag
            if (c == '"') {
                quotesActive = !quotesActive;
            }

            // Add the character to the field
            currentField.append(c);
        }
        splitLine.add(currentField.toString());

        // If the separator is ", ", manually remove the leading
        //   spaces.
        if (separator.equals(", ")) {
            // Iterate over each field except for the first
            for (int i = 1; i < splitLine.size(); i++) {
                String field = splitLine.get(i);

                // If the first character is a space, remove it
                if (field.charAt(0) == ' ') {
                    splitLine.set(i, field.substring(1));
                }
            }
        }

        // Copy the ArrayList into a String[]
        String[] out = new String[splitLine.size()];
        for (int i = 0; i < splitLine.size(); i++) {
            out[i] = splitLine.get(i);
        }

        return out;
    }

    // Validates some information about the line:
    //  - RFC 4180 section 2.5: fields may or may not include double quotes
    //    - If a field contains double quotes, it must be enclosed
    //        in double quotes
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
                    System.out.println(val);
                    System.out.println(firstQuoteIndex);
                    System.out.println(lastQuoteIndex);
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
        validateLine(line);
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
