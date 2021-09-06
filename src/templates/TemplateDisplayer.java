package templates;

import textui.PresenterPrompts;

import java.util.List;
import java.util.Map;

/**
 * Displays anything related to templates
 */
public class TemplateDisplayer {
    private Map<PresenterPrompts, String> promptsMap;

    public TemplateDisplayer(Map<PresenterPrompts, String> promptsMap) {
        this.promptsMap = promptsMap;
    }

    public void displayTemplate(Map<String, String> template) {
        System.out.println("{");
        for (String config : template.keySet()) {
            System.out.println(promptsMap.get(PresenterPrompts.valueOf("templateDisplayer" + config)) +
                    template.get(config) + ";");
        }
        System.out.println("}\n");
    }

    public void displayAllTemplates(TemplateManager templateManager) {
        System.out.println(promptsMap.get(PresenterPrompts.templateDisplayerTemplates));
        List<Map<String, String>> templates = templateManager.getTemplates();
        for (Map<String, String> template : templates) {
            displayTemplate(template);
        }
        System.out.println();
    }
}
