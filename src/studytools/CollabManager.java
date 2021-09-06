package studytools;

import filereadwriter.IGateway;
import filereadwriter.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Manages collaboration feature, keeps track of who is invited to edit which study tool, as well as the
 * entire edit history of each study tool
 */
public class CollabManager implements Serializable {

    // History maps each study tool id to its history, invitation maps each id to a list of user id's allowed to edit it
    private HashMap<String, StudyToolHistory> historyHashMap;
    private HashMap<String, ArrayList<String>> invitationHashMap;
    private IGateway iGateway = new Serializer();

    public CollabManager() {
        this.historyHashMap = new HashMap<>();
        this.invitationHashMap = new HashMap<>();
    }

    public void loadFromMaps(HashMap<String, StudyToolHistory> historyHashMap, HashMap<String, ArrayList<String>> invitationHashMap) {
        this.historyHashMap = historyHashMap;
        this.invitationHashMap = invitationHashMap;
    }

    public void saveToSer(String historyFilePath, String invitationFilePath) throws IOException {
        this.iGateway.serializeObject(historyHashMap, historyFilePath);
        this.iGateway.serializeObject(invitationHashMap, invitationFilePath);
    }

    public void addCollaborator(String studyToolID, String userID) {
        if (this.invitationHashMap.containsKey(studyToolID)) {
            this.invitationHashMap.get(studyToolID).add(userID);
        }
        else {
            this.invitationHashMap.put(studyToolID, new ArrayList<>(Collections.singletonList(userID)));
        }
    }

    public void removeCollaborator(String studyToolID, String userID) {
        if (this.invitationHashMap.containsKey(studyToolID)) {
            this.invitationHashMap.get(studyToolID).remove(userID);
        }
    }

    public boolean isCollaborator(String studyToolID, String userID) {
        return this.invitationHashMap.containsKey(studyToolID) && this.invitationHashMap.get(studyToolID).contains(userID);
    }

    /**
     * @return a list of user ids of all collaborators (excluding owner) of a study tool
     */
    public List<String> getCollaboratorsID(String studyToolID) {
        return this.invitationHashMap.get(studyToolID);
    }

    /**
     * @return a list of id for all study tools owned by the user
     */
    public List<String> getAllInvitedStudyTools(String userID) {
        List<String> retList = new ArrayList<>();
        for (String studyToolID : this.invitationHashMap.keySet()) {
            if (this.isCollaborator(studyToolID, userID)) retList.add(studyToolID);
        }
        return retList;
    }

    /**
     * Adds a copy of the study tool provided to history
     */
    public void addToHistory(StudyTool studyTool, String editorID) {
        if (this.historyHashMap.containsKey(studyTool.getIdentifier())) {
            this.historyHashMap.get(studyTool.getIdentifier()).recordHistory(studyTool, editorID);
        }
        else {
            this.historyHashMap.put(studyTool.getIdentifier(), new StudyToolHistory(studyTool, editorID));
        }
    }

    public StudyToolHistory getEntireHistory(String studyToolID) {
        return this.historyHashMap.get(studyToolID);
    }

    /**
     * Fetches the n'th most recent edit of a certain study tool, counting from 1
     */
    public StudyTool fetchFromHistory(String studyToolID, int mostRecentIndex) throws IndexOutOfBoundsException{
        StudyToolHistory studyToolHistory = getEntireHistory(studyToolID);
        List<LocalDateTime> times = studyToolHistory.getLatestToEarliestDateTimes();
        LocalDateTime wantedTime = times.get(mostRecentIndex-1);
        return studyToolHistory.getHistoryMapper().get(wantedTime);
    }

    /**
     * Returns if the index provided is a valid version index, that is,
     * a number between (inclusive) 1 and the number of versions
     */
    public boolean isValidVersionIndex(String studyToolId, String versionIndex) {
        if (versionIndex.matches("^\\d+$")) {
            int version = Integer.parseInt(versionIndex);
            return version >= 1 && version <= this.getEntireHistory(studyToolId).getHistoryMapper().size();
        }
        else return false;
    }
}
