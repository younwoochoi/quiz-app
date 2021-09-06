package filereadwriter;


import textui.PresenterPrompts;

import java.util.Map;

public interface PromptsLoader {
    Map<PresenterPrompts, String> promptsLoader(String filePath);
}

