package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileUtil {
    public static void writeFile(String filePath, Map<String, List<String>> stateBuffer) throws IOException {
        FileWriter writer = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            writer = new FileWriter(file);
            if(stateBuffer!=null) {
                for (String key : stateBuffer.keySet()) {
                    writer.write(key + "\n");
                    List<String> list = stateBuffer.get(key);
                    for (String str : list) {
                        writer.write(str + "\n");
                    }
                }
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("文件写入异常"+e);
            throw e;
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("文件写入时流关闭异常"+e);
                }
            }
        }
    }
}
