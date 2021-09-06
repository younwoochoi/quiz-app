package filereadwriter;

import textui.PresenterPrompts;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deals with loading/saving text files
 */
public class TextReadWriter implements PromptsLoader {

    String directory = "";

    // File paths
    String adminFilePath = directory + "settings/Adminkey.txt";
    String promptsDirectoryPath = directory + "settings/";
    String promptsFilePath = "ms.txt";

    public TextReadWriter() {}

    /**
     * Loads the admin key from file or sets it to default accordingly.
     * @return The admin key for the program.
     */
    public String loadAdminKey() {
        String adminKey;
        String defaultAdminKey = "1234";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(adminFilePath));
            adminKey = bufferedReader.readLine();
            bufferedReader.close();
            return adminKey;
        } catch (IOException e) {
            return defaultAdminKey;
        }
    }

    /**
     * Load the prompts stored in filePath, with pattern {promptName: promptBody}
     * @param language the specific language file to be loaded
     * @return a map that maps each prompt title to prompt body
     */
    @Override
    public Map<PresenterPrompts, String> promptsLoader(String language) {
        Map<PresenterPrompts, String> retMap = new HashMap<>();
        try {
            String str = batchReader(promptsDirectoryPath + language + "/" + promptsFilePath);
            Pattern pattern = Pattern.compile("\\{(\\w*)\\s*:\\s*([^}]*)}");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                PresenterPrompts promptItem = PresenterPrompts.valueOf(matcher.group(1));
                String prompt = matcher.group(2);
                retMap.put(promptItem, prompt);
            }
        } catch (IOException|IllegalArgumentException e) {
            e.printStackTrace();
        }
        return retMap;
    }

    private String batchReader(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine()).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    /**
     * Edits the welcomer message.
     * @param newMessage The new welcomer message.
     * @param language The language the welcomer message will be changed in.
     */
    public void editWelcomer(String newMessage, String language) {
        editPrompt(newMessage, language, 2, "welcomer");
    }
    /**
     * Edits the exit message.
     * @param newMessage The new exit message.
     * @param language The language the exit message will be changed in.
     */
    public void editExiter(String newMessage, String language) {
        editPrompt(newMessage, language, 3, "exiter");
    }
    /**
     * Edits the prompt on line lineNumber to contain newMessage for the current language given
     * the promptName of that line is correct.
     * @param newMessage The new message.
     * @param language The language the prompt will be edited in.
     * @param lineNumber The line number of this prompt in the file.
     * @param promptName The prompt name for alignment with PresenterPrompts.
     */
    private void editPrompt(String newMessage, String language, int lineNumber, String promptName) {
        String filepath = promptsDirectoryPath + language + "/" + promptsFilePath;
        int counter = 1;

        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filepath));
            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = fileIn.readLine()) != null) {
                if (counter == lineNumber) {
                    line = "{" + promptName + ": " + newMessage + "}";
                }
                counter++;
                buffer.append(line);
                buffer.append('\n');
            }
            fileIn.close();

            FileOutputStream fileOut = new FileOutputStream(filepath);
            fileOut.write(buffer.toString().getBytes());
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits the admin key.
     * @param newKey The new admin key.
     */
    public void editAdminKey(String newKey) {
        String filepath = adminFilePath;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filepath));
            StringBuffer buffer = new StringBuffer();

            buffer.append(newKey);
            fileIn.close();

            FileOutputStream fileOut = new FileOutputStream(filepath);
            fileOut.write(buffer.toString().getBytes());
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
