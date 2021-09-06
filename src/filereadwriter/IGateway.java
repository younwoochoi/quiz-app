package filereadwriter;

import java.io.IOException;
import java.io.Serializable;

public interface IGateway {

    public void serializeObject(Serializable serializable, String filePath) throws IOException;

    public Object loadFromSerializedObject(String filePath) throws IOException, ClassNotFoundException;
}
