package studytools;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Records the edit history of a study tool
 */
public class StudyToolHistory implements Serializable {

    private Map<LocalDateTime, StudyTool> historyMapper;    // Maps each time stamp to a copy of study tool
    private Map<LocalDateTime, String> editorMapper;        // Maps each time stamp to editor's id

    StudyToolHistory(StudyTool initialStudyTool, String initialEditorId) {
        this.historyMapper = new HashMap<>();
        this.editorMapper = new HashMap<>();
        this.recordHistory(initialStudyTool, initialEditorId);
    }

    /**
     * Records a duplicate of the study tool into history
     * @param s study tool to be recorded
     * @param editorID editor's id
     */
    void recordHistory(StudyTool s, String editorID) {
        LocalDateTime nowTime = LocalDateTime.now(ZoneId.of("GMT-4"));
        this.historyMapper.put(nowTime, s.duplicateStudyTool());
        this.editorMapper.put(nowTime, editorID);
    }

    Map<LocalDateTime, StudyTool> getHistoryMapper() {
        return historyMapper;
    }

    Map<LocalDateTime, String> getEditorMapper() {
        return editorMapper;
    }

    List<LocalDateTime> getLatestToEarliestDateTimes() {
        List<LocalDateTime> times = new ArrayList<>(this.historyMapper.keySet());
        times.sort(LocalDateTime::compareTo);   // Sorted in earliest to latest fashion
        Collections.reverse(times);
        return times;
    }
}
