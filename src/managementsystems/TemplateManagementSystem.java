package managementsystems;

import templates.TemplateDisplayer;
import templates.TemplateManager;
import textui.AdminPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * An instance of this class represents a template management system to control interactions of users
 * with templates in the program.
 *
 * Controls creating templates, viewing templates and editing templates.
 */
public class TemplateManagementSystem implements ManagementRunnable {

    private TemplateDisplayer templateDisplayer;
    private AdminPresenter adminPresenter;
    private Scanner scanner;
    private TemplateManager templateManager;

    /**
     * Create an instance of TemplateManagementSystem.
     * @param templateDisplayer A template displayer.
     * @param adminPresenter An admin presenter.
     * @param templateManager The template manager of the program, already instantiated.
     * @param scanner The scanner for the program.
     */
    public TemplateManagementSystem(TemplateDisplayer templateDisplayer, AdminPresenter adminPresenter,
                                    Scanner scanner, TemplateManager templateManager) {
        this.templateDisplayer = templateDisplayer;
        this.adminPresenter = adminPresenter;
        this.scanner = scanner;
        this.templateManager = templateManager;
    }

    /**
     * Runs the main menu of the TemplateManagementSystem allowing users to select what they
     * would like to do with the templates of the following options:
     *      1, View all templates to edit.
     *      2, Create a template.
     *      3, Quit.
     */
    public void run() {
        boolean running = true;
        while (running) {
            adminPresenter.templateMenu();
            String selection = scanner.nextLine();
            switch (selection) {
                case "1":  // view all templates to edit
                    selectingTemplateToEditMenu();
                    break;
                case "2":  // create a template
                    createTemplateMenu();
                    break;
                case "3":  // quit
                    running = false;
                    break;
                default:  // invalid input
                    adminPresenter.sayInvalidInput();
            }
        }
    }

    // editing template helper methods
    /**
     * Runs the selecting template to edit menu of the AdminManagementSystem allowing admin users
     * to select what template they would like to make changes to out of the available templates
     * displayed above.
     *
     * Upon a valid ID, the program will advance to the editing menu.
     * If an invalid input is entered, the user will be prompted again until a correct id is chosen
     * or q is selected to quit this menu.
     */
    private void selectingTemplateToEditMenu() {
        boolean selecting = true;
        while (selecting) {
            templateDisplayer.displayAllTemplates(templateManager);
            adminPresenter.selectTemplatePrompt();
            String selection = scanner.nextLine();

            if (!selection.equals("q") && templateManager.validId(selection)) {
                editingMenu(Integer.parseInt(selection));
            } else if (selection.equals("q")) {
                selecting = false;
            } else {
                adminPresenter.sayInvalidInput();
            }
        }
    }
    /**
     * Runs the editing menu of the AdminManagementSystem allowing admin users to select what
     * edits they would like to make to the selected template displayed above, out of the following options:
     *      1, Change the template's name.
     *      2, Delete the template.
     *      3, Change the template's timed settings.
     *      4, Change the template's score settings.
     *
     *      If flash card template:
     *      5, If unfamiliar words reappear at the end of the quiz.
     *      6, Quit.
     *
     *      If quiz template:
     *      5, If answers are shown after each question.
     *      6, If answers are shown after the entire quiz.
     *      7, Quit.
     *
     *      If sorting template:
     *      5, Change the number of categories
     *      6, Quit.
     *
     * Makes the changes directly or directs them to the appropriate method to make said changes.
     */
    private void editingMenu(int tID) {
        templateDisplayer.displayTemplate(templateManager.getTemplate(tID));
        TemplateManager.TemplateType templateType = templateManager.getTemplateType(tID);

        adminPresenter.editTemplatePrompt(templateType.toString());
        String selection = scanner.nextLine();

        switch (selection) {
            case "1": // edit template name
                adminPresenter.editNamePrompt();
                templateManager.editTemplateName(tID, scanner.nextLine());
                adminPresenter.sayEdited();
                break;
            case "2": // delete template
                templateManager.deleteTemplate(tID);
                adminPresenter.sayTemplateDeleted();
                break;
            case "3": // change timed settings
                editTimedSettingsMenu(tID);
                adminPresenter.sayEdited();
                break;
            case "4": // change scored settings
                boolean isScored;
                while(true) {
                    adminPresenter.editIsScoredPrompt();
                    String input = scanner.nextLine();
                    if (input.equals("true") || input.equals("false")) {
                        isScored = Boolean.parseBoolean(input);
                        break;
                    }
                }
                templateManager.editIsScored(tID, isScored);
                adminPresenter.sayEdited();
                break;
            case "5": // depends
                if (templateType.equals(TemplateManager.TemplateType.FC)) {
                    boolean wordsReappear;
                    while(true) {
                        adminPresenter.editWordsReappearPrompt();
                        String input = scanner.nextLine();
                        if (input.equals("true") || input.equals("false")) {
                            wordsReappear = Boolean.parseBoolean(input);
                            break;
                        }
                    }
                    templateManager.editWordsReappear(tID, wordsReappear);
                    adminPresenter.sayEdited();
                } else if (templateType.equals(TemplateManager.TemplateType.SOR)){
                    while (true) {
                        adminPresenter.editNumCategories();
                        String input = scanner.nextLine();
                        int numCategories;
                        numCategories = Integer.parseInt(input);
                        templateManager.editNumCategories(tID, numCategories);
                    }
                }
                break;
        }
    }
    /**
     * Runs the editing timed settings menu of the AdminManagementSystem allowing admin users to select what
     * edits they would like to make to the selected template's timed settings, out of the following options:
     *      1, If the template is timed or not.
     *      2, If the template is timed per quiz or per question.
     *      3, The time limit.
     *      4, Quit.
     *
     * Makes the changes directly.
     */
    private void editTimedSettingsMenu(int tID) {
        adminPresenter.editTimePrompt();
        String selection = scanner.nextLine();

        if (selection.equals("1")) { // edit whether it is timed or not
            boolean isTimed;
            while(true) {
                adminPresenter.editIsTimedPrompt();
                String input = scanner.nextLine();
                if (input.equals("true") || input.equals("false")) {
                    isTimed = Boolean.parseBoolean(input);
                    break;
                }
            }
            templateManager.editIsTimed(tID, isTimed);
        } else if (selection.equals("2")) { // edit time per question or quiz
            boolean timedPerQuestionNotQuiz;
            while(true) {
                adminPresenter.editTimedPerQuestionNotQuizPrompt();
                String input = scanner.nextLine();
                if (input.equals("true") || input.equals("false")) {
                    timedPerQuestionNotQuiz = Boolean.parseBoolean(input);
                    break;
                }
            }
            templateManager.editTimedPerQuestionNotQuiz(tID, timedPerQuestionNotQuiz);
        } else if (selection.equals("3")) { // edit time limit
            int timeLimit;
            while(true) {
                adminPresenter.editTimeLimitPrompt();
                String input = scanner.nextLine();
                try {
                    if (0 < Integer.parseInt(input)) {
                        timeLimit = Integer.parseInt(input);
                        break;
                    }
                } catch (NumberFormatException ignored) {}
            }
            templateManager.editTimeLimit(tID, timeLimit);
        }
    }

    // creating template helper methods
    /**
     * Runs the create template menu of the AdminManagementSystem allowing admin users to select what
     * template type they would like to make, out of the following options:
     *      1, Flash card template.
     *      2, Multiple Choice Quiz Template.
     *      3, Exact Answer Quiz Template.
     *      4, Quit.
     *
     * Creates the template directly.
     */
    private void createTemplateMenu(){
        adminPresenter.createTemplatePrompt();
        String selection = scanner.nextLine();
        List info;

        switch (selection) {
            case "1": { // create Flash Card Quiz Template
                info = retList();
                adminPresenter.editWordsReappearPrompt();
                info.add(scanner.nextLine());
                adminPresenter.sayFCCreated(templateManager.createTemplate(TemplateManager.TemplateType.FC, info));
                break;
            }
            case "2": { // create Multiple Choice Quiz Template
                info = retList();
                adminPresenter.sayMCQCreated(templateManager.createTemplate(TemplateManager.TemplateType.MCQ, info));
                break;
            }
            case "3": { // create Exact Answer Quiz Template
                info = retList();
                adminPresenter.sayEAQCreated(templateManager.createTemplate(TemplateManager.TemplateType.EAQ, info));
                break;
            }
            case "4": { // create Sorting Quiz Template
                info = retList();
                adminPresenter. editNumCategories();
                info.add(scanner.nextLine());
                adminPresenter.saySORCreated(templateManager.createTemplate(TemplateManager.TemplateType.SOR, info));
                break;
            }
        }
    }
    /**
     * Prompts and creates a List representing the basic commonalities of all templates.
     * @return The information received from prompts in a List of the format:
     *      [name, isTimed, timedPerQuestionNotQuiz, timeLimit, isScored]
     */
    private List retList() {
        List retList = new ArrayList<String>();

        adminPresenter.setNamePrompt();
        retList.add(scanner.nextLine());
        adminPresenter.editIsTimedPrompt();
        retList.add(scanner.nextLine());
        adminPresenter.editTimedPerQuestionNotQuizPrompt();
        retList.add(scanner.nextLine());
        while (true) {
            adminPresenter.editTimeLimitPrompt();
            String userIn = scanner.nextLine();
            if (userIn.matches("^\\d+$")) {  // Make sure this is an integer
                retList.add(userIn);
                break;
            }
            else adminPresenter.sayInvalidInput();
        }
        adminPresenter.editIsScoredPrompt();
        retList.add(scanner.nextLine());
        return retList;
    }
}
